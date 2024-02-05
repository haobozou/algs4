import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;

    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
        int first = 0;
        char[] t = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            if (circularSuffixArray.index(i) == 0) {
                first = i;
            }
            t[i] = s.charAt((circularSuffixArray.index(i) + s.length() - 1) % s.length());
        }
        BinaryStdOut.write(first);
        for (int i = 0; i < s.length(); i++) {
            BinaryStdOut.write(t[i]);
        }
        BinaryStdOut.close();
    }

    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int[] count = new int[R + 1];
        int[] next = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            count[s.charAt(i) + 1]++;
        }
        for (int i = 0; i < R; i++) {
            count[i + 1] += count[i];
        }
        for (int i = 0; i < s.length(); i++) {
            next[count[s.charAt(i)]++] = i;
        }
        for (int i = 0; i < s.length(); i++) {
            BinaryStdOut.write(s.charAt(next[first]));
            first = next[first];
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        }
        if (args[0].equals("+")) {
            inverseTransform();
        }
    }
}