import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.NoSuchElementException;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int length;

    private class Node {
        Item value;
        Node prev, next;

        private Node(Item value) {
            this.value = value;
            this.prev = null;
            this.next = null;
        }
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        length = 0;
    }

    // is the deque empty?
    public boolean isEmpty() { return length == 0; }

    // return the number of items on the deque
    public int size() { return length; }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Add null to first");
        Node oldFirst = first;
        first = new Node(item);

        // update pointers
        first.next = oldFirst;

        length++;

        // edge case
        if (size() == 1) { last = first; }
        else { oldFirst.prev = first; }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Add null to last");
        Node oldLast = last;
        last = new Node(item);

        // update pointers
        last.prev = oldLast;
        length++;

        // edge case
        if (size() == 1) { first = last; } 
        else { oldLast.next = last; }
    }

    // remove and return the item from the front
    public Item removeFirst() { 
        Node oldFirst = first;

        if (oldFirst == null) {
            throw new NoSuchElementException("remove null from first");
        }

        first = first.next; // first.next could be null
        Item firstItem = oldFirst.value;
        // if the new first != null, first.prev should be null 
        if (first != null) {
            first.prev = null;
        } else {
            last = null;
        }

        length--;
        return firstItem;
    }

    // remove and return the item from the back
    public Item removeLast() {
        Node oldLast = last;

        if (oldLast == null) {
            throw new NoSuchElementException("remove null from last");
        }

        last = last.prev; // last.prev could be null (only 1 element before removing)
        Item lastItem = oldLast.value;
        // if the new last != null, last.next should be null
        if (last != null) {
            last.next = null;
        } else {
            first = null;
        }

        length--;

        return lastItem;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() { return current != null; }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No more elements");

            Item item = current.value;
            current = current.next;

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
            Deque<String> dq = new Deque<>();
            int i = 0;

            while (!in.isEmpty()) {
                String s = in.readString();
                dq.addLast(s);    
                StdOut.println("adding item #" + i + " - " + s);
                i++;
            }

            StdOut.println("i - " + i);
            StdOut.println("iterating...");
            for (String s : dq) {
                StdOut.println(s);
            }

            while (i > 0) {
                String s = dq.removeLast();
                StdOut.println("removing item #" + i + " - " + s);
                i--;
            }
        } else {
            Deque<Integer> deque = new Deque<>();

            deque.addLast(1);
            // deque.addLast(3);
            Integer i = deque.removeLast();
            StdOut.println("Removed: " + i);

            StdOut.println("iterating...");
            for (Integer s : deque) {
                StdOut.println(s);
            }
        }
    }
}