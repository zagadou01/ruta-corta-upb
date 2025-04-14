package model;

import java.util.Arrays;

// Grafo
public class Graph {
    private final LinkedList<Building> buildings; // Lista de vértices
    private Route[][] routes; // Matriz de adyacencia

    public Graph() {
        this.buildings = new LinkedList<>();
        this.routes = new Route[0][0];
    }

    public void addBuilding(Building building) {
        buildings.add(building);
        int size = buildings.getSize();
        Route[][] auxiliar = new Route[size][size];
        for (int i = 0; i < size - 1; i++) {
            auxiliar[i] = Arrays.copyOf(routes[i], size);
        }
        routes = auxiliar;
    }

    public void addRoute(String initialBuilding, String finalBuilding, int distance, boolean stairs) {
        int initialIndex = buildings.getIndex(initialBuilding);
        if (initialIndex == -1 || initialIndex >= buildings.getSize()) {
            return;
        }

        int finalIndex = buildings.getIndex(finalBuilding);
        if (finalIndex == -1 || finalIndex >= buildings.getSize()) {
            return;
        }

        Route route = new Route(distance, stairs);
        routes[initialIndex][finalIndex] = route;
    }

    public void print() {
        int size = buildings.getSize();
        if (size > 0) {
            System.out.print("  ");
            for (int i = 0; i < size; i++) {
                System.out.print(buildings.getName(i) + " ");
            }

            System.out.println();

            for (int i = 0; i < size; i++) {
                System.out.print(buildings.getName(i) + " ");
                for (int j = 0; j < size; j++) {
                    Route route = routes[i][j];
                    if (route != null) {
                        System.out.print(route.getDistance() + " ");
                    } else {
                        System.out.print("- ");
                    }

                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public LinkedList<Building> getBuildings() {
        return buildings;
    }
}
