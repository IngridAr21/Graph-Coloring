# Graph Colouring
> A software project that explores the Graph Colouring problem, combining algorithms and cognitive experiments to calculate chromatic numbers and study human problem-solving.

## Table of Contents

- [About](#about)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Demo / Screenshots](#demo--screenshots)
- [Technologies & Skills](#technologies--skills)
- [Acknowledgements](#acknowledgements)
- [License](#license)
- [Contact](#contact)

---

## About

The Graph Colouring Project addresses the classic NP-hard problem of colouring the vertices of a graph such that no two adjacent vertices share the same colour.  

The objectives of the project are:

- Efficiently calculate the chromatic number of various graph types using multiple algorithms.
- Explore the impact of different restrictions on human performance in solving graph colouring tasks.
- Develop a visual interface to interactively test and visualise graph colouring.

This project was implemented as part of a first-year group assignment at Maastricht University (PBL system) using Java and JavaFX.

---

## Features

- Calculation of chromatic numbers for undirected graphs.
- Detection of special graph types (bipartite, cycle, complete) for optimized computation.
- Integration of multiple algorithms:
  - Backtracking
  - Brook’s Theorem
  - Greedy Algorithm
  - Bron-Kerbosch + Tomita Pivoting for cliques
  - Depth-First Search (DFS)
  - HeapSort for degree prioritization
- Three interactive game modes to test human graph colouring efficiency:
  1. No restrictions
  2. Timed restrictions
  3. Set route restrictions

---

## Installation

1. Clone the repository:

```bash
git clone <repo_url>
cd graph-colouring
To run the application javafx project should be setted up (IntelliJ recommended). 

To start run the App.java file

You'll see 2 textfields where you can choose to generate random graph(first field), for that you need to specify upperbound of your graph,
 or upload existing one(second field)(upperbound will be automaticcaly set equal to the amount of vertices of chosen graph)
Also you'll see combobox where you can choose gamemode.
When everything setted up you can play
Good luck!


