import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private Node root;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        MinPQ<Node> minPQ = new MinPQ<>();
        minPQ.insert(new Node(initial, 0, null));
        MinPQ<Node> twinMinPQ = new MinPQ<>();
        twinMinPQ.insert(new Node(initial.twin(), 0, null));
        this.root = minPQ.delMin();
        Node twinRoot = twinMinPQ.delMin();
        while (!this.root.board.isGoal() && !twinRoot.board.isGoal()) {
            insertNeighbors(minPQ, this.root);
            insertNeighbors(twinMinPQ, twinRoot);
            this.root = minPQ.delMin();
            twinRoot = twinMinPQ.delMin();
        }
    }

    public boolean isSolvable() {
        return this.root.board.isGoal();
    }

    public int moves() {
        return isSolvable() ? this.root.moves : -1;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        Stack<Board> solution = new Stack<>();
        Node node = this.root;
        while (node != null) {
            solution.push(node.board);
            node = node.prev;
        }
        return solution;
    }

    private void insertNeighbors(MinPQ<Node> minPQ, Node node) {
        for (Board neighbor : node.board.neighbors()) {
            if (node.prev == null || !node.prev.board.equals(neighbor)) {
                minPQ.insert(new Node(neighbor, node.moves + 1, node));
            }
        }
    }

    private static class Node implements Comparable<Node> {
        private final Board board;
        private final int moves;
        private final Node prev;
        private final int manhattanPriority;

        public Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.manhattanPriority = board.manhattan() + moves;
        }

        @Override
        public int compareTo(Node that) {
            return this.manhattanPriority - that.manhattanPriority;
        }
    }
}