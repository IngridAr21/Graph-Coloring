# Graph Colouring

**Skills:** `Java`, `Algorithms`, `Graph Theory`, `Optimization`, `JavaFX`, `Data Structures`.

## Real-World Applications
Graph colouring is more than a theoretical exercise; it is a fundamental tool for solving complex optimization problems in industry:
* **Scheduling & Timetabling:** Assigning time slots to exams or meetings where participants have conflicts.
* **Resource Allocation:** Managing register allocation in compilers or frequency assignment in wireless networks.
* **Data Science:** Cluster analysis and partitioning large-scale networks.
  
## Overview
The Graph Colouring Project implements algorithms to assign colours to vertices of a graph such that no two adjacent vertices share the same colour. The main objective is to calculate the **chromatic number** of a graph efficiently while supporting various graph structures, from simple to complex.

## Demo
[ADD PICTURES]

## Features
- Calculation of the chromatic number using:
  - **Backtracking Algorithm**
  - **Greedy Algorithm**
  - **Bron-Kerbosch Algorithm** (with Tomita Pivoting)
  - **Depth-First Search** and **Heapsort** optimizations
- Supports recognition of special graph types:
  - Bipartite graphs
  - Cycle graphs
  - Complete graphs
- GUI visualization of graphs using **JavaFX**
- Modular and extendable Java code structure

## Technologies
- **Java 11+**
- **JavaFX** for interactive graph visualization
- Core algorithms implemented manually for learning and optimization

## Getting Started
1. Clone the repository:
```bash
git clone https://github.com/yourusername/graph-colouring.git
```
2. Open the project in **IntelliJ IDEA** or any Java IDE.
3. Run `App.java`.
4. You'll see two text fields:
   - **Generate Random Graph** – specify an upper bound for the number of vertices.
   - **Upload Existing Graph** – the upper bound will be set automatically based on the chosen graph.
5. Choose a **game mode** from the combo box:
   - No restrictions
   - Timed restrictions
   - Set route restrictions
6. Click **play** and interact with the graph visualization.




