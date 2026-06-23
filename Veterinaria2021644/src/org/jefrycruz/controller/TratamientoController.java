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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jefrycruz.db.Conexion;
import org.jefrycruz.models.Tratamientos;

public class TratamientoController {

    // La tabla mas las columnas de solo el listar
    @FXML
    private TableView<Tratamientos> tblTratamiento;
    @FXML
    private TableColumn<Tratamientos, Integer> colcodigoTratamiento;
    @FXML
    private TableColumn<Tratamientos, String> coldescripcion;
    @FXML
    private TableColumn<Tratamientos, Date> colfechaInicio;
    @FXML
    private TableColumn<Tratamientos, Date> colfechaFin;
    @FXML
    private TableColumn<Tratamientos, String> colmedicamentosIndicados;
    @FXML
    private TableColumn<Tratamientos, Integer> colcodigoConsulta;
    // La tabla mas la coluimnas del buscar
    @FXML
    private TextField txtBuscarTratamiento;
    @FXML
    private TableView<Tratamientos> tblTratamientoBuscar;

    @FXML
    private TableColumn<Tratamientos, Integer> colcodigoTratamientoBuscar;
    @FXML
    private TableColumn<Tratamientos, String> coldescripcionBuscar;
    @FXML
    private TableColumn<Tratamientos, Date> colfechaInicioBuscar;
    @FXML
    private TableColumn<Tratamientos, Date> colfechaFinBuscar;
    @FXML
    private TableColumn<Tratamientos, String> colmedicamentosIndicadosBuscar;
    @FXML
    private TableColumn<Tratamientos, Integer> colcodigoConsultaBuscar;
    // Para el eliminar
    @FXML
    private TextField txtEliminarTratamiento;

    // Para hacer el AgregarClienteView
    @FXML
    private TextField txtIngresarDescripcion;
    @FXML
    private DatePicker dpIngresarFechaInicio;
    @FXML
    private DatePicker dpIngresarFechaFin;
    @FXML
    private TextField txtIngresarMedicamentosIndicados;
    @FXML
    private TextField txtIngresarCodigoConsulta;

    // para hacer el modificar
    @FXML
    private TextField txtModificarCodigoTratamiento;
    @FXML
    private TextField txtModificarDescripcion;
    @FXML
    private DatePicker dpModificarFechaInicio;
    @FXML
    private DatePicker dpModificarFechaFin;
    @FXML
    private TextField txtModificarMedicamentosIndicados;
    @FXML
    private TextField txtModificarCodigoConsulta;

    private ObservableList<Tratamientos> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (tblTratamiento != null) {
            inicializarTablaListar();
            cargarDatos();
        }

