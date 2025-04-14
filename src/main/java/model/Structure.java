package model;

// Clase padre de la que heredan place y building
public class Structure {
    private int index;
    private final String name;

    public Structure(String name) {
        this.index = 0;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }
}
