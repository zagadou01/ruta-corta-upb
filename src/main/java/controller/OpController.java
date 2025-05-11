package controller;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import model.Building;
import model.Place;
import model.Route;
import model.LinkedList;

public class OpController extends Controller{
    
    @FXML
    private MenuItem addRoute;
    @FXML
    private MenuItem delRoute;
    @FXML
    private MenuItem addBuild;
    @FXML
    private MenuItem delBuild;
    @FXML
    private Button cancelAddBuild;
    @FXML
    private MenuButton addList;
    @FXML
    private MenuButton removeList;
    @FXML
    private Button exitDev;

    private boolean addingBuilding = false;

    //Este es un String para el botón de ayuda. Como no hay cambio de idiomas, resulta fácil hacerlo como un String
    private String infoHelp = "El modo desarrollador cuenta con 5 opciones: Agregar o quitar rutas y edificios, y también editar las propiedades de un edificio.\nPara agregar edificios, deberá específicar un nombre y, opcionalmente, lugares importantes. Si quiere editarlo, puede hacer click sobre el correspondiente edificio, donde también podrá cambiar su posición. Al eliminar un edificio, también se borrarán las rutas asociadas\nPara agregar rutas, deberá específicar el inicio y destino, el tiempo que toma recorrerla (en segundos) y si tiene escaleras. Para eliminar una ruta, sólo debe seleccionar los dos edificios asociados.";

    /**
     * Crea un edificio (Point) y le asigna la función editBuildingPlaces(Building, Point) al darle click para editar sus lugares.
     */
    @Override
    protected void createBuilding(Building building){
        
        super.createBuilding(building);
        Point boton = (Point)frontPane.lookup("#B-" + building.getName());

        //Añadir funcionalidades al botón.
        boton.setOnAction(e ->{
            editBuildingPlaces(building.getName());
        });
    }

    /**
     * Crea una línea (Line) y una etiqueta (Label, la cual indica la distancia de la ruta) entre dos Edificios (Point)
     */
    @Override
    protected void createRoute(Route route){
        super.createRoute(route);

        //Obtiene la ruta creada en la clase padre.
        String strStart = route.getBuildings()[0];
        String strEnd = route.getBuildings()[1];
        Line line = (Line)backPane.lookup("#R-" + strStart + "-" + strEnd + "-" + (route.hasStairs() ? "1" : "0"));

        //Si tiene escaleras, se vuelve una línea punteada.
        if (route.hasStairs()) line.getStrokeDashArray().add(6d);

        //Se crea una etiqueta que indique el peso de la Arista.
        Label weight = new Label(Integer.toString(route.getDistance()));
        weight.setLayoutX((line.getStartX() + line.getEndX())/2 - 4.25);
        weight.setLayoutY((line.getStartY() + line.getEndY())/2 - 6.75);
        weight.setStyle("");
        weight.setId("B" + line.getId());
        frontPane.getChildren().add(weight);
    }

