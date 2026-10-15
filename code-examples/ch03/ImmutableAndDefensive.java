import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Making a class immutable when it holds mutable things such as lists and arrays. */
public class ImmutableAndDefensive {

    // region leaky
    static final class LeakySchedule {
        private final List<String> slots;

        LeakySchedule(List<String> slots) {
            this.slots = slots;                       // stores the caller's list itself
        }

        List<String> slots() {
            return slots;                             // hands out the internal list
        }
    }
    // endregion leaky

    // region safe
    static final class Schedule {
        private final List<String> slots;
        private final int[] durations;

        Schedule(List<String> slots, int[] durations) {
            this.slots = List.copyOf(slots);          // unmodifiable copy, taken on entry
            this.durations = durations.clone();       // arrays: copy them too
        }

        List<String> slots() {
            return slots;                             // safe: nobody can modify it
        }

        int[] durations() {
            return durations.clone();                 // copy on the way out as well
        }
    }
    // endregion safe

    public static void main(String[] args) {
        // region usage
        List<String> input = new ArrayList<>(List.of("9:00", "10:00"));
        LeakySchedule leaky = new LeakySchedule(input);
        input.add("11:00");                    // the caller changes its own list...
        leaky.slots().add("12:00");            // ...and the returned list
        System.out.println("leaky: " + leaky.slots());

        List<String> input2 = new ArrayList<>(List.of("9:00", "10:00"));
        int[] minutes = {60, 45};
        Schedule safe = new Schedule(input2, minutes);
        input2.add("11:00");
        minutes[0] = 0;
        safe.durations()[1] = 0;                      // changes only a copy
        System.out.println("safe:  " + safe.slots() + " "
                + Arrays.toString(safe.durations()));
        try {
            safe.slots().add("12:00");
        } catch (UnsupportedOperationException e) {
            System.out.println("safe.slots().add -> UnsupportedOperationException");
        }
        // endregion usage
    }
}
