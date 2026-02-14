# Graph Colouring

## Overview
The Graph Colouring Project implements algorithms to assign colours to vertices of a graph such that no two adjacent vertices share the same colour. The main objective is to calculate the **chromatic number** of a graph efficiently while supporting various graph structures, from simple to complex.

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

## Structure
- ReadGraph.java – Reads graph input and initiates calculations.
- GraphCalculator.java – Handles chromatic number computation, graph properties, and algorithm selection.
- HeapSort.java – Used for sorting vertex degrees.
- Algorithms/ – Contains implemented algorithms (Backtracking, Greedy, Bron-Kerbosch, DFS, etc.)
- GUI/ – JavaFX classes for graph visualization and optional game modes.



