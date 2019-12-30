import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;
public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private  int n;

    private class Node {
        Item item;
        Node previous;
        Node next;
    }

    public Deque() {
        // Initialize an empty deque
        first = null;
        last = null;
        n = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.previous = null;
        if (isEmpty()) {
            last = first;
            first.next = null;
        }
        else {
            first.next = oldfirst;
            oldfirst.previous = first;
        }
        n++;
    }

    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        if (isEmpty()) {
            first = last;
            last.previous = null;
        }
        else {
            oldlast.next = last;
            last.previous = oldlast;
        }
        n++;
    }

    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("No element");
        Item item = first.item;
        first = first.next;
        n--;
        if (isEmpty()) {
            first = null;
            last = null;
        }
        else {
            first.previous = null;
        }
        return item;
    }

    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("No element");
        Item item = last.item;
        last = last.previous;
        n--;
        if (isEmpty()) {
            first = null;
            last = null;
        }
        else {
            last.next = null;
        }
        return item;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Deque<Integer> q = new Deque<>();
        for (int i = 0; i < 10; i++) {
            q.addLast(i);
            StdOut.println(q.removeFirst());
        }

//        while(!StdIn.isEmpty()) {
//            String item = StdIn.readString();
//            if (!item.equals("-")) q.addLast(item);
//            else if (!q.isEmpty()) StdOut.print(q.removeLast() + " ");
//        }
    }
}
