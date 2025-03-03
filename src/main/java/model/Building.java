package model;

// Vértice (Nodo) del grafo, en este caso un edificio
public class Building {
    private final String id;
    private final String name;
    // TODO: Lista de sitios (private SiteList sites;)

    public Building(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // TODO: ¿Hace falta implementar setters?
}
