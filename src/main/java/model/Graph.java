package model;

import java.util.Arrays;
import java.util.function.Consumer;

// Grafo
public class Graph {
    private final LinkedList<Building> buildings; // Lista de vértices
    private Route[][] routes; // Matriz de adyacencia

    private Consumer<Building> onBuildingAdded;
    private Consumer<Route> onRouteAdded;

    public Graph() {
        this.buildings = new LinkedList<>();
        this.routes = new Route[0][0];
    }

    //Estos dos métodos son para que el Grafo le avise a la interfaz cuando se añaden nuevas rutas y edificios.
    public void setOnBuildingAdded(Consumer<Building> callback) {
        this.onBuildingAdded = callback;
    }

    public void setOnRouteAdded(Consumer<Route> callback) {
        this.onRouteAdded = callback;
    }

    public void addBuilding(Building building) {
        buildings.add(building);
        
        //Avisar la interfaz de que se añadió una ruta nueva.
        if (onBuildingAdded != null) onBuildingAdded.accept(building);

        int size = buildings.getSize();
        Route[][] auxiliar = new Route[size][size];
        for (int i = 0; i < size - 1; i++) {
            auxiliar[i] = Arrays.copyOf(routes[i], size);
        }
        routes = auxiliar;
    }

    public void addBuildings(LinkedList<Building> buildings) {
        int size = buildings.getSize();

        for (int i = 0; i < size; i++) {
            Building newBuilding = buildings.getNode(i);
            addBuilding(new Building(newBuilding.getName(), newBuilding.getPosition()[0], newBuilding.getPosition()[1]));
        }
    }

    public void removeBuilding(String building) {
        // Eliminar rutas con el edificio
        int buildingIndex = buildings.getIndex(building);
        for (int i = 0; i < routes.length; i++) {
            if (routes[buildingIndex][i] != null) {
                String routeBuildingName = buildings.getName(i);
                removeRoute(building, routeBuildingName);
            }
        }

        // Eliminar edificio de la lista de edificios y reducir el tamaño
        buildings.remove(buildingIndex);

        // Reducir el tamaño de rutas y rellenar el espacio de la ruta eliminada
        int size = buildings.getSize();
        Route[][] auxiliaryRoutesMatrix = new Route[size][size];
        for (int i = 0; i < buildingIndex; i++) {
            Route[] array1 = Arrays.copyOfRange(routes[i], 0, buildingIndex);
            Route[] array2 = Arrays.copyOfRange(routes[i], buildingIndex + 1, size + 1);

            auxiliaryRoutesMatrix[i] = concatenateArrays(array1, array2);
        }
        for (int i = buildingIndex; i < size; i++) {
            Route[] array1 = Arrays.copyOfRange(routes[i + 1], 0, buildingIndex);
            Route[] array2 = Arrays.copyOfRange(routes[i + 1], buildingIndex + 1, size + 1);

            auxiliaryRoutesMatrix[i] = concatenateArrays(array1, array2);
        }
        routes = auxiliaryRoutesMatrix;

        // Actualizar el índice de los edificios luego del eliminado para que sigan en orden
        int auxiliaryBuildingIndex = buildingIndex + 1;
        Building auxiliaryBuilding = buildings.getNode(auxiliaryBuildingIndex);
        while (auxiliaryBuilding != null) {
            auxiliaryBuilding.setIndex(auxiliaryBuilding.getIndex() - 1);
            auxiliaryBuildingIndex += 1;
            auxiliaryBuilding = buildings.getNode(auxiliaryBuildingIndex);
        }
    }

    public void addRoute(String initialBuilding, String finalBuilding, int distance, boolean stairs) {
        int initialIndex = buildings.getIndex(initialBuilding);
        int finalIndex = buildings.getIndex(finalBuilding);

        routes[initialIndex][finalIndex] = new Route(distance, stairs, initialBuilding, finalBuilding);
        routes[finalIndex][initialIndex] = new Route(distance, stairs, initialBuilding, finalBuilding);

        //Avisar la interfaz de que se añadió una ruta nueva.
        if (onRouteAdded != null) onRouteAdded.accept(new Route(distance, stairs, initialBuilding, finalBuilding));
    }

    public void removeRoute(String initialBuilding, String finalBuilding) {
        int initialIndex = buildings.getIndex(initialBuilding);
        int finalIndex = buildings.getIndex(finalBuilding);

        routes[initialIndex][finalIndex] = null;
        routes[finalIndex][initialIndex] = null;
    }

