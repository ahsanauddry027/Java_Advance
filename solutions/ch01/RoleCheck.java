/** Exercise 1.3: comparing strings with == works for literals and fails for input. */
public class RoleCheck {

    // region solve
    static boolean isAdminBuggy(String role) {
        return role == "admin";               // compares references
    }

    static boolean isAdmin(String role) {
        return "admin".equals(role);          // compares characters; null-safe
    }
    // endregion solve

    public static void main(String[] args) {
        String typed = new StringBuilder("nimda").reverse().toString();  // as if typed
        System.out.println("literal: buggy " + isAdminBuggy("admin") + ", fixed "
                + isAdmin("admin"));
        System.out.println("typed:   buggy " + isAdminBuggy(typed) + ", fixed "
                + isAdmin(typed));
        System.out.println("null:    fixed " + isAdmin(null));
    }
}
