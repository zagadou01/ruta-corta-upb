package model;

// TODO Main temporal para probar algoritmos y grafos por consola, se eliminará al final
public class TerminalMain {
    public static void main(String[] args) {
        /*
        Building a = new Building("A");
        Place cafeteria = new Place("Cafeteria");
        a.addPlace(cafeteria);
        System.out.println(a.getPlaces().toString());
        */

        Graph graph = new Graph();

        graph.addBuilding(new Building("a"));
        graph.addBuilding(new Building("b"));
        graph.addBuilding(new Building("c"));
        graph.addBuilding(new Building("d"));
        graph.addBuilding(new Building("e"));
        graph.addBuilding(new Building("f"));
        graph.addBuilding(new Building("g"));

        graph.addRoute("a", "b", 2, true);
        graph.addRoute("a", "c", 6, true);

        graph.addRoute("b", "a", 2, true);
        graph.addRoute("b", "d", 5, true);

        graph.addRoute("c", "a", 6, true);
        graph.addRoute("c", "d", 8, true);

        graph.addRoute("d", "b", 5, true);
        graph.addRoute("d", "c", 8, true);
        graph.addRoute("d", "e", 10, true);
        graph.addRoute("d", "f", 15, true);

        graph.addRoute("e", "d", 10, true);
        graph.addRoute("e", "f", 6, true);
        graph.addRoute("e", "g", 2, true);

        graph.addRoute("f", "d", 15, true);
        graph.addRoute("f", "e", 6, true);
        graph.addRoute("f", "g", 6, true);

        graph.addRoute("g", "e", 2, true);
        graph.addRoute("g", "f", 6, true);

        graph.print();

        String[] path = graph.shortestPath("a", "g");

        System.out.print("Start -> ");
        for (String s : path) {
            System.out.print(s + " -> ");
        }
        System.out.print("End");
    }
}
