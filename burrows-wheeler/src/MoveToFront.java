import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    public static void encode() {
        char[] alphabet = new char[R];
        for (int i = 0; i < R; i++) {
            alphabet[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int index = 0;
            for (int i = 0; i < R; i++) {
                if (alphabet[i] == c) {
                    index = i;
                    break;
                }
            }
            System.arraycopy(alphabet, 0, alphabet, 1, index);
            alphabet[0] = c;
            BinaryStdOut.write(index, 8);
        }
        BinaryStdOut.close();
    }

    public static void decode() {
        char[] alphabet = new char[R];
        for (int i = 0; i < R; i++) {
            alphabet[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readChar();
            char c = alphabet[index];
            System.arraycopy(alphabet, 0, alphabet, 1, index);
            alphabet[0] = c;
            BinaryStdOut.write(c);
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        }
        if (args[0].equals("+")) {
            decode();
        }
    }
}