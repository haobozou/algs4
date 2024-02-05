import java.util.Arrays;

public class CircularSuffixArray {
    private final int length;
    private final Integer[] indices;

    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        this.length = s.length();
        this.indices = new Integer[this.length];
        for (int i = 0; i < this.length; i++) {
            this.indices[i] = i;
        }
        Arrays.sort(this.indices, (a, b) -> {
            for (int i = 0; i < this.length; i++) {
                char c1 = s.charAt((i + a) % this.length);
                char c2 = s.charAt((i + b) % this.length);
                if (c1 < c2) {
                    return -1;
                }
                if (c1 > c2) {
                    return 1;
                }
            }
            return 0;
        });
    }

    public int length() {
        return this.length;
    }

    public int index(int i) {
        if (i < 0 || i > length() - 1) {
            throw new IllegalArgumentException();
        }
        return this.indices[i];
    }
}