package model;

// Aristas del grafo, en este caso una ruta entre dos edificios
public class Route {
    private final int distance;
    private final boolean stairs;

    public Route(int distance, boolean stairs) {
        this.distance = distance;
        this.stairs = stairs;
    }

    public int getDistance() {
        return distance;
    }

    public boolean hasStairs() {
        return stairs;
    }
}
