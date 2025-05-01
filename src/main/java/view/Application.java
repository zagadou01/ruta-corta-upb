package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {

        /*
         * Por ahora hay que cambiar la vista desde acá xd
         * 
         * operator
         * user
         * 
         * La contraseña es admin
         */

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("user-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage.setTitle("Ruta corta UPB");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}