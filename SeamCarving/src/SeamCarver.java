import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

/**
 * Solution for programming assignment in Algorithms Part 2
 * https://coursera.cs.princeton.edu/algs4/assignments/seam/specification.php
 *
 * Taken a picture as input, do seam carving.
 *
 * 使用的算法类似于Topological Shortest Path Algorithm. 遍历整个图片的每个像素点，维持更新两个数组，一个保存
 * 到第一排的距离disTo[][]，一个保存对应的上一个像素edgTo[][]。
 * 每个像素点有三个ancestor，<em>relax</em>每个像素点.
 * 遍历完后，从最底一行中挑选最先的disTo，逐个向上追溯，获取整个vertical seam line.
 *
 */


public class SeamCarver {
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = new Picture(picture);
    }

    // current picture, if it is transposed, return the original picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // return energy of pixel at column x and row y
    public double energy(int x, int y) {
        validateXY(x, y);
        if (x == 0 || x == width()-1 || y == 0 || y == height()-1) return 1000;
        int upRGB = picture.get(x, y-1).getRGB();
        int downRGB = picture.get(x, y+1).getRGB();
        int leftRGB = picture.get(x-1, y).getRGB();
        int rightRGB = picture.get(x+1, y).getRGB();

        int rx = ((leftRGB >> 16) & 0xFF) - ((rightRGB >> 16) & 0xFF);
        int gx = ((leftRGB >> 8) & 0xFF) - ((rightRGB >> 8) & 0xFF);
        int bx = ((leftRGB >> 0) & 0xFF) - ((rightRGB >> 0) & 0xFF);

        int ry = ((upRGB >> 16) & 0xFF) - ((downRGB >> 16) & 0xFF);
        int gy = ((upRGB >> 8) & 0xFF) - ((downRGB >> 8) & 0xFF);
        int by = ((upRGB >> 0) & 0xFF) - ((downRGB >> 0) & 0xFF);

        return Math.sqrt(rx*rx + gx*gx + bx*bx + ry*ry + gy*gy + by*by);
    }

    private static class Pair {
        private int col;
        private int row;
        private Pair(int col, int row) {
            this.col = col;
            this.row = row;
        }
        private void setColRow(int col, int row) {
            this.col = col;
            this.row = row;
        }
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energy = new double[width()][height()];// energy of each point in picture
        double[][] disTo = new double[width()][height()]; // distance to the first row
        Pair[][] edgeTo = new Pair[width()][height()];    // the edge of former point in picture

        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                energy[col][row] = energy(col, row);        // calculate energy of each point
                disTo[col][row] = Double.POSITIVE_INFINITY; // initialize disTo to infinity
                edgeTo[col][row] = new Pair(Integer.MAX_VALUE, Integer.MAX_VALUE); //
            }
        }

        // go through every point, relax the edges pointed to this point.
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                // if is the first row, initialize disTo to zero, edgeTo to itself
                if (row == 0) {
                    disTo[col][row] = 0;
                    edgeTo[col][row].setColRow(col, row);
                }

                // else relax every other point not in the first row
                else {
                    relax(col, row, energy, disTo, edgeTo);
                }
            }
        }

        // go through the bottom line, find the point with the smallest disTo
        double min = Double.POSITIVE_INFINITY;
        int minBottomCol = Integer.MAX_VALUE;
        for (int col = 0; col < width(); col++) {
            if (disTo[col][height()-1] < min) {
                min = disTo[col][height()-1];
                minBottomCol = col;
            }
        }

        // from the minBottomCol point, trace back to find the sequences of indies for vertical seam
        int[] verticalSeam = new int[height()];
        int minCol = minBottomCol;
        for (int row = height()-1; row >=0; row--) {
            verticalSeam[row] = minCol;
            minCol = edgeTo[minCol][row].col;
        }

        return verticalSeam;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] horizontalSeam = findVerticalSeam();
        transpose();
        return horizontalSeam;
    }

    // transpose the picture, update boolean isTransposed.
    private void transpose() {
        picture = transposePicture(picture);
    }

    private Picture transposePicture(Picture pic) {
        Picture tmp = new Picture(pic.height(), pic.width());
        for (int row = 0; row < pic.height(); row++) {
            for (int col = 0; col < pic.width(); col++) {
                tmp.setRGB(row, col, pic.getRGB(col, row));
            }
        }
        return tmp;
    }

    // relax the point, update edgeTo and disTo, if the disTo(curr) > disTo(ancestor) + energy(ancestor)
    private void relax(int col, int row, double[][] energy, double[][] disTo, Pair[][] edgeTo) {
        // relax its up-left edge
        if (col-1 >= 0 && disTo[col][row] > disTo[col-1][row-1] + energy[col-1][row-1]) {
            edgeTo[col][row].setColRow(col-1, row-1);
            disTo[col][row] = disTo[col-1][row-1] + energy[col-1][row-1];
        }
        // relax its up-right edge
        if (col+1 <= width()-1 && disTo[col][row] > disTo[col+1][row-1] + energy[col+1][row-1]) {
            edgeTo[col][row].setColRow(col+1, row-1);
            disTo[col][row] = disTo[col+1][row-1] + energy[col+1][row-1];
        }
        // relax its upper edger
        if (disTo[col][row] > disTo[col][row-1] + energy[col][row-1]) {
            edgeTo[col][row].setColRow(col, row-1);
            disTo[col][row] = disTo[col][row-1] + energy[col][row-1];
        }
    }


    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (width()<=1) throw new IllegalArgumentException(
                "call removeVerticalSeam while picture width is less or equal than 1");
        if (seam.length != height()) throw new IllegalArgumentException("seam in wrong length");
        for (int i = 0; i < seam.length; i++)
            if (seam[i] < 0 || seam[i] > width()-1) throw new IllegalArgumentException();
        validate(seam);

        Picture newPic = new Picture(width()-1, height());
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width()-1; col++) {
                if (col < seam[row]) newPic.setRGB(col, row, picture.getRGB(col, row));
                else if (col >= seam[row]) newPic.setRGB(col, row, picture.getRGB(col+1, row));
            }
        }
        picture = newPic;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (height()<=1) throw new IllegalArgumentException(
                "call removeHorizontalSeam while picture height is less or equal than 1");
        if (seam.length != width()) throw new IllegalArgumentException("seam in wrong length");
        for (int i = 0; i < seam.length; i++)
            if (seam[i] < 0 || seam[i] > height()-1) throw new IllegalArgumentException();
        validate(seam);
        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    private void validateXY(int x, int y) {
        if (x < 0 || x > width()-1) throw new IllegalArgumentException("x out of bound");
        if (y < 0 || y > height()-1) throw new IllegalArgumentException("x out of bound");
    }

    private void validate(int[] seam) {
        for (int i = 0; i < seam.length-1; i++){
            if (Math.abs(seam[i+1] - seam[i]) > 1) throw new IllegalArgumentException(
                    "two adjacent entries differ by more than 1");
        }
    }
}
