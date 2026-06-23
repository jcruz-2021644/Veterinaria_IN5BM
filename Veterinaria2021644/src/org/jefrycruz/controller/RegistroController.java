package org.jefrycruz.controller;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jefrycruz.db.Conexion;
import org.jefrycruz.models.Registros;

public class RegistroController {

    private String usuario;
    private String contrasena;

    @FXML
    private TextField txtIngresarUsuario;
    @FXML
    private TextField txtIngresarContrasena;


    /*Tabla listar*/
    @FXML
    private TableView<Registros> tblRegistro;
    @FXML
    private TableColumn<Registros, Integer> colcodigoRegistro;
    @FXML
    private TableColumn<Registros, String> colusuarioTabla;
    @FXML
    private TableColumn<Registros, String> colcontrasenaTabla;

    /*Tabla Buscar*/
    @FXML
    private ComboBox<String> cmbBuscarRegistro;
    @FXML
    private TableView<Registros> tblRegistroBuscar;
    @FXML
    private TableColumn<Registros, Integer> colcodigoRegistroBuscar;
    @FXML
    private TableColumn<Registros, String> colusuarioTablaBuscar;
    @FXML
    private TableColumn<Registros, String> colcontrasenaTablaBuscar;
    // eliminar
    @FXML
    private ComboBox<String> cmbEliminarRegistro;

    // Modificar
    @FXML
    private ComboBox<String> cmbModificarCodigoRegistro;

    @FXML
    private TextField txtModificarUsuario;
    @FXML
    private TextField txtModificarContrasena;
    private ObservableList<Registros> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (tblRegistro != null) {
            inicializarTablaListar();
            cargarDatos();
        }

        if (tblRegistroBuscar != null) {
            inicializarTablaBuscar();
        }

