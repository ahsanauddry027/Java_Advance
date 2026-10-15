import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Interface, abstract class, final class, record and composition working together. */
public class ShapesDesign {

    // region types
    /** What every shape can do. An interface says WHAT, not HOW. */
    interface Shape {
        double area();

        default String describe() {               // a default method: shared behaviour
            return getClass().getSimpleName() + " with area "
                    + String.format(Locale.ROOT, "%.2f", area());
        }
    }

    /** Shared state and code for shapes with straight sides. Cannot be instantiated. */
    abstract static class Polygon implements Shape {
        private final int sides;

        Polygon(int sides) {
            this.sides = sides;
        }

        int sides() {
            return sides;
        }
    }

    static final class Rectangle extends Polygon {  // final: no further subclasses
        private final double width;
        private final double height;

        Rectangle(double width, double height) {
            super(4);
            this.width = width;
            this.height = height;
        }

        @Override
        public double area() {
            return width * height;
        }
    }

    record Circle(double radius) implements Shape {   // a record can implement interfaces
        @Override
        public double area() {
            return Math.PI * radius * radius;
        }
    }

    /** Composition: a Drawing HAS shapes; it is not itself a kind of shape. */
    static final class Drawing {
        private final List<Shape> shapes = new ArrayList<>();

        void add(Shape shape) {
            shapes.add(shape);
        }

        double totalArea() {
            double total = 0;
            for (Shape shape : shapes) {
                total += shape.area();               // works for every kind of Shape
            }
            return total;
        }
    }
    // endregion types

    public static void main(String[] args) {
        Drawing drawing = new Drawing();
        drawing.add(new Rectangle(3, 4));
        drawing.add(new Circle(1));
        Polygon square = new Rectangle(2, 2);
        drawing.add(square);
        System.out.println(new Rectangle(3, 4).describe());
        System.out.println(new Circle(1).describe());
        System.out.println("square has " + square.sides() + " sides");
        System.out.printf(Locale.ROOT, "total area %.2f%n", drawing.totalArea());
    }
}
