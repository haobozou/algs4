import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int dimension;
    private final WeightedQuickUnionUF sitesWithBackwash;
    private final WeightedQuickUnionUF sitesWithoutBackwash;
    private final boolean[] isOpen;
    private final int source;
    private final int target;
    private int numberOfOpenSites;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.dimension = n;
        int numberOfSites = this.dimension * this.dimension;
        this.sitesWithBackwash = new WeightedQuickUnionUF(numberOfSites + 2);
        this.sitesWithoutBackwash = new WeightedQuickUnionUF(numberOfSites + 1);
        this.isOpen = new boolean[numberOfSites + 1];
        this.source = 0;
        this.target = numberOfSites + 1;
        this.numberOfOpenSites = 0;
    }

    public void open(int row, int col) {
        validateIndices(row, col);
        if (!isOpen(row, col)) {
            unionNeighbors(row, col);
            this.isOpen[getUFIndex(row, col)] = true;
            this.numberOfOpenSites++;
        }
    }

    public boolean isOpen(int row, int col) {
        validateIndices(row, col);
        return this.isOpen[getUFIndex(row, col)];
    }

    public boolean isFull(int row, int col) {
        validateIndices(row, col);
        return this.sitesWithoutBackwash.find(getUFIndex(row, col)) == this.sitesWithoutBackwash.find(this.source);
    }

    public int numberOfOpenSites() {
        return this.numberOfOpenSites;
    }

    public boolean percolates() {
        return this.sitesWithBackwash.find(this.target) == this.sitesWithBackwash.find(this.source);
    }

    private void validateIndices(int row, int col) {
        if (!isIndicesValid(row, col)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isIndicesValid(int row, int col) {
        return row >= 1 && row <= this.dimension && col >= 1 && col <= this.dimension;
    }

    private int getUFIndex(int row, int col) {
        return (row - 1) * this.dimension + col;
    }

    private void unionNeighbors(int row, int col) {
        int[][] offsets = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        int p = getUFIndex(row, col);
        if (row == 1) {
            this.sitesWithBackwash.union(p, this.source);
            this.sitesWithoutBackwash.union(p, this.source);
        }
        if (row == this.dimension) {
            this.sitesWithBackwash.union(p, this.target);
        }
        for (int[] offset : offsets) {
            if (isIndicesValid(row + offset[0], col + offset[1]) && isOpen(row + offset[0], col + offset[1])) {
                int q = getUFIndex(row + offset[0], col + offset[1]);
                this.sitesWithBackwash.union(p, q);
                this.sitesWithoutBackwash.union(p, q);
            }
        }
    }
}