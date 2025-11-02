package graph.scc;

import graph.util.Graph;
import java.util.*;

public class TarjanSCC {
    private Graph graph;
    private int time;
    private int sccCount;
    private int dfsVisits;
    private double executionTimeMs;
    private List<List<Integer>> sccs;
    private int[] disc, low, comp;
    private boolean[] inStack;
    private Deque<Integer> stack;

    public TarjanSCC(Graph graph) {
        this.graph = graph;
        int n = graph.getVertices();
        disc = new int[n];
        low = new int[n];
        comp = new int[n];
        inStack = new boolean[n];
        stack = new ArrayDeque<>();
        sccs = new ArrayList<>();
        Arrays.fill(disc, -1);
        Arrays.fill(low, -1);
        Arrays.fill(comp, -1);
    }

    public void runTarjan() {
        long start = System.nanoTime();
        for (int i = 0; i < graph.getVertices(); i++) {
            if (disc[i] == -1) dfs(i);
        }
        executionTimeMs = (System.nanoTime() - start) / 1_000_000.0;
    }

    private void dfs(int u) {
        disc[u] = low[u] = ++time;
        stack.push(u);
        inStack[u] = true;
        dfsVisits++;

        for (Graph.Edge e : graph.getAdj().get(u)) {
            int v = e.to;
            if (disc[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (inStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        if (low[u] == disc[u]) {
            List<Integer> compList = new ArrayList<>();
            while (true) {
                int v = stack.pop();
                inStack[v] = false;
                comp[v] = sccCount;
                compList.add(v);
                if (v == u) break;
            }
            sccs.add(compList);
            sccCount++;
        }
    }

    public Graph buildCondensationGraph() {
        Graph dag = new Graph(sccCount);
        Set<String> addedEdges = new HashSet<>();

        for (int u = 0; u < graph.getVertices(); u++) {
            for (Graph.Edge e : graph.getAdj().get(u)) {
                int v = e.to;
                int cu = comp[u];
                int cv = comp[v];
                if (cu != cv) {
                    String key = cu + "-" + cv;
                    if (addedEdges.add(key)) {
                        dag.addEdge(cu, cv, e.weight);
                    }
                }
            }
        }

        System.out.println("[DEBUG] Condensation DAG: " + dag.getEdges().size() + " edges");
        return dag;
    }

    public List<List<Integer>> getSCCs() { return sccs; }
    public int getSCCCount() { return sccCount; }
    public int getDFSVisits() { return dfsVisits; }
    public double getExecutionTimeMs() { return executionTimeMs; }
}
