--[[
book.lua: Pandoc Lua filter that turns the book's Markdown conventions into Typst.

Conventions handled here (documented in content/front/how-to-use.md and CLAUDE.md):

  Callouts      ::: {.definition title="Stack"} ... :::
                kinds: definition, note, warning, tip, example, optional, keyidea,
                       objectives, quiz
  Exercises     ::: {.exercise number="10.1" level="Easy" title="..."} ... :::
  Solutions     ::: {.solution number="10.1"} ... :::
  Code listing  ```{.java include="code-examples/ch10/Foo.java"}```
                optional: region="name" (shows only that region), kind="Method"
  Program I/O   ```{.output from="code-examples/ch10/Foo"}```   (captured by the build)
                ```{.input from="code-examples/ch10/Foo"}```    (reads Foo.in)
  Other code    ```{.fragment}```  ```{.pseudocode}```  ```{.console}```
                ```{.diagram caption="..."}```
  References    []{.ref to="section-id"}   -> "Section 10.3" (clickable)
  Index terms   [monotonic stack]{.idx}  or  [LIFO]{.idx term="LIFO (last in, first out)"}
  Optional      ## Heading {.optional}   -> adds an OPTIONAL badge

Every Java listing must come from a file, so the PDF shows exactly the code that the
build compiled. A bare ```java block is an error.
]]

local stringify = pandoc.utils.stringify

local prefix = ""
local chapter_num = nil
local root = "."
local MAX_LINE = 90

local CALLOUTS = {
  definition = true, note = true, warning = true, tip = true, example = true,
  optional = true, keyidea = true, objectives = true, quiz = true,
}

-- ---------------------------------------------------------------- helpers

local function fail(msg)
  error("[book.lua] " .. msg, 0)
end

-- Typst string literal.
local function tstr(s)
  s = s:gsub("\\", "\\\\")
  s = s:gsub('"', '\\"')
  return '"' .. s .. '"'
end

-- Inline Markdown (e.g. a title containing `code`) -> Typst content block.
local function tcontent(md)
  local doc = pandoc.read(md, "markdown-citations-raw_html")
  local out = pandoc.write(pandoc.Pandoc(doc.blocks), "typst")
  out = out:gsub("%s+$", "")
  return "[" .. out .. "]"
end

local function read_file(rel)
  local path = root .. "/" .. rel
  local f = io.open(path, "rb")
  if not f then fail("cannot open file: " .. rel) end
  local s = f:read("a")
  f:close()
  s = s:gsub("\r\n", "\n")
  return s
end

local function split_lines(s)
  local lines = {}
  for line in (s .. "\n"):gmatch("(.-)\n") do
    table.insert(lines, line)
  end
  return lines
end

local function is_marker(line)
  return line:match("^%s*//%s*region%s") ~= nil or line:match("^%s*//%s*endregion") ~= nil
end

-- Keep the lines of one region (or the whole file), drop all region markers,
-- remove common indentation and surrounding blank lines.
-- scripts/bookutil.py implements the same algorithm; keep them in sync.
local function extract(src, region, what)
  local lines = split_lines(src)
  local out = {}
  if region then
    local inside, found = false, false
    for _, l in ipairs(lines) do
      local open = l:match("^%s*//%s*region%s+([%w_%-]+)%s*$")
      local close = l:match("^%s*//%s*endregion%s+([%w_%-]+)%s*$")
      if open then
        if open == region then inside, found = true, true end
      elseif close then
        if close == region then inside = false end
      elseif l:match("^%s*//%s*endregion") then
        fail(what .. ": endregion must name its region")
      elseif inside then
        table.insert(out, l)
      end
    end
    if not found then fail(what .. ": region '" .. region .. "' not found") end
  else
    for _, l in ipairs(lines) do
      if not is_marker(l) then table.insert(out, l) end
    end
  end
  -- tabs -> 4 spaces, strip trailing whitespace
  for i, l in ipairs(out) do
    l = l:gsub("\t", "    ")
    out[i] = l:gsub("%s+$", "")
  end
  -- trim blank lines at both ends
  while #out > 0 and out[1] == "" do table.remove(out, 1) end
  while #out > 0 and out[#out] == "" do table.remove(out) end
  -- common indentation
  local indent = nil
  for _, l in ipairs(out) do
    if l ~= "" then
      local n = #l:match("^( *)")
      if indent == nil or n < indent then indent = n end
    end
  end
  if indent and indent > 0 then
    for i, l in ipairs(out) do out[i] = l:sub(indent + 1) end
  end
  return out
end

local function check_width(lines, what)
  for i, l in ipairs(lines) do
    local n = utf8.len(l) or #l
    if n > MAX_LINE then
      fail(string.format("%s: line %d is %d characters (max %d)", what, i, n, MAX_LINE))
    end
  end
end

local function with_label(close, id)
  if id ~= nil and id ~= "" then
    return close .. " <" .. prefix .. id .. ">"
  end
  return close
end

local function wrap(open, blocks, close)
  local out = pandoc.List({ pandoc.RawBlock("typst", open) })
  out:extend(blocks)
  out:insert(pandoc.RawBlock("typst", close))
  return out
end

local function add_prefix(id)
  if id == nil or id == "" then return id end
  if id:match("^ch%d%d%-") or id:match("^front%-") or id:match("^back%-") then return id end
  if prefix ~= "" and id:sub(1, #prefix) ~= prefix then return prefix .. id end
  return id
end

-- ---------------------------------------------------------------- metadata

local function read_meta(doc)
  local m = doc.meta
  if m.idprefix then prefix = stringify(m.idprefix) end
  if m.chapter then chapter_num = tonumber(stringify(m.chapter)) end
  if m.root then root = stringify(m.root) end
  return nil
end

-- ---------------------------------------------------------------- elements

-- Level-4 headings are run-in labels such as "Syntax breakdown"; keep them out of the
-- PDF bookmarks and the contents.
local function minor_heading(inlines)
  local body = pandoc.write(pandoc.Pandoc({ pandoc.Plain(inlines) }), "typst"):gsub("%s+$", "")
  return pandoc.RawBlock("typst", "#heading(level: 4, numbering: none, outlined: false)["
                         .. body .. "]")
end

-- A paragraph that is only bold text ("**Syntax breakdown.**") becomes a minor heading,
-- so it can never be left alone at the bottom of a page.
local function Para(el)
  local c = el.content
  if #c == 1 and c[1].t == "Strong" then
    local inlines = pandoc.List(c[1].content)
    local last = inlines[#inlines]
    if last and last.t == "Str" and last.text:sub(-1) == "." then
      inlines[#inlines] = pandoc.Str(last.text:sub(1, -2))
    end
    return minor_heading(inlines)
  end
  return nil
end

local function Header(el)
  el.identifier = add_prefix(el.identifier)
  if el.level == 4 then
    return minor_heading(el.content)
  end
  if el.classes:includes("optional") then
    el.content:insert(pandoc.Space())
    el.content:insert(pandoc.RawInline("typst", "#opt-badge();"))
  end
  if el.level == 1 and chapter_num and not el.classes:includes("unnumbered") then
    return { pandoc.RawBlock("typst", string.format("#chapter-begin(%d)", chapter_num)), el }
  end
  return el
end

local function Link(el)
  if el.target:sub(1, 1) == "#" then
    el.target = "#" .. add_prefix(el.target:sub(2))
  end
  return el
end

local function Span(el)
  if el.classes:includes("ref") then
    local to = el.attributes["to"]
    if not to or to == "" then fail("ref span without 'to'") end
    return pandoc.RawInline("typst", "#ref(<" .. add_prefix(to) .. ">);")
  elseif el.classes:includes("idx") then
    local term = el.attributes["term"] or stringify(el.content)
    local out = pandoc.List(el.content)
    out:insert(pandoc.RawInline("typst", "#idx(" .. tstr(term) .. ");"))
    return out
  end
  return nil
end

local function Div(el)
  local cls = el.classes
  for _, c in ipairs(cls) do
    if CALLOUTS[c] then
      local args = { "kind: " .. tstr(c) }
      if el.attributes["title"] then
        table.insert(args, "title: " .. tcontent(el.attributes["title"]))
      end
      return wrap("#callout(" .. table.concat(args, ", ") .. ")[", el.content,
                  with_label("]", el.identifier))
    end
  end
  if cls:includes("exercise") or cls:includes("solution") then
    local num = el.attributes["number"]
    if not num or num == "" then fail("exercise/solution without number") end
    local key = (num:gsub("%.", "-"))
    local args = { "number: " .. tstr(num) }
    if el.attributes["title"] then
      table.insert(args, "title: " .. tcontent(el.attributes["title"]))
    end
    if cls:includes("exercise") then
      if el.attributes["level"] then
        table.insert(args, "level: " .. tstr(el.attributes["level"]))
      end
      table.insert(args, 'sol: "sol-' .. key .. '"')
      return wrap("#exercise(" .. table.concat(args, ", ") .. ")[", el.content,
                  "] <ex-" .. key .. ">")
    else
      table.insert(args, 'ex: "ex-' .. key .. '"')
      return wrap("#solution(" .. table.concat(args, ", ") .. ")[", el.content,
                  "] <sol-" .. key .. ">")
    end
  end
  return nil
end

local SHORT_LISTING = 14  -- listings up to this many lines never split across pages

local function listing_block(kind, file, numbered, lang, lines, id)
  local cb = pandoc.CodeBlock(table.concat(lines, "\n"), pandoc.Attr("", lang and { lang } or {}))
  local open = "#listing(kind: " .. tstr(kind)
  if file then open = open .. ", file: " .. tstr(file) end
  open = open .. ", numbered: " .. (numbered and "true" or "false")
  if #lines <= SHORT_LISTING then open = open .. ", breakable: false" end
  open = open .. ")["
  return wrap(open, { cb }, with_label("]", id))
end

local function CodeBlock(el)
  local cls = el.classes
  local attr = el.attributes
  local id = el.identifier

  if attr["include"] then
    local inc = attr["include"]
    local region = attr["region"]
    local what = inc .. (region and ("#" .. region) or "")
    local lines = extract(read_file(inc), region, what)
    check_width(lines, what)
    local kind = attr["kind"] or (region and "Method" or "Complete program")
    local lang = cls[1] or "java"
    local fname = inc:match("([^/]+)$")
    return listing_block(kind, fname, attr["numbers"] ~= "false", lang, lines, id)
  end

  if cls:includes("output") or cls:includes("input") then
    local from = attr["from"]
    if not from then fail("output/input block needs from=\"code-examples/chNN/Name\"") end
    local path, kind
    if cls:includes("output") then
      path = "build/outputs/" .. from .. ".txt"
      kind = "Output"
    else
      path = from .. ".in"
      kind = "Input"
    end
    local lines = split_lines((read_file(path):gsub("%s+$", "")))
    check_width(lines, path)
    local fname = from:match("([^/]+)$")
    return listing_block(attr["kind"] or kind, fname .. (kind == "Input" and ".in" or ""),
                         false, nil, lines, id)
  end

  local lines = split_lines(el.text)
  if cls:includes("diagram") then
    check_width(lines, "diagram")
    local cap = attr["caption"] and tcontent(attr["caption"]) or "none"
    local cb = pandoc.CodeBlock(el.text, pandoc.Attr("", {}))
    return wrap("#diagram(caption: " .. cap .. ")[", { cb }, with_label("]", id))
  end
  if cls:includes("fragment") then
    check_width(lines, "fragment")
    return listing_block(attr["kind"] or "Fragment (not a complete program)", nil,
                         false, "java", lines, id)
  end
  if cls:includes("pseudocode") then
    check_width(lines, "pseudocode")
    return listing_block("Pseudocode", nil, true, nil, lines, id)
  end
  if cls:includes("console") then
    check_width(lines, "console")
    return listing_block(attr["kind"] or "Terminal", nil, false, nil, lines, id)
  end
  if cls:includes("plain") then
    check_width(lines, "plain")
    return listing_block(attr["kind"] or "Text", nil, false, nil, lines, id)
  end
  fail("code block needs include=, or class output/input/diagram/fragment/pseudocode/"
       .. "console/plain. Starts with: " .. el.text:sub(1, 60))
end

return {
  { Pandoc = read_meta },
  { Header = Header, Link = Link, Span = Span, Div = Div, CodeBlock = CodeBlock, Para = Para },
}