    @FXML
    private void addNewRoute(){
        Dialog<String[]> dialog = new Dialog<>();

        dialog.setTitle("Agregar Ruta");
        dialog.setHeaderText("Agregue una ruta entre dos edificios.");
        ButtonType accept = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(accept, ButtonType.CANCEL);

        //ComboBox, para tener las dos listas donde se mostrarán los edificios.
        ComboBox<String> bStart = new ComboBox<>();
        ComboBox<String> bEnd = new ComboBox<>();
        //CheckBox para marcar si tiene escaleras o no
        CheckBox hasStairs = new CheckBox();
        //TextField para indicar el peso o distancia de la Arista.
        TextField weight = new TextField("0");

        // Agregar un listener que solo permita números de 4 cifras para el peso usando regEx.
        weight.setTextFormatter(new TextFormatter<>(change -> {
            String txt = change.getControlNewText();

            if (txt.matches("\\d*") && txt.length()<5) {
                return change;
            }
            return null;
        }));

        //Se le añaden los nombres de los edificios a las listas desplegables y se asignan opciones por defecto.

        String[] builds = getBuildings();

        bStart.getItems().addAll(builds);
        bEnd.getItems().addAll(builds);
        bStart.getSelectionModel().select(0);
        bEnd.getSelectionModel().select(1);

        //layout
        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Inicio:"), 0, 0);
        grid.add(bStart, 1, 0);
        grid.add(new Label("Destino:"), 0, 1);
        grid.add(bEnd, 1, 1);
        grid.add(new Label("¿Hay escaleras?:"), 0, 2);
        grid.add(hasStairs, 1, 2);
        grid.add(new Label("Distancia:"), 0, 3);
        grid.add(weight, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Obtener el resultado al darle ACCEPT
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == accept) {
                return new String[]{bStart.getValue(), bEnd.getValue(), weight.getText(), hasStairs.isSelected() ? "1" : "0"};
            }
            return null;
        });

        // Mostrar y manejar resultado
        Optional<String[]> result = dialog.showAndWait();

        result.ifPresent(pair -> {

            String b1 = pair[0];
            String b2 = pair[1];

            if (b1 != b2){
                LinkedList<Building> buildings = grafo.getBuildings();

                //Si ambos nodos están en el grafo

                Route[][] routes = grafo.getRoutes();

                if (routes[buildings.getIndex(b1)][buildings.getIndex(b2)] == null){

                    FileController.addRoute(grafo, b1, b2, Integer.parseInt(pair[2]), pair[3] == "1");
                }else{
                    showPopUp(AlertType.ERROR, "ERROR", "Ha ocurrido un error", "La ruta que conecta [" + b1 + "] y [" + b2 + "] ya existe.");
                }
            }else{
                showPopUp(AlertType.ERROR, "ERROR", "Ha ocurrido un error", "No se puede añadir una ruta hacia el mismo edificio.");
            }

            grafo.print();
        });

    }

    @FXML
    private void deleteRoute(){

        Dialog<String[]> dialog = new Dialog<>();

        dialog.setTitle("Eliminar Ruta");
        dialog.setHeaderText("Elimine una Ruta entre dos edificios.");
        ButtonType accept = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(accept, ButtonType.CANCEL);

        //ComboBox, para tener las dos listas donde se mostrarán los edificios.
        ComboBox<String> bStart = new ComboBox<>();
        ComboBox<String> bEnd = new ComboBox<>();  

        String[] builds = getBuildings();

        bStart.getItems().addAll(builds);
        bEnd.getItems().addAll(builds);
        bStart.getSelectionModel().select(0);
        bEnd.getSelectionModel().select(1);

        //layout
        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Inicio:"), 0, 0);
        grid.add(bStart, 1, 0);
        grid.add(new Label("Destino:"), 0, 1);
        grid.add(bEnd, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Obtener el resultado al darle ACCEPT
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == accept) {
                return new String[]{bStart.getValue(), bEnd.getValue()};
            }
            return null;
        });

        // Mostrar y manejar resultado
        Optional<String[]> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            
            //Si el nodo de inicio es diferente del nodo de llegada, para evitar eliminar todas las rutas de un edificio
            if (pair[0] != pair[1]){
                deleteLineRoute(pair[0], pair[1]);
            }else{
                showPopUp(AlertType.ERROR, "ERROR", "Ha ocurrido un error", "Los edificios inicial y final deben ser diferentes.");
            }
        });
    }

    /**
     * Permite ubicar un nuevo edificio moviendo el puntero del ratón.
     * Al dar click se llama a la función addBuildingConfig(x, y) siendo x, y las coordenadas del click.
     */
    @FXML
    private void addNewBuilding(){
        
        if (!addingBuilding){
            //addBuild.setText("Cancelar");
            delBuild.setDisable(true);
            addRoute.setDisable(true);
            delRoute.setDisable(true);

            addList.setDisable(true);
            removeList.setDisable(true);

            cancelAddBuild.setVisible(true);
            cancelAddBuild.setDisable(false);

            Circle btnPlace = new Circle(25.5);
            btnPlace.setId("NEW-B");
            btnPlace.setVisible(false);

            //Obtener las coordenadas del mouse para la función de agregar Edificios.
            frontPane.setOnMouseMoved(e ->{
                double x = e.getX();
                double y = e.getY();

                if (y > 30 && y < 575 && x < 820){
                    btnPlace.setTranslateY(y);
                    btnPlace.setTranslateX(x);
                    btnPlace.setVisible(true);
                }else{
                    btnPlace.setVisible(false);
                }
            });
            
            btnPlace.setStroke(Color.DARKGRAY);
            btnPlace.setFill(Color.TRANSPARENT);
            btnPlace.setStrokeWidth(2.0);
            btnPlace.getStrokeDashArray().add(9.0);

            btnPlace.setOnMouseClicked(e ->{
                addBuildingConfig((int)btnPlace.getTranslateX(), (int)btnPlace.getTranslateY());
                addNewBuilding();
            });

            frontPane.getChildren().add(btnPlace);
        }else{
            addBuild.setText("Edificio");
            delBuild.setDisable(false);
            addRoute.setDisable(false);
            delRoute.setDisable(false);

            addList.setDisable(false);
            removeList.setDisable(false);

            cancelAddBuild.setVisible(false);
            cancelAddBuild.setDisable(true);

            frontPane.getChildren().remove(frontPane.lookup("#NEW-B"));
        }

        addingBuilding = !addingBuilding;
    }

    /**
     * Crea una ventana para eliminar un edificio.
     * La ventana contiene una lista desplegable (ComboBox) con los edificios disponibles.
     */
    @FXML
    private void deleteBuilding(){

        Dialog<String> dialog = new Dialog<>();

        dialog.setTitle("Eliminar Edificio");
        dialog.setHeaderText("Elimine un edificio y todas las rutas relacionadas.");
        ButtonType accept = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(accept, ButtonType.CANCEL);

        //ComboBox, para tener la lista donde se mostrarán los edificios.
        ComboBox<String> builds = new ComboBox<>();

        builds.getItems().addAll(getBuildings());
        builds.getSelectionModel().select(0);
        //layout
        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Nombre del Edificio:"), 0, 0);
        grid.add(builds, 1, 0);

        dialog.getDialogPane().setContent(grid);

        // Obtener el resultado al darle ACCEPT
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == accept) {
                return builds.getValue();
            }
            return null;
        });

        // Mostrar y manejar resultado
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            //Se eliminan tanto el edificio como las rutas relacionadas.
            deleteLineRoute(pair, pair);
            deletePointBuilding(pair);
        });
    }

    /**
     * Transforma la LinkedList de edificios del grafo en un Array.
     * 
     * @return un Array de Strings con los nombres de los edificios presentes en el Grafo.
     */
    private String[] getBuildings(){

        int size = grafo.getBuildings().getSize();
        String[] builds = new String[size];

        for (int i = 0; i < size; i++){

            builds[i] = grafo.getBuildings().getName(i);
        }

        return builds;
    }

    /**
     * Crea una ventana emergente con las propiedades necesarias para crear un nuevo edificio en la interfaz gráfica y el grafo.
     * La ventana contiene:
     * * Lista con los lugares (ListView)
     * * Campos de texto para especificar el nombre del edificio y el nombre del lugar a agregar
     * * Un botón para agregar los lugares a la lista
     * 
     * @param x componente horizontal de la posición del nuevo edificio.
     * @param y componente vertical de la posición del nuevo edificio.
     */
    private void addBuildingConfig(int x, int y){
        Dialog<String[]> dialog = new Dialog<>();

        // Lista de lugares.
        ListView<String> listView = placesListGUI();
        listView.setMaxHeight(100);

        dialog.setTitle("Agregar Edificio");
        dialog.setHeaderText("Agregue un nuevo edificio");
        ButtonType accept = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(accept, ButtonType.CANCEL);

        //TextField, para ponerle nombre al edificio y específicar los lugares
        TextField name = new TextField();
        name.setPromptText("Nombre del edificio");
        TextField places = new TextField();
        places.setPromptText("Escriba el lugar");
        Button addPlace = new Button("Agregar");

        // Botón para agregar nuevos lugares
        addPlace.setOnAction(e -> {
            String placeName = places.getText().trim();

            if (!placeName.isEmpty() && !listView.getItems().contains(placeName)) {

                listView.getItems().add(placeName);
                places.clear();
            }
        });

        //layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        HBox hbox = new HBox();
        hbox.setSpacing(10);
        HBox.setHgrow(places, Priority.ALWAYS);
        hbox.getChildren().addAll(places, addPlace);

        grid.add(name, 0, 0);
        grid.add(hbox,0,1);
        grid.add(listView, 0, 2);
        
        dialog.getDialogPane().setContent(grid);

        // Obtener el resultado al darle ACCEPT
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == accept) {
                String strPlaces = "";

                for (int i = 0; i < listView.getItems().size(); i++){
                    strPlaces += listView.getItems().get(i) + ";";
                }
                strPlaces += places.getText();
                return new String[]{name.getText(), strPlaces};
            }
            return null;
        });

        // Mostrar y manejar resultado
        Optional<String[]> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            if (!pair[0].isEmpty()){

                if (grafo.getBuildings().getNode(pair[0]) == null){
                    Building newBuild = new Building(pair[0], x, y);
                    
                    newBuild.setPlaces(placesToList(pair[1]));

                    FileController.addBuilding(grafo, newBuild);
                    grafo.print();
                }else{
                    showPopUp(AlertType.ERROR, "ERROR", "Ha ocurrido un error", "Este Edificio ya existe.");
                }
            }else{
                showPopUp(AlertType.ERROR, "ERROR", "Ha ocurrido un error", "Debe especificar un nombre para el edificio.");
            }
        });
    }

    /**
     * Elimina una ruta del grafo junto a su equivalente (Line) en la interfaz gráfica.
     * 
     * @param bStart Nombre del edificio inicial
     * @param bEnd Nombre del edificio final
     */
    private void deleteLineRoute(String bStart, String bEnd){      
        for(int i=0; i < backPane.getChildren().size(); i++){

            if(backPane.getChildren().get(i) instanceof Line) {
                
                Line l = (Line)backPane.getChildren().get(i);
                String crrntRoute = l.getId();

                String bc1 = "";
                String bc2 = "";

                for(int k = 2; k < crrntRoute.length(); k++){
                    if (crrntRoute.toCharArray()[k] != '-'){
                        bc1 += crrntRoute.toCharArray()[k];
                    }else{
                        break;
                    }
                }

                for(int k = 3 + bc1.length(); k < crrntRoute.length(); k++){
                    if (crrntRoute.toCharArray()[k] != '-'){
                        bc2 += crrntRoute.toCharArray()[k];
                    }else{
                        break;
                    }
                }

                if ((bc1.equals(bStart) && bc2.equals(bEnd)) || (bc2.equals(bStart) && bc1.equals(bEnd)) || (bEnd == bStart && (bc1.equals(bStart) || bc2.equals(bStart)))){
                    //Elimina el numerito que muestra el peso
                    frontPane.getChildren().remove(frontPane.lookup("#B" + l.getId()));
                    //Elimina la línea.
                    backPane.getChildren().remove(l);
                    //Cuando se elimina uno, el siguiente pasa a ser el index actual, entonces hay que volverlo a cuadrar.
                    i--;

                    //Se elimina la ruta del grafo.
                    FileController.removeRoute(grafo, bStart, bEnd);
                }
            }
        }
        grafo.print();
    }

    /**
     * Elimina un Edificio del grafo y al botón que lo representa de la interfaz (frontPane).
     * 
     * @param name nombre del edificio.
     */
    private void deletePointBuilding(String name){
        Point p = (Point)frontPane.lookup("#B-" + name);

        frontPane.getChildren().remove(p);
        FileController.removeBuilding(grafo, name);
        
        grafo.print();
    }

    /**
     * Muestra una ventana emergente para editar los lugares de un edificio.
     * La ventana se compone de:
     * * una lista (ListView)
     * * una caja de texto para introducir el nombre de lugar (TextField)
     * * un botón para agregar el String del lugar a la listra.
     * 
     * @param building
     * @param point
     */
    private void editBuildingPlaces(String bName){
        Dialog<String> dialog = new Dialog<>();
        Point point = (Point)frontPane.lookup("#B-" + bName);

        // Lista de lugares.
        ListView<String> listView = placesListGUI();
        listView.setMaxHeight(100);

        //Añadir los lugares que ya existen.
        LinkedList<Place> bPlaces = point.getPlaces();

        for (int i = 0; i < bPlaces.getSize(); i++) {
            listView.getItems().add(bPlaces.getName(i));
        }

        dialog.setTitle("Editar Edificio");
        dialog.setHeaderText("Edite el edificio " + bName);
        ButtonType accept = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(accept, ButtonType.CANCEL);

        TextField places = new TextField();
        places.setPromptText("Escriba el lugar");
        Button addPlace = new Button("Agregar");

        int[] pos = grafo.getBuildings().getNode(bName).getPosition();

        TextField coordX = new TextField(pos[0] + "");
        TextField coordY = new TextField(pos[1] + "");

        // Botón para agregar nuevos lugares
        addPlace.setOnAction(e -> {
            String placeName = places.getText().trim();

            if (!placeName.isEmpty() && !listView.getItems().contains(placeName)) {

                listView.getItems().add(placeName);
                places.clear();
            }
        });

        //layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        HBox hbox = new HBox();
        hbox.setSpacing(10);
        HBox.setHgrow(places, Priority.ALWAYS);
        hbox.getChildren().addAll(places, addPlace);

        HBox hboxPos = new HBox();
        hboxPos.setSpacing(10);
        HBox.setHgrow(coordX, Priority.ALWAYS);
        HBox.setHgrow(coordY, Priority.ALWAYS);
        hboxPos.getChildren().addAll(coordX, coordY);

        grid.add(hbox, 0, 0);
        grid.add(listView, 0, 1);
        grid.add(new Label("Posición"), 0, 2);
        grid.add(hboxPos, 0, 3);

        dialog.getDialogPane().setContent(grid);

        // Obtener el resultado al darle ACCEPT
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == accept) {
                
                return bName;
            }
            return null;
        });

        // Mostrar y manejar resultado
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            LinkedList<Place> newPlaces = new LinkedList<>();

            for (int i = 0; i < listView.getItems().size(); i++){

                Place nPlace = new Place(listView.getItems().get(i));
                newPlaces.add(nPlace);
            }

            if (!places.getText().isEmpty()){
                
                Place nPlace = new Place(places.getText());
                newPlaces.add(nPlace);
            }

            for (int i = 0; i < grafo.getBuildings().getSize(); i ++){
                System.out.println(grafo.getBuildings().getNode(i).getPlaces().toString());
            }

            FileController.changePlaces(grafo, bName, newPlaces);
            point.setPlaces(newPlaces);

            placesList.setText("[Ninguno]");

            //Cambiar las coordenadas de las líneas relacionadas.
            int newX = Integer.parseInt(coordX.getText());
            int newY = Integer.parseInt(coordY.getText());

            if (pos[0] != newX || pos[1] != newY){
                if (newY > 30 && newY < 575 && newX > 30 && newX < 820){
                    System.out.println("La coordenada de la ruta es diferente");

                    for(int i=0; i < backPane.getChildren().size(); i++){
                        if(backPane.getChildren().get(i) instanceof Line) {
                            Line l = (Line)backPane.getChildren().get(i);
                            String crrntRoute = l.getId();

                            System.out.println(crrntRoute + " : " + bName);
                            if (crrntRoute.contains(bName)){
                                String r = "";

                                for(int j = 2; j < crrntRoute.length(); j++){
                                    if (crrntRoute.toCharArray()[j] != '-'){
                                        r += crrntRoute.toCharArray()[j];
                                    }else{
                                        break;
                                    }
                                }
                                System.out.println("R es: " + r);

                                //está de primero
                                if (r.equals(bName)){
                                    l.setStartX(newX);
                                    l.setStartY(newY);
                                }else{
                                    System.out.println("CAMBIANDO COORDS FINALES");
                                    l.setEndX(newX);
                                    l.setEndY(newY);
                                }

                                Label weight = (Label)frontPane.lookup("#B" + crrntRoute);
                                weight.setLayoutX((l.getStartX() + l.getEndX())/2);
                                weight.setLayoutY((l.getStartY() + l.getEndY())/2);
                            }
                        }
                    }
                    point.setLayoutX(newX - point.getPrefWidth()/2);
                    point.setLayoutY(newY - point.getPrefHeight()/2);
                    FileController.changePosition(grafo, bName, newX, newY);
                }
            }
            
            grafo.print();
        });
    }

    /**
     * Transforma un String con lugares separados por punto y coma (;) a una LinkedList de lugares (Place).
     * 
     * @param strPlaces String de los lugares, donde cada lugar está separado por un punto y coma (;)
     * @return
     */
    private LinkedList<Place> placesToList (String strPlaces){
    
        String placeName = "";
        LinkedList<Place> newPlaces = new LinkedList<>();

        System.out.println(strPlaces);

        for (int i = 0; i < strPlaces.length(); i++){

            if (strPlaces.charAt(i) != ';'){
                placeName += strPlaces.charAt(i);
                continue;
            }else{
                newPlaces.add(new Place(placeName));
                placeName = "";
            }
        }

        newPlaces.add(new Place(placeName));
        return newPlaces;
    }

    /**
     * Crea una lista para mostrar los lugares de cada edificio.
     * Cada celda se compone de una Label (texto) para ver el nombre de lugar,
     * y un botón (Button) para eliminar el elemento de la lista.
     * 
     * @return una ListView, que es un elemento de javafx que corresponde a una lista.
     */
    private ListView<String> placesListGUI(){

        // Lista de lugares.
        ListView<String> listView = new ListView<>();

        listView.setMaxHeight(100);

        // Personalizar cómo se ve cada celda
        listView.setCellFactory(lv -> new ListCell<>() {
            private Label placeName = new Label();
            private Button btnDelete = new Button("X");
            private HBox content = new HBox(10, placeName, btnDelete);

            //Las llaves son un initializer para que en cada celda se ejecute este código.
            {
                placeName.setPrefWidth(197);

                btnDelete.setAlignment(Pos.TOP_RIGHT);

                btnDelete.setOnAction(e -> {

                    listView.getItems().remove(listView.getItems().size() - 1);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    //Se le asigna el valor a la label.
                    placeName.setText(item);
                    setGraphic(content);
                }
            }
        });

        return listView;
    }

    @FXML
    private void switchMode(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmación");
        alert.setContentText("¿Estas seguro de que quiere salir?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK){
            changeScene("user-view.fxml", exitDev);
        }
    }

    @FXML
    private void showHelp(){
        showPopUp(AlertType.INFORMATION, "Ayuda", "Guía para el uso del programa.", infoHelp);
    }
}