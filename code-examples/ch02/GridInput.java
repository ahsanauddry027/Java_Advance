import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/** Reads a grid of characters: a line "rows cols", then one line per row. */
public class GridInput {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer first = new StringTokenizer(reader.readLine());
        int rows = Integer.parseInt(first.nextToken());
        int cols = Integer.parseInt(first.nextToken());
        char[][] grid = new char[rows][];
        for (int r = 0; r < rows; r++) {
            grid[r] = reader.readLine().toCharArray();  // each row as a char array
        }

        int walls = 0;
        int startRow = -1;
        int startCol = -1;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == '#') {
                    walls++;
                } else if (grid[r][c] == 'S') {
                    startRow = r;
                    startCol = c;
                }
            }
        }
        System.out.println(rows + "x" + cols + " grid, " + walls + " walls, start at ("
                + startRow + ", " + startCol + ")");
    }
}
