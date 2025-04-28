package model;

// Vértice (Nodo) del grafo, en este caso un edificio
public class Building extends Structure {
    private LinkedList<Place> places = new LinkedList<>();
    private int[] position;

    public Building(String name) {
        super(name);
        position = new int[]{0, 0};
    }

    public Building(String name, int x, int y) {
        super(name);
        position = new int[]{x, y};
    }

    public void addPlace(Place place) {
        places.add(place);
    }

    public LinkedList<Place> getPlaces() {
        return places;
    }

    public void setPlaces(LinkedList<Place> newPlaces){
        places = newPlaces;
    }

    public int[] getPosition(){
        return position;
    }
}
