package byow.ASTAR.bearmaps.hw4;

import byow.ASTAR.bearmaps.proj2ab.ArrayHeapMinPQ;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private ArrayHeapMinPQ<Vertex> heep;
    private List<Vertex> leest;
    private Map<Vertex, Double> bastDist;
    private Map<Vertex, Vertex> edgey;
    private double weit;
    private double taim;
    private int staits;
    private SolverOutcome salved; // 1 = solved, 2 = unsolved, 0 = overtime


    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        long startTime = System.currentTimeMillis();
        heep = new ArrayHeapMinPQ<>();
        leest = new ArrayList<>();
        bastDist = new HashMap<>();
        edgey = new HashMap<>();
        weit = 0;
        taim = 0;
        staits = 0;

        //setting initial source
        bastDist.put(start, 0.0);
        heep.add(start, input.estimatedDistanceToGoal(start, end));

        //actually doing stuff now
        Vertex naw = start;
        while (true) {
            long currTime = System.currentTimeMillis();
            if (((currTime - startTime) / 1000) > timeout) {
                taim = (currTime - startTime) / 1000.0;
                weit = 0;
                salved = SolverOutcome.TIMEOUT;
                break;
            }
            if (heep.size() == 0) {
                salved = SolverOutcome.UNSOLVABLE;
                taim = (currTime - startTime) / 1000.0;
                weit = 0;
                break;
            }
            if (heep.getSmallest().equals(end)) {
                salved = SolverOutcome.SOLVED;
                taim = (currTime - startTime) / 1000.0;
                weit = bastDist.get(end);
                pathTracer(start, end);
                break;
            }

            naw = heep.removeSmallest();
            staits++;
            for (WeightedEdge<Vertex> edge : input.neighbors(naw)) {
                relax(edge, input, end);
            }
        }

    }
    private void relax(WeightedEdge<Vertex> edge, AStarGraph<Vertex> input, Vertex end) {
        Vertex from = edge.from();
        Vertex to = edge.to();
        double weight = edge.weight();
        double newDis = bastDist.get(from) + weight;

        if (!bastDist.containsKey(to) || newDis < bastDist.get(to)) {
            bastDist.put(to, bastDist.get(from) + weight);
            edgey.put(to, from);
            if (heep.contains(to)) {
                heep.changePriority(to, newDis + input.estimatedDistanceToGoal(to, end));
            } else {
                heep.add(to, newDis + input.estimatedDistanceToGoal(to, end));
            }
        }
    }

    private void pathTracer(Vertex start, Vertex end) {
        Vertex curr = end;
        while (!curr.equals(start)) {
            leest.add(0, curr);
            curr = edgey.get(curr);
        }
        leest.add(0, start);
    }

    public SolverOutcome outcome() {
        return salved;
    }
    public List<Vertex> solution() {
        return leest;
    }
    public double solutionWeight() {
        return weit;
    }
    public int numStatesExplored() {
        return staits;
    }
    public double explorationTime() {
        return taim;
    }
}