        AgregarCodigoRegistroBox();
    }

    private void inicializarTablaListar() {
        colcodigoRegistro.setCellValueFactory(new PropertyValueFactory<>("codigoRegistro"));
        colusuarioTabla.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colcontrasenaTabla.setCellValueFactory(new PropertyValueFactory<>("contrasena"));
    }

    private void inicializarTablaBuscar() {
        colcodigoRegistroBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoRegistro"));
        colusuarioTablaBuscar.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colcontrasenaTablaBuscar.setCellValueFactory(new PropertyValueFactory<>("contrasena"));
    }

    private Connection obtenerConexionSegura() {
        Connection conn = Conexion.getInstancia().getConexion();
        if (conn == null) {
            mostrarAlerta("No se pudo establecer conexión con la base de datos.");
        }
        return conn;
    }

    private void cargarDatos() {
        lista.clear();
        Connection conn = obtenerConexionSegura();
        if (conn == null) {
            return;
        }

        try {
            String sql = "CALL sp_ListarRegistros();";
            CallableStatement cstmt = conn.prepareCall(sql);
            ResultSet rs = cstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Registros(
                        rs.getInt("codigoRegistro"),
                        rs.getString("usuario"),
                        rs.getString("contrasena")
                ));
            }
            tblRegistro.setItems(lista);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void AgregarCodigoRegistroBox() {
        ObservableList<String> registros = FXCollections.observableArrayList();
        String sql = "select codigoRegistro from Registros";

        Connection conn = obtenerConexionSegura();
        if (conn == null) {
            return;
        }

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                registros.add(String.valueOf(rs.getInt("codigoRegistro")));
            }

            if (cmbEliminarRegistro != null) {
                cmbEliminarRegistro.setItems(registros);
            }
            if (cmbModificarCodigoRegistro != null) {
                cmbModificarCodigoRegistro.setItems(registros);
            }
            if (cmbBuscarRegistro != null) {
                cmbBuscarRegistro.setItems(registros);
            }
        } catch (SQLException e) {
            mostrarAlerta("Error al cargar registros: " + e.getMessage());
        }
    }

    @FXML
    private void onAgregarRegistro(ActionEvent event) {
        if (txtIngresarUsuario.getText().isEmpty() || txtIngresarContrasena.getText().isEmpty()) {
            mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
            return;
        }

        Connection conn = obtenerConexionSegura();
        if (conn == null) {
            return;
        }

        try {
            String sql = "CALL sp_AgregarRegistro(?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtIngresarUsuario.getText());
            stmt.setString(2, txtIngresarContrasena.getText());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                mostrarAlerta("Registro agregado correctamente.");
                txtIngresarUsuario.clear();
                txtIngresarContrasena.clear();
                irLogin(event);
            } else {
                mostrarAlerta("No se pudo agregar el registro.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("El nombre de usuario ya existe. Intenta con otro.");
        } catch (SQLException e) {
            mostrarAlerta("Error de base de datos: " + e.getMessage());
        }
    }

    @FXML
    private void onBuscarRegistro(ActionEvent event) {
        ObservableList<Registros> registroBuscado = FXCollections.observableArrayList();
        Connection conn = obtenerConexionSegura();
        if (conn == null) {
            return;
        }

        try {
            PreparedStatement stmt = conn.prepareCall("CALL sp_BuscarRegistro(?);");
            stmt.setInt(1, Integer.parseInt(cmbBuscarRegistro.getValue()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                registroBuscado.add(new Registros(
                        rs.getInt("codigoRegistro"),
                        rs.getString("usuario"),
                        rs.getString("contrasena")
                ));
            }
            if (registroBuscado.isEmpty()) {
                mostrarAlerta("No se encontró ningún Registro.");
            }
            tblRegistroBuscar.setItems(registroBuscado);
        } catch (NumberFormatException e) {
            mostrarAlerta("Por favor ingrese el id del registro");
        } catch (SQLException e) {
            mostrarAlerta("Error al buscar registros: " + e.getMessage());
        }
        cargarDatos();
    }

    @FXML
    private void onModificarRegistro(ActionEvent event) {
        if (cmbModificarCodigoRegistro.getValue() == null
                || txtModificarUsuario.getText().isEmpty()
                || txtModificarContrasena.getText().isEmpty()) {
            mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
            return;
        }

        Connection conn = obtenerConexionSegura();
        if (conn == null) {
            return;
        }

        try {
            String sql = "CALL sp_EditarRegistro(?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, cmbModificarCodigoRegistro.getValue());
            stmt.setString(2, txtModificarUsuario.getText());
            stmt.setString(3, txtModificarContrasena.getText());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                mostrarAlerta("Registro modificado correctamente.");
            } else {
                mostrarAlerta("No se pudo modificar el registro.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("El código es incorrecto.");
        } catch (SQLException e) {
            mostrarAlerta("Error en la base de datos: " + e.getMessage());
        }

        cargarDatos();
        cmbModificarCodigoRegistro.setValue(null);
        txtModificarUsuario.clear();
        txtModificarContrasena.clear();
    }

    @FXML
    private void onEliminarRegistro(ActionEvent event) {
        Connection conn = obtenerConexionSegura();
        if (conn == null) {
            return;
        }

        try {
            int id = Integer.parseInt(cmbEliminarRegistro.getValue());
            PreparedStatement stmt = conn.prepareStatement("CALL sp_EliminarRegistro(?);");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt("filasEliminadas") > 0) {
                mostrarAlerta("Registro eliminado correctamente.");
            } else {
                mostrarAlerta("No se pudo eliminar el Registro.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Ingresa el id correcto por favor.");
        } catch (SQLException e) {
            mostrarAlerta("Error al eliminar el registro: " + e.getMessage());
        }

        cargarDatos();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void irMenuAdmin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuAdminView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Veterinaria JCruz-2021644");
            stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));
            stage.show();

            // Cerrar la ventana actual
            Stage ventanaActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            ventanaActual.close();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo abrir la ventana de login.");
        }
    }

    @FXML
    private void onSalir(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void irLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/LoginView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Veterinaria JCruz-2021644");
            stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));
            stage.show();

            // Cerrar la ventana actual
            Stage ventanaActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            ventanaActual.close();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo abrir la ventana de login.");
        }
    }
}
