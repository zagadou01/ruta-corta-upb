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

        routes[initialIndex][finalIndex] = new Route(distance, stairs, initialBuilding, finalBuilding);

        //La matriz es par porque el nodo es No-Dirigido, por lo que hay que poner la ruta inversa también.
        routes[finalIndex][initialIndex] = new Route(distance, stairs);
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

    public String[] shortestPath(String initialBuilding, String finalBuilding /*add boolean stairs*/) {
        int initialIndex = buildings.getIndex(initialBuilding);
        int finalIndex = buildings.getIndex(finalBuilding);

        int[] distances = new int[buildings.getSize()];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[initialIndex] = 0;

        boolean[] visitedNodes = new boolean[buildings.getSize()];
        Arrays.fill(visitedNodes, false);

        int currentBuilding = initialIndex;

        while (!visitedNodes[currentBuilding]) {
            visitedNodes[currentBuilding] = true;

            for (int i = 0; i < routes[currentBuilding].length; i++) {
                Route auxiliaryRoutes = routes[currentBuilding][i];
                if (auxiliaryRoutes != null) {
                    int distance = auxiliaryRoutes.getDistance() + distances[currentBuilding];
                    if (distance < distances[i]) {
                        distances[i] = distance;
                    }
                }
            }

            int auxiliaryCurrentDistance = Integer.MAX_VALUE;
            for (int i = 0; i < distances.length; i++) {
                if (!visitedNodes[i] && distances[i] != Integer.MAX_VALUE) {
                    if (distances[i] < auxiliaryCurrentDistance) {
                        auxiliaryCurrentDistance = distances[i];
                        currentBuilding = i;
                    }
                }
            }
        }

        int pathSize = 1;
        String[] path = new String[pathSize];
        path[0] = buildings.getName(finalIndex);

        int currentDistance = distances[finalIndex];
        currentBuilding = finalIndex;
        while (currentDistance != 0) {
            for (int i = 0; i < routes[currentBuilding].length; i++) {
                Route auxiliaryRoutes = routes[currentBuilding][i];
                if (auxiliaryRoutes != null) {
                    int distance = currentDistance - auxiliaryRoutes.getDistance();
                    if (distances[i] == distance) {
                        currentDistance -= auxiliaryRoutes.getDistance();
                        currentBuilding = i;
                        path = Arrays.copyOf(path, pathSize + 1);
                        path[pathSize] = buildings.getName(currentBuilding);
                        pathSize++;
                        break;
                    }
                }
            }
        }
        path = reverse(path);

        return path;
    }

    private String[] reverse(String[] array) {
        String[] auxiliary = new String[array.length];
        int j = 0;

        for (int i = array.length - 1; i >= 0; i--) {
            auxiliary[j] = array[i];
            j++;
        }

        return auxiliary;
    }

    public Route[][] getRoutes(){
        return routes;
    }
}
