import java.util.Comparator;

public class Point implements Comparable<Point> {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw() {
    }

    public void drawTo(Point that) {
    }

    public double slopeTo(Point that) {
        if (this.compareTo(that) == 0) {
            return Double.NEGATIVE_INFINITY;
        }
        if (this.x == that.x) {
            return Double.POSITIVE_INFINITY;
        }
        if (this.y == that.y) {
            return 0.0;
        }
        return (double) (that.y - this.y) / (that.x - this.x);
    }

    public int compareTo(Point that) {
        if (this.y == that.y) {
            return this.x - that.x;
        }
        return this.y - that.y;
    }

    public Comparator<Point> slopeOrder() {
        return (a, b) -> Double.compare(slopeTo(a), slopeTo(b));
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}