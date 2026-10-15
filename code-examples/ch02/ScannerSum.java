import java.util.Scanner;

/** Reads n and then n integers with Scanner, and prints their sum. */
public class ScannerSum {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += in.nextInt();
        }
        System.out.println(sum);
    }
}
