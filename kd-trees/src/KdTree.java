import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private static final int DIM = 2;
    private static final RectHV UNIT_SQUARE = new RectHV(0, 0, 1, 1);
    private final Node root;
    private int count;

    public KdTree() {
        this.root = new Node(UNIT_SQUARE);
        this.count = 0;
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    public int size() {
        return this.count;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (!contains(p)) {
            insert(p, this.root, 0);
            this.count++;
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return contains(p, this.root, 0);
    }

    public void draw() {
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> points = new ArrayList<>();
        range(points, rect, this.root, 0);
        return points;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }
        return nearest(this.root.point, p, this.root, 0);
    }

    private void insert(Point2D p, Node node, int cuttingDimension) {
        if (node.point == null) {
            node.point = p;
            double xmin = node.boundingBox.xmin();
            double ymin = node.boundingBox.ymin();
            double xmax = node.boundingBox.xmax();
            double ymax = node.boundingBox.ymax();
            if (cuttingDimension == 0) {
                node.lb = new Node(new RectHV(xmin, ymin, node.point.x(), ymax));
                node.rt = new Node(new RectHV(node.point.x(), ymin, xmax, ymax));
            } else {
                node.lb = new Node(new RectHV(xmin, ymin, xmax, node.point.y()));
                node.rt = new Node(new RectHV(xmin, node.point.y(), xmax, ymax));
            }
        } else {
            Node subtree = getSubtree(p, node, cuttingDimension);
            insert(p, subtree, (cuttingDimension + 1) % DIM);
        }
    }

    private boolean contains(Point2D p, Node node, int cuttingDimension) {
        if (node.point == null) {
            return false;
        }
        if (node.point.equals(p)) {
            return true;
        }
        Node subtree = getSubtree(p, node, cuttingDimension);
        return contains(p, subtree, (cuttingDimension + 1) % DIM);
    }

    private void range(List<Point2D> points, RectHV rect, Node node, int cuttingDimension) {
        if (node.point == null) {
            return;
        }
        if (rect.contains(node.point)) {
            points.add(node.point);
        }
        if (rect.intersects(node.lb.boundingBox)) {
            range(points, rect, node.lb, (cuttingDimension + 1) % DIM);
        }
        if (rect.intersects(node.rt.boundingBox)) {
            range(points, rect, node.rt, (cuttingDimension + 1) % DIM);
        }
    }

    private Point2D nearest(Point2D nearest, Point2D p, Node node, int cuttingDimension) {
        double min = p.distanceSquaredTo(nearest);
        double distSqToNode = p.distanceSquaredTo(node.point);
        double distSqToLB = node.lb.boundingBox.distanceSquaredTo(p);
        double distSqToRT = node.rt.boundingBox.distanceSquaredTo(p);
        if (Double.compare(distSqToNode, min) < 0) {
            nearest = node.point;
            min = distSqToNode;
        }
        if (Double.compare(Math.min(distSqToLB, distSqToRT), min) >= 0) {
            return nearest;
        }
        if (node.lb.point == null && node.rt.point == null) {
            return nearest;
        }
        if (node.lb.point == null) {
            if (Double.compare(distSqToRT, min) >= 0) {
                return nearest;
            }
            return nearest(nearest, p, node.rt, (cuttingDimension + 1) % DIM);
        }
        if (node.rt.point == null) {
            if (Double.compare(distSqToLB, min) >= 0) {
                return nearest;
            }
            return nearest(nearest, p, node.lb, (cuttingDimension + 1) % DIM);
        }
        if (Double.compare(distSqToLB, distSqToRT) < 0) {
            nearest = nearest(nearest, p, node.lb, (cuttingDimension + 1) % DIM);
            min = p.distanceSquaredTo(nearest);
            if (Double.compare(distSqToRT, min) >= 0) {
                return nearest;
            }
            return nearest(nearest, p, node.rt, (cuttingDimension + 1) % DIM);
        }
        nearest = nearest(nearest, p, node.rt, (cuttingDimension + 1) % DIM);
        min = p.distanceSquaredTo(nearest);
        if (Double.compare(distSqToLB, min) >= 0) {
            return nearest;
        }
        return nearest(nearest, p, node.lb, (cuttingDimension + 1) % DIM);
    }

    private Node getSubtree(Point2D p, Node node, int cuttingDimension) {
        if (cuttingDimension == 0) {
            return Double.compare(p.x(), node.point.x()) < 0 ? node.lb : node.rt;
        }
        return Double.compare(p.y(), node.point.y()) < 0 ? node.lb : node.rt;
    }

    private static class Node {
        private Point2D point;
        private final RectHV boundingBox;
        private Node lb;
        private Node rt;

        public Node(RectHV boundingBox) {
            this.boundingBox = boundingBox;
        }
    }
}