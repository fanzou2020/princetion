import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Insertion;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        fastCollinearPoints(points);
    }

    private void fastCollinearPoints(Point[] points) {
        // Corner cases
        validateIndex(points);

        int n = points.length;
        Point[] copy = points.clone();

        // for each points, calculate the slopes of all other points to this point
        // by slopeOrder().
        for (Point p : points) {
            Arrays.sort(copy, p.slopeOrder());
            double[] slope = new double[n];
            for (int k = 0; k < n; k++) {
                slope[k] = copy[k].slopeTo(p);
            }

            // find the points whose slope are equal
            for (int j = 1; j < n-1; j++) {
                int start = j;
                int end;
                while (slope[j] == slope[j+1]) {
                    j++;
                    if (j == n-1) break;
                }
                end = j+1;
                int length = end - start;
                if (length < 3) continue;
                else {
                    Point[] subPoints = new Point[length];
                    for (int m = 0; m < length; m++) {
                        subPoints[m] = copy[m+start];
                    }
                    Insertion.sort(subPoints);
                    if (p.compareTo(subPoints[0]) < 0) {
                        lineSegments.add(new LineSegment(p, subPoints[length-1]));
                    }
                }

            }

//            for (int k = 0; k < n; k++) {
//                slope[k] = copy[k].slopeTo(p);
//            }
//
//            // find the points whose slope are equal
//            // the points between start and end is are equal slope
//            int start = 0;
//            for (int end = 1; end < n; end++) {
//                if (slope[end] == slope[end-1]) {
//                    end++;
//                }
//                else {
//                    start = end++;
//                }
//                if ((end - start) >= 3 && isFinalSegment(copy, p, start, end)) {
//                    lineSegments.add(new LineSegment(p, copy[end-1]));
//                }
//            }
        }
    }

//    private boolean isFinalSegment(Point[] copy, Point p, int start, int length) {
//        for (int m = 0; m < length; m++) {
//            if (p.compareTo(copy[m+start]) >= 0) return false;
//        }
//        return true;
//    }

    public int numberOfSegments() {return lineSegments.size(); }

    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[numberOfSegments()];
        return lineSegments.toArray(segments);
    }


    private void validateIndex(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Point[] is null");
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException("elements is null");
            for (int j = i+1; j < points.length; j++) {
                if (points[i].equals(points[j])) throw new IllegalArgumentException("Duplicates");
            }
        }
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        // StdDraw.setPenRadius(0.04);
        // StdDraw.setPenColor(Color.red);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        // StdDraw.setPenColor(Color.black);
        // StdDraw.setPenRadius(0.02);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
