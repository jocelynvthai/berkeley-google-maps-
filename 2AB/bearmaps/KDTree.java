package bearmaps;

import java.util.List;
import java.lang.Math;

/**
 @source Professor Hug's walkthrough videos & pseudocode
 */
public class KDTree implements PointSet {
    private static final boolean HORIZONTAL = false;
    private Node root;

    /* Your KDTree class should be immutable.
     * While k-d trees can theoretically work for any number of dimensions,
     * your implementation only has to work for the 2-dimensional case,
     * i.e. when our points have only x and y coordinates.
     */

    // Orientation is vertical (y) when true and horizontal (x) when false
    private class Node {
        private Point p;
        private boolean orientation;
        private Node left_down;
        private Node right_up;

        public Node (Point p, boolean orientation) {
            this.p = p;
            this.orientation = orientation;
            left_down = null;
            right_up = null;
        }
    }

    /* You can assume points has at least size 1.
     */
    public KDTree(List<Point> points) {
        for (Point p : points) {
            root = add(p, root, HORIZONTAL);
        }
    }

    // add Point p to KDTree
    private Node add(Point p, Node n, boolean orientation) {
        if (n == null) {
            return new Node(p, orientation);
        }

        if (p.equals(n.p)) {
            return n;
        }

        double compare = compare(p, n.p, orientation);
        if (compare < 0) {
            n.left_down = add(p, n.left_down, !orientation);
        } else if (compare >= 0) {
            n.right_up = add(p, n.right_up, !orientation);
        }
        return n;
    }

    // compare two points
    private double compare(Point a, Point b, boolean orientation) {
        if (orientation == HORIZONTAL) {
            return Double.compare(a.getX(), b.getX());
        } else {
            return Double.compare(a.getY(), b.getY());
        }
    }

    // compare two points
    private double comparator(Node n, Point goal) {
        if (n.orientation == HORIZONTAL) {
            return n.p.getX() - goal.getX();
        } else {
            return n.p.getY() - goal.getY();
        }
    }

    /* Returns the closest point to the inputted coordinates.
     * This should take \(O(\log N)\) time on average,
     * where \(N\) is the number of points.
     */
    @Override
    public Point nearest(double x, double y) {
        Point goal = new Point(x, y);
        Node best = nearestHelper(root, goal, root);
        return best.p;
    }

    private Node nearestHelper(Node n, Point goal, Node best) {
        if (n == null) {
            return best;
        }
        if (Point.distance(n.p, goal) < Point.distance(best.p, goal)) {
            best = n;
        }

        Node goodSide;
        Node badSide;
        double comparator = comparator(n, goal);
        if (comparator > 0) {
            goodSide = n.left_down;
            badSide = n.right_up;
        } else {
            goodSide = n.right_up;
            badSide = n.left_down;
        }

        best = nearestHelper(goodSide, goal, best);
        Point bestBad = bestBadSide(n, goal);
        if (Point.distance(bestBad, goal) < Point.distance(best.p, goal)) {
            best = nearestHelper(badSide, goal, best);
        }
        return best;
    }

    private Point bestBadSide(Node n, Point goal) {
        Point bestBad;
        if (n.orientation == HORIZONTAL) {
            bestBad = new Point(n.p.getX(), goal.getY());
        } else {
            bestBad = new Point(goal.getX(), n.p.getY());
        }
        return bestBad;
    }

    public static void main(String [] args) {
        Point p1 = new Point(2, 3); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(4, 2);
        Point p3 = new Point(4, 2);
        Point p4 = new Point(4, 5);
        Point p5 = new Point(3, 3);
        Point p6 = new Point(1, 5);
        Point p7 = new Point(4, 4);

        KDTree kd = new KDTree(List.of(p1, p2, p3, p4, p5, p6, p7));
    }
}
