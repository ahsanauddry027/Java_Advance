import java.util.HashMap;
import java.util.Map;

/** Enums with data and behaviour, and records as small immutable values and map keys. */
public class EnumsAndRecords {

    // region enum
    enum Direction {
        UP(-1, 0), RIGHT(0, 1), DOWN(1, 0), LEFT(0, -1);  // the only four instances

        final int rowStep;
        final int colStep;

        Direction(int rowStep, int colStep) {             // enum constructors are private
            this.rowStep = rowStep;
            this.colStep = colStep;
        }

        Direction turnRight() {
            Direction[] all = values();                   // in declaration order
            return all[(ordinal() + 1) % all.length];
        }
    }
    // endregion enum

    // region record
    record Cell(int row, int col) {
        Cell {                                  // compact constructor: validation
            if (row < 0 || col < 0) {
                throw new IllegalArgumentException("negative: " + row + ", " + col);
            }
        }

        Cell move(Direction d) {
            return new Cell(row + d.rowStep, col + d.colStep);  // records are immutable
        }
    }
    // endregion record

    public static void main(String[] args) {
        // region usage
        Direction facing = Direction.UP;
        Cell position = new Cell(2, 2);
        Map<Cell, Integer> visits = new HashMap<>();      // records work as keys
        visits.merge(position, 1, Integer::sum);
        String commands = "FRFFRFLF";
        for (char command : commands.toCharArray()) {
            switch (command) {
                case 'F' -> position = position.move(facing);
                case 'R' -> facing = facing.turnRight();
                case 'L' -> facing = facing.turnRight().turnRight().turnRight();
                default -> throw new IllegalArgumentException("bad command " + command);
            }
            visits.merge(position, 1, Integer::sum);
        }
        System.out.println("final: " + position + " facing " + facing);
        System.out.println("visited (1, 3) " + visits.get(new Cell(1, 3)) + " time(s)");
        System.out.println("RIGHT.turnRight() == Direction.valueOf(\"DOWN\"): "
                + (Direction.RIGHT.turnRight() == Direction.valueOf("DOWN")));
        try {
            new Cell(-1, 0);
        } catch (IllegalArgumentException e) {
            System.out.println("rejected: " + e.getMessage());
        }
        // endregion usage
    }
}
