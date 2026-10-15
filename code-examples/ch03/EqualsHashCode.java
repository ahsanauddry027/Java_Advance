import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/** Why a class used in a HashSet or as a HashMap key needs BOTH equals and hashCode. */
public class EqualsHashCode {

    // region versions
    /** No equals or hashCode: inherits identity-based versions from Object. */
    static final class PointA {
        final int x;
        final int y;

        PointA(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /** equals but no hashCode: breaks the contract (javac warns; silenced on purpose). */
    @SuppressWarnings("overrides")
    static final class PointB {
        final int x;
        final int y;

        PointB(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof PointB p && x == p.x && y == p.y;
        }
    }
    // endregion versions

    // region correct
    /** Both methods, consistent with each other. */
    static final class PointC {
        private final int x;
        private final int y;

        PointC(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;                     // same object: certainly equal
            }
            if (!(other instanceof PointC)) {
                return false;                    // also handles other == null
            }
            PointC p = (PointC) other;
            return x == p.x && y == p.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);           // uses exactly the fields equals uses
        }
    }

    record PointR(int x, int y) { }               // a record generates both for you
    // endregion correct

    // region mutable
    static final class MutablePoint {
        int x;
        int y;

        MutablePoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof MutablePoint p && x == p.x && y == p.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
    // endregion mutable

    public static void main(String[] args) {
        // region usage
        Set<PointA> a = new HashSet<>();
        a.add(new PointA(1, 2));
        Set<PointB> b = new HashSet<>();
        b.add(new PointB(1, 2));
        Set<PointC> c = new HashSet<>();
        c.add(new PointC(1, 2));
        Set<PointR> r = new HashSet<>();
        r.add(new PointR(1, 2));
        System.out.println("no equals/hashCode: " + a.contains(new PointA(1, 2)));
        System.out.println("equals only:        " + b.contains(new PointB(1, 2)));
        System.out.println("both:               " + c.contains(new PointC(1, 2)));
        System.out.println("record:             " + r.contains(new PointR(1, 2)));

        Set<MutablePoint> points = new HashSet<>();
        MutablePoint p = new MutablePoint(1, 2);
        points.add(p);
        p.x = 5;                                  // changes p's hash code while inside
        System.out.println("after mutation: contains(p) = " + points.contains(p)
                + ", size = " + points.size());
        // endregion usage
    }
}
