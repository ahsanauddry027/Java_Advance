// =====================================================================
// book.typ: page design for "Advanced Java, DSA and Job Interview Preparation".
// scripts/build.py generates build/main.typ, which applies `book` and includes
// every chapter (each chapter file imports this template for its functions).
// =====================================================================

// ---------------------------------------------------------------- palette

#let accent = rgb("#1f4e79")
#let ink = rgb("#1b1f24")
#let muted = rgb("#5b6673")
#let rule-color = rgb("#c9d1d9")
#let code-bg = rgb("#f6f8fa")
#let code-head = rgb("#e8edf2")

#let body-font = ("Libertinus Serif",)
#let sans-font = ("Segoe UI", "Libertinus Serif")
#let mono-font = ("DejaVu Sans Mono",)

#let kinds = (
  definition: (label: "Definition", color: rgb("#0b5cad")),
  note: (label: "Note", color: rgb("#57606a")),
  warning: (label: "Warning", color: rgb("#b42318")),
  tip: (label: "Interview tip", color: rgb("#1a7f37")),
  example: (label: "Example", color: rgb("#7a3fb8")),
  optional: (label: "Optional", color: rgb("#9a6700")),
  keyidea: (label: "Key idea", color: rgb("#1f4e79")),
  objectives: (label: "In this chapter", color: rgb("#1f4e79")),
  quiz: (label: "Warm-up quiz", color: rgb("#0e7490")),
)

#let level-colors = (
  "Easy": rgb("#1a7f37"),
  "Medium": rgb("#9a6700"),
  "Hard": rgb("#b42318"),
  "Challenge": rgb("#7a3fb8"),
)

// Pandoc emits #horizontalrule for "---".
#let horizontalrule = block(above: 1em, below: 1em, line(length: 100%, stroke: 0.5pt + rule-color))

// ---------------------------------------------------------------- small pieces

#let badge(label, color) = box(
  fill: color.lighten(86%),
  inset: (x: 3.5pt, y: 1.6pt),
  outset: (y: 0.6pt),
  radius: 2pt,
  text(font: sans-font, size: 6.8pt, weight: "bold", fill: color, tracking: 0.4pt, upper(label)),
)

#let opt-badge() = badge("Optional", kinds.optional.color)

// Index entry (collected by index-page).
#let idx(term) = [#metadata(term) <idx>]

// Numbers chapters, sections and subsections ("10", "10.3", "10.3.2"); deeper
// headings are not numbered.
#let heading-numbering = (..nums) => {
  let n = nums.pos()
  if n.len() <= 3 { numbering("1.1", ..n) }
}

// Chapter number of the current position (0 in front matter).
#let current-chapter() = counter(heading).get().first()

// ---------------------------------------------------------------- callouts

#let callout(kind: "note", title: none, body) = {
  let k = kinds.at(kind)
  block(
    width: 100%,
    breakable: true,
    fill: k.color.lighten(94%),
    stroke: (left: 2.5pt + k.color),
    inset: (left: 11pt, right: 10pt, top: 7pt, bottom: 8pt),
    above: 1.2em,
    below: 1.2em,
    {
      block(below: 0.7em, sticky: true, text(font: sans-font, size: 8.3pt, fill: k.color)[
        #text(weight: "bold", tracking: 0.5pt, upper(k.label))#if title != none [
          #h(0.35em)·#h(0.35em)#text(weight: "bold", fill: ink, title)
        ]
      ])
      body
    },
  )
}

// ---------------------------------------------------------------- listings