    public String[] shortestPath(String initialBuilding, String finalBuilding, Boolean stairsPath) {
        // Obtener el índice del edificio de salida y de llegada
        int initialIndex = buildings.getIndex(initialBuilding);
        int finalIndex = buildings.getIndex(finalBuilding);

        // Inicializar array con distancias desde el inicio hasta cada nodo
        // Distancia del nodo inicial empieza en cero, las demás en "infinito"
        int[] distances = new int[buildings.getSize()];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[initialIndex] = 0;

        // Inicializar array para indicar cuando se visite un nodo
        boolean[] visitedBuildings = new boolean[buildings.getSize()];
        Arrays.fill(visitedBuildings, false);

        // Variable que lleva el índice del vértice actual
        int currentBuilding = initialIndex;

        // Ciclo que termina cuando todos los edificios hayan sido visitados
        while (!visitedBuildings[currentBuilding]) {
            // Marcamos el edificio actual como visitado
            visitedBuildings[currentBuilding] = true;

            // Ciclo para asignar la distancia más corta a los edificios adyacentes (vecinos)
            for (int i = 0; i < routes.length; i++) {
                // Busca las rutas (aristas) conectadas con el edificio actual
                Route auxiliaryRoute = routes[currentBuilding][i];
                if (auxiliaryRoute != null) {
                    // Si stairsPath es verdadero no hay problema (todos los edificios tienen escalera)
                    // Si es falso busca edificios con ruta alterna sin escaleras
                    if (stairsPath || !auxiliaryRoute.hasStairs()) {
                        // Calcula la distancia para llegar a la ruta, suma la distancia de la ruta con la acumulada actual
                        int distance = auxiliaryRoute.getDistance() + distances[currentBuilding];
                        // Si la nueva distancia es menor, se reemplaza, se ha encontrado un nuevo camino más corto
                        if (distance < distances[i]) {
                            distances[i] = distance;
                        }
                    }
                }
            }

            // Cambia el edificio actual por otro edificio que tenga la menor ruta calculada y no haya sido visitado
            int auxiliaryCurrentDistance = Integer.MAX_VALUE;
            for (int i = 0; i < distances.length; i++) {
                if (!visitedBuildings[i] && distances[i] != Integer.MAX_VALUE) {
                    if (distances[i] < auxiliaryCurrentDistance) {
                        auxiliaryCurrentDistance = distances[i];
                        currentBuilding = i;
                    }
                }
            }
        }

        // Creación del camino más corto desde un edificio inicial a otro final
        // Agregamos como primer edificio el edificio final
        int pathSize = 1;
        String[] path = new String[pathSize];
        path[0] = buildings.getName(finalIndex);

        // Empezamos con la distancia calculada para llegar al edificio final, nos ubicamos en el edificio final
        int currentDistance = distances[finalIndex];
        currentBuilding = finalIndex;
        // Se va restando la distancia actual hasta llegar a 0, indicando que volvimos al nodo inicial
        while (currentDistance != 0) {
            // Recorremos todas las rutas conectadas con el edificio actual
            for (int i = 0; i < routes.length; i++) {
                Route auxiliaryRoutes = routes[currentBuilding][i];
                if (auxiliaryRoutes != null) {
                    // Restamos a la distancia actual la distancia de la ruta encontrada
                    // Comprobamos que la nueva distancia calculada es igual a la minima encontrada
                    int distance = currentDistance - auxiliaryRoutes.getDistance();
                    // Si es igual, le restamos la distancia de la ruta a la actual, cambiamos a ese edificio
                    // Aumentamos el tamaño del arreglo, agregamos el edificio al camino
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
        // Invertimos el camino para que quede de la forma inicio -> fin
        path = reverse(path);

        return path;
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

    private String[] reverse(String[] array) {
        String[] auxiliary = new String[array.length];
        int j = 0;

        for (int i = array.length - 1; i >= 0; i--) {
            auxiliary[j] = array[i];
            j++;
        }

        return auxiliary;
    }

    // Concatena los dos arrays en los que se separan las rutas al eliminar una ruta
    private Route[] concatenateArrays(Route[] array1, Route[] array2) {
        int size = array1.length + array2.length;
        Route[] concatenatedArray = new Route[size];

        for (int i = 0; i < array1.length; i++) {
            concatenatedArray[i] = array1[i];
        }

        for (int i = 0, j = array1.length; i < array2.length; i++, j++) {
            concatenatedArray[j] = array2[i];
        }

        return concatenatedArray;
    }

    public LinkedList<Building> getBuildings() {
        return buildings;
    }

    public Route[][] getRoutes(){
        return routes;
    }

    public void setRoutes(Route[][] routes) {
        this.routes = routes;
    }
}
