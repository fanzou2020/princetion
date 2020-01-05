import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;


public class PointSET {
    // Brute-force implementation
    private final SET<Point2D> points;


    public PointSET() {
        // Initializes an empty PointSet
        points = new SET<>();
    }

    public boolean isEmpty() {
        // is the set Empty?
        return points.size() == 0;
    }

    public int size() {
        // number of points in the set
        return points.size();
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new IllegalArgumentException("Point2D p is null");
        if (!contains(p))
            points.add(p);
    }

    public boolean contains(Point2D p) {
        // does the set contain point p
        if (p == null)
            throw new IllegalArgumentException("Point2D p is null");
        return points.contains(p);
    }

    public void draw() {
        // draw all points to standard draw
        for (Point2D p : points) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null)
            throw new IllegalArgumentException("RectHV rect is  null");
        ArrayList<Point2D> list = new ArrayList<>();
        for (Point2D q : points) {
            if (rect.contains(q)) list.add(q);
        }
        return list;
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null)
            throw new IllegalArgumentException("Point p is null");
        if (isEmpty()) return null;

        double nearestDistance = Double.POSITIVE_INFINITY;
        Point2D nearestPoint = null;
        for (Point2D q : points) {
            double distance = p.distanceSquaredTo(q);
            if (distance <= nearestDistance) {
                nearestDistance = distance;
                nearestPoint = q;
            }
        }
        return nearestPoint;
    }
}
