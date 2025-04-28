package model;

// Aristas del grafo, en este caso una ruta entre dos edificios
public class Route {
    private final int distance;
    private final boolean stairs;
    private String[] buildings;

    public Route(int distance, boolean stairs, String bStart, String bEnd) {
        this.distance = distance;
        this.stairs = stairs;
        buildings = new String[]{bStart, bEnd};
    }

    public int getDistance() {
        return distance;
    }

    public boolean hasStairs() {
        return stairs;
    }

    public String[] getBuildings(){
        return buildings;
    }
}
