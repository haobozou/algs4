import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        int numberOfStrings = 0;
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String string = StdIn.readString();
            numberOfStrings++;
            if (numberOfStrings <= k) {
                randomizedQueue.enqueue(string);
                continue;
            }
            if (StdRandom.uniformInt(1, numberOfStrings + 1) <= k) {
                randomizedQueue.dequeue();
                randomizedQueue.enqueue(string);
            }
        }
        randomizedQueue.forEach(StdOut::println);
    }
}