package org.jefrycruz.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private void salirPrograma(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/LoginView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Vista Cliente JCruz-2021644");
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));
        stage.setResizable(false);
        stage.show();
        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irMenuCliente(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuClienteView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Vista Cliente JCruz-2021644");
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));
        stage.setResizable(false);
        stage.show();
        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irMenuCita(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuCitaView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Vista Cita JCruz-2021644");
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));
        stage.setResizable(false);
        stage.show();

        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irMenuReceta(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuRecetaView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Vista Receta JCruz-2021644");
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));
        stage.setResizable(false);
        stage.show();
        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irMenuMascota(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuMascotaView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Vista Mascota JCruz-2021644");
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));
        stage.setResizable(false);
        stage.show();
        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irMenuVeterinario(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuVeterinarioView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Vista Veterinario JCruz-2021644");

        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));
        stage.setResizable(false);
        stage.show();
        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irMenuTratamiento(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuTratamientoView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Vista Tratamiento JCruz-2021644");
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));
        stage.setResizable(false);
        stage.show();
        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irMenuVacunacion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuVacunacionesView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Vista Vacunacion JCruz-2021644");
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));
        stage.setResizable(false);
        stage.show();
        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irMenuMedicamento(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuMedicamentosView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Vista Medicamento JCruz-2021644");
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));
        stage.setResizable(false);
        stage.show();
        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irMenuFactura(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuFacturaView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Vista Factura JCruz-2021644");
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));
        stage.setResizable(false);
        stage.show();
        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irMenuAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ContrasenaAdminView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Vista Login Admin JCruz-2021644");
        stage.show();
        
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));

        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }
}
