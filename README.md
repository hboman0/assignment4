1. Data Summary

The experimental dataset includes eight directed graphs grouped by size and edge density:

Small graphs: 8–10 vertices

Medium graphs: 12–18 vertices

Large graphs: 25–40 vertices

Dense graph: 40 vertices with high edge density

Each graph uses weighted edges (integer weights between 1 and 20).
All graphs were processed sequentially through the following stages:

SCC Detection using Tarjan’s algorithm.

Condensation Graph Construction, reducing SCCs into single nodes.

Topological Sorting of the condensed DAG.

DAG Shortest and Longest Path Computation using topological relaxation.

2. Results Overview

<img width="1213" height="261" alt="img" src="https://github.com/user-attachments/assets/38ce8924-e07c-4b7a-9171-7c00c85bb3ee" />


All algorithms executed correctly for every input graph.
The following general trends were observed:

Computation Time:
SCC decomposition ranged from 0.01–0.05 ms for small graphs up to 0.05–0.1 ms for large and dense ones.
Topological sorting remained efficient even on dense graphs (≤0.1 ms).
DAG-SP (shortest/longest path) showed time between 0.003–0.05 ms, scaling roughly linearly with the number of edges.

SCC Metrics:
Small graphs typically contained 6–7 SCCs, medium graphs 8–16, large graphs 22–40.
Dense graphs showed no large SCC clusters — each vertex formed an individual SCC, confirming acyclic construction.

Topological Sort Metrics:
The number of pushes and pops matched the SCC count, confirming a consistent and cycle-free ordering.
Larger graphs required up to 40 pushes/pops, scaling linearly with the number of components.

DAG Shortest/Longest Paths:
Both shortest and longest paths were successfully computed for all DAGs.
Path lengths increased proportionally to graph size and connectivity:
from ~15 for small graphs to ~115 for large graphs.
The dense graph produced complex path distributions, validating correct edge relaxation and distance propagation.

3. Analysis
   SCC and Condensation

Tarjan’s algorithm efficiently identified SCCs in O(V + E) time.
The condensation graphs demonstrated strong structural simplification:
dense regions of the original graph were reduced into compact DAG representations.
No redundant edges or cycles were detected, verifying the correctness of the SCC–DAG transformation.

Topological Sorting

Topological sorting performed consistently, with equal counts of pushes and pops for all SCCs.
Execution time grew linearly with the number of SCCs.
This confirms that the condensation graphs were acyclic and that vertex processing order was correct.

DAG Shortest and Longest Paths

Shortest and longest path computations used a single topological traversal for relaxation.
The relaxation count (number of edge updates) reflected the DAG density:
from ~10 for small graphs to over 300 for the dense one.
No infinite or invalid distances were reported, proving numerical stability.
Shortest-path metrics grew predictably with the number of vertices and weights,
while longest-path metrics demonstrated correct propagation of maximum distances without overflow.

Performance Bottlenecks

For small and medium graphs, runtime was dominated by SCC decomposition (≈40–50% of total).
For large and dense graphs, DAG-SP dominated due to increased relaxations.
No algorithm exceeded linear complexity, confirming scalability.

4. Conclusions and Recommendations

SCC Detection:
Tarjan’s algorithm is optimal for static directed graphs where identifying cycles or components is required.
It should be used as a preprocessing step before DAG-based optimizations.

Topological Sorting:
Works best on sparse DAGs or condensation graphs.
Minimal time complexity and deterministic results make it suitable for scheduling, dependency resolution, and compiler optimization tasks.

DAG Shortest/Longest Paths:
The topological relaxation approach is ideal for DAGs — faster than Dijkstra or Bellman-Ford since it avoids cycles.
It should be preferred when the graph structure is known to be acyclic.

Overall System Design:
Combining these algorithms provides a robust framework for analyzing arbitrary directed graphs.
Dense graphs increase relaxation cost, but performance remains manageable for up to 40 vertices and several hundred edges.
