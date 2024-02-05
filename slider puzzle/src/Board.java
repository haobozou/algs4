import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int dimension;
    private final int[][] tiles;
    private int blankRow;
    private int blankCol;
    private int hamming, manhattan;

    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException();
        }
        this.dimension = tiles.length;
        this.tiles = new int[this.dimension][this.dimension];
        this.hamming = 0;
        this.manhattan = 0;
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (this.tiles[i][j] == 0) {
                    this.blankRow = i;
                    this.blankCol = j;
                    continue;
                }
                int goalRow = (this.tiles[i][j] - 1) / this.dimension;
                int goalCol = (this.tiles[i][j] - 1) % this.dimension;
                if (i != goalRow || j != goalCol) {
                    this.hamming++;
                    this.manhattan += Math.abs(i - goalRow) + Math.abs(j - goalCol);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.dimension + "\n");
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                stringBuilder.append(" ").append(this.tiles[i][j]).append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public int dimension() {
        return this.dimension;
    }

    public int hamming() {
        return this.hamming;
    }

    public int manhattan() {
        return this.manhattan;
    }

    public boolean isGoal() {
        return this.hamming == 0;
    }

    @Override
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (this.getClass() != y.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (this.dimension != that.dimension) {
            return false;
        }
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        int[][] offsets = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        List<Board> neighbors = new ArrayList<>();
        for (int[] offset : offsets) {
            if (isIndicesValid(this.blankRow + offset[0], this.blankCol + offset[1])) {
                int[] tileA = {this.blankRow, this.blankCol};
                int[] tileB = {this.blankRow + offset[0], this.blankCol + offset[1]};
                neighbors.add(swap(tileA, tileB));
            }
        }
        return neighbors;
    }

    public Board twin() {
        if (this.tiles[0][0] != 0 && this.tiles[0][1] != 0) {
            return swap(new int[]{0, 0}, new int[]{0, 1});
        }
        return swap(new int[]{1, 0}, new int[]{1, 1});
    }

    private boolean isIndicesValid(int row, int col) {
        return row >= 0 && row <= this.dimension - 1 && col >= 0 && col <= this.dimension - 1;
    }

    private Board swap(int[] tileA, int[] tileB) {
        int[][] copy = new int[this.dimension][this.dimension];
        for (int i = 0; i < this.dimension; i++) {
            System.arraycopy(this.tiles[i], 0, copy[i], 0, this.dimension);
        }
        copy[tileA[0]][tileA[1]] = this.tiles[tileB[0]][tileB[1]];
        copy[tileB[0]][tileB[1]] = this.tiles[tileA[0]][tileA[1]];
        return new Board(copy);
    }
}