/** Primitive variables hold values; reference variables hold references to objects. */
public class ValuesAndReferences {

    // region method
    static void tryToChange(int number, int[] array) {
        number = 99;               // changes only this method's copy of the value
        array[0] = 99;             // changes the array object that the caller also sees
        array = new int[] {-1};    // re-points only this method's copy of the reference
    }
    // endregion method

    public static void main(String[] args) {
        // region copies
        int a = 5;
        int b = a;                 // copies the value 5 into b
        b++;
        System.out.println("a = " + a + ", b = " + b);

        int[] first = {1, 2, 3};
        int[] second = first;      // copies the reference: both names, ONE array
        second[0] = 100;
        System.out.println("first[0] = " + first[0]);
        // endregion copies

        // region call
        int number = 1;
        int[] data = {1, 2, 3};
        tryToChange(number, data);
        System.out.println("number = " + number + ", data[0] = " + data[0]
                + ", data.length = " + data.length);
        // endregion call
    }
}
