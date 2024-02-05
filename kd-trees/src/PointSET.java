import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PointSET {
    private final Set<Point2D> pointSet;

    public PointSET() {
        this.pointSet = new TreeSet<>();
    }

    public boolean isEmpty() {
        return this.pointSet.isEmpty();
    }

    public int size() {
        return this.pointSet.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        this.pointSet.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return this.pointSet.contains(p);
    }

    public void draw() {
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> points = new ArrayList<>();
        for (Point2D point : this.pointSet) {
            if (rect.contains(point)) {
                points.add(point);
            }
        }
        return points;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D nearest = null;
        double min = Double.POSITIVE_INFINITY;
        for (Point2D point : this.pointSet) {
            if (Double.compare(p.distanceSquaredTo(point), min) < 0) {
                nearest = point;
                min = p.distanceSquaredTo(point);
            }
        }
        return nearest;
    }
}