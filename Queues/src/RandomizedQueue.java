import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item>{
    /*
       public RandomizedQueue()                 // construct an empty randomized queue
       public boolean isEmpty()                 // is the randomized queue empty?
       public int size()                        // return the number of items on the randomized queue
       public void enqueue(Item item)           // add the item
       public Item dequeue()                    // remove and return a random item
       public Item sample()                     // return a random item (but do not remove it)
       public Iterator<Item> iterator()         // return an independent iterator over items in random order
       public static void main(String[] args)   // unit testing (optional)
    */
    private Item[] q;  // queue elements
    private int n;     // number of elements on the queue
    private int first; // index of first element
    private int last;  // index of last element

    public RandomizedQueue() {
        q = (Item[]) new Object[2];
        n = 0;
        first = 0;
        last = 0;
    }

    public boolean isEmpty() { return n == 0; }

    public int size() { return n; }

    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = q[(first + i) % q.length];
        }
        q = temp;
        first = 0;
        last = n;
    }

    /*
    add item to the last of array, if last == q.length, wrap around.
     */
    public void enqueue(Item item) {
        if (item == null) { throw new IllegalArgumentException(); }
        if (n == q.length) resize(2 * q.length);
        q[last++] = item;
        if (last == q.length) last = 0;     //wrap-around
        n++;
    }

    /*
    generate a random number, swap it with the first item, then dequeue the first item.
    if n < q.length / 4 or n > q.length, resizing array
    if first == q.length, wrap around
     */
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        int r;
        r = (StdRandom.uniform(n) + first) % q.length;
        Item item = q[r];
        q[r] = q[first];
        q[first] = null;
        first++;
        n--;
        if (first == q.length) first = 0;  //wrap around
        //shrink size of array if necessary
        if (n > 0 && n == q.length/4) resize(q.length/2);
        return item;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        int r;
        r = (StdRandom.uniform(n) + first) % q.length;
        Item item = q[r];
        return item;
    }

    public Iterator<Item> iterator() {
        return new RandomQueueIterator();
    }

    private class RandomQueueIterator implements Iterator<Item> {
        private int i = 0;
        private final Item[] q2;

        private RandomQueueIterator() {
            q2 = (Item[]) new Object[n];
            for (int j = 0; j < q2.length; j++) q2[j] = q[(first + j) % q.length];
            StdRandom.shuffle(q2);
        }

        public boolean hasNext() { return i < n; }
        public void remove() { throw new UnsupportedOperationException(); }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = q2[i];
            i++;
            return item;
        }
    }
}
