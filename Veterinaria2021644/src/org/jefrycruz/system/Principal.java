package org.jefrycruz.system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class Principal extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/RegistrarView.fxml"));
            Parent root = loader.load();
            
            // Cargar el icono de la app
            primaryStage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));
            
            // Configurar la escena
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Veterinaria JCruz-2021644");
            primaryStage.show();
            
        } catch (IOException e) {
            System.err.println("Error al cargar la vista: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}