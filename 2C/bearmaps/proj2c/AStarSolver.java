package bearmaps.proj2c;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import bearmaps.proj2ab.DoubleMapPQ;
import bearmaps.proj2ab.ExtrinsicMinPQ;
import edu.princeton.cs.introcs.Stopwatch;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {

    private AStarGraph<Vertex> aStarGraph;
    private ExtrinsicMinPQ<Vertex> fringe = new DoubleMapPQ<Vertex>();
    private HashMap<Vertex, Double> distTo = new HashMap<>();
    private HashMap<Vertex, Vertex> edgeTo = new HashMap<>();
    private Vertex end;

    private SolverOutcome outcome;
    private List<Vertex> vertexList;
    private int explored;
    private Double explorationTime;


    /**
     * Constructor which finds the solution, computing everything necessary
     * for all other methods to return their results in constant time.
     * Note that the timeout passed in is in seconds.
     */
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        aStarGraph = input;
        explored = 0;
        this.end = end;
        fringe.add(start, aStarGraph.estimatedDistanceToGoal(start, end));
        distTo.put(start, 0.0);
        edgeTo.put(start, null);

        Stopwatch timer = new Stopwatch();

        boolean isEmpty = fringe.size() == 0;
        boolean isEnd = fringe.getSmallest().equals(end);

        // relax
        while (!isEmpty && !isEnd && !isTime(timer, timeout)) {
            Vertex current = fringe.removeSmallest();
            explored += 1;

            for (WeightedEdge<Vertex> edge : aStarGraph.neighbors(current)) {
                Vertex from = edge.from();
                Vertex to = edge.to();
                double weight = edge.weight();
                // directed graph, make sure edge is from current, not to
                if (!current.equals(from)) {
                    break;
                }

                Double currentDist = getDistTo(to);
                Double newDist = getDistTo(current) + weight;
                Double heuristicDist = newDist + aStarGraph.estimatedDistanceToGoal(to, end);

                if (newDist < currentDist) {
                    distTo.put(to, newDist);
                    edgeTo.put(to, current);

                    if (fringe.contains(to)) {
                        fringe.changePriority(to, heuristicDist);
                    } else {
                        fringe.add(to, heuristicDist);
                    }
                }
            }
            isEmpty = fringe.size() == 0;
            if (!isEmpty) {
                isEnd = fringe.getSmallest().equals(end);
            }
        }

        explorationTime = timer.elapsedTime();

        // unsolvable, priority queue became empty
        if (fringe.size() == 0) {
            outcome = SolverOutcome.UNSOLVABLE;
            vertexList = new ArrayList<>();
            distTo.put(end, 0.0);
            return;
        }

        // solved, completed all work in given time
        if (fringe.getSmallest().equals(end)) {
            outcome = SolverOutcome.SOLVED;
            vertexList = path(end);
        }

        // solver ran out of time
        if (explorationTime >= timeout) {
            outcome = SolverOutcome.TIMEOUT;
            vertexList = new ArrayList<>();
            distTo.put(end, 0.0);
            return;
        }
    }

    // returns whether the solver ran out of time
    private boolean isTime(Stopwatch timer, double timeout) {
        return timer.elapsedTime() >= timeout;
    }


    // returns the distance to a vertex
    private Double getDistTo(Vertex vertex) {
        if (distTo.containsKey(vertex)) {
            return distTo.get(vertex);
        }
        return Double.POSITIVE_INFINITY;
    }


   // get path from start to end
    /**
     * @Source https://www.geeksforgeeks.org/collections-reverse-java-examples/
     * used to reverse array
     */
    private List<Vertex> path(Vertex e) {
        List<Vertex> path = new ArrayList<>();
        Vertex current = e;
        path.add(e);
        while (edgeTo.get(current) != null) {
            Vertex from = edgeTo.get(current);
            path.add(from);
            current = from;
        }
        Collections.reverse(path);
        return path;
    }

    /**
     * Returns: SolverOutcome.SOLVED, SolverOutcome.TIMEOUT, or SolverOutcome.UNSOLVABLE.
     * Should be SOLVED if the AStarSolver was able to complete all work in the time given.
     * UNSOLVABLE if the priority queue became empty.
     * TIMEOUT if the solver ran out of time.
     * You should check to see if you have run out of time every time you dequeue.
     */
    public SolverOutcome outcome() {
        return outcome;
    }

    /**
     * A list of vertices corresponding to a solution.
     * Should be empty if result was TIMEOUT or UNSOLVABLE.
     */
    public List<Vertex> solution() {
        return vertexList;
    }

    /**
     * The total weight of the given solution, taking into account edge weights.
     * Should be 0 if result was TIMEOUT or UNSOLVABLE.
     */
    public double solutionWeight() {
        return getDistTo(end);
    }

    /**
     * The total number of priority queue dequeue operations.
     */
    public int numStatesExplored() {
        return explored;
    }

    /**
     * The total time spent in seconds by the constructor.
     */
    public double explorationTime() {
        return explorationTime;
    }
}
