package bearmaps;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.ArrayList;

public class ArrayHeapMinPQTest {
    private static Random r = new Random();

    @Test
    public void testAdd() {
        ArrayHeapMinPQ<Integer> arrayHeap = new ArrayHeapMinPQ<>();
        arrayHeap.add(5, 5.0);
        arrayHeap.add(4, 4.0);
        arrayHeap.add(3, 3.0);
        arrayHeap.add(2, 2.0);
        arrayHeap.add(1, 1.0);
        NaiveMinPQ<Integer> naive = new NaiveMinPQ<>();
        naive.add(5, 5.0);
        naive.add(4, 4.0);
        naive.add(3, 3.0);
        naive.add(2, 2.0);
        naive.add(1, 1.0);
        for (int i = 0; i < 5; i++) {
            assertEquals(arrayHeap.removeSmallest(), naive.removeSmallest());
        }
        // test random numbers
        ArrayHeapMinPQ<Integer> arrayHeap2 = new ArrayHeapMinPQ<>();
        NaiveMinPQ<Integer> naive2 = new NaiveMinPQ<>();
        for (int i = 0; i < 1000; i++) {
            int randVal = r.nextInt();
            double randPriority = r.nextDouble();
            arrayHeap2.add(randVal, randPriority);
            naive2.add(randVal, randPriority);
        }
        for (int i = 0; i < 1000; i++) {
            assertEquals(arrayHeap2.removeSmallest(), naive2.removeSmallest());
        }
    }

    @Test
    public void testContains() {
        ArrayHeapMinPQ<Integer> arrayHeap = new ArrayHeapMinPQ<>();
        for (int i = 0; i < 1000; i++) {
            int randVal = r.nextInt();
            double randPriority = r.nextDouble();
            arrayHeap.add(randVal, randPriority);
            assertTrue(arrayHeap.contains(randVal));
        }

        ArrayHeapMinPQ<String> arrayHeap2 = new ArrayHeapMinPQ<>();
        arrayHeap2.add("a", 1.0);
        arrayHeap2.add("b", 2.0);
        arrayHeap2.add("c", 3.0);
        assertTrue(arrayHeap2.contains("a"));
        assertTrue(arrayHeap2.contains("b"));
        assertTrue(arrayHeap2.contains("c"));
        assertFalse(arrayHeap2.contains("d"));
    }

