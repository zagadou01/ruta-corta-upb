package model;

import controller.FileController;

// TODO Main temporal para probar algoritmos y grafos por consola, se eliminará al final
public class TerminalMain {
    public static void main(String[] args) {
        Graph graph = new Graph();

        graph.addBuilding(new Building("A"));
        graph.addBuilding(new Building("B"));
        graph.addBuilding(new Building("C"));
        graph.addBuilding(new Building("J"));

        graph.addRoute("A", "B", 5, false);
        graph.addRoute("B", "C", 0, true);
        graph.addRoute("A", "J", 100, true);
        graph.addRoute("C", "J", 0, true);

        graph.print();

        String[] path = graph.shortestPath("A", "J", true);

        printPath(path);
    }

    private static void printPath(String[] path) {
        System.out.print("Start -> ");
        for (String s : path) {
            System.out.print(s + " -> ");
        }
        System.out.println("End");
        System.out.println();
    }
}
