import java.util.ArrayList;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> segmentArrayList = new ArrayList<LineSegment>();

    // finds all line segments containing 4 or more points
    public BruteCollinearPoints(Point[] points) {
        int n = points.length;

        // Corner cases
        if (points == null) throw new IllegalArgumentException();
        for (int i = 0; i < n; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
        }
        // Check whether the array contains a repeated point.
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (points[i].equals(points[j])) throw new IllegalArgumentException();
            }
        }

        double dis = 1e-6;
        for (int p = 0; p < n; p++) {
            for (int q = 0; q < n; q++) {
                for (int r = 0; r < n; r++) {
                    for (int s = 0; s < n; s++) {
                        Point[] a = {points[p], points[q], points[r], points[s]};
                        if (a[0].compareTo(a[1]) < 0 && a[1].compareTo(a[2]) < 0 && a[2].compareTo(a[3]) < 0){
                            // if statement, include vertical lines, POSITVE_Infinity == POSITIVE_Infinity
                            if ((Math.abs(a[0].slopeTo(a[1]) - a[0].slopeTo(a[2])) < dis
                                    && Math.abs(a[0].slopeTo(a[1]) - a[0].slopeTo(a[3])) < dis)
                            || (a[0].slopeTo(a[1]) == a[0].slopeTo(a[2]) && a[0].slopeTo(a[1]) == a[0].slopeTo(a[3]))) {
                                segmentArrayList.add(new LineSegment(a[0], a[3]));
                            }
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {return segmentArrayList.size();}

    public  LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[numberOfSegments()];
        return segmentArrayList.toArray(segments);
    }
}
