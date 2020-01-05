import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;

public class KdTree {
    private Node root;
    private int size;

    public KdTree() { size = 0; }

    private static class Node {
        private final Point2D p;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("call to insert is null;");
        RectHV rect = new RectHV(0, 0, 1, 1);
        root = insert(root, p, rect, true);
    }
    private Node insert(Node x, Point2D p, RectHV rect, boolean isVertical) {
        if (x == null) {
            size++;
            return new Node(p, rect);
        }

        int cmp = comparePoint(p, x.p, isVertical);
        if (cmp < 0 && isVertical) {
            // assign the left rectHV to new point p.
            RectHV rect1 = new RectHV(x.rect.xmin(), x.rect.ymin(), x.p.x(), x.rect.ymax());
            x.lb = insert(x.lb, p, rect1, !isVertical);
        }
        else if (cmp > 0 && isVertical) {
            RectHV rect2 = new RectHV(x.p.x(), x.rect.ymin(), x.rect.xmax(), x.rect.ymax());
            x.rt = insert(x.rt, p, rect2, !isVertical);
        }
        else if (cmp < 0 && !isVertical) {
            RectHV rect3 = new RectHV(x.rect.xmin(), x.rect.ymin(), x.rect.xmax(), x.p.y());
            x.lb = insert(x.lb, p, rect3, !isVertical);
        }
        else if (cmp > 0 && !isVertical) {
            RectHV rect4 = new RectHV(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.rect.ymax());
            x.rt = insert(x.rt, p, rect4, !isVertical);
        }
        // p and x.p have the same x or y coordinate,
        // but they are not equal, put it into right subtree as specified.
        // keep the same rectangle
        else if (!p.equals(x.p)) { // cmp == 0 && !p.equals(x.p)
            x.rt = insert(x.rt, p, x.rect, !isVertical);
        }
        // cmp == 0 && p.equals(x.p), do nothing.
        return x;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("call for contains is null");
        return contains(root, p, true);
    }
    private boolean contains(Node x, Point2D p, boolean isVertical) {
        if (x == null) return false;
        int cmp = comparePoint(p, x.p, isVertical);
        if (cmp < 0) return contains(x.lb, p, !isVertical);
        else if (cmp > 0) return contains(x.rt, p, !isVertical);
        else if (!p.equals(x.p)) return contains(x.rt, p, !isVertical);
        else return true;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("call to range() is null");
        ArrayList<Point2D> pointRange = new ArrayList<>();
        range(root, rect, pointRange);
        return pointRange;
    }
    private void range(Node x, RectHV rect, ArrayList<Point2D> pointRange) {
        if (x == null) return;
        if (rect.contains(x.p)) pointRange.add(x.p);
        if (!x.rect.intersects(rect)) return;

        if (x.lb != null && rect.intersects(x.lb.rect))
            range(x.lb, rect, pointRange);
        if (x.rt != null && rect.intersects(x.rt.rect))
            range(x.rt, rect, pointRange);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("call to nearest() is null");
        if (root == null) return null;

        return nearest(root, p, root.p, true);
    }
    private Point2D nearest(Node x, Point2D p, Point2D nearestPoint, boolean isVertical) {
        // p: given point2D; nearestPoint: nearest point found
        if (x == null) return nearestPoint;
        if (x.p.equals(p)) return p;

        // nearest distance is the distance between champion and p.
        double nearestDistance = nearestPoint.distanceSquaredTo(p);

        // if distance between p and x.rect is larger than nearest distance,
        // skip explore node x and its subtrees.
        if (x.rect.distanceSquaredTo(p) >= nearestDistance) return nearestPoint;

        // if distance between x.p and p is closer than nearestDistance,
        // set x.p as the nearest point.
        if (x.p.distanceSquaredTo(p) < nearestDistance) nearestPoint = x.p;

        // search the subtree of Node x.
        // compare p and x.p, decide to explore left subtree first or to explore right subtree first.
        int cmp = comparePoint(p, x.p, isVertical);

        if (cmp < 0) {
            // explore to the left/bottom subtree first, then explore the right/top subtree,
            // this may enable the pruning the second subtree, because the nearest distance may
            // be smaller than the distance between champion and the second subtree in the recursive process.
            nearestPoint = nearest(x.lb, p, nearestPoint, !isVertical);
            nearestPoint = nearest(x.rt, p, nearestPoint, !isVertical);
        }
        else {
            nearestPoint = nearest(x.rt, p, nearestPoint, !isVertical);
            nearestPoint = nearest(x.lb, p, nearestPoint, !isVertical);

        }
        return nearestPoint;
    }


    private int comparePoint(Point2D x, Point2D y, boolean isVertical) {
        if (isVertical) {
            double cmp = x.x() - y.x();
            if (cmp > 0) return +1;
            else if (cmp < 0) return -1;
            else return 0;
        }
        else {
            double cmp = x.y() - y.y();
            if (cmp > 0) return +1;
            else if (cmp < 0) return -1;
            else return 0;
        }
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenRadius(0.003);
        StdDraw.setPenColor(StdDraw.BLACK);
        RectHV boundary = new RectHV(0, 0, 1, 1);
        boundary.draw();
        draw(root, true);
    }
    private void draw(Node x, boolean isVertical) {
        if (x == null) return;
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenRadius(0.008);
        StdDraw.setPenColor(StdDraw.BLACK);
        x.p.draw();
        StdDraw.show();
        // StdDraw.pause(20);

        if (isVertical) {
            StdDraw.setPenRadius(0.002);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
//            x.rect.draw();
        }
        else {
            StdDraw.setPenRadius(0.002);
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
//            x.rect.draw();
        }
        // StdDraw.pause(20);
        draw(x.lb, !isVertical);
        draw(x.rt, !isVertical);
    }
}
