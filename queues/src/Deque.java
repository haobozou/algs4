import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int size;

    public Deque() {
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node node = new Node(item);
        if (isEmpty()) {
            this.first = node;
            this.last = node;
        } else {
            node.next = this.first;
            this.first.prev = node;
            this.first = node;
        }
        this.size++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node node = new Node(item);
        if (isEmpty()) {
            this.first = node;
            this.last = node;
        } else {
            node.prev = this.last;
            this.last.next = node;
            this.last = node;
        }
        this.size++;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = this.first.item;
        if (this.size == 1) {
            this.first = null;
            this.last = null;
        } else {
            this.first = this.first.next;
            this.first.prev = null;
        }
        this.size--;
        return item;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = this.last.item;
        if (this.size == 1) {
            this.first = null;
            this.last = null;
        } else {
            this.last = this.last.prev;
            this.last.next = null;
        }
        this.size--;
        return item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class Node {
        private final Item item;
        private Node prev, next;

        public Node(Item item) {
            this.item = item;
        }
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current;

        public DequeIterator() {
            this.current = first;
        }

        @Override
        public boolean hasNext() {
            return this.current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = this.current.item;
            this.current = this.current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
    }
}