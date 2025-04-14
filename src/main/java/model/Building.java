package model;

// Vértice (Nodo) del grafo, en este caso un edificio
public class Building extends Structure {
    private final LinkedList<Place> places;

    public Building(String name) {
        super(name);
        places = new LinkedList<>();
    }

    public void addPlace(Place place) {
        places.add(place);
    }

    public LinkedList<Place> getPlaces() {
        return places;
    }
}
