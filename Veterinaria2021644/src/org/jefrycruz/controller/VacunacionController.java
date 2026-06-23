package org.jefrycruz.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
import javax.swing.JOptionPane;
import org.jefrycruz.db.Conexion;
import org.jefrycruz.models.Vacunaciones;
import org.jefrycruz.report.GenerarReporte;

/**
 *
 * @author PC
 */
public class VacunacionController {

    // La tabla mas las columnas de solo el listar
    @FXML
    private TableView<Vacunaciones> tblVacunacion;
    @FXML
    private TableColumn<Vacunaciones, Integer> colcodigoVacunacion;
    @FXML
    private TableColumn<Vacunaciones, Date> colfechaAplicacion;
    @FXML
    private TableColumn<Vacunaciones, String> colobservaciones;
    @FXML
    private TableColumn<Vacunaciones, Integer> colcodigoMascota;
    @FXML
    private TableColumn<Vacunaciones, Integer> colcodigoVacuna;
    @FXML
    private TableColumn<Vacunaciones, Integer> colcodigoVeterinario;
    // tabla buscar
    @FXML
    private TextField txtBuscarVacunacion;
    @FXML
    private TableView<Vacunaciones> tblVacunacionBuscar;
    @FXML
    private TableColumn<Vacunaciones, Integer> colcodigoVacunacionBuscar;
    @FXML
    private TableColumn<Vacunaciones, Date> colfechaAplicacionBuscar;
    @FXML
    private TableColumn<Vacunaciones, String> colobservacionesBuscar;
    @FXML
    private TableColumn<Vacunaciones, Integer> colcodigoMascotaBuscar;
    @FXML
    private TableColumn<Vacunaciones, Integer> colcodigoVacunaBuscar;
    @FXML
    private TableColumn<Vacunaciones, Integer> colcodigoVeterinarioBuscar;

    // Para el eliminar
    @FXML
    private TextField txtEliminarVacunaciones;

    // Para hacer el AgregarClienteView
    @FXML
    private DatePicker dpIngresarFechaAplicacion;
    @FXML
    private TextField txtIngresarObservaciones;
    @FXML
    private TextField txtIngresarCodigoMascota;
    @FXML
    private TextField txtIngresarCodigoVacuna;
    @FXML
    private TextField txtIngresarCodigoVeterinario;

    // para hacer el modificar
    @FXML
    private TextField txtModificarCodigoVacunaciones;
    @FXML
    private DatePicker dpModificarFechaAplicacion;
    @FXML
    private TextField txtModificarObservaciones;
    @FXML
    private TextField txtModificarCodigoMascota;
    @FXML
    private TextField txtModificarCodigoVacuna;
    @FXML
    private TextField txtModificarCodigoVeterinario;

    private ObservableList<Vacunaciones> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (tblVacunacion != null) {
            inicializarTablaListar();
            cargarDatos();
        }

