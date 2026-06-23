package org.jefrycruz.controller;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jefrycruz.db.Conexion;
import org.jefrycruz.models.Consultas;

public class ConsultaController {

    // La tabla mas las columnas de solo el listar
    @FXML
    private TableView<Consultas> tblConsulta;
    @FXML
    private TableColumn<Consultas, Integer> colcodigoConsulta;
    @FXML
    private TableColumn<Consultas, Date> colfechaConsulta;
    @FXML
    private TableColumn<Consultas, String> colmotivo;
    @FXML
    private TableColumn<Consultas, String> coldiagnostico;
    @FXML
    private TableColumn<Consultas, String> colobservaciones;
    @FXML
    private TableColumn<Consultas, Integer> colcodigoMascota;
    @FXML
    private TableColumn<Consultas, Integer> colcodigoVeterinario;

    // La tabla mas la coluimnas del buscar
    @FXML
    private TextField txtBuscarConsulta;
    @FXML
    private TableView<Consultas> tblConsultaBuscar;
    @FXML
    private TableColumn<Consultas, Integer> colcodigoConsultaBuscar;
    @FXML
    private TableColumn<Consultas, Date> colfechaConsultaBuscar;
    @FXML
    private TableColumn<Consultas, String> colmotivoBuscar;
    @FXML
    private TableColumn<Consultas, String> coldiagnosticoBuscar;
    @FXML
    private TableColumn<Consultas, String> colobservacionesBuscar;
    @FXML
    private TableColumn<Consultas, Integer> colcodigoMascotaBuscar;
    @FXML
    private TableColumn<Consultas, Integer> colcodigoVeterinarioBuscar;
    // Para el eliminar
    @FXML
    private TextField txtEliminarConsulta;

    // Para hacer el AgregarClienteView
    @FXML
    private DatePicker dpIngresarFechaConsulta;
    @FXML
    private TextField txtIngresarMotivoConsulta;
    @FXML
    private TextField txtIngresarDiagnosticoConsulta;
    @FXML
    private TextField txtIngresarObservacionesConsulta;
    @FXML
    private TextField txtIngresarCodigoMascotaConsulta;
    @FXML
    private TextField txtIngresarCodigoVeterinarioConsulta;

    // para hacer el modificar
    @FXML
    private TextField txtModificarCodigoConsulta;
    @FXML
    private DatePicker dpModificarFechaConsulta;
    @FXML
    private TextField txtModificarMotivoConsulta;
    @FXML
    private TextField txtModificarDiagnosticoConsulta;
    @FXML
    private TextField txtModificarObservacionesConsulta;
    @FXML
    private TextField txtModificarCodigoMascotaConsulta;
    @FXML
    private TextField txtModificarCodigoVeterinarioConsulta;

    private ObservableList<Consultas> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (tblConsulta != null) {
            inicializarTablaListar();
            cargarDatos();
        }

