package controller;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import model.Building;
import javafx.scene.control.ToggleButton;

public class ViewController extends Controller{
    
    @FXML
    private ToggleButton switchStairs;
    @FXML
    private Button calcRoute;
    @FXML
    private Button devMode;

    private boolean stairs = false;
    private String bStart = null;
    private String bEnd = null;
    private boolean shortRoute = false;

    //Este es un String para el botón de ayuda. Como no hay cambio de idiomas, resulta fácil hacerlo como un String
    private String infoHelp = "Para buscar una ruta entre dos edificios, presione los botones con el nombre de los edificios correspondientes y presione el botón Calcular Ruta, entonces podrá ver los caminos que debe tomar resaltados en otro color.\nSi necesita obtener las rutas sin escaleras, presione el botón en la parte superior izquierda del mapa para activar o desactivar esta opción.";

    @FXML
    /**
     * Llama al algoritmo Dijkstra del Grafo inicializado, para utilizar
     * el array de la ruta para cambiar el color de las aristas (Líneas) que corresponden
     * a la ruta deseada
     */
    public void calculateRoute(){
        if (!shortRoute){
            if (bStart != null && bEnd != null){
                String[] ruta = grafo.shortestPath(bStart, bEnd, !stairs);
                
                if (ruta != null){
                    for (int i = 0; i < ruta.length-1; i ++){
                        
                        String b1 = ruta[i];
                        String b2 = ruta[i + 1];

                        System.out.println("BUSCANDO: "+ b1 + " to " + b2);

                        //Buscar las líneas con un ID que contenga el edificio en cuestión.
                        for(int j = 0; j < backPane.getChildren().size(); j++){

                            if(backPane.getChildren().get(j) instanceof Line) {
                                
                                Line l = (Line)backPane.getChildren().get(j);
                                String crrnt = l.getId();

                                String bc1 = "";
                                String bc2 = "";

                                for(int k = 2; k < crrnt.length(); k++){
                                    if (crrnt.toCharArray()[k] != '-'){
                                        bc1 += crrnt.toCharArray()[k];
                                    }else{
                                        break;
                                    }
                                }

                                for(int k = 3 + bc1.length(); k < crrnt.length(); k++){
                                    if (crrnt.toCharArray()[k] != '-'){
                                        bc2 += crrnt.toCharArray()[k];
                                    }else{
                                        break;
                                    }
                                }

                                System.out.println(crrnt);
                                if ((bc1.equals(b1) && bc2.equals(b2)) || (bc2.equals(b1) && bc1.equals(b2))){

                                    l.setStroke(Color.GREEN);//l.setStroke(Color.rgb(204, 51, 101));
                                    System.out.println("ENCONTRÉ LA RUTA");

                                    if (i < ruta.length - 2) break;
                                }else{
                                    if (l.getStroke() != Color.GREEN) l.setStroke(Color.LIGHTGRAY);
                                }
                            }
                        }

                        System.out.println(i);
                    }        
                }else{
                    showPopUp(AlertType.INFORMATION, "INFO", "Ruta inexistente", "La ruta entre " + bStart + " y " + bEnd + " no existe.");
                }
                shortRoute = true;
            }else{
                showPopUp(AlertType.ERROR, "ERROR", "Ha ocurrido un error", "Debe seleccionar un edificio inicial y uno final.");
            }
        }
        System.out.println("grafo.Dijkstra(Nodo inicio: " + bStart + ", Nodo fin: " + bEnd + ", Escaleras? " + stairs + ")");
    }

    @FXML
    /**
     * Recorre todas las líneas existentes en el backPane para puntearlas
     * si las escaleras se están activando o quitarle el punteado a todas si se están desactivando.
     */
    private void setStairs(){
        
        if (shortRoute){
            //Buscar las líneas con un ID que contenga el edificio en cuestión.
            for(int j = 0; j < backPane.getChildren().size(); j++){

                if(backPane.getChildren().get(j) instanceof Line) {
                    
                    Line l = (Line)backPane.getChildren().get(j);

                    l.setStroke(Color.BLACK);
                }
            }
            shortRoute = false;
        }

        for(int i=0; i<backPane.getChildren().size(); i++){

            if(backPane.getChildren().get(i) instanceof Line) {
                
                Line l = (Line)backPane.getChildren().get(i);
                
                if (stairs){
                    l.getStrokeDashArray().clear();
                }else{
                    if (l.getId().charAt(l.getId().length() - 1) == '1'){

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

            if (shortRoute && boton.isSelected()){
                //Buscar las líneas con un ID que contenga el edificio en cuestión.
                for(int j = 0; j < backPane.getChildren().size(); j++){

                    if(backPane.getChildren().get(j) instanceof Line) {
                        
                        Line l = (Line)backPane.getChildren().get(j);
                        l.setStroke(Color.DARKGRAY);
                    }
                }
                shortRoute = false;
            }

            //Seleccionar Nodos
            if (!boton.isSelected()){
                if (bStart == null){
                    boton.setSelected(true);
                    bStart = boton.getText();
                    boton.setStyle("-fx-border-color: Linear-gradient(to left, #CC00FF, #CC3365); -fx-border-width: 4px; -fx-border-insets: -1;");

                }else if (bEnd == null){
                    boton.setSelected(true);
                    bEnd = boton.getText();
                    boton.setStyle("-fx-border-color: Linear-gradient(to left, #CC00FF, #CC3365); -fx-border-width: 4px; -fx-border-insets: -1;");
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

    @FXML
    private void switchMode(){

        //no hay necesidad de hacer que sea una autentificiación de la contraseña por ser un proyecto de aula.
        String rlPass = "admin";

        Dialog<String> dialog = new Dialog<>();

        dialog.setTitle("Modo operador");
        dialog.setHeaderText("Inserte una contraseña");
        ButtonType accept = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(accept, ButtonType.CANCEL);

        //ComboBox, para tener las dos listas donde se mostrarán los edificios.
        PasswordField pass = new PasswordField();
        pass.setPromptText("Contraseña");

        //layout
        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(pass, 0, 0);

        dialog.getDialogPane().setContent(grid);

        // Obtener el resultado al darle ACCEPT
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == accept) {
                return pass.getText();
            }
            return null;
        });

        // Mostrar y manejar resultado
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            
            //Si el nodo de inicio es diferente del nodo de llegada, para evitar eliminar todas las rutas de un edificio
            if (pair.equals(rlPass)){
                changeScene("operator-view.fxml", devMode);
            }else{
                showPopUp(AlertType.ERROR, "ERROR", "Ha ocurrido un error", "La contraseña no es válida.");
            }
        });
    }

    @FXML
    private void showHelp(){
        showPopUp(AlertType.INFORMATION, "Ayuda", "Guía para el uso del programa.", infoHelp);
    }
}
