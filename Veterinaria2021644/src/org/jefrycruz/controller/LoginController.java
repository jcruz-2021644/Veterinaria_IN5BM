package org.jefrycruz.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.image.Image;
import org.jefrycruz.db.Conexion;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private void onIngresar(ActionEvent event) {
        String usuario = txtUsuario.getText();
        String contrasena = txtContrasena.getText();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta("Por favor, complete todos los campos.");
            return;
        }

        try {
            Connection conn = Conexion.getInstancia().getConexion();

            if (conn == null) {
                mostrarAlerta("No hay conexión con la base de datos. Verifique las credenciales o el estado del servidor.");
                return;
            }

            String sql = "SELECT * FROM Registros WHERE usuario = ? AND contrasena = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                loginStage.close();

                cargarMenuPrincipal(event);

            } else {
                mostrarAlerta("Usuario o contraseña incorrectos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error al verificar usuario: " + e.getMessage());
        }
    }

    @FXML
    private void onSalir(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void cargarMenuPrincipal(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Menú Principal");
            stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));
            stage.show();
            ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo cargar el menú principal.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void irRegistro(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/RegistrarView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Veterinaria JCruz-2021644");
        stage.show();
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));

        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }
}
