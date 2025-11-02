package graph.dagsp;

import graph.util.Graph;
import java.util.*;

public class DAGShortestPaths {
    private final Graph graph;
    private int relaxations;
    private double executionTimeMs;

    public DAGShortestPaths(Graph graph) {
        this.graph = graph;
        this.relaxations = 0;
    }

    public double[] shortestPathsFromSources(List<Integer> topoOrder) {
        long start = System.nanoTime();
        int n = graph.getVertices();
        double[] dist = new double[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);

        boolean[] hasIncoming = new boolean[n];
        for (int u = 0; u < n; u++) {
            for (Graph.Edge e : graph.getAdj().get(u)) {
                hasIncoming[e.to] = true;
            }
        }

        for (int i = 0; i < n; i++) {
            if (!hasIncoming[i]) dist[i] = 0.0;
        }

        for (int u : topoOrder) {
            if (dist[u] != Double.POSITIVE_INFINITY) {
                for (Graph.Edge e : graph.getAdj().get(u)) {
                    int v = e.to;
                    double w = e.weight;
                    if (dist[v] > dist[u] + w) {
                        dist[v] = dist[u] + w;
                        relaxations++;
                    }
                }
            }
        }

        executionTimeMs = (System.nanoTime() - start) / 1_000_000.0;
        return dist;
    }

    public double[] longestPathsFromSources(List<Integer> topoOrder) {
        long start = System.nanoTime();
        int n = graph.getVertices();
        double[] dist = new double[n];
        Arrays.fill(dist, Double.NEGATIVE_INFINITY);

        boolean[] hasIncoming = new boolean[n];
        for (int u = 0; u < n; u++) {
            for (Graph.Edge e : graph.getAdj().get(u)) {
                hasIncoming[e.to] = true;
            }
        }

        for (int i = 0; i < n; i++) {
            if (!hasIncoming[i]) dist[i] = 0.0;
        }

        for (int u : topoOrder) {
            if (dist[u] != Double.NEGATIVE_INFINITY) {
                for (Graph.Edge e : graph.getAdj().get(u)) {
                    int v = e.to;
                    double w = e.weight;
                    if (dist[v] < dist[u] + w) {
                        dist[v] = dist[u] + w;
                        relaxations++;
                    }
                }
            }
        }

        executionTimeMs = (System.nanoTime() - start) / 1_000_000.0;
        return dist;
    }

    public int getRelaxations() {
        return relaxations;
    }

    public double getExecutionTimeMs() {
        return executionTimeMs;
    }
}
