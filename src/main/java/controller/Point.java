package controller;

import javafx.scene.control.Button;
import model.LinkedList;
import model.Place;
/**
 * Esta clase sólo existe para poder añadirle propiedades a los botones que sirven como Nodos visuales.
 * - Los lugares se extraen directamente del Building para poder utilizarlos después.
 * - Selected es para saber si está seleccionado, ya sea como nodo inicial o final.
 */
public class Point extends Button{
    
    LinkedList<Place> places = new LinkedList<>();
    private boolean selected = false;

    public void setPlaces(LinkedList<Place> newPlaces){

        places = newPlaces;
    }

    public LinkedList<Place> getPlaces(){

        return places;
    }

    public boolean isSelected(){
        return selected;
    }

    public void setSelected(boolean state){
        selected = state;
    }

}
