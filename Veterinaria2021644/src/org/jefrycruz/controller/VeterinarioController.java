package org.jefrycruz.controller;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jefrycruz.db.Conexion;
import org.jefrycruz.models.Veterinarios;

public class VeterinarioController {

    // La tabla mas las columnas de solo el listar
    @FXML
    private TableView<Veterinarios> tblVeterinario;
    @FXML
    private TableColumn<Veterinarios, Integer> colcodigoVeterinario;
    @FXML
    private TableColumn<Veterinarios, String> colnombreVeterinario;
    @FXML
    private TableColumn<Veterinarios, String> colapellidoVeterinario;
    @FXML
    private TableColumn<Veterinarios, String> colespecialidadVeterinario;
    @FXML
    private TableColumn<Veterinarios, String> coltelefonoVeterinario;
    @FXML
    private TableColumn<Veterinarios, String> colcorreoVeterinario;
    @FXML
    private TableColumn<Veterinarios, Date> colfechaVeterinario;
    @FXML
    private TableColumn<Veterinarios, String> colestadoVeterinario;

    // La tabla mas la coluimnas del buscar
    @FXML
    private TextField txtBuscarVeterinario;
    @FXML
    private TableView<Veterinarios> tblVeterinarioBuscar;
    @FXML
    private TableColumn<Veterinarios, Integer> colcodigoVeterinarioBuscar;
    @FXML
    private TableColumn<Veterinarios, String> colnombreVeterinarioBuscar;
    @FXML
    private TableColumn<Veterinarios, String> colapellidoVeterinarioBuscar;
    @FXML
    private TableColumn<Veterinarios, String> colespecialidadVeterinarioBuscar;
    @FXML
    private TableColumn<Veterinarios, String> coltelefonoVeterinarioBuscar;
    @FXML
    private TableColumn<Veterinarios, String> colcorreoVeterinarioBuscar;
    @FXML
    private TableColumn<Veterinarios, Date> colfechaVeterinarioBuscar;
    @FXML
    private TableColumn<Veterinarios, String> colestadoVeterinarioBuscar;

    // Para eliminar
    @FXML
    private TextField txtEliminarVeterinario;

    // Para agregar
    @FXML
    private TextField txtIngresarNombreVeterinario;
    @FXML
    private TextField txtIngresarApellidoVeterinario;
    @FXML
    private TextField txtIngresarEspecialidadVeterinario;
    @FXML
    private TextField txtIngresarTelefonoVeterinario;
    @FXML
    private TextField txtIngresarCorreoVeterinario;
    @FXML
    private DatePicker dpIngresarRegistroVeterinario;
    @FXML
    private ComboBox<String> cmbEstadoVeterinario;

    // Para Modificar
    @FXML
    private TextField txtModificarCodigoVeterinario;
    @FXML
    private TextField txtModificarNombreVeterinario;
    @FXML
    private TextField txtModificarApellidoVeterinario;
    @FXML
    private TextField txtModificarEspecialidadVeterinario;
    @FXML
    private TextField txtModificarTelefonoVeterinario;
    @FXML
    private TextField txtModificarCorreoVeterinario;
    @FXML
    private DatePicker dpModificarRegistroVeterinario;

    private ObservableList<Veterinarios> lista = FXCollections.observableArrayList();

    // --- Inicializar Listar ---
    @FXML
    public void initialize() {
        if (tblVeterinario != null) {
            inicializarTablaListar();
            cargarDatos();
        }

        if (tblVeterinarioBuscar != null) {
            inicializarTablaBuscar();
        }

        cargarEnumVeterinario();

    }