    @Test
    public void testEverything() {
        //      a
        //     / \
        //    c  b
        ArrayHeapMinPQ<String> arrayHeap = new ArrayHeapMinPQ<>();
        arrayHeap.add("c", 3.0);
        arrayHeap.add("b", 2.0);
        arrayHeap.add("a", 1.0);
        assertEquals(arrayHeap.getSmallest(), "a");
        assertEquals(arrayHeap.getSmallest(), "a");
        assertEquals(arrayHeap.size(), 3);

        //      z
        //     / \
        //    a  b
        //   /
        //  c
        arrayHeap.add("z", 0.5);
        assertEquals(arrayHeap.getSmallest(), "z");
        assertEquals(arrayHeap.size(), 4);

        //      a
        //     / \
        //    c   b
        arrayHeap.removeSmallest();
        assertEquals(arrayHeap.getSmallest(), "a");

        //      b
        //     / \
        //    c   a
        arrayHeap.changePriority("b", 0.1);
        assertEquals(arrayHeap.getSmallest(), "b");
        assertEquals(arrayHeap.size(), 3);

        //      b
        //     / \
        //    y   a
        //   /
        //  c
        arrayHeap.add("y", 0.5);
        assertEquals(arrayHeap.getSmallest(), "b");
        assertEquals(arrayHeap.size(), 4);

        //      a
        //     / \
        //    y   b
        //   /
        //  c
        arrayHeap.changePriority("y", 1.1);
        arrayHeap.changePriority("b", 2.0);
        assertEquals(arrayHeap.getSmallest(), "a");
        assertTrue(arrayHeap.contains("a"));
        assertTrue(arrayHeap.contains("b"));
        assertTrue(arrayHeap.contains("c"));
        assertTrue(arrayHeap.contains("y"));
        assertFalse(arrayHeap.contains("z"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddException() {
        ArrayHeapMinPQ<String> arrayHeap = new ArrayHeapMinPQ<>();
        arrayHeap.add("a", 1.0);
        arrayHeap.add("a", 1.0);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetSmallestException1() {
        ArrayHeapMinPQ<String> arrayHeap = new ArrayHeapMinPQ<>();
        arrayHeap.getSmallest();
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetSmallestException2() {
        ArrayHeapMinPQ<String> arrayHeap = new ArrayHeapMinPQ<>();
        arrayHeap.add("a", 1.0);
        arrayHeap.removeSmallest();
        arrayHeap.getSmallest();
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveSmallestException1() {
        ArrayHeapMinPQ<String> arrayHeap = new ArrayHeapMinPQ<>();
        arrayHeap.removeSmallest();
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveSmallestException2() {
        ArrayHeapMinPQ<String> arrayHeap = new ArrayHeapMinPQ<>();
        arrayHeap.add("a", 1.0);
        arrayHeap.removeSmallest();
        arrayHeap.removeSmallest();
    }

    @Test(expected = NoSuchElementException.class)
    public void testChangePriorityException() {
        ArrayHeapMinPQ<String> arrayHeap = new ArrayHeapMinPQ<>();
        arrayHeap.add("a", 1.0);
        arrayHeap.changePriority("b", 2.0);
    }

    // Θ(log N)
    @Test
    public void testAddTiming() {
        List<Integer> N = List.of(31250, 62500, 125000, 500000, 1000000, 2000000, 4000000, 8000000);
        ArrayList<Double> times = new ArrayList<>();
        List<Integer> ops = List.of(31250, 62500, 125000, 500000, 1000000, 2000000, 4000000);
        ArrayList<Double> microsecPerOp = new ArrayList<>();

        for (int count : N) {
            ArrayHeapMinPQ heap = new ArrayHeapMinPQ();
            Stopwatch sw = new Stopwatch();
            for (int i = 0; i < count; i++) {
                heap.add(i, r.nextDouble());
            }
            Double time = sw.elapsedTime();
            times.add(time);
            microsecPerOp.add(time / count);
        }
        System.out.println("N " + N);
        System.out.println("times " + times);
        System.out.println("# ops " + ops);
        System.out.println("microsec/op " + microsecPerOp);
    }

    // Θ(1)
    @Test
    public void testContainsTiming() {
        List<Integer> N = List.of(31250, 62500, 125000, 500000, 1000000, 2000000, 4000000);
        ArrayList<Double> times = new ArrayList<>();
        List<Integer> ops = List.of(1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000);
        ArrayList<Double> microsecPerOp = new ArrayList<>();

        for (int count : N) {
            ArrayHeapMinPQ heap = new ArrayHeapMinPQ();
            for (int i = 0; i < count; i++) {
                heap.add(i, r.nextDouble());
            }

            Stopwatch sw = new Stopwatch();
            for (int i = 0; i < 1000000; i++) {
                heap.contains(r.nextInt());
            }
            Double time = sw.elapsedTime();
            times.add(time);
            microsecPerOp.add(time / count);
        }
        System.out.println("N " + N);
        System.out.println("times " + times);
        System.out.println("# ops " + ops);
        System.out.println("microsec/op " + microsecPerOp);
    }

    // Θ(1)
    @Test
    public void testGetSmallestTiming() {
        List<Integer> N = List.of(31250, 62500, 125000, 500000, 1000000, 2000000, 4000000);
        ArrayList<Double> times = new ArrayList<>();
        List<Integer> ops = List.of(1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000);
        ArrayList<Double> microsecPerOp = new ArrayList<>();

        for (int count : N) {
            ArrayHeapMinPQ heap = new ArrayHeapMinPQ();
            for (int i = 0; i < count; i++) {
                heap.add(i, r.nextDouble());
            }

            Stopwatch sw = new Stopwatch();
            for (int i = 0; i < 1000000; i++) {
                heap.getSmallest();
            }
            Double time = sw.elapsedTime();
            times.add(time);
            microsecPerOp.add(time / count);
        }
        System.out.println("N " + N);
        System.out.println("times " + times);
        System.out.println("# ops " + ops);
        System.out.println("microsec/op " + microsecPerOp);
    }

    // Θ(log N)
    @Test
    public void testRemoveSmallestTiming() {
        List<Integer> N = List.of(31250, 62500, 125000, 500000, 1000000, 2000000, 4000000, 8000000);
        ArrayList<Double> times = new ArrayList<>();
        List<Integer> ops = List.of(31250, 31250, 31250, 31250, 31250, 31250, 31250, 31250);
        ArrayList<Double> microsecPerOp = new ArrayList<>();

        for (int count : N) {
            ArrayHeapMinPQ heap = new ArrayHeapMinPQ();
            for (int i = 0; i < count; i++) {
                heap.add(i, r.nextDouble());
            }

            Stopwatch sw = new Stopwatch();
            for (int i = 0; i < 31250; i++) {
                heap.removeSmallest();
            }
            Double time = sw.elapsedTime();
            times.add(time);
            microsecPerOp.add(time / count);
        }
        System.out.println("N " + N);
        System.out.println("times " + times);
        System.out.println("# ops " + ops);
        System.out.println("microsec/op " + microsecPerOp);
    }
    // Θ(1)
    @Test
    public void testSizeTiming() {
        List<Integer> N = List.of(31250, 62500, 125000, 500000, 1000000, 2000000, 4000000);
        ArrayList<Double> times = new ArrayList<>();
        List<Integer> ops = List.of(1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000);
        ArrayList<Double> microsecPerOp = new ArrayList<>();

        for (int count : N) {
            ArrayHeapMinPQ heap = new ArrayHeapMinPQ();
            for (int i = 0; i < count; i++) {
                heap.add(i, r.nextDouble());
            }

            Stopwatch sw = new Stopwatch();
            for (int i = 0; i < 1000000; i++) {
                heap.size();
            }
            Double time = sw.elapsedTime();
            times.add(time);
            microsecPerOp.add(time / count);
        }
        System.out.println("N " + N);
        System.out.println("times " + times);
        System.out.println("# ops " + ops);
        System.out.println("microsec/op " + microsecPerOp);
    }

    // Θ(log N)
    @Test
    public void testChangePriorityTiming() {
        List<Integer> N = List.of(31250, 62500, 125000, 500000, 1000000, 2000000, 4000000, 8000000);
        ArrayList<Double> times = new ArrayList<>();
        List<Integer> ops = List.of(1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000);
        ArrayList<Double> microsecPerOp = new ArrayList<>();

        for (int count : N) {
            ArrayHeapMinPQ heap = new ArrayHeapMinPQ();
            for (int i = 0; i < count; i++) {
                heap.add(i, r.nextDouble());
            }

            Stopwatch sw = new Stopwatch();
            for (int i = 0; i < 1000000; i++) {
                heap.changePriority(r.nextInt(count), r.nextDouble());
            }
            Double time = sw.elapsedTime();
            times.add(time);
            microsecPerOp.add(time / count);
        }
        System.out.println("N " + N);
        System.out.println("times " + times);
        System.out.println("# ops " + ops);
        System.out.println("microsec/op " + microsecPerOp);
    }

    @Test
    public void compareTimingToNaive() {
        /** Your getSmallest, contains, size and changePriority methods must run in O(log(n)) time.
         * Your add and removeSmallest must run in O(log(n)) average time,
         * i.e. they should be logarithmic, except for the rare resize operation.
         * For reference, when making 1000 queries on a heap of size 1,000,000:
         * our solution is about 300x faster than the naive solution
         */

        ArrayHeapMinPQ heap = new ArrayHeapMinPQ();
        NaiveMinPQ naive = new NaiveMinPQ();

        // arrays of times
        // index 0: ArrayHeapMinPQ
        // index 1: NaiveMinPQ
        ArrayList<Double> addTime = new ArrayList<>();
        ArrayList<Double> containsTime = new ArrayList<>();
        ArrayList<Double> sizeTime = new ArrayList<>();
        ArrayList<Double> removeSmallestTime = new ArrayList<>();
        ArrayList<Double> changePriorityTime = new ArrayList<>();

        // add
        Stopwatch heapAddSW = new Stopwatch();
        for (int i = 0; i < 1000000; i++) {
            try {
                heap.add(r.nextInt(), r.nextDouble());
            }
            catch(IllegalArgumentException e) {
            }
        }
        addTime.add(heapAddSW.elapsedTime());
        Stopwatch naiveAddSW = new Stopwatch();
        for (int i = 0; i < 1000000; i++) {
            naive.add(r.nextInt(), r.nextDouble());
        }
        addTime.add(naiveAddSW.elapsedTime());


        // contains
        Stopwatch heapContainsSW = new Stopwatch();
        for (int i = 0; i < 1000; i++) {
            heap.contains(r.nextInt());
        }
        containsTime.add(heapContainsSW.elapsedTime());
        Stopwatch naiveContainsSW = new Stopwatch();
        for (int i = 0; i < 1000; i++) {
            naive.contains(r.nextInt());
        }
        containsTime.add(naiveContainsSW.elapsedTime());


        // size
        Stopwatch heapSizeSW = new Stopwatch();
        for (int i = 0; i < 1000; i++) {
            heap.size();
        }
        sizeTime.add(heapSizeSW.elapsedTime());
        Stopwatch naiveSizeSW = new Stopwatch();
        for (int i = 0; i < 1000; i++) {
            naive.size();
        }
        sizeTime.add(naiveSizeSW.elapsedTime());


        // changePriority
        Stopwatch heapChangePrioritySW = new Stopwatch();
        for (int i = 0; i < 1000; i++) {
            try {
                heap.changePriority(i, r.nextDouble());
            }
            catch (NoSuchElementException e) {
            }
        }
        changePriorityTime.add(heapChangePrioritySW.elapsedTime());
        Stopwatch naiveChangePrioritySW = new Stopwatch();
        for (int i = 0; i < 1000; i++) {
            try {
                naive.changePriority(i, r.nextDouble());
            }
            catch (NoSuchElementException e) {
            }
        }
        changePriorityTime.add(naiveChangePrioritySW.elapsedTime());


        // removeSmallest
        Stopwatch heapRemoveSmallestSW = new Stopwatch();
        for (int i = 0; i < 1000; i++) {
            heap.removeSmallest();
        }
        removeSmallestTime.add(heapRemoveSmallestSW.elapsedTime());
        Stopwatch naiveRemoveSmallestSW = new Stopwatch();
        for (int i = 0; i < 1000; i++) {
            naive.removeSmallest();
        }
        removeSmallestTime.add(naiveRemoveSmallestSW.elapsedTime());

        // print out times and comparison
        System.out.println("add: " + addTime);
        System.out.println("ArrayHeap add is " + addTime.get(1) / addTime.get(0) + "x faster");
        System.out.println("note ArrayHeap add N items runs in NlogN time, naive is only logN");
        System.out.println("contains: " + containsTime);
        System.out.println("ArrayHeap contains is " + containsTime.get(1) / containsTime.get(0) + "x faster");
        System.out.println("size: " + sizeTime);
        System.out.println("ArrayHeap size is " + sizeTime.get(1) / sizeTime.get(0) + "x faster");
        System.out.println("changePriority: " + changePriorityTime);
        System.out.println("ArrayHeap changePriority is " + changePriorityTime.get(1) / changePriorityTime.get(0) + "x faster");
        System.out.println("removeSmallest: " + removeSmallestTime);
        System.out.println("ArrayHeap removeSmallest is " + removeSmallestTime.get(1) / removeSmallestTime.get(0) + "x faster");
    }
}
