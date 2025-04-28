package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.shape.Line;
import model.Building;

public class ViewController extends Controller{
    
    @FXML
    private Button switchStairs;
    @FXML
    private Button calcRoute;

    private boolean stairs = false;
    private String bStart = null;
    private String bEnd = null;

    @FXML
    /**
     * Llama al algoritmo Dijkstra del Grafo inicializado, para utilizar
     * el array de la ruta para cambiar el color de las aristas (Líneas) que corresponden
     * a la ruta deseada
     */
    public void calculateRoute(){

        // TODO falta el método dijkstra para poder implementar este.

        System.out.println("grafo.Dijkstra(Nodo inicio: " + bStart + ", Nodo fin: " + bEnd + ", Escaleras? " + stairs + ")");
    }

    @FXML
    /**
     * Recorre todas las líneas existentes en el backPane para puntearlas
     * si las escaleras se están activando o quitarle el punteado a todas si se están desactivando.
     */
    private void setStairs(){

        for(int i=0; i<backPane.getChildren().size(); i++){

            if(backPane.getChildren().get(i) instanceof Line) {
                
                Line l = (Line)backPane.getChildren().get(i);
                
                if (stairs){
                    l.getStrokeDashArray().clear();
                }else{
                    if (l.getId().charAt(6) == '1'){

                        l.getStrokeDashArray().add(6d);
                    }
                }
            }
        }
        stairs = !stairs;
    }

    /**
     * Crea el botón con base en la clase Controller, pero le añade métodos para seleccionarlos y emplearlos en
     * el algoritmo Dijkstra.
     * 
     * @param building
     */
    @Override
    protected void createBuilding(Building building){
        
        super.createBuilding(building);
        Point boton = (Point)frontPane.lookup("#B-" + building.getName());

        //Añadir funcionalidades al botón.
        boton.setOnAction(e ->{

            //Seleccionar Nodos
            if (!boton.isSelected()){
                if (bStart == null){
                    boton.setSelected(true);
                    bStart = boton.getText();
                    boton.setStyle("-fx-border-color: #ff0000; -fx-border-width: 5px");

                }else if (bEnd == null){
                    boton.setSelected(true);
                    bEnd = boton.getText();
                    boton.setStyle("-fx-border-color: GREEN; -fx-border-width: 5px");
                }
            }else{
            //Deseleccionar Nodos
                if (bStart == boton.getText()){
                    boton.setStyle(null);
                    bStart = null;
                }else if (bEnd == boton.getText()){
                    boton.setStyle(null);
                    bEnd = null;
                }

                boton.setSelected(false);
            }
        });
    }
}