package org.jefrycruz.controller;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
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
import org.jefrycruz.models.Citas;

/**
 *
 * @author informatica
 */
public class CitaController {

    // La tabla mas las columnas de solo el listar
    @FXML
    private TableView<Citas> tblCitas;
    @FXML
    private TableColumn<Citas, Integer> colcodigoCita;
    @FXML
    private TableColumn<Citas, Date> colfechaCita;
    @FXML
    private TableColumn<Citas, LocalTime> colhoraCita;
    @FXML
    private TableColumn<Citas, String> colmotivo;
    @FXML
    private TableColumn<Citas, Integer> colcodigoMascota;
    @FXML
    private TableColumn<Citas, Integer> colcodigoVeterinario;
    @FXML
    private TableColumn<Citas, String> colestado;

    // La tabla mas la coluimnas del buscar
    @FXML
    private TextField txtBuscarCita;
    @FXML
    private TableView<Citas> tblCitasBuscar;
    @FXML
    private TableColumn<Citas, Integer> colcodigoCitaBuscar;
    @FXML
    private TableColumn<Citas, Date> colfechaCitaBuscar;
    @FXML
    private TableColumn<Citas, LocalTime> colhoraCitaBuscar;
    @FXML
    private TableColumn<Citas, String> colmotivoBuscar;
    @FXML
    private TableColumn<Citas, Integer> colcodigoMascotaBuscar;
    @FXML
    private TableColumn<Citas, Integer> colcodigoVeterinarioBuscar;
    @FXML
    private TableColumn<Citas, String> colestadoBuscar;
    // Para el eliminar
    @FXML
    private TextField txtEliminarCita;

    // Para hacer el AgregarClienteView
    @FXML
    private DatePicker dpIngresarFechaCita;
    @FXML
    private TextField txtIngresarHoraCita;
    @FXML
    private TextField txtIngresarMotivo;
    @FXML
    private TextField txtIngresarCodigoMascota;
    @FXML
    private TextField txtIngresarCodigoVeterinario;
    @FXML
    private TextField txtIngresarEstado;
    @FXML
    private ComboBox<String> cmbEstadoCita;

    // para hacer el modificar
    @FXML
    private TextField txtModificarCodigoCita;
    @FXML
    private DatePicker dpModificarFechaCita;
    @FXML
    private TextField txtModificarHoraCita;
    @FXML
    private TextField txtModificarMotivo;
    @FXML
    private TextField txtModificarCodigoMascota;
    @FXML
    private TextField txtModificarCodigoVeterinario;
    @FXML
    private TextField txtModificarEstado;

    private ObservableList<Citas> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (tblCitas != null) {
            inicializarTablaListar();
            cargarDatos();
        }

        if (tblCitasBuscar != null) {
            inicializarTablaBuscar();
        }

