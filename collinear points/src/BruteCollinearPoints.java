import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private final Point[] points;
    private final List<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) {
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
        for (int i = 0; i < this.points.length; i++) {
            for (int j = i + 1; j < this.points.length; j++) {
                for (int k = j + 1; k < this.points.length; k++) {
                    double slope1 = this.points[i].slopeTo(this.points[j]);
                    double slope2 = this.points[i].slopeTo(this.points[k]);
                    if (Double.compare(slope1, slope2) != 0) {
                        continue;
                    }
                    for (int m = k + 1; m < this.points.length; m++) {
                        double slope3 = this.points[i].slopeTo(this.points[m]);
                        if (Double.compare(slope1, slope3) == 0) {
                            this.segments.add(new LineSegment(this.points[i], this.points[m]));
                        }
                    }
                }
            }
        }
    }
}