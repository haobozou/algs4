import edu.princeton.cs.algs4.Picture;

import java.util.ArrayList;
import java.util.List;

public class SeamCarver {
    private int width, height;
    private int[][] pixels;
    private boolean isTransposed;

    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.width = picture.width();
        this.height = picture.height();
        this.pixels = new int[this.height][this.width];
        for (int row = 0; row < this.pixels.length; row++) {
            for (int col = 0; col < this.pixels[0].length; col++) {
                this.pixels[row][col] = picture.getRGB(col, row);
            }
        }
        this.isTransposed = false;
    }

    public Picture picture() {
        Picture picture = new Picture(this.width, this.height);
        for (int row = 0; row < this.height; row++) {
            for (int col = 0; col < this.width; col++) {
                int rgb = this.isTransposed ? this.pixels[col][row] : this.pixels[row][col];
                picture.setRGB(col, row, rgb);
            }
        }
        return picture;
    }

    public int width() {
        return this.width;
    }

    public int height() {
        return this.height;
    }

    public double energy(int x, int y) {
        if (this.isTransposed) {
            return getEnergy(y, x);
        }
        return getEnergy(x, y);
    }

    public int[] findHorizontalSeam() {
        if (!this.isTransposed) {
            transpose();
        }
        return getSeam();
    }

    public int[] findVerticalSeam() {
        if (this.isTransposed) {
            transpose();
        }
        return getSeam();
    }

    public void removeHorizontalSeam(int[] seam) {
        validateSeam(seam, this.width, this.height);
        if (this.height <= 1) {
            throw new IllegalArgumentException();
        }
        if (!this.isTransposed) {
            transpose();
        }
        removeSeam(seam);
        this.height--;
    }

    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam, this.height, this.width);
        if (this.width <= 1) {
            throw new IllegalArgumentException();
        }
        if (this.isTransposed) {
            transpose();
        }
        removeSeam(seam);
        this.width--;
    }

    private double getEnergy(int col, int row) {
        if (!isIndexValid(col, this.pixels[0].length) || !isIndexValid(row, this.pixels.length)) {
            throw new IllegalArgumentException();
        }
        if (col == 0 || col == this.pixels[0].length - 1 || row == 0 || row == this.pixels.length - 1) {
            return 1000;
        }
        int top = this.pixels[row - 1][col];
        int bottom = this.pixels[row + 1][col];
        int left = this.pixels[row][col - 1];
        int right = this.pixels[row][col + 1];
        return Math.sqrt(gradientSq(top, bottom) + gradientSq(left, right));
    }

    private boolean isIndexValid(int index, int bound) {
        return index >= 0 && index <= bound - 1;
    }

    private int gradientSq(int pixelA, int pixelB) {
        int dR = (pixelB >> 16 & 0xFF) - (pixelA >> 16 & 0xFF);
        int dG = (pixelB >> 8 & 0xFF) - (pixelA >> 8 & 0xFF);
        int dB = (pixelB & 0xFF) - (pixelA & 0xFF);
        return dR * dR + dG * dG + dB * dB;
    }

    private void transpose() {
        int[][] copy = new int[this.pixels[0].length][this.pixels.length];
        for (int i = 0; i < copy.length; i++) {
            for (int j = 0; j < copy[0].length; j++) {
                copy[i][j] = this.pixels[j][i];
            }
        }
        this.pixels = copy;
        this.isTransposed = !this.isTransposed;
    }

    private int[] getSeam() {
        double[][] energy = new double[this.pixels.length][this.pixels[0].length];
        double[][] distTo = new double[this.pixels.length][this.pixels[0].length];
        int[][] colTo = new int[this.pixels.length][this.pixels[0].length];
        for (int row = 0; row < this.pixels.length; row++) {
            for (int col = 0; col < this.pixels[0].length; col++) {
                energy[row][col] = getEnergy(col, row);
                distTo[row][col] = Double.POSITIVE_INFINITY;
            }
        }
        System.arraycopy(energy[0], 0, distTo[0], 0, this.pixels[0].length);
        for (int row = 1; row < this.pixels.length; row++) {
            for (int col = 0; col < this.pixels[0].length; col++) {
                for (int parentCol : getParentCols(col)) {
                    if (Double.compare(distTo[row - 1][parentCol] + energy[row][col], distTo[row][col]) < 0) {
                        distTo[row][col] = distTo[row - 1][parentCol] + energy[row][col];
                        colTo[row][col] = parentCol;
                    }
                }
            }
        }

        int[] seam = new int[this.pixels.length];
        double min = Double.POSITIVE_INFINITY;
        for (int col = 0; col < this.pixels[0].length; col++) {
            if (Double.compare(distTo[this.pixels.length - 1][col], min) < 0) {
                seam[this.pixels.length - 1] = col;
                min = distTo[this.pixels.length - 1][col];
            }
        }
        for (int i = this.pixels.length - 1; i > 0; i--) {
            seam[i - 1] = colTo[i][seam[i]];
        }
        return seam;
    }

    private Iterable<Integer> getParentCols(int col) {
        int[] offsets = {-1, 0, 1};
        List<Integer> cols = new ArrayList<>();
        for (int offset : offsets) {
            if (isIndexValid(col + offset, this.pixels[0].length)) {
                cols.add(col + offset);
            }
        }
        return cols;
    }

    private void validateSeam(int[] seam, int length, int bound) {
        if (seam == null || seam.length != length) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length; i++) {
            if (!isIndexValid(seam[i], bound)) {
                throw new IllegalArgumentException();
            }
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void removeSeam(int[] seam) {
        int[][] copy = new int[this.pixels.length][this.pixels[0].length - 1];
        for (int i = 0; i < this.pixels.length; i++) {
            System.arraycopy(this.pixels[i], 0, copy[i], 0, seam[i]);
            if (seam[i] < this.pixels[0].length - 1)
                System.arraycopy(this.pixels[i], seam[i] + 1, copy[i], seam[i], this.pixels[0].length - 1 - seam[i]);
        }
        this.pixels = copy;
    }
}