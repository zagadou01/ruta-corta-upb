package model;

import controller.FileController;

// TODO Main temporal para probar algoritmos y grafos por consola, se eliminará al final
public class TerminalMain {
    public static void main(String[] args) {
        Building a = new Building("0");
        Place restaurant = new Place("Restaurant");
        Place library = new Place("Library");
        a.addPlace(restaurant);
        a.addPlace(library);

        Graph graph = new Graph();

        /*
        graph.addBuilding(a);
        graph.addBuilding(new Building("1"));
        graph.addBuilding(new Building("2"));
        graph.addBuilding(new Building("3"));
        graph.addBuilding(new Building("4"));
        graph.addBuilding(new Building("5"));
        graph.addBuilding(new Building("6"));
        */

        graph.addBuildings(FileController.readBuildings());

        graph.addRoute("0", "1", 2, true);
        graph.addRoute("0", "2", 6, false);
        graph.addRoute("1", "3", 5, true);
        graph.addRoute("2", "3", 8, false);
        graph.addRoute("3", "4", 9/*10*/, true);
        graph.addRoute("3", "5", 9/*15*/, false);
        graph.addRoute("4", "5", 6, false);
        graph.addRoute("4", "6", 2, false);
        graph.addRoute("5", "6", 6, true);

        graph.print();

        String[] path = graph.shortestPath("0", "6", false);

        printPath(path);
        /*
        graph.removeBuilding("3");

        graph.print();

        FileController.writeBuildings(graph.getBuildings());
        */
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
