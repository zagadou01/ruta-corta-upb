package model;

// Vértice (Nodo) del grafo, en este caso un edificio
public class Building extends Structure {
    private LinkedList<Place> places;

    public Building(String name) {
        super(name);
    }

    public LinkedList<Place> getPlaces() {
        return places;
    }
}