        if (tblTratamientoBuscar != null) {
            inicializarTablaBuscar();
        }
    }

    private void inicializarTablaListar() {
        colcodigoTratamiento.setCellValueFactory(new PropertyValueFactory<>("codigoTratamiento"));
        coldescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colfechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colfechaFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colmedicamentosIndicados.setCellValueFactory(new PropertyValueFactory<>("medicamentosIndicados"));
        colcodigoConsulta.setCellValueFactory(new PropertyValueFactory<>("codigoConsulta"));
    }

    private void inicializarTablaBuscar() {
        colcodigoTratamientoBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoTratamiento"));
        coldescripcionBuscar.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colfechaInicioBuscar.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colfechaFinBuscar.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colmedicamentosIndicadosBuscar.setCellValueFactory(new PropertyValueFactory<>("medicamentosIndicados"));
        colcodigoConsultaBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoConsulta"));
    }

    private void cargarDatos() {

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            lista.clear();
            String sql = "CALL sp_ListarTratamientos();";

            CallableStatement cstmt = conn.prepareCall(sql);
            ResultSet rs = cstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Tratamientos(
                        rs.getInt("codigoTratamiento"),
                        rs.getString("descripcion"),
                        rs.getDate("fechaInicio"),
                        rs.getDate("fechaFin"),
                        rs.getString("medicamentosIndicados"),
                        rs.getInt("codigoConsulta")
                ));
            }
            tblTratamiento.setItems(lista);
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    @FXML
    private void onBuscarTratamiento(ActionEvent event) {
        ObservableList<Tratamientos> tratamientoBuscado = FXCollections.observableArrayList();

        if (txtBuscarTratamiento.getText().isEmpty()) {
            mostrarAlerta("Por favor ingrese un ID de tratamiento.");
            return;
        }

        int codTratamiento;

        try {
            codTratamiento = Integer.parseInt(txtBuscarTratamiento.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Por favor ingrese un número válido para el ID del tratamiento.");
            return;
        }

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareCall("CALL sp_BuscarTratamiento(?)");
            stmt.setInt(1, codTratamiento);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tratamientoBuscado.add(new Tratamientos(
                        rs.getInt("codigoTratamiento"),
                        rs.getString("descripcion"),
                        rs.getDate("fechaInicio"),
                        rs.getDate("fechaFin"),
                        rs.getString("medicamentosIndicados"),
                        rs.getInt("codigoConsulta")
                ));
            }

            if (tratamientoBuscado.isEmpty()) {
                mostrarAlerta("No se encontró ningún tratamiento.");
            }

            tblTratamientoBuscar.setItems(tratamientoBuscado);

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error al buscar el tratamiento.");
        }
    }

    @FXML
    private void onAgregarTratamiento(ActionEvent event) {
        // Verifica si algún campo está vacío
        if (txtIngresarDescripcion.getText().isEmpty()
                || dpIngresarFechaInicio.getValue() == null
                || dpIngresarFechaFin.getValue() == null
                || txtIngresarMedicamentosIndicados.getText().isEmpty()
                || txtIngresarCodigoConsulta.getText().isEmpty()) {
            mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
            return;
        }

        int codigoConsulta;

        try {
            // Verifica que el código de consulta sea un número
            codigoConsulta = Integer.parseInt(txtIngresarCodigoConsulta.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("El código de consulta debe ser un número válido.");
            return;
        }

        // Continuamos si todo está correcto
        try  {
            Connection conn = Conexion.getInstancia().getConexion();
            String sql = "CALL sp_AgregarTratamiento(?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, txtIngresarDescripcion.getText());
            stmt.setDate(2, java.sql.Date.valueOf(dpIngresarFechaInicio.getValue()));
            stmt.setDate(3, java.sql.Date.valueOf(dpIngresarFechaFin.getValue()));
            stmt.setString(4, txtIngresarMedicamentosIndicados.getText());
            stmt.setInt(5, codigoConsulta);  // Usamos el valor ya convertido

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarAlerta("Tratamiento agregado correctamente.");
                // Limpiamos solo si fue exitoso
                txtIngresarDescripcion.clear();
                dpIngresarFechaInicio.setValue(null);
                dpIngresarFechaFin.setValue(null);
                txtIngresarMedicamentosIndicados.clear();
                txtIngresarCodigoConsulta.clear();
            } else {
                mostrarAlerta("No se pudo agregar el Tratamiento.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("El código de consulta no existe.");
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().toLowerCase().contains("fk")) {
                mostrarAlerta("Error: El código de consulta es inválido.");
            } else {
                mostrarAlerta("Error al agregar el tratamiento: " + e.getMessage());
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error inesperado al agregar el tratamiento: " + e.getMessage());
        }
    }

    @FXML
    private void onModificarTratamiento(ActionEvent event) {
        if (txtModificarCodigoTratamiento.getText().isEmpty()
                || txtModificarDescripcion.getText().isEmpty()
                || dpModificarFechaInicio.getValue() == null
                || dpModificarFechaFin.getValue() == null
                || txtModificarMedicamentosIndicados.getText().isEmpty()
                || txtModificarCodigoConsulta.getText().isEmpty()) {
            mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
            return;
        }

        int codigoTratamiento;
        int codigoConsulta;

        try {
            codigoTratamiento = Integer.parseInt(txtModificarCodigoTratamiento.getText());
            codigoConsulta = Integer.parseInt(txtModificarCodigoConsulta.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Por favor, ingrese solo números en los campos de código.");
            return;
        }

        try  {
            Connection conn = Conexion.getInstancia().getConexion();
            String sql = "CALL sp_EditarTratamiento(?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, codigoTratamiento);
            stmt.setString(2, txtModificarDescripcion.getText());
            stmt.setDate(3, java.sql.Date.valueOf(dpModificarFechaInicio.getValue()));
            stmt.setDate(4, java.sql.Date.valueOf(dpModificarFechaFin.getValue()));
            stmt.setString(5, txtModificarMedicamentosIndicados.getText());
            stmt.setInt(6, codigoConsulta);

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                mostrarAlerta("Tratamiento modificado correctamente.");
            } else {
                mostrarAlerta("No se pudo modificar el Tratamiento.");
            }

            txtModificarCodigoTratamiento.clear();
            txtModificarDescripcion.clear();
            dpModificarFechaInicio.setValue(null);
            dpModificarFechaFin.setValue(null);
            txtModificarMedicamentosIndicados.clear();
            txtModificarCodigoConsulta.clear();

        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("Uno de los dos códigos no existe.");
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().toLowerCase().contains("fk")) {
                mostrarAlerta("Error: Uno de los códigos está mal o no existe.");
            } else {
                mostrarAlerta("Error al modificar el tratamiento: " + e.getMessage());
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error inesperado al modificar el tratamiento: " + e.getMessage());
        }
    }

    @FXML
    private void onEliminarTratamiento(ActionEvent event) {

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            int id = Integer.parseInt(txtEliminarTratamiento.getText());

            PreparedStatement stmt = conn.prepareStatement("CALL sp_EliminarTratamiento(?);");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int contador = rs.getInt("filasEliminadas");

                if (contador > 0) {
                    mostrarAlerta("Tratemiento eliminado correctamente.");
                } else {
                    mostrarAlerta("No se pudo eliminar el Tratmiento.");
                }
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id del Tratamiento a Eliminar.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtEliminarTratamiento.clear();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void irMenuTratamientoAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuTratamientoAdminView.fxml"));
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
    private void irMenuTratamiento(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuTratamientoView.fxml"));
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
    private void irAgregarTratamiento(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/AgregarTratamientoView.fxml"));
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
    private void irListarTratamientos(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ListarTratamientosView.fxml"));
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
    private void irModificarTratamiento(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ModificarTratamientoView.fxml"));
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
    private void irEliminarTratamiento(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/EliminarTratamientoView.fxml"));
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
    private void irMenu(ActionEvent event) throws IOException {
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
    private void irBuscarTratamiento(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/BuscarTratamientoView.fxml"));
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
