import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private final Point[] points;
    private final List<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        this.points = new Point[points.length];
        for (int i = 0; i < this.points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            this.points[i] = points[i];
        }
        Arrays.sort(this.points);
        for (int i = 0; i < this.points.length - 1; i++) {
            if (this.points[i].compareTo(this.points[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
        this.segments = new ArrayList<>();
        findLineSegments();
    }

    public int numberOfSegments() {
        return this.segments.size();
    }

    public LineSegment[] segments() {
        return this.segments.toArray(new LineSegment[0]);
    }

    private void findLineSegments() {
        for (Point point : this.points) {
            List<Point> copy = new ArrayList<>(List.of(this.points));
            copy.sort(point.slopeOrder());
            boolean discard = false;
            int numberOfPoints = 2;
            for (int j = 0; j < copy.size() - 1; j++) {
                if (point.compareTo(copy.get(j)) >= 0) {
                    discard = true;
                }
                double slope1 = point.slopeTo(copy.get(j));
                double slope2 = point.slopeTo(copy.get(j + 1));
                if (Double.compare(slope1, slope2) == 0) {
                    numberOfPoints++;
                    continue;
                }
                if (!discard && numberOfPoints >= 4) {
                    this.segments.add(new LineSegment(point, copy.get(j)));
                }
                discard = false;
                numberOfPoints = 2;
            }
            if (!discard && numberOfPoints >= 4) {
                this.segments.add(new LineSegment(point, copy.get(copy.size() - 1)));
            }
        }
    }
}