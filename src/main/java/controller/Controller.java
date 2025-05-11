package controller;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import model.Building;
import model.Graph;
import model.Route;


// Clase padre que genera los Nodos y líneas, además de cargar el grafo.
 
public abstract class Controller {

    @FXML
    protected Label placesList;
    @FXML
    protected VBox mainAnchor;
    @FXML
    protected AnchorPane backPane;
    @FXML
    protected AnchorPane frontPane;
    @FXML
    protected Button help;

    private String infoHelp;

    protected Graph grafo;

    @FXML
    public void initialize(){

        mainAnchor.setMaxSize(1080, 720);
        frontPane.setMaxSize(1080, 720);
        backPane.setMaxSize(1080, 720);

        mainAnchor.setPrefSize(1080, 720);

        //Listeners para agregar automáticamente Nodos o Aristas a la GUI
        FileController.setOnBuildingAdded(nodo -> {
            Platform.runLater(() -> createBuilding(nodo));
        });

        FileController.setOnRouteAdded(route -> {
            Platform.runLater(() -> createRoute(route));
        });

        grafo = FileController.createGraph();

        // TODO carga de archivos, csv o Json

        // agregando nodos (TEST, luego se cargarán desde un archivo csv)
        /*Building b1 = new Building("J", 500, 300);
        b1.addPlace(new Place("Biblioteca"));
        b1.addPlace(new Place("Salas de asesoría"));

        FileController.addBuilding(grafo, new Building("A", 100, 100));
        FileController.addBuilding(grafo, new Building("B", 100, 200));
        FileController.addBuilding(grafo, new Building("C", 200, 100));
        FileController.addBuilding(grafo, b1);

        FileController.addRoute(grafo, "A", "B", 5, true);
        FileController.addRoute(grafo, "A", "B", 5, true);
        FileController.addRoute(grafo, "C", "B", 0, false);
        FileController.addRoute(grafo, "C", "J", 0, false);
        FileController.addRoute(grafo,"A", "J", 100, false);
*/
        //FileController.
        grafo.print();
    }

    /**
     * Crea botones de clase Point en la interfaz gráfica.
     * 
     * @param building
     * @return Point
     */
    protected void createBuilding(Building building){
        int x = building.getPosition()[0];
        int y = building.getPosition()[1];
        String name = building.getName();
        Point boton = new Point();
        boton.setText(name);
        boton.setPlaces(building.getPlaces());

        boton.setPrefSize(50.0, 50.0);
        boton.setLayoutX(x - boton.getPrefWidth()/2);
        boton.setLayoutY(y - boton.getPrefHeight()/2);
        
        boton.setId("B-" + name);

        boton.setShape(new Circle(5));

        boton.setOnMouseEntered(e ->{

            /*
             * Esto se va a cambiar cuando se empiece a trabajar en añadir el estilo.
             */

            //Actualizar la información de los lugares.
            if (boton.getPlaces().getSize() > 0){
                String strPlaces = "[" + boton.getText()+"]\n";

                for (int i = 0; i < boton.getPlaces().getSize(); i++){
                    strPlaces += boton.getPlaces().getName(i) + "\n";
                }
                placesList.setText(strPlaces);

            }else{
                placesList.setText("[" + boton.getText()+"]\n[Ninguno]");
            }
        });

        frontPane.getChildren().add(boton);
    }

    /**
     * Crea una línea entre dos botones (Point).
     * 
     * @param route
     * @param grafo
     */
    protected void createRoute(Route route){
        String strStart = route.getBuildings()[0];
        String strEnd = route.getBuildings()[1];

        Building bStart = grafo.getBuildings().getNode(strStart);
        Building bEnd = grafo.getBuildings().getNode(strEnd);

        double[] position = new double[]{bStart.getPosition()[0], bStart.getPosition()[1], bEnd.getPosition()[0], bEnd.getPosition()[1]};

        Line line = new Line(position[0], position[1], position[2], position[3]);
        line.setId("R-" + strStart + "-" + strEnd + "-" + (route.hasStairs() ? 1 : 0));
        line.setStrokeWidth(3.0);

        backPane.getChildren().add(line);

        System.out.println(line.getId());
    }

    protected void showPopUp(AlertType type, String title, String header, String info){
        Alert alert = new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(header);

        TextArea textArea = new TextArea(info);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        // Hacer que el área de texto se expanda
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane content = new GridPane();
        content.setMaxWidth(Double.MAX_VALUE);
        content.add(textArea, 0, 0);

        alert.getDialogPane().setContent(content);

        alert.setContentText(info);

        alert.show();
    }

    protected void changeScene(String sceneName, Node reference){

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/" + sceneName));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage)reference.getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        }catch (IOException e){

            e.printStackTrace();
        }
    }
}