#let listing(kind: "Complete program", file: none, numbered: true, body) = figure(
  kind: "listing",
  supplement: [Listing],
  {
    set align(left)
    let head = {
      set text(font: sans-font, size: 7.8pt, fill: muted)
      grid(
        columns: (auto, 1fr, auto),
        column-gutter: 8pt,
        context {
          let n = counter(figure.where(kind: "listing")).get().first()
          let ch = current-chapter()
          text(weight: "bold", fill: accent)[Listing #if ch > 0 [#ch.#n] else [#n]]
        },
        text(weight: "semibold", kind),
        if file != none { text(font: mono-font, size: 7.4pt, file) },
      )
    }
    block(
      width: 100%,
      breakable: true,
      fill: code-bg,
      stroke: 0.6pt + rule-color,
      radius: 3pt,
      inset: 0pt,
      {
        block(
          width: 100%,
          fill: code-head,
          inset: (x: 8pt, y: 4.5pt),
          radius: (top-left: 3pt, top-right: 3pt),
          below: 0pt,
          sticky: true,
          head,
        )
        block(width: 100%, inset: (x: 8pt, top: 6pt, bottom: 7pt), above: 0pt, breakable: true, {
          if numbered {
            show raw.line: it => {
              box(width: 1.9em, align(right, text(fill: rgb("#8c959f"), str(it.number))))
              h(0.9em)
              it.body
            }
            body
          } else {
            body
          }
        })
      },
    )
  },
)

// ---------------------------------------------------------------- diagrams

#let diagram(caption: none, body) = figure(
  kind: "diagram",
  supplement: [Figure],
  caption: caption,
  block(
    breakable: true,
    stroke: 0.6pt + rule-color,
    radius: 3pt,
    inset: (x: 12pt, y: 9pt),
    {
      set align(left)
      set par(leading: 0.36em)
      body
    },
  ),
)

// ---------------------------------------------------------------- exercises

#let exercise(number: "", level: none, title: none, sol: none, body) = block(
  width: 100%,
  breakable: true,
  above: 1.15em,
  below: 1.15em,
  stroke: (left: 2pt + accent.lighten(35%)),
  inset: (left: 10pt, top: 2pt, bottom: 3pt),
  {
    block(below: 0.65em, sticky: true, {
      text(font: sans-font, size: 9.5pt, weight: "bold", fill: accent)[Exercise #number]
      if level != none {
        h(0.5em)
        badge(level, level-colors.at(level, default: muted))
      }
      if title != none {
        h(0.5em)
        text(font: sans-font, size: 9.5pt, weight: "semibold", title)
      }
    })
    body
    if sol != none {
      block(width: 100%, above: 0.55em, align(right, text(font: sans-font, size: 7.8pt)[
        #link(label(sol))[#text(fill: accent)[Solution #sym.arrow.r]]
      ]))
    }
  },
)

