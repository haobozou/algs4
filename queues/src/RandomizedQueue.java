import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final int INITIAL_SIZE = 2;
    private Item[] items;
    private int size;

    public RandomizedQueue() {
        this.items = (Item[]) new Object[INITIAL_SIZE];
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (this.size == this.items.length) {
            resize(2 * this.items.length);
        }
        this.items[this.size++] = item;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (this.size == this.items.length / 4) {
            resize(this.items.length / 2);
        }
        int index = StdRandom.uniformInt(0, this.size);
        Item item = this.items[index];
        this.items[index] = this.items[--this.size];
        this.items[this.size] = null;
        return item;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniformInt(0, this.size);
        return this.items[index];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        System.arraycopy(this.items, 0, copy, 0, this.size);
        this.items = copy;
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private final int[] indices;
        private int current;

        public RandomizedQueueIterator() {
            this.indices = new int[size];
            for (int i = 0; i < this.indices.length; i++) {
                this.indices[i] = i;
            }
            StdRandom.shuffle(this.indices, 0, this.indices.length);
            this.current = 0;
        }

        @Override
        public boolean hasNext() {
            return this.current <= this.indices.length - 1 && this.indices[this.current] <= size - 1;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int index = this.indices[this.current++];
            return items[index];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
    }
}