        if (tblVacunacionBuscar != null) {
            inicializarTablaBuscar();
        }
    }

    private void inicializarTablaListar() {
        colcodigoVacunacion.setCellValueFactory(new PropertyValueFactory<>("codigoVacunacion"));
        colfechaAplicacion.setCellValueFactory(new PropertyValueFactory<>("fechaAplicacion"));
        colobservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        colcodigoMascota.setCellValueFactory(new PropertyValueFactory<>("codigoMascota"));
        colcodigoVacuna.setCellValueFactory(new PropertyValueFactory<>("codigoVacuna"));
        colcodigoVeterinario.setCellValueFactory(new PropertyValueFactory<>("codigoVeterinario"));

    }

    private void inicializarTablaBuscar() {
        colcodigoVacunacionBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoVacunacion"));
        colfechaAplicacionBuscar.setCellValueFactory(new PropertyValueFactory<>("fechaAplicacion"));
        colobservacionesBuscar.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        colcodigoMascotaBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoMascota"));
       colcodigoVacunaBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoVacuna"));
        colcodigoVeterinarioBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoVeterinario"));
    }

    private void cargarDatos() {

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            lista.clear();
            String sql = "CALL sp_ListarVacunaciones();";
            CallableStatement cstmt = conn.prepareCall(sql);
            ResultSet rs = cstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Vacunaciones(
                        rs.getInt("codigoVacunacion"),
                        rs.getDate("fechaAplicacion"),
                        rs.getString("observaciones"),
                        rs.getInt("codigoMascota"),
                        rs.getInt("codigoVacuna"),
                        rs.getInt("codigoVeterinario")
                ));
            }
            tblVacunacion.setItems(lista);
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    @FXML
    private void onBuscarVacunacion(ActionEvent event) {
        ObservableList<Vacunaciones> vacunacionBuscada = FXCollections.observableArrayList();

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareCall("CALL sp_BuscarVacunacion(?);");
            stmt.setInt(1, Integer.parseInt(txtBuscarVacunacion.getText()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vacunacionBuscada.add(new Vacunaciones(
                        rs.getInt("codigoVacunacion"),
                        rs.getDate("fechaAplicacion"),
                        rs.getString("observaciones"),
                        rs.getInt("codigoMascota"),
                        rs.getInt("codigoVacuna"),
                        rs.getInt("codigoVeterinario")
                ));
            }
            if (vacunacionBuscada.isEmpty()) {
                mostrarAlerta("No se encontro ninguna vacunacion.");
            }
            tblVacunacionBuscar.setItems(vacunacionBuscada);
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id de la vacunacion a bucar.");
        } catch (SQLException e) {
            mostrarAlerta("Error al buscar vacunacion: " + e.getMessage());
            e.printStackTrace();
        }
        txtBuscarVacunacion.clear();
    }

    @FXML
    private void onAgregarVacunacion(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();

            // Validar que ningún campo esté vacío
            if (dpIngresarFechaAplicacion == null
                    || txtIngresarObservaciones.getText().isEmpty()
                    || txtIngresarCodigoMascota.getText().isEmpty()
                    || txtIngresarCodigoVacuna.getText().isEmpty()
                    || txtIngresarCodigoVeterinario.getText().isEmpty()) {
                mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
                return;
            }

            // Convertir a enteros con validación
            int codigoMascota;
            int codigoVacuna;
            int codigoVeterinario;

            try {
                codigoMascota = Integer.parseInt(txtIngresarCodigoMascota.getText());
                codigoVacuna = Integer.parseInt(txtIngresarCodigoVacuna.getText());
                codigoVeterinario = Integer.parseInt(txtIngresarCodigoVeterinario.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Los códigos deben ser números válidos.");
                return;
            }

            // Preparar la llamada al procedimiento
            String sql = "CALL sp_AgregarVacunacion(?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDate(1, java.sql.Date.valueOf(dpIngresarFechaAplicacion.getValue()));
            stmt.setString(2, txtIngresarObservaciones.getText());
            stmt.setInt(3, codigoMascota);
            stmt.setInt(4, codigoVacuna);
            stmt.setInt(5, codigoVeterinario);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarAlerta("Vacunación agregada correctamente.");
            } else {
                mostrarAlerta("No se pudo agregar la vacunación.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("Uno de los códigos ingresados no existe.");
        } catch (SQLException e) {
            if (e.getMessage().toLowerCase().contains("foreign key") || e.getMessage().toLowerCase().contains("fk")) {
                mostrarAlerta("Error: Uno de los códigos ingresados no existe.");
            } else {
                mostrarAlerta("Error al agregar la vacunación: " + e.getMessage());
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error inesperado: " + e.getMessage());
        }

        // Limpiar campos
        dpIngresarFechaAplicacion.setValue(null);
        txtIngresarObservaciones.clear();
        txtIngresarCodigoMascota.clear();
        txtIngresarCodigoVacuna.clear();
        txtIngresarCodigoVeterinario.clear();
    }

    @FXML
    private void onModificarVacunaciones(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();

            // Verifica si los campos están vacíos
            if (txtModificarCodigoVacunaciones.getText().isEmpty()
                    || dpModificarFechaAplicacion == null
                    || txtModificarObservaciones.getText().isEmpty()
                    || txtModificarCodigoMascota.getText().isEmpty()
                    || txtModificarCodigoVacuna.getText().isEmpty()
                    || txtModificarCodigoVeterinario.getText().isEmpty()) {
                mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
                return;
            }

            // Convertimos los códigos a número y validamos
            int codigoVacunacion;
            int codigoMascota;
            int codigoVacuna;
            int codigoVeterinario;

            try {
                codigoVacunacion = Integer.parseInt(txtModificarCodigoVacunaciones.getText());
                codigoMascota = Integer.parseInt(txtModificarCodigoMascota.getText());
                codigoVacuna = Integer.parseInt(txtModificarCodigoVacuna.getText());
                codigoVeterinario = Integer.parseInt(txtModificarCodigoVeterinario.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Los códigos deben ser números válidos.");
                return;
            }

            // Llamamos al procedimiento
            String sql = "CALL sp_EditarVacunacion(?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, codigoVacunacion);
            stmt.setDate(2, java.sql.Date.valueOf(dpModificarFechaAplicacion.getValue()));
            stmt.setString(3, txtModificarObservaciones.getText());
            stmt.setInt(4, codigoMascota);
            stmt.setInt(5, codigoVacuna);
            stmt.setInt(6, codigoVeterinario);

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                mostrarAlerta("Vacunación modificada correctamente.");
            } else {
                mostrarAlerta("No se pudo modificar la vacunación.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("Uno de los códigos no existe.");
        } catch (SQLException e) {
            if (e.getMessage().toLowerCase().contains("foreign key") || e.getMessage().toLowerCase().contains("fk")) {
                mostrarAlerta("Error: Uno de los códigos no existe.");
            } else {
                mostrarAlerta("Error al modificar la vacunación: " + e.getMessage());
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error inesperado: " + e.getMessage());
        }

        // Limpiar campos
        txtModificarCodigoVacunaciones.clear();
        dpModificarFechaAplicacion.setValue(null);
        txtModificarObservaciones.clear();
        txtModificarCodigoMascota.clear();
        txtModificarCodigoVacuna.clear();
        txtModificarCodigoVeterinario.clear();
    }

    @FXML
    private void onEliminarVacunacion(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            int id = Integer.parseInt(txtEliminarVacunaciones.getText());

            PreparedStatement stmt = conn.prepareStatement("CALL sp_EliminarVacunacion(?);");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int contador = rs.getInt("filasEliminadas");

                if (contador > 0) {
                    mostrarAlerta("Vacunacion eliminada correctamente.");
                } else {
                    mostrarAlerta("No se pudo eliminar la Vacunacion.");
                }
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id de la Vacunacion a Eliminar.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtEliminarVacunaciones.clear();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void irMenuVacunacion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuVacunacionesView.fxml"));
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
    private void irMenuVacunacionAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuVacunacionesAdminView.fxml"));
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
        stage.show();
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));

        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irBuscarVacunacion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/BuscarVacunacionView.fxml"));
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
    private void irAgregarVacunacion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/AgregarVacunacionView.fxml"));
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
    private void irListarVacunaciones(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ListarVacunacionesView.fxml"));
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
    private void irModificarVacunacion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ModificarVacunacionView.fxml"));
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
    private void irEliminarVacunacion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/EliminarVacunacionView.fxml"));
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
    private void irCarneVacunacion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/CarnedeVacunacionView.fxml"));
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
    private void generarReporte() {
        try {
            // Puedes mostrar un mensaje si quieres
            JOptionPane.showMessageDialog(null, "Generando reporte...");
            imprimirVacunacion();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void imprimirVacunacion() {
        Map<String, Object> parametros = new HashMap<>();
        try {
            InputStream hoja = getClass().getResourceAsStream("/org/jefrycruz/image/Hoja_membretada.jpg");
            if (hoja == null) {
                System.out.println("No se encontró la imagen de la hoja membretada en /org/jefrycruz/image/Hoja_membretada.jpg");
            } else {
                // Pasa el InputStream directamente, sin convertir a BufferedImage
                parametros.put("HojaMembretada", hoja);
            }

            GenerarReporte.mostrarReporte(
                    "/org/jefrycruz/report/CarneVacunacion.jasper",
                    "Vacunacion de la mascota",
                    parametros
            );

        } catch (Exception e) {
            System.out.println("Error al cargar la imagen o generar reporte: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
