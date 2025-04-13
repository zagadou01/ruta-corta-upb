package model;

// TODO Main temporal para probar algoritmos y grafos por consola, se eliminará al final
public class TerminalMain {
    public static void main(String[] args) {
        Graph graph = new Graph();

        graph.addBuilding(new Building("a"));
        graph.addBuilding(new Building("b"));
        graph.addBuilding(new Building("c"));
        graph.addBuilding(new Building("d"));

        graph.addRoute("a", "b", 5, true);
        graph.addRoute("b", "a", 5, true);

        graph.addRoute("b", "d", 4, true);
        graph.addRoute("d", "b", 4, true);

        graph.addRoute("c", "d", 3, true);
        graph.addRoute("d", "c", 3, true);

        graph.addRoute("a", "c", 2, true);
        graph.addRoute("c", "a", 2, true);

        graph.print();
    }
}
