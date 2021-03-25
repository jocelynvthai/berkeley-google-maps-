package bearmaps;

import java.util.ArrayList;
import java.util.List;

public class NaivePointSet implements PointSet {

    /* Note that your NaivePointSet class should be immutable,
     * meaning that you cannot add or or remove points from it.
     * You do not need to do anything special to guarantee this.
     */

    private List<Point> allPoints;

    // You can assume points has at least size 1.
    public NaivePointSet(List<Point> points) {
        allPoints = new ArrayList<>();
        for (Point p : points) {
            allPoints.add(p);
        }
    }

    @Override
    /* Returns the closest point to the inputted coordinates.
     * This should take \(\Theta(N)\) time where \(N\) is the number of points.
     */
    public Point nearest(double x, double y) {
        Point best = allPoints.get(0);
        Point goal = new Point(x, y);
        for (Point p : allPoints) {
            double bestDist = Point.distance(best, goal);
            double currentDist = Point.distance(p, goal);
            if (currentDist < bestDist) {
                best = p;
            }
        }
        return best;
    }

    public static void main(String [] args) {
        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3));
        Point ret = nn.nearest(3.0, 4.0); // returns p2
        System.out.println(ret.getX()); // evaluates to 3.3
        System.out.println(ret.getY()); // evaluates to 4.4
    }
}