        cargarEnumCita();
    }

    private void inicializarTablaListar() {
        colcodigoCita.setCellValueFactory(new PropertyValueFactory<>("codigoCita"));
        colfechaCita.setCellValueFactory(new PropertyValueFactory<>("fechaCita"));
        colhoraCita.setCellValueFactory(new PropertyValueFactory<>("horaCita"));
        colmotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colcodigoMascota.setCellValueFactory(new PropertyValueFactory<>("codigoMascota"));
        colcodigoVeterinario.setCellValueFactory(new PropertyValueFactory<>("codigoVeterinario"));
        colestado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void inicializarTablaBuscar() {
        colcodigoCitaBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoCita"));
        colfechaCitaBuscar.setCellValueFactory(new PropertyValueFactory<>("fechaCita"));
        colhoraCitaBuscar.setCellValueFactory(new PropertyValueFactory<>("horaCita"));
        colmotivoBuscar.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colcodigoMascotaBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoMascota"));
        colcodigoVeterinarioBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoVeterinario"));
        colestadoBuscar.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void cargarEnumCita() {
        ObservableList<String> enumCita = FXCollections.observableArrayList(
                "Pendiente", "Completa", "Cancelada");
        if (cmbEstadoCita != null) {
            cmbEstadoCita.setItems(enumCita);
        }
    }

    private void cargarDatos() {

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            lista.clear();
            String sql = "CALL sp_ListarCitas();";
            CallableStatement stmt = conn.prepareCall(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Citas(
                        rs.getInt("codigoCita"),
                        rs.getDate("fechaCita"),
                        rs.getTime("horaCita").toLocalTime(),
                        rs.getString("motivo"),
                        rs.getInt("codigoMascota"),
                        rs.getInt("codigoVeterinario"),
                        Citas.Estado.valueOf(rs.getString("estado"))
                ));
            }
            tblCitas.setItems(lista);
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    @FXML
    private void onBuscarCita(ActionEvent event) {
        ObservableList<Citas> citaBuscada = FXCollections.observableArrayList();

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareCall("CALL sp_BuscarCita(?);");
            stmt.setInt(1, Integer.parseInt(txtBuscarCita.getText()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                citaBuscada.add(new Citas(
                        rs.getInt("codigoCita"),
                        rs.getDate("fechaCita"),
                        rs.getTime("horaCita").toLocalTime(),
                        rs.getString("motivo"),
                        rs.getInt("codigoMascota"),
                        rs.getInt("codigoVeterinario"),
                        Citas.Estado.valueOf(rs.getString("estado"))
                ));
            }
            if (citaBuscada.isEmpty()) {
                mostrarAlerta("No se encontro ninguna cita.");
            }
            tblCitasBuscar.setItems(citaBuscada);
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id de la cita a bucar.");
        } catch (SQLException e) {
            mostrarAlerta("Error al buscar cita: " + e.getMessage());
            e.printStackTrace();

        }
        txtBuscarCita.clear();
    }

    @FXML
    private void onAgregarCita(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();

            if (dpIngresarFechaCita == null
                    || txtIngresarHoraCita.getText().isEmpty()
                    || txtIngresarMotivo.getText().isEmpty()
                    || txtIngresarCodigoMascota.getText().isEmpty()
                    || txtIngresarCodigoVeterinario.getText().isEmpty()
                    || cmbEstadoCita == null) {
                mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
                return;
            }

            int codMascota;
            int codVeterinario;
            try {
                codMascota = Integer.parseInt(txtIngresarCodigoMascota.getText());
                codVeterinario = Integer.parseInt(txtIngresarCodigoVeterinario.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("El codigo tiene que ser un numero entero");
                return;
            }

            try {
                LocalTime.parse(txtIngresarHoraCita.getText());
            } catch (DateTimeParseException e) {
                mostrarAlerta("La hora debe estar en formato hh:mm:ss --> 05:01");
                return;
            }

            // Si pasa todas las validaciones, ejecuta el procedimiento
            String sql = "CALL sp_AgregarCita(?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDate(1, java.sql.Date.valueOf(dpIngresarFechaCita.getValue()));
            stmt.setString(2, txtIngresarHoraCita.getText());
            stmt.setString(3, txtIngresarMotivo.getText());
            stmt.setString(4, txtIngresarCodigoMascota.getText());
            stmt.setString(5, txtIngresarCodigoVeterinario.getText());
            stmt.setString(6, cmbEstadoCita.getValue());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarAlerta("Cita agregada correctamente.");
            } else {
                mostrarAlerta("No se pudo agregar la Cita.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("Uno de los dos codigos no existe.");
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().toLowerCase().contains("fk")) {
                mostrarAlerta("Error: Uno de los codigos ingresados no existe.");
            } else {
                mostrarAlerta("Error al agregar la Cita: " + e.getMessage());
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al agregar la Cita: " + e.getMessage());
        }

        dpIngresarFechaCita.setValue(null);
        txtIngresarHoraCita.clear();
        txtIngresarMotivo.clear();
        txtIngresarCodigoMascota.clear();
        txtIngresarCodigoVeterinario.clear();
        cmbEstadoCita.setValue(null);
    }

    @FXML
    private void onModificarCita(ActionEvent event) {

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            if (txtModificarCodigoCita.getText().isEmpty()
                    || dpModificarFechaCita == null
                    || txtModificarHoraCita.getText().isEmpty()
                    || txtModificarMotivo.getText().isEmpty()
                    || txtModificarCodigoMascota.getText().isEmpty()
                    || txtModificarCodigoVeterinario.getText().isEmpty()
                    || cmbEstadoCita == null) {
                mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
                return;
            }

            int codCita;
            int codMascota;
            int codVeterinario;
            try {
                codCita = Integer.parseInt(txtModificarCodigoCita.getText());
                codMascota = Integer.parseInt(txtModificarCodigoMascota.getText());
                codVeterinario = Integer.parseInt(txtModificarCodigoVeterinario.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("El codigo tiene que ser un numero entero");
                return;
            }

            try {
                LocalTime.parse(txtModificarHoraCita.getText());
            } catch (DateTimeParseException e) {
                mostrarAlerta("La hora debe estar en formato hh:mm:ss --> 05:01");
                return;
            }

            String sql = "CALL sp_EditarCita(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, Integer.parseInt(txtModificarCodigoCita.getText()));
            stmt.setDate(2, java.sql.Date.valueOf(dpModificarFechaCita.getValue()));
            stmt.setString(3, txtModificarHoraCita.getText());
            stmt.setString(4, txtModificarMotivo.getText());
            stmt.setString(5, txtModificarCodigoMascota.getText());
            stmt.setString(6, txtModificarCodigoVeterinario.getText());
            stmt.setString(7, cmbEstadoCita.getValue());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                mostrarAlerta("Cita modificada correctamente.");
            } else {
                mostrarAlerta("No se pudo modificar la cita.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("Uno de los dos codigos no existe.");
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().toLowerCase().contains("fk")) {
                mostrarAlerta("Error: Uno de los dos codigos esta mal no existe.");
            } else {
                mostrarAlerta("Error al modificar la Cita: " + e.getMessage());
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al modificar la Cita: " + e.getMessage());
        }

        txtModificarCodigoCita.clear();
        dpModificarFechaCita.setValue(null);
        txtModificarHoraCita.clear();
        txtModificarMotivo.clear();
        txtModificarCodigoMascota.clear();
        txtModificarCodigoVeterinario.clear();
        cmbEstadoCita.setValue(null);
    }

    @FXML
    private void onEliminarCita(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            int id = Integer.parseInt(txtEliminarCita.getText());

            PreparedStatement stmt = conn.prepareStatement("CALL sp_EliminarCita(?);");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int contador = rs.getInt("filasEliminadas");

                if (contador > 0) {
                    mostrarAlerta("Cita eliminada correctamente.");
                } else {
                    mostrarAlerta("No se pudo eliminar la Cita.");
                }
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id de la cita a Eliminar.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtEliminarCita.clear();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void irMenuCita(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuCitaView.fxml"));
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
    private void irMenuCitaAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuCitaAdminView.fxml"));
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
    private void irMenuPrincipal(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuView.fxml"));
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
    private void irAgregarCita(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/AgregarCitaView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Vista Cliente JCruz-2021644");
        stage.show();
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));

        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irModificarCita(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ModificarCitaView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Vista Cliente JCruz-2021644");
        stage.show();
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));

        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irBuscarCita(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/BuscarCitaView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Vista Cliente JCruz-2021644");
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
        stage.setTitle("Veterinaria JCruz-2021644");
        stage.show();
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));

        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irListarCita(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ListarCitasView.fxml"));
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
    private void irEliminarCita(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/EliminarCitaView.fxml"));
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
