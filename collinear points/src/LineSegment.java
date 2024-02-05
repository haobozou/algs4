public class LineSegment {
    private final Point p;
    private final Point q;

    public LineSegment(Point p, Point q) {
        if (p == null || q == null) {
            throw new IllegalArgumentException();
        }
        if (p.equals(q)) {
            throw new IllegalArgumentException();
        }
        this.p = p;
        this.q = q;
    }

    public void draw() {
    }

    @Override
    public String toString() {
        return this.p + " -> " + this.q;
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}