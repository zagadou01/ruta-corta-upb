# Shortest route detection between UPB buildings

This project implements a system to find the shortest path between two 
buildings within the campus of Universidad Pontificia Bolivariana (UPB) in 
Bucaramanga, Colombia. It uses data provided by us, graphs and Dijkstra's 
algorithm.

## Features

- Calculates the shortest path between two campus buildings.
- Graphical visualization of nodes (buildings) and edges (paths).
- User-friendly interface built with JavaFX.
- Implementation of Dijkstra's algorithm.
- Loads graph data from CSV files.
- Agile development using the Kanban methodology.

## Technologies Used

- **Language**: Java 21 LTS
- **GUI**: JavaFX
- **IDE**: IntelliJ IDEA, Eclipse IDE
- **Version Control**: Git + GitHub
- **Development Methodology**: Kanban

## How to Run

Clonar el repositorio:
```
git clone https://github.com/zagadou01/ruta-corta-upb.git
```    

Compilar el proyecto, realizar dentro de la carpeta del proyecto:
```
mvn package
mvn exec:java -Dexec.mainClass="view.Application"
```