import java.util.Arrays;

/** Exercise 10.5: for each day, how many days until a warmer temperature (0 if never). */
public class DailyTemperatures {

    // region solve
    static int[] daysUntilWarmer(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];        // 0 by default: "no warmer day"
        int[] waiting = new int[n];       // stack of day indices, temperatures decreasing
        int top = 0;
        for (int day = 0; day < n; day++) {
            while (top > 0 && temperatures[waiting[top - 1]] < temperatures[day]) {
                int earlier = waiting[--top];
                answer[earlier] = day - earlier;
            }
            waiting[top++] = day;
        }
        return answer;
    }
    // endregion solve

    public static void main(String[] args) {
        int[] temperatures = {30, 32, 31, 29, 33, 33, 28};
        System.out.println(Arrays.toString(daysUntilWarmer(temperatures)));
    }
}
