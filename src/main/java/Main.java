import com.google.gson.*;
import graph.util.Graph;
import graph.scc.TarjanSCC;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestPaths;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String inputPath = "data/assign_4_input.json";
        String outputPath = "data/assign_4_output.json";

        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().setPrettyPrinting().create();
        JsonObject resultsRoot = new JsonObject();
        JsonArray resultsArray = new JsonArray();

        try (Reader reader = new FileReader(inputPath)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray graphs = root.getAsJsonArray("graphs");

            for (JsonElement gElem : graphs) {
                JsonObject gObj = gElem.getAsJsonObject();
                String name = gObj.has("name") ? gObj.get("name").getAsString() : "UnnamedGraph";
                int vertices = gObj.get("vertices").getAsInt();
                JsonArray edges = gObj.getAsJsonArray("edges");

                Graph graph = new Graph(vertices);
                for (JsonElement eElem : edges) {
                    JsonObject eObj = eElem.getAsJsonObject();
                    int u = eObj.get("u").getAsInt();
                    int v = eObj.get("v").getAsInt();
                    double w = eObj.get("weight").getAsDouble();
                    graph.addEdge(u, v, w);
                }

                TarjanSCC tarjan = new TarjanSCC(graph);
                tarjan.runTarjan();

                Graph dag = tarjan.buildCondensationGraph();
                TopologicalSort topo = new TopologicalSort(dag);
                List<Integer> topoOrder = topo.kahnSort();

                DAGShortestPaths dagsp = new DAGShortestPaths(dag);
                double[] shortest = dagsp.shortestPathsFromSources(topoOrder);
                double[] longest = dagsp.longestPathsFromSources(topoOrder);

                JsonObject res = new JsonObject();
                res.addProperty("graph_name", name);
                res.addProperty("vertices", vertices);
                res.addProperty("scc_count", tarjan.getSCCCount());
                res.addProperty("dfs_visits", tarjan.getDFSVisits());
                res.addProperty("scc_time_ms", tarjan.getExecutionTimeMs());
                res.addProperty("topo_pushes", topo.getPushes());
                res.addProperty("topo_pops", topo.getPops());
                res.addProperty("topo_time_ms", topo.getExecutionTimeMs());
                res.addProperty("relaxations", dagsp.getRelaxations());
                res.addProperty("dagsp_time_ms", dagsp.getExecutionTimeMs());
                res.add("topological_order", gson.toJsonTree(topoOrder));
                res.add("shortest_paths", gson.toJsonTree(shortest));
                res.add("longest_paths", gson.toJsonTree(longest));

                JsonArray sccArr = new JsonArray();
                for (List<Integer> scc : tarjan.getSCCs()) {
                    sccArr.add(gson.toJsonTree(scc));
                }
                res.add("scc_components", sccArr);

                System.out.println("Condensation edges: " + dag.getEdges().size());

                resultsArray.add(res);

                System.out.println("Processed: " + name);
            }

            resultsRoot.add("results", resultsArray);
            try (Writer writer = new FileWriter(outputPath)) {
                gson.toJson(resultsRoot, writer);
            }

            System.out.println("\nAll graphs processed. Results saved to " + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
