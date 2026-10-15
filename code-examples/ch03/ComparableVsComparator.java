import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/** A natural order (Comparable) versus other orders (Comparator), and a TreeSet trap. */
public class ComparableVsComparator {

    // region version
    /** Software versions such as 1.9 and 1.10, ordered numerically part by part. */
    record Version(int major, int minor) implements Comparable<Version> {
        @Override
        public int compareTo(Version other) {
            if (major != other.major) {
                return Integer.compare(major, other.major);
            }
            return Integer.compare(minor, other.minor);
        }

        @Override
        public String toString() {
            return major + "." + minor;
        }
    }
    // endregion version

    // region person
    record Person(String name, int age) {
        @Override
        public String toString() {
            return name + "(" + age + ")";
        }
    }
    // endregion person

    public static void main(String[] args) {
        // region sorting
        List<Version> versions = new ArrayList<>(List.of(
                new Version(1, 10), new Version(1, 9),
                new Version(2, 0), new Version(1, 2)));
        versions.sort(null);                       // null: use the natural order
        System.out.println("natural order:   " + versions);
        versions.sort(Comparator.comparingInt(Version::minor));  // another order
        System.out.println("by minor only:   " + versions);
        // endregion sorting

        // region treeset
        TreeSet<Person> byAge = new TreeSet<>(Comparator.comparingInt(Person::age));
        byAge.add(new Person("Rafi", 21));
        byAge.add(new Person("Mim", 21));                     // "equal" to Rafi by age!
        byAge.add(new Person("Anika", 23));
        System.out.println("age only:        " + byAge);

        TreeSet<Person> fixed = new TreeSet<>(Comparator.comparingInt(Person::age)
                .thenComparing(Person::name));               // a tie-breaker
        fixed.add(new Person("Rafi", 21));
        fixed.add(new Person("Mim", 21));
        fixed.add(new Person("Anika", 23));
        System.out.println("age, then name:  " + fixed);
        // endregion treeset
    }
}
