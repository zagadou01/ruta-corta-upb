package model;

import java.util.Arrays;
import java.util.function.Consumer;

// Grafo
public class Graph {
    private final LinkedList<Building> buildings; // Lista de vértices
    private Route[][] routes; // Matriz de adyacencia

    private Consumer<Building> onBuildingAdded;
    private Consumer<Route> onRouteAdded;

    //Estos dos métodos son para que el Grafo le avise a la interfaz cuando se añaden nuevas rutas y edificios.
    public void setOnBuildingAdded(Consumer<Building> callback) {
        this.onBuildingAdded = callback;
    }

    public void setOnRouteAdded(Consumer<Route> callback) {
        this.onRouteAdded = callback;
    }

    public Graph() {
        this.buildings = new LinkedList<>();
        this.routes = new Route[0][0];
    }

    public void addBuilding(Building building) {
        buildings.add(building);
        if (onBuildingAdded != null) onBuildingAdded.accept(building);

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

        Route route = new Route(distance, stairs, initialBuilding, finalBuilding);

        //La matriz es par porque el nodo es No-Dirigido, por lo que hay que poner la ruta inversa también.
        routes[initialIndex][finalIndex] = route;
        routes[finalIndex][initialIndex] = route;

        if (onRouteAdded != null) onRouteAdded.accept(route);
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

    public Route[][] getRoutes(){
        return routes;
    }
}
