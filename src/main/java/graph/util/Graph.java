package graph.util;

import java.util.*;

public class Graph {
    public static class Edge {
        public int from, to;
        public double weight;

        public Edge(int from, int to, double weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "(" + from + " -> " + to + ", w=" + weight + ")";
        }
    }

    private int vertices;
    private List<List<Edge>> adj;

    public Graph(int vertices) {
        this.vertices = vertices;
        adj = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v, double w) {
        adj.get(u).add(new Edge(u, v, w));
    }

    public int getVertices() {
        return vertices;
    }

    public List<List<Edge>> getAdj() {
        return adj;
    }

    public List<Edge> getEdges() {
        List<Edge> allEdges = new ArrayList<>();
        for (List<Edge> edges : adj) {
            allEdges.addAll(edges);
        }
        return allEdges;
    }
}