#let solution(number: "", title: none, ex: none, body) = block(
  width: 100%,
  breakable: true,
  above: 1.15em,
  below: 1.15em,
  stroke: (left: 2pt + kinds.tip.color.lighten(30%)),
  inset: (left: 10pt, top: 2pt, bottom: 3pt),
  {
    block(below: 0.65em, sticky: true, {
      text(font: sans-font, size: 9.5pt, weight: "bold", fill: kinds.tip.color)[Solution #number]
      if title != none {
        h(0.5em)
        text(font: sans-font, size: 9.5pt, weight: "semibold", title)
      }
      if ex != none {
        h(1fr)
        text(font: sans-font, size: 7.8pt)[#link(label(ex))[#text(fill: accent)[#sym.arrow.l Exercise]]]
      }
    })
    body
  },
)

// ---------------------------------------------------------------- structure

#let chapter-begin(n) = {
  pagebreak(weak: true)
  counter(heading).update(n - 1)
  counter(figure.where(kind: "listing")).update(0)
  counter(figure.where(kind: "diagram")).update(0)
  counter(figure.where(kind: table)).update(0)
  context [#metadata((number: n, page: here().page())) <chapter-start>]
}

#let part-page(number: 1, title: "", blurb: none, chapters: ()) = {
  pagebreak(weak: true)
  [#metadata((number: number, title: title)) <part-entry>]
  {
    // Invisible heading so the part appears in the PDF bookmarks.
    show heading: it => none
    heading(level: 1, numbering: none, outlined: false, bookmarked: true)[
      Part #numbering("I", number): #title
    ]
  }
  v(40mm)
  text(font: sans-font, size: 12pt, weight: "bold", fill: accent, tracking: 2.5pt)[
    PART #numbering("I", number)
  ]
  v(3mm)
  block(text(font: sans-font, size: 26pt, weight: "bold", fill: ink, hyphenate: false, title))
  v(2mm)
  line(length: 35%, stroke: 1.5pt + accent)
  if blurb != none {
    v(7mm)
    block(width: 88%, text(size: 11pt, fill: muted, blurb))
  }
  if chapters.len() > 0 {
    v(9mm)
    set text(font: sans-font, size: 10pt)
    for c in chapters {
      block(above: 0.55em, below: 0.55em)[
        #box(width: 2.6em, text(weight: "bold", fill: accent, str(c.number)))#c.title
      ]
    }
  }
  pagebreak()
}

#let book-end() = context [#metadata(here().page()) <book-end>]

// ---------------------------------------------------------------- header/footer

#let running-header() = context {
  let pg = here().page()
  let openers = query(heading.where(level: 1)).filter(h => h.location().page() == pg)
  let chapters = query(heading.where(level: 1).before(here()))
  if openers.len() == 0 and chapters.len() > 0 {
    let ch = chapters.last()
    let lhs = if ch.numbering != none {
      [Chapter #counter(heading).at(ch.location()).first()#h(0.4em)·#h(0.4em)#ch.body]
    } else { ch.body }
    let on-page = query(heading.where(level: 2)).filter(s => s.location().page() == pg)
    let earlier = query(heading.where(level: 2).before(here())).filter(s => (
      s.location().page() >= ch.location().page()
    ))
    let sec = if on-page.len() > 0 { on-page.first() } else if earlier.len() > 0 {
      earlier.last()
    } else { none }
    let rhs = if sec != none {
      let nums = counter(heading).at(sec.location())
      if sec.numbering != none and ch.numbering != none {
        [#nums.slice(0, 2).map(str).join(".")#h(0.5em)#sec.body]
      } else { sec.body }
    } else { none }
    set text(font: sans-font, size: 7.8pt, fill: muted)
    block(width: 100%, stroke: (bottom: 0.4pt + rule-color), inset: (bottom: 3.5pt), grid(
      columns: (1fr, 1fr),
      column-gutter: 10pt,
      align(left, lhs),
      align(right, rhs),
    ))
  }
}

#let running-footer() = context {
  let pg = here().page()
  let is-part = query(<part-entry>).any(m => m.location().page() == pg)
  if not is-part and here().page-numbering() != none {
    align(center, text(font: sans-font, size: 8.5pt, fill: muted, counter(page).display()))
  }
}

// ---------------------------------------------------------------- cover

#let cover(title: "", subtitle: "", edition: "", version: "", date: "", topics: ()) = page(
  margin: 0pt,
  header: none,
  footer: none,
  numbering: none,
  {
    place(top + left, rect(width: 100%, height: 58%, fill: accent))
    place(top + left, dx: 22mm, dy: 38mm, block(width: 166mm, {
      text(font: sans-font, size: 10.5pt, weight: "bold", fill: white.transparentize(25%), tracking: 3pt)[
        #upper(edition)
      ]
      v(9mm)
      block(text(font: sans-font, size: 33pt, weight: "bold", fill: white, hyphenate: false, title))
      v(5mm)
      block(width: 92%, text(font: sans-font, size: 13.5pt, fill: white.transparentize(8%), subtitle))
    }))
    place(top + left, dx: 22mm, dy: 182mm, block(width: 166mm, {
      set text(font: mono-font, size: 9pt, fill: accent)
      block(fill: code-bg, stroke: 0.6pt + rule-color, radius: 3pt, inset: 10pt, width: 122mm, {
        set par(leading: 0.55em)
        raw(block: true, lang: "java", (
          "// Java 21 LTS · data structures · algorithms · interviews",
          "Deque<Integer> stack = new ArrayDeque<>();",
          "PriorityQueue<int[]> pq =",
          "        new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));",
          "Map<String, List<String>> groups = new HashMap<>();",
        ).join("\n"))
      })
    }))
    place(bottom + left, dx: 22mm, dy: -22mm, block(width: 166mm, {
      set text(font: sans-font, size: 9.5pt, fill: muted)
      if topics.len() > 0 { block(below: 4mm, topics.join("  ·  ")) }
      line(length: 100%, stroke: 0.5pt + rule-color)
      v(2mm)
      [Version #version#h(1fr)Generated #date]
    }))
  },
)

// ---------------------------------------------------------------- contents

#let toc-row(loc, number: none, body: [], size: 10pt, weight: "regular", indent: 0pt, num-width: 2.4em) = {
  let pnum = {
    let pat = loc.page-numbering()
    if pat == none { str(loc.page()) } else { numbering(pat, ..counter(page).at(loc)) }
  }
  pad(left: indent, link(loc, block(width: 100%, {
    set text(size: size, weight: weight)
    if number != none { box(width: num-width, text(fill: accent, number)) }
    body
    box(width: 1fr, inset: (x: 4pt), repeat(text(fill: rule-color, weight: "regular")[.#h(2.5pt)]))
    box(width: 2.2em, align(right, pnum))
  })))
}

#let toc() = {
  heading(level: 1, numbering: none, outlined: false)[Contents]
  context {
    let parts = query(<part-entry>).map(p => (kind: "part", el: p))
    let heads = query(heading.where(outlined: true)).filter(h => h.level <= 2).map(h => (
      kind: "head",
      el: h,
    ))
    let items = (parts + heads).sorted(key: it => {
      let pos = it.el.location().position()
      pos.page * 100000 + pos.y.pt()
    })
    set text(font: sans-font)
    for it in items {
      let loc = it.el.location()
      if it.kind == "part" {
        block(above: 1.4em, below: 0.7em, link(loc, text(size: 10.5pt, weight: "bold", fill: accent)[
          Part #numbering("I", it.el.value.number)#h(0.6em)#it.el.value.title
        ]))
      } else {
        let hd = it.el
        let nums = counter(heading).at(loc)
        if hd.level == 1 {
          block(above: 0.8em, below: 0.4em, toc-row(
            loc,
            number: if hd.numbering != none { str(nums.first()) } else { none },
            body: hd.body,
            size: 10pt,
            weight: "bold",
          ))
        } else {
          block(above: 0.32em, below: 0.32em, toc-row(
            loc,
            number: if hd.numbering != none { nums.slice(0, 2).map(str).join(".") } else { none },
            body: hd.body,
            size: 9pt,
            indent: 2.4em,
            num-width: 2.6em,
          ))
        }
      }
    }
  }
}

// ---------------------------------------------------------------- index

#let index-page() = context {
  let marks = query(<idx>)
  if marks.len() > 0 {
    heading(level: 1, numbering: none)[Index]
    let entries = (:)
    for m in marks {
      let term = m.value
      let loc = m.location()
      let pnum = counter(page).at(loc).first()
      let found = entries.at(term, default: ())
      if found.all(e => e.p != pnum) { found.push((p: pnum, loc: loc)) }
      entries.insert(term, found)
    }
    let keys = entries.keys().sorted(key: k => lower(k))
    set text(size: 9pt)
    set par(justify: false, spacing: 0.45em, hanging-indent: 1em)
    columns(2, gutter: 9mm, {
      let last-letter = none
      for k in keys {
        let letter = upper(k.first())
        if letter != last-letter {
          block(above: 1em, below: 0.5em, sticky: true, text(font: sans-font, weight: "bold", fill: accent, letter))
          last-letter = letter
        }
        let pages = entries.at(k).sorted(key: e => e.p).map(e => link(e.loc, str(e.p)))
        par[#k, #pages.join(", ")]
      }
    })
  }
}

// ---------------------------------------------------------------- the book

#let book(title: "", body) = {
  set document(title: title)
  set page(
    paper: "a4",
    margin: (top: 24mm, bottom: 21mm, left: 21mm, right: 21mm),
    header: running-header(),
    footer: running-footer(),
    header-ascent: 35%,
    footer-descent: 35%,
  )
  set text(font: body-font, size: 10.5pt, lang: "en", hyphenate: true, fill: ink)
  set par(justify: true, leading: 0.62em, spacing: 0.95em)

  // Code
  show raw: set text(font: mono-font)
  show raw.where(block: false): set text(size: 1.12em, fill: rgb("#24292f"))
  show raw.where(block: true): set text(size: 8pt)
  show raw.where(block: true): set par(justify: false, leading: 0.5em)

  // Lists
  set list(indent: 0.35em, body-indent: 0.55em)
  set enum(indent: 0.35em, body-indent: 0.55em)
  set terms(separator: [: ], hanging-indent: 1.4em, spacing: 0.9em)
  show terms.item: it => par(hanging-indent: 1.4em)[#strong(it.term): #it.description]

  // Links: external URLs in the accent color
  show link: it => if type(it.dest) == str { text(fill: accent, it) } else { it }

  // Headings
  set heading(numbering: heading-numbering)
  show heading: set text(font: sans-font, fill: ink, hyphenate: false)
  show heading.where(level: 1): it => {
    pagebreak(weak: true)
    v(12mm)
    if it.numbering != none {
      context text(font: sans-font, size: 10.5pt, weight: "bold", fill: accent, tracking: 2pt)[
        CHAPTER #counter(heading).get().first()
      ]
      v(0.5mm)
    }
    block(below: 3mm, text(size: 23pt, weight: "bold", it.body))
    line(length: 100%, stroke: 1.2pt + accent)
    v(5mm)
  }
  show heading.where(level: 2): it => block(above: 1.7em, below: 0.85em, sticky: true, {
    set text(size: 13.5pt, weight: "bold")
    if it.numbering != none {
      context text(fill: accent, counter(heading).display("1.1"))
      h(0.55em)
    }
    it.body
  })
  show heading.where(level: 3): it => block(above: 1.4em, below: 0.75em, sticky: true, {
    set text(size: 11.2pt, weight: "bold")
    if it.numbering != none {
      context text(fill: accent, counter(heading).display("1.1"))
      h(0.5em)
    }
    it.body
  })
  show heading.where(level: 4): it => block(above: 1.2em, below: 0.6em, sticky: true, {
    text(size: 10.5pt, weight: "bold", it.body)
  })

  // Cross-references: "Chapter 10", "Section 10.3"
  show ref: it => {
    let el = it.element
    if el != none and el.func() == heading {
      context {
        let nums = counter(heading).at(el.location())
        let word = if el.level == 1 { "Chapter" } else { "Section" }
        let num = if el.level == 1 { str(nums.first()) } else {
          nums.slice(0, calc.min(el.level, 3)).map(str).join(".")
        }
        link(el.location(), text(fill: accent)[#word~#num])
      }
    } else if el != none and el.func() == figure {
      text(fill: accent, it)
    } else { it }
  }

  // Figures and tables: numbered per chapter (10.1, 10.2, ...)
  set figure(numbering: n => {
    let ch = current-chapter()
    if ch == 0 { str(n) } else { str(ch) + "." + str(n) }
  })
  show figure: set block(breakable: true)
  show figure.where(kind: "listing"): set block(above: 1.1em, below: 1.1em)
  show figure.where(kind: table): set figure.caption(position: top)
  show figure.caption: it => text(font: sans-font, size: 8.5pt, fill: muted)[
    #text(weight: "bold", fill: accent)[#it.supplement #context it.counter.display(it.numbering)]#it.separator#it.body
  ]
  set figure(gap: 0.7em)

  // Tables
  set table(
    stroke: (x, y) => (
      top: if y == 0 { 0.8pt + ink } else { 0pt },
      bottom: if y == 0 { 0.6pt + ink } else { 0.35pt + rule-color },
    ),
    fill: (x, y) => if y == 0 { rgb("#eef2f6") },
    inset: (x: 5pt, y: 4pt),
  )
  show table: set text(size: 9pt)
  show table: set align(left)
  show table: set par(justify: false, leading: 0.5em)
  show table.cell.where(y: 0): set text(font: sans-font, weight: "bold", size: 8.4pt)

  body
}
