/** Overloading is chosen at compile time; overriding is chosen at run time. */
public class DispatchDemo {

    // region overload
    static String describe(Object value) {
        return "Object version";
    }

    static String describe(String value) {
        return "String version";
    }
    // endregion overload

    // region override
    static class Animal {
        String name = "animal";                 // fields are NOT polymorphic

        String sound() {
            return "...";
        }

        static String kind() {                  // static: hidden, not overridden
            return "Animal.kind";
        }
    }

    static class Dog extends Animal {
        String name = "dog";

        @Override
        String sound() {
            return "woof";
        }

        static String kind() {
            return "Dog.kind";
        }
    }
    // endregion override

    public static void main(String[] args) {
        // region calls
        Object text = "hello";                  // static type Object, dynamic type String
        System.out.println("describe(text)          -> " + describe(text));
        System.out.println("describe(\"hello\")       -> " + describe("hello"));

        Animal pet = new Dog();                 // static type Animal, dynamic type Dog
        System.out.println("pet.sound()             -> " + pet.sound());
        System.out.println("pet.name                -> " + pet.name);
        System.out.println("Animal.kind(), Dog.kind -> " + Animal.kind() + ", "
                + Dog.kind());
        // endregion calls
    }
}