    private void inicializarTablaListar() {
        colcodigoVeterinario.setCellValueFactory(new PropertyValueFactory<>("codigoVeterinario"));
        colnombreVeterinario.setCellValueFactory(new PropertyValueFactory<>("nombreVeterinario"));
        colapellidoVeterinario.setCellValueFactory(new PropertyValueFactory<>("apellidoVeterinario"));
        colespecialidadVeterinario.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        coltelefonoVeterinario.setCellValueFactory(new PropertyValueFactory<>("telefonoVeterinario"));
        colcorreoVeterinario.setCellValueFactory(new PropertyValueFactory<>("correoVeterinario"));
        colfechaVeterinario.setCellValueFactory(new PropertyValueFactory<>("fechaIngreso"));
        colestadoVeterinario.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void inicializarTablaBuscar() {
        colcodigoVeterinarioBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoVeterinario"));
        colnombreVeterinarioBuscar.setCellValueFactory(new PropertyValueFactory<>("nombreVeterinario"));
        colapellidoVeterinarioBuscar.setCellValueFactory(new PropertyValueFactory<>("apellidoVeterinario"));
        colespecialidadVeterinarioBuscar.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        coltelefonoVeterinarioBuscar.setCellValueFactory(new PropertyValueFactory<>("telefonoVeterinario"));
        colcorreoVeterinarioBuscar.setCellValueFactory(new PropertyValueFactory<>("correoVeterinario"));
        colfechaVeterinarioBuscar.setCellValueFactory(new PropertyValueFactory<>("fechaIngreso"));
        colestadoVeterinarioBuscar.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void cargarEnumVeterinario() {
        ObservableList<String> enumCita = FXCollections.observableArrayList(
                "Activo", "Inactivo");
        if (cmbEstadoVeterinario != null) {
            cmbEstadoVeterinario.setItems(enumCita);
        }
    }

    private void cargarDatos() {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            lista.clear();
            String sql = "CALL sp_ListarVeterinarios();";
            CallableStatement cstmt = conn.prepareCall(sql);
            ResultSet rs = cstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Veterinarios(
                        rs.getInt("codigoVeterinario"),
                        rs.getString("nombreVeterinario"),
                        rs.getString("apellidoVeterinario"),
                        rs.getString("especialidad"),
                        rs.getString("telefonoVeterinario"),
                        rs.getString("correoVeterinario"),
                        rs.getDate("fechaIngreso"),
                        Veterinarios.Estado.valueOf(rs.getString("estado"))
                ));
            }

            tblVeterinario.setItems(lista);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAgregarVeterinario(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            if (txtIngresarNombreVeterinario.getText().isEmpty()
                    || txtIngresarApellidoVeterinario.getText().isEmpty()
                    || txtIngresarEspecialidadVeterinario.getText().isEmpty()
                    || txtIngresarTelefonoVeterinario.getText().isEmpty()
                    || txtIngresarCorreoVeterinario.getText().isEmpty()
                    || dpIngresarRegistroVeterinario == null
                    || cmbEstadoVeterinario == null) {
                mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
                return;
            }

            String sql = "CALL sp_AgregarVeterinario(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtIngresarNombreVeterinario.getText());
            stmt.setString(2, txtIngresarApellidoVeterinario.getText());
            stmt.setString(3, txtIngresarEspecialidadVeterinario.getText());
            stmt.setString(4, txtIngresarTelefonoVeterinario.getText());
            stmt.setString(5, txtIngresarCorreoVeterinario.getText());
            stmt.setDate(6, java.sql.Date.valueOf(dpIngresarRegistroVeterinario.getValue()));
            stmt.setString(7, cmbEstadoVeterinario.getValue());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarAlerta("Veterinario agregado correctamente.");
            } else {
                mostrarAlerta("No se pudo agregar el veterinario.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al agregar veterinario: " + e.getMessage());
        }
        txtIngresarNombreVeterinario.clear();
        txtIngresarApellidoVeterinario.clear();
        txtIngresarEspecialidadVeterinario.clear();
        txtIngresarTelefonoVeterinario.clear();
        txtIngresarCorreoVeterinario.clear();
        dpIngresarRegistroVeterinario.setValue(null);
        cmbEstadoVeterinario.setValue(null);
    }

