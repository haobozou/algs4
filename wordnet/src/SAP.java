import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

import java.util.List;

public class SAP {
    private final Digraph digraph;
    private BreadthFirstDirectedPaths bfsV, bfsW;

    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
        this.digraph = new Digraph(G);
    }

    public int length(int v, int w) {
        return length(List.of(v), List.of(w));
    }

    public int ancestor(int v, int w) {
        return ancestor(List.of(v), List.of(w));
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int ancestor = ancestor(v, w);
        if (ancestor == -1) {
            return -1;
        }
        return this.bfsV.distTo(ancestor) + this.bfsW.distTo(ancestor);
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        if (isEmpty(v) || isEmpty(w)) {
            return -1;
        }
        this.bfsV = new BreadthFirstDirectedPaths(this.digraph, v);
        this.bfsW = new BreadthFirstDirectedPaths(this.digraph, w);
        int ancestor = -1;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < this.digraph.V(); i++) {
            if (this.bfsV.distTo(i) == Integer.MAX_VALUE || this.bfsW.distTo(i) == Integer.MAX_VALUE) {
                continue;
            }
            if (this.bfsV.distTo(i) + this.bfsW.distTo(i) < min) {
                ancestor = i;
                min = this.bfsV.distTo(i) + this.bfsW.distTo(i);
            }
        }
        return ancestor;
    }

    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException();
        }
        for (Integer vertex : vertices) {
            if (vertex == null || vertex < 0 || vertex > this.digraph.V() - 1) {
                throw new IllegalArgumentException();
            }
        }
    }

    private boolean isEmpty(Iterable<Integer> vertices) {
        int size = 0;
        for (Integer ignored : vertices) {
            size++;
        }
        return size == 0;
    }
}