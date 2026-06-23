package org.jefrycruz.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import org.jefrycruz.db.Conexion;

public class ContrasenaAdminController {

    @FXML
    private TextField txtContrasena;
    @FXML
    private void onIngresar(ActionEvent event) {
        String contrasena = txtContrasena.getText();

        if (verificarContrasena("1", contrasena)) {
            try {
                mostrarAlerta("Login Exitoso");
                irMenuAdmin(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Contraseña Incorrecta");
        }
    }

    private boolean verificarContrasena(String usuario, String contrasena) {
        String sql = "SELECT * FROM Registros WHERE usuario = ? AND contrasena = ?";

        try  {
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void irMenuAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuAdminView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Veterinaria JCruz-2021644");
        stage.show();
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();

    }

    // Método para salir y regresar a la ventana principal
    @FXML
    private void onSalir() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Veterinaria JCruz-2021644");
        stage.show();
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));

        // Cerrar la ventana actual
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