    @FXML
    private void onModificarVeterinario(ActionEvent event) {

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            if (txtModificarCodigoVeterinario.getText().isEmpty()
                    || txtModificarNombreVeterinario.getText().isEmpty()
                    || txtModificarApellidoVeterinario.getText().isEmpty()
                    || txtModificarEspecialidadVeterinario.getText().isEmpty()
                    || txtModificarTelefonoVeterinario.getText().isEmpty()
                    || txtModificarCorreoVeterinario.getText().isEmpty()
                    || dpModificarRegistroVeterinario == null
                    || cmbEstadoVeterinario == null) {
                mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
                return;
            }

            int codigoV;
            try {
                codigoV = Integer.parseInt(txtModificarCodigoVeterinario.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("El código del veterinario debe ser un número válido.");
                return;
            }
            String sql = "CALL sp_EditarVeterinario(?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, Integer.parseInt(txtModificarCodigoVeterinario.getText()));
            stmt.setString(2, txtModificarNombreVeterinario.getText());
            stmt.setString(3, txtModificarApellidoVeterinario.getText());
            stmt.setString(4, txtModificarEspecialidadVeterinario.getText());
            stmt.setString(5, txtModificarTelefonoVeterinario.getText());
            stmt.setString(6, txtModificarCorreoVeterinario.getText());
            stmt.setDate(7, java.sql.Date.valueOf(dpModificarRegistroVeterinario.getValue()));
            stmt.setString(8, cmbEstadoVeterinario.getValue());
            int filasAfectadas = stmt.executeUpdate();
            // igual el menssaje si se realizo bien o hay un error
            if (filasAfectadas > 0) {
                mostrarAlerta("Veterianrio modificado correctamente.");
            } else {
                mostrarAlerta("No se pudo modificar el veterinario.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("Codigo Veterinario no existe.");
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().toLowerCase().contains("fk")) {
                mostrarAlerta("Error:Codigo Veterianrio esta mal no existe.");
            } else {
                mostrarAlerta("Error al agregar el veterimario: " + e.getMessage());
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al modificar veterinario: " + e.getMessage());
        }
        txtModificarCodigoVeterinario.clear();
        txtModificarNombreVeterinario.clear();
        txtModificarApellidoVeterinario.clear();
        txtModificarEspecialidadVeterinario.clear();
        txtModificarTelefonoVeterinario.clear();
        txtModificarCorreoVeterinario.clear();
        dpModificarRegistroVeterinario.setValue(null);
        cmbEstadoVeterinario.setValue(null);

    }

    @FXML
    private void onEliminarVeterinario(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            int id = Integer.parseInt(txtEliminarVeterinario.getText());

            PreparedStatement stmt = conn.prepareStatement("CALL sp_EliminarVeterinario(?);");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int contador = rs.getInt("filasEliminadas");

                if (contador > 0) {
                    mostrarAlerta("Veterinario eliminado correctamente.");
                } else {
                    mostrarAlerta("No se pudo eliminar el Veterinario.");
                }
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id del Veterinario a Eliminar.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtEliminarVeterinario.clear();

    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void onBuscarVeterinario(ActionEvent event) {
        ObservableList<Veterinarios> veterinarioBuscado = FXCollections.observableArrayList();

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement("CALL sp_BuscarVeterinario(?);");
            stmt.setInt(1, Integer.parseInt(txtBuscarVeterinario.getText()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                veterinarioBuscado.add(new Veterinarios(
                        rs.getInt("codigoVeterinario"),
                        rs.getString("nombreVeterinario"),
                        rs.getString("apellidoVeterinario"),
                        rs.getString("especialidad"),
                        rs.getString("telefonoVeterinario"),
                        rs.getString("correoVeterinario"),
                        rs.getDate("fechaIngreso"),
                        Veterinarios.Estado.valueOf(rs.getString("estado"))
                ));

            }

            if (veterinarioBuscado.isEmpty()) {
                mostrarAlerta("No se encontro ningun veterinario.");
            }
            tblVeterinarioBuscar.setItems(veterinarioBuscado);
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id del veterinario a bucar.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        txtBuscarVeterinario.clear();
    }

    @FXML
    private void irMenuVeterinario(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuVeterinarioView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("JCruz-2021644");
        stage.show();
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));

        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irMenuVeterinarioAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuVeterinarioAdminView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("JCruz-2021644");
        stage.show();
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));

        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Veterinaria JCruz-2021644");

        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));
        stage.show();
        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irListarVeterinarios(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ListarVeterinariosView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Veterinaria JCruz-2021644");
        stage.show();
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));

        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irMenuAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuAdminView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("JCruz-2021644");
        stage.show();
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));

        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irAgregarVeterinario(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/AgregarVeterinarioView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Veterinaria JCruz-2021644");
        stage.show();
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));

        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irBuscarVeterinario(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/BuscarVeterinarioView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Veterinaria JCruz-2021644");
        stage.show();
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));

        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irModificarVeterinario(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ModificarVeterinarioView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Veterinaria JCruz-2021644");
        stage.show();
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));

        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irEliminarVeterinario(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/EliminarVeterinarioView.fxml"));
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
