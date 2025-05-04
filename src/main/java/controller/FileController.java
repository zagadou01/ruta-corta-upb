package controller;

import model.*;
import java.io.*;
import java.util.function.Consumer;

public class FileController {
    private static Consumer<Building> onBuildingAdded;
    private static Consumer<Route> onRouteAdded;

    //Estos dos métodos son para que el Grafo le avise a la interfaz cuando se añaden nuevas rutas y edificios.
    public static void setOnBuildingAdded(Consumer<Building> callback) {
        onBuildingAdded = callback;
    }

    public static void setOnRouteAdded(Consumer<Route> callback) {
        onRouteAdded = callback;
    }

    public static void addBuilding(Building building) {
        //Avisar la interfaz de que se añadió una ruta nueva.
        if (onBuildingAdded != null) onBuildingAdded.accept(building);

        Graph graph = createGraph();
        graph.addBuilding(building);
        saveGraph(graph);
    }

    public static void removeBuilding(String buildingName) {
        Graph graph = createGraph();
        graph.removeBuilding(buildingName);
        saveGraph(graph);
    }

    public static void addRoute(String initialBuilding, String finalBuilding, int distance, boolean stairs) {
        //Avisar la interfaz de que se añadió una ruta nueva.
        if (onRouteAdded != null) onRouteAdded.accept(new Route(distance, stairs, initialBuilding, finalBuilding));

        Graph graph = createGraph();
        graph.addRoute(initialBuilding, finalBuilding, distance, stairs);
        saveGraph(graph);
    }

    public static void removeRoute(String initialBuilding, String finalBuilding) {
        Graph graph = createGraph();
        graph.removeRoute(initialBuilding, finalBuilding);
        saveGraph(graph);
    }

    private static Graph createGraph() {
        Graph graph = new Graph();
        LinkedList<Building> buildings = readBuildings();
        Route[][] routes = readRoutes(buildings);
        graph.addBuildings(buildings);
        graph.setRoutes(routes);
        return graph;
    }

    private static void saveGraph(Graph graph) {
        writeBuildings(graph.getBuildings());
        writeRoutes(graph.getRoutes());
    }

    private static LinkedList<Building> readBuildings() {
        LinkedList<Building> buildings = new LinkedList<>();
        try {
            BufferedReader reader = new BufferedReader(
                                    new FileReader("src/main/resources/files/buildings.csv"));
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitLine = line.split(",");
                Building building = new Building(splitLine[0], Integer.parseInt(splitLine[1]),
                                                 Integer.parseInt(splitLine[2]));
                if (splitLine.length == 4) {
                    String[] splitPlaces = splitLine[3].split(";");
                    LinkedList<Place> places = new LinkedList<Place>();
                    for (String splitPlace : splitPlaces) {
                        places.add(new Place(splitPlace));
                    }
                    building.setPlaces(places);
                }
                buildings.add(building);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return buildings;
    }

    private static void writeBuildings(LinkedList<Building> buildings) {
        try {
            BufferedWriter writer = new BufferedWriter(
                                    new FileWriter("src/main/resources/files/buildings.csv", false));
            writer.write("name,x,y,places");
            int index = 0;
            Building building = buildings.getNode(index);
            while (building != null) {
                writer.newLine();
                writer.write(building.getName() + "," + building.getPosition()[0] + "," +
                                 building.getPosition()[1] + "," + building.getPlaces().toString());
                index++;
                building = buildings.getNode(index);
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Route[][] readRoutes(LinkedList<Building> buildings) {
        int buildingsSize = buildings.getSize();
        Route[][] routes = new Route[buildingsSize][buildingsSize];;
        String line = "";
        int lineNumberCount = 0;
        try {
            BufferedReader reader = new BufferedReader(
                                    new FileReader("src/main/resources/files/routes.csv"));
            while ((line = reader.readLine()) != null) {
                String[] splitLine = line.split(",");
                for (int i = 0; i < splitLine.length; i++) {
                    Route route = null;
                    if (!splitLine[i].equals("-")) {
                        String[] routeInfo = splitLine[i].split(";");
                        route = new Route(Integer.parseInt(routeInfo[0]), Boolean.parseBoolean(routeInfo[1]),
                                                routeInfo[2], routeInfo[3]);
                    }
                    routes[lineNumberCount][i] = route;
                }
                lineNumberCount++;
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return routes;
    }

    private static void writeRoutes(Route[][] routes) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/files/routes.csv", false));
            for (Route[] buildingRoutes : routes) {
                for (Route route : buildingRoutes) {
                    if (route != null) {
                        writer.write(route.getDistance() + ";" + route.hasStairs() + ";" +
                                route.getBuildings()[0] + ";" + route.getBuildings()[1] + ",");
                    } else {
                        writer.write("-" + ",");
                    }
                }
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
