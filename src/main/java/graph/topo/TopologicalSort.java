package graph.topo;

import graph.util.Graph;
import java.util.*;

public class TopologicalSort {
    private Graph graph;
    private int pushes;
    private int pops;
    private double executionTimeMs;

    public TopologicalSort(Graph graph) {
        this.graph = graph;
        this.pushes = 0;
        this.pops = 0;
    }

    public List<Integer> kahnSort() {
        long start = System.nanoTime();
        int n = graph.getVertices();
        int[] indegree = new int[n];

        for (int u = 0; u < n; u++) {
            for (Graph.Edge e : graph.getAdj().get(u)) {
                indegree[e.to]++;
            }
        }

        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                q.add(i);
                pushes++;
            }
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            pops++;
            topoOrder.add(u);

            for (Graph.Edge e : graph.getAdj().get(u)) {
                int v = e.to;
                indegree[v]--;
                if (indegree[v] == 0) {
                    q.add(v);
                    pushes++;
                }
            }
        }

        executionTimeMs = (System.nanoTime() - start) / 1_000_000.0;
        return topoOrder;
    }

    public int getPushes() {
        return pushes;
    }

    public int getPops() {
        return pops;
    }

    public double getExecutionTimeMs() {
        return executionTimeMs;
    }
}
