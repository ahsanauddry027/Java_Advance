import java.util.ArrayDeque;
import java.util.Deque;

/** Exercise 10.7: simplify an absolute Unix-style path such as "/a/./b/../../c/". */
public class SimplifyPath {

    // region solve
    static String simplify(String path) {
        Deque<String> folders = new ArrayDeque<>();  // used as a stack of folder names
        for (String part : path.split("/")) {
            if (part.isEmpty() || part.equals(".")) {
                continue;                 // "//" and "." do not change the location
            }
            if (part.equals("..")) {
                folders.pollFirst();      // go up one level (nothing happens at the root)
            } else {
                folders.push(part);
            }
        }
        StringBuilder result = new StringBuilder();
        while (!folders.isEmpty()) {
            result.append('/').append(folders.pollLast());  // bottom of the stack first
        }
        return result.isEmpty() ? "/" : result.toString();
    }
    // endregion solve

    public static void main(String[] args) {
        String[] paths = {
                "/home/", "/a/./b/../../c/", "/../", "/home//user/./docs/..", "/..."};
        for (String path : paths) {
            System.out.printf("%-24s -> %s%n", path, simplify(path));
        }
    }
}
