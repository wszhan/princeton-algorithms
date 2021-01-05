import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.NoSuchElementException;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final int INIT_CAPACITY = 8;

    private Item[] queue;
    private int length;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[INIT_CAPACITY];
        length = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() { 
        return length == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return length;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("enqueuing null");
        if (length == queue.length) resize(2 * queue.length);

        // enqueue and update length
        queue[length++] = item;
    }

    private void resize(int capacity) {
        assert capacity > 0;

        // array of new size
        Item[] copy = (Item[]) new Object[capacity];

        // copy
        for (int i = 0; i < length; i++) {
            copy[i] = queue[i];
        }

        queue = copy;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("no element to dequeue");

        // random index for dequeuing
        int randomIndex = StdRandom.uniform(0, length);

        Item dequeuedItem = queue[randomIndex];

        // avoid loitering
        queue[randomIndex] = queue[length-1]; 
        queue[length-1] = null;

        // update length
        length--;

        // resize
        if (length > 0 && length == queue.length/4) resize(queue.length/2);

        return dequeuedItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("no element to sample");

        // random index 
        int randomIndex = StdRandom.uniform(0, length);
        return queue[randomIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private final Item[] shuffledArray; 
        private int currentIndex;

        private RandomizedQueueIterator() {
            shuffledArray = (Item[]) new Object[length];

            for (int i = 0; i < length; i++) {
                shuffledArray[i] = queue[i];
            }

            StdRandom.shuffle(shuffledArray);

            currentIndex = 0;
        }

        public boolean hasNext() { 
            return currentIndex < length; 
        }
        
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No more elements");
            Item item = shuffledArray[currentIndex++];

            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("No remove operation in iterator");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        if (args.length > 0) {
            In in = new In(args[0]);
            RandomizedQueue<String> rq = new RandomizedQueue<>();
            int i = 0;

            while (!in.isEmpty()) {
                String s = in.readString();
                rq.enqueue(s);    
                i++;
            }

            StdOut.println("\ni - " + i);
            StdOut.println("iterating...");

            for (String s : rq) {
                StdOut.println(s);
            }

            StdOut.println("\nSampling...");
            int count = i;
            while (count > 0) {
                String s = rq.sample();
                StdOut.println("sampleing item #" + i + " - " + s);
                count--;
            }
            
            StdOut.println("\nDequeuing...");
            while (i > 0) {
                String s = rq.dequeue();
                StdOut.println("removing item #" + i + " - " + s);
                i--;
            }

        } else {
            StdOut.println("No input file.");
        }
    }
}