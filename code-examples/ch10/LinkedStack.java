import java.util.NoSuchElementException;

/** A generic stack built from linked nodes; every operation is O(1) in the worst case. */
public class LinkedStack<T> {

    private static final class Node<E> {
        final E value;
        final Node<E> next;  // the node below this one

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<T> top;  // null when the stack is empty
    private int size;

    public void push(T value) {
        top = new Node<>(value, top);
        size++;
    }

    public T pop() {
        if (top == null) {
            throw new NoSuchElementException("pop from an empty stack");
        }
        T value = top.value;
        top = top.next;  // the old top node is now unreachable
        size--;
        return value;
    }

    public T peek() {
        if (top == null) {
            throw new NoSuchElementException("peek at an empty stack");
        }
        return top.value;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return size;
    }

    public static void main(String[] args) {
        LinkedStack<String> pages = new LinkedStack<>();
        pages.push("home");
        pages.push("search");
        pages.push("results");
        System.out.println("current page: " + pages.peek());
        String left = pages.pop();
        System.out.println("left " + left + ", back on " + pages.peek());
        System.out.println("pages left: " + pages.size());
    }
}
