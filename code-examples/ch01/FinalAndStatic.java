import java.util.Arrays;

/** What final, static, this and super mean in the code you write for problems. */
public class FinalAndStatic {

    // region fields
    static final int MOD = 1_000_000_007;  // a constant: one copy, never reassigned
    static int created = 0;                // one copy, shared by every object

    private final String name;             // set once, in the constructor
    private final int[] counts;            // the reference is final; the array is not

    FinalAndStatic(String name, int size) {
        this.name = name;                  // this.name is the field, name the parameter
        this.counts = new int[size];
        created++;
    }

    void record(int index) {
        counts[index] = (counts[index] + 1) % MOD;  // allowed: changes the contents
        // counts = new int[3];                     // would not compile: counts is final
    }
    // endregion fields

    @Override
    public String toString() {
        return name + Arrays.toString(counts);
    }

    // region super
    static class Labelled extends FinalAndStatic {
        Labelled(String name) {
            super(name, 3);                // runs the parent constructor first
        }

        @Override
        public String toString() {
            return "<" + super.toString() + ">";  // reuses the parent's version
        }
    }
    // endregion super

    public static void main(String[] args) {
        FinalAndStatic plain = new FinalAndStatic("plain", 3);
        FinalAndStatic labelled = new Labelled("labelled");
        plain.record(1);
        plain.record(1);
        labelled.record(2);
        System.out.println(plain + " " + labelled);
        System.out.println("objects created: " + FinalAndStatic.created);
    }
}
