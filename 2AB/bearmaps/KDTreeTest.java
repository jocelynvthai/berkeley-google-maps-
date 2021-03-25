package bearmaps;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.Stopwatch;

import javax.print.attribute.standard.MediaSize;

/**
 @source Professor Hug's walkthrough videos & pseudocode
 */
public class KDTreeTest {
    private static Random r = new Random(500);

    private static KDTree buildLectureTree() {
        Point p1 = new Point(2, 3); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(4, 2);
        Point p3 = new Point(4, 2);
        Point p4 = new Point(4, 5);
        Point p5 = new Point(3, 3);
        Point p6 = new Point(1, 5);
        Point p7 = new Point(4, 4);

        KDTree kd = new KDTree(List.of(p1, p2, p3, p4, p5, p6, p7));
        return kd;
    }

    private static void buildTreeWithDoubles() {
        Point p1 = new Point(2, 3);
        Point p2 = new Point(2, 3);

        KDTree kd = new KDTree(List.of(p1, p2));
    }

    @Test
    // Tests code using examples from the nearest demo slides
    public void testNearestSlides() {
        KDTree kd = buildLectureTree();
        Point goal = new Point(0, 7);

        Point actual = kd.nearest(goal.getX(), goal.getY());
        Point expected = new Point(1, 5);

        assertEquals(actual, expected);
    }

    private Point randomPoint() {
        double x = r.nextDouble();
        double y = r.nextDouble();
        return new Point(x, y);
    }

    private List<Point> randomPoints(int N) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            points.add(randomPoint());
        }
        return points;
    }

    private void testNPointsandGGoals(int N, int G) {
        List<Point> points = randomPoints(N);
        NaivePointSet nps = new NaivePointSet(points);
        KDTree kd = new KDTree(points);

        List<Point> goalPoints = randomPoints(G);

        for (Point goal : goalPoints) {
            Point expected = nps.nearest(goal.getX(), goal.getY());
            Point actual = kd.nearest(goal.getX(), goal.getY());
            assertEquals(expected, actual);
        }
    }

    @Test
    public void test1000Points500Goals() {
        testNPointsandGGoals(1000, 500);
    }

    @Test
    public void test10000Points5000Goals() {
        testNPointsandGGoals(10000, 5000);
    }

    private double testNPS(int pointCount) {
        List<Point> points = randomPoints(pointCount);
        NaivePointSet nps = new NaivePointSet(points);

        List<Point> goalPoints = randomPoints(1000000);

        Stopwatch npsSW = new Stopwatch();
        for (Point goal : goalPoints) {
            nps.nearest(goal.getX(), goal.getY());
        }
        return npsSW.elapsedTime();
    }

    private double testKDTree(int pointCount) {
        List<Point> points = randomPoints(pointCount);
        NaivePointSet kd = new NaivePointSet(points);

        List<Point> goalPoints = randomPoints(1000000);

        Stopwatch kdSW = new Stopwatch();
        for (Point goal : goalPoints) {
            kd.nearest(goal.getX(), goal.getY());
        }
        return kdSW.elapsedTime();
    }

    @Test
    public void testKDTreeConstructionTime() {
        List<Integer> pointsCount = List.of(31250, 62500, 125000, 500000, 1000000, 2000000);
        List<Double> KDTreeConstructionTimes = new ArrayList<>();

        for (int count : pointsCount) {
            List<Point> points = randomPoints(count);
            Stopwatch sw = new Stopwatch();
            KDTree kd = new KDTree(points);
            KDTreeConstructionTimes.add(sw.elapsedTime());
        }
        System.out.println("KDTree Construction Times: " + KDTreeConstructionTimes);
    }

    @Test
    public void testNPSNearestTiming() {
        List<Integer> npsPointCounts = List.of(125, 250, 500, 1000);
        List<Double> npsTimes = new ArrayList<>();

        for (int count : npsPointCounts) {
            npsTimes.add(testNPS(count));
        }

        System.out.println("NaivePointSet Times: " + npsTimes);
    }

    @Test
    public void testKDNearestTiming() {
        List<Integer> kdPointCounts = List.of(31250, 62500, 125000, 500000, 1000000);
        // List<Integer> kdPointCounts = List.of(125, 250, 500, 1000);
        List<Double> kdTimes = new ArrayList<>();

        for (int count : kdPointCounts) {
            kdTimes.add(testKDTree(count));
        }

        /**
        for (int i = 0; i < npsTimes.size(); i++) {
            assertTrue((kdTimes.get(i) / npsTimes.get(i)) < 0.1);
        }
         */
        System.out.println("KDTree Times: " + kdTimes);
    }

    @Test
    public void testWith100000() {
        List<Point> points = randomPoints(100000);
        List<Point> goals = randomPoints(1000000);

        NaivePointSet nps = new NaivePointSet(points);
        KDTree kd = new KDTree(points);

        Stopwatch npsSW = new Stopwatch();
        for (Point p : goals) {
            Point nearestNPS = nps.nearest(p.getX(), p.getY());
        }
        Double npsTime = npsSW.elapsedTime();

        Stopwatch kdSW = new Stopwatch();
        for (Point p : goals) {
            Point nearestKD = kd.nearest(p.getX(), p.getY());
        }
        Double kdTime = kdSW.elapsedTime();

        System.out.println("NPS Time " + npsTime);
        System.out.println("KD Time " + kdTime);
    }
}