        if (tblConsultaBuscar != null) {
            inicializarTablaBuscar();
        }
    }

    private void inicializarTablaListar() {
        colcodigoConsulta.setCellValueFactory(new PropertyValueFactory<>("codigoConsulta"));
        colfechaConsulta.setCellValueFactory(new PropertyValueFactory<>("fechaConsulta"));
        colmotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        coldiagnostico.setCellValueFactory(new PropertyValueFactory<>("diagnostico"));
        colobservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        colcodigoMascota.setCellValueFactory(new PropertyValueFactory<>("codigoMascota"));
        colcodigoVeterinario.setCellValueFactory(new PropertyValueFactory<>("codigoVeterinario"));

    }

    private void inicializarTablaBuscar() {
        colcodigoConsultaBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoConsulta"));
        colfechaConsultaBuscar.setCellValueFactory(new PropertyValueFactory<>("fechaConsulta"));
        colmotivoBuscar.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        coldiagnosticoBuscar.setCellValueFactory(new PropertyValueFactory<>("diagnostico"));
        colobservacionesBuscar.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        colcodigoMascotaBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoMascota"));
        colcodigoVeterinarioBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoVeterinario"));
    }

    private void cargarDatos() {

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            lista.clear();
            String sql = "CALL sp_ListarConsultas();";
            CallableStatement stmt = conn.prepareCall(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Consultas(
                        rs.getInt("codigoConsulta"),
                        rs.getDate("fechaConsulta"),
                        rs.getString("motivo"),
                        rs.getString("diagnostico"),
                        rs.getString("observaciones"),
                        rs.getInt("codigoMascota"),
                        rs.getInt("codigoVeterinario")
                ));
            }
            tblConsulta.setItems(lista);
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    @FXML
    private void onBuscarConsulta(ActionEvent event) {
        ObservableList<Consultas> consultaBuscada = FXCollections.observableArrayList();

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareCall("CALL sp_BuscarConsulta(?);");
            stmt.setInt(1, Integer.parseInt(txtBuscarConsulta.getText()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                consultaBuscada.add(new Consultas(
                        rs.getInt("codigoConsulta"),
                        rs.getDate("fechaConsulta"),
                        rs.getString("motivo"),
                        rs.getString("diagnostico"),
                        rs.getString("observaciones"),
                        rs.getInt("codigoMascota"),
                        rs.getInt("codigoVeterinario")
                ));
            }
            if (consultaBuscada.isEmpty()) {
                mostrarAlerta("No se encontro ninguna consulta.");
            }
            tblConsultaBuscar.setItems(consultaBuscada);
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id de la consulta a bucar.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        txtBuscarConsulta.clear();
    }

    @FXML
    private void onAgregarConsulta(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            if (dpIngresarFechaConsulta == null
                    || txtIngresarMotivoConsulta.getText().isEmpty()
                    || txtIngresarDiagnosticoConsulta.getText().isEmpty()
                    || txtIngresarObservacionesConsulta.getText().isEmpty()
                    || txtIngresarCodigoMascotaConsulta.getText().isEmpty()
                    || txtIngresarCodigoVeterinarioConsulta.getText().isEmpty()) {
                mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
                return;
            }

            int codMascota;
            try {
                codMascota = Integer.parseInt(txtIngresarCodigoMascotaConsulta.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("El codigo mascota tiene que set un numero entero");
                return;
            }

            int codVeterinario;
            try {
                codVeterinario = Integer.parseInt(txtIngresarCodigoVeterinarioConsulta.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("El codigo veterinario tiene que set un numero entero");
                return;
            }

            String sql = "CALL sp_AgregarConsulta(?, ?, ?, ?, ?, ?);";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDate(1, java.sql.Date.valueOf(dpIngresarFechaConsulta.getValue()));
            stmt.setString(2, txtIngresarMotivoConsulta.getText());
            stmt.setString(3, txtIngresarDiagnosticoConsulta.getText());
            stmt.setString(4, txtIngresarObservacionesConsulta.getText());
            stmt.setString(5, txtIngresarCodigoMascotaConsulta.getText());
            stmt.setString(6, txtIngresarCodigoVeterinarioConsulta.getText());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarAlerta("Consulta agregada correctamente.");
            } else {
                mostrarAlerta("No se pudo agregar la Consulta.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("Uno de los dos codigos no existe.");
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().toLowerCase().contains("fk")) {
                mostrarAlerta("Error: Uno de los dos codigos esta mal no existe.");
            } else {
                mostrarAlerta("Error al modificar la consulta: " + e.getMessage());
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al modificar la consulta: " + e.getMessage());
        }

        dpIngresarFechaConsulta.setValue(null);
        txtIngresarMotivoConsulta.clear();
        txtIngresarDiagnosticoConsulta.clear();
        txtIngresarObservacionesConsulta.clear();
        txtIngresarCodigoMascotaConsulta.clear();
        txtIngresarCodigoVeterinarioConsulta.clear();
    }

    @FXML
    private void onModificarConsulta(ActionEvent event) {

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            if (txtModificarCodigoConsulta.getText().isEmpty()
                    || dpModificarFechaConsulta == null
                    || txtModificarMotivoConsulta.getText().isEmpty()
                    || txtModificarDiagnosticoConsulta.getText().isEmpty()
                    || txtModificarObservacionesConsulta.getText().isEmpty()
                    || txtModificarCodigoMascotaConsulta.getText().isEmpty()
                    || txtModificarCodigoVeterinarioConsulta.getText().isEmpty()) {
                mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
                return;
            }
            int codConsulta;
            try {
                codConsulta = Integer.parseInt(txtModificarCodigoConsulta.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("El codigo consulta tiene que ser un numero entero");
                return;
            }

            int codMascota;
            try {
                codMascota = Integer.parseInt(txtModificarCodigoMascotaConsulta.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("El codigo mascota tiene que ser un numero entero");
                return;
            }

            int codVeterinario;
            try {
                codVeterinario = Integer.parseInt(txtModificarCodigoVeterinarioConsulta.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("El codigo veterinario tiene que ser un numero entero");
                return;
            }

            String sql = "CALL sp_EditarConsulta(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, Integer.parseInt(txtModificarCodigoConsulta.getText()));
            stmt.setDate(2, java.sql.Date.valueOf(dpModificarFechaConsulta.getValue()));
            stmt.setString(3, txtModificarMotivoConsulta.getText());
            stmt.setString(4, txtModificarDiagnosticoConsulta.getText());
            stmt.setString(5, txtModificarObservacionesConsulta.getText());
            stmt.setString(6, txtModificarCodigoMascotaConsulta.getText());
            stmt.setString(7, txtModificarCodigoVeterinarioConsulta.getText());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                mostrarAlerta("Consulta modificada correctamente.");
            } else {
                mostrarAlerta("No se pudo modificar la Consulta.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("Uno de los dos codigos no existe.");
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().toLowerCase().contains("fk")) {
                mostrarAlerta("Error: Uno de los dos codigos esta mal no existe.");
            } else {
                mostrarAlerta("Error al modificar la consulta: " + e.getMessage());
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al modificar la consulta: " + e.getMessage());
        }

        txtModificarCodigoConsulta.clear();
        dpModificarFechaConsulta.setValue(null);
        txtModificarMotivoConsulta.clear();
        txtModificarDiagnosticoConsulta.clear();
        txtModificarObservacionesConsulta.clear();
        txtModificarCodigoMascotaConsulta.clear();
        txtModificarCodigoVeterinarioConsulta.clear();
    }

    @FXML
    private void onEliminarConsulta(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            int id = Integer.parseInt(txtEliminarConsulta.getText());

            PreparedStatement stmt = conn.prepareStatement("CALL sp_EliminarConsulta(?);");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int contador = rs.getInt("filasEliminadas");

                if (contador > 0) {
                    mostrarAlerta("Consulta eliminada correctamente.");
                } else {
                    mostrarAlerta("No se pudo eliminar la Consulta.");
                }
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id de la consulta a Eliminar.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtEliminarConsulta.clear();

    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void irMenuConsultaAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuConsultaAdminView.fxml"));
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
    private void irAgregarConsulta(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/AgregarConsultaView.fxml"));
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
    private void irListarConsulta(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ListarConsultasView.fxml"));
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
    private void irBuscarConsulta(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/BuscarConsultaView.fxml"));
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
    private void irModificarConsulta(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ModificarConsultaView.fxml"));
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
    private void irEliminarConsulta(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/EliminarConsultaView.fxml"));
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
