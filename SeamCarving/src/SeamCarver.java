import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

/**
 * Solution for programming assignment in Algorithms Part 2
 * https://coursera.cs.princeton.edu/algs4/assignments/seam/specification.php
 */


public class SeamCarver {
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return picture;
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
        if (x == 0 || x == width()-1 || y == 0 || y == height()-1) return 1000;
        Color up = picture.get(x, y-1);
        Color down = picture.get(x, y+1);
        Color left = picture.get(x-1, y);
        Color right = picture.get(x+1, y);

        double rx = left.getRed() - right.getRed();
        double gx = left.getGreen() - right.getGreen();
        double bx = left.getBlue() - right.getBlue();

        double ry = up.getRed() - down.getRed();
        double gy = up.getGreen() - down.getGreen();
        double by = up.getBlue() - down.getBlue();

        return Math.sqrt(rx*rx + gx*gx + bx*bx + ry*ry + gy*gy + by*by);
    }

}
