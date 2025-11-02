import graph.scc.TarjanSCC;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestPaths;
import graph.util.Graph;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class GraphAlgorithmsTest {

    @Test
    public void testTarjanSCC_simpleCycle() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);

        TarjanSCC tarjan = new TarjanSCC(g);
        tarjan.runTarjan();

        assertEquals("Should have exactly 1 SCC", 1, tarjan.getSCCCount());
        assertEquals("SCC should contain 3 vertices", 3, tarjan.getSCCs().get(0).size());
        assertTrue("Execution time should be positive", tarjan.getExecutionTimeMs() >= 0);
    }

    @Test
    public void testTarjanSCC_twoComponents() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1);
        g.addEdge(2, 3, 1);

        TarjanSCC tarjan = new TarjanSCC(g);
        tarjan.runTarjan();

        assertEquals("Should have 3 SCCs: {0,1}, {2}, {3}", 3, tarjan.getSCCCount());
    }

    @Test
    public void testTopologicalSort_simpleDAG() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 2);
        g.addEdge(1, 2, 3);

        TopologicalSort topo = new TopologicalSort(g);
        List<Integer> order = topo.kahnSort();

        assertEquals(Arrays.asList(0, 1, 2), order);
        assertEquals(3, topo.getPushes());
        assertTrue(topo.getExecutionTimeMs() >= 0);
    }

    @Test
    public void testDAGShortestPaths_linearChain() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 5);
        g.addEdge(1, 2, 2);

        TopologicalSort topo = new TopologicalSort(g);
        List<Integer> order = topo.kahnSort();

        DAGShortestPaths dagSP = new DAGShortestPaths(g);
        double[] dist = dagSP.shortestPathsFromSources(order);

        assertEquals("Distance from 0 to 0", 0.0, dist[0], 1e-9);
        assertEquals("Distance from 0 to 1", 5.0, dist[1], 1e-9);
        assertEquals("Distance from 0 to 2", 7.0, dist[2], 1e-9);
    }

    @Test
    public void testDAGLongestPaths_linearChain() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 3);
        g.addEdge(1, 2, 4);

        TopologicalSort topo = new TopologicalSort(g);
        List<Integer> order = topo.kahnSort();

        DAGShortestPaths dagSP = new DAGShortestPaths(g);
        double[] longest = dagSP.longestPathsFromSources(order);

        assertEquals("Longest path 0->1", 3.0, longest[1], 1e-9);
        assertEquals("Longest path 0->2", 7.0, longest[2], 1e-9);
    }

    @Test
    public void testCondensationGraphStructure() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1);
        g.addEdge(2, 3, 1);

        TarjanSCC tarjan = new TarjanSCC(g);
        tarjan.runTarjan();
        Graph dag = tarjan.buildCondensationGraph();

        assertTrue("Condensation should have fewer or equal vertices",
                dag.getVertices() <= g.getVertices());
        assertTrue("No self-loops expected in condensation graph",
                dag.getEdges().stream().noneMatch(e -> e.from == e.to));
    }
}
