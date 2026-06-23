package org.jefrycruz.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.jefrycruz.db.Conexion;
import org.jefrycruz.models.Mascotas;
import org.jefrycruz.report.GenerarReporte;

/**
 *
 * @author informatica
 */
public class MascotaController {

    // La tabla mas las columnas de solo el listar
    @FXML
    private TableView<Mascotas> tblMascota;
    @FXML
    private TableColumn<Mascotas, Integer> colcodigoMascota, colcodigoCliente;
    @FXML
    private TableColumn<Mascotas, String> colnombreMascota;
    @FXML
    private TableColumn<Mascotas, String> colespecie;
    @FXML
    private TableColumn<Mascotas, String> colraza;
    @FXML
    private TableColumn<Mascotas, String> colsexo;
    @FXML
    private TableColumn<Mascotas, String> colcolor;
    @FXML
    private TableColumn<Mascotas, Date> colfechaNacimiento;
    @FXML
    private TableColumn<Mascotas, Double> colpesoActualKg;

    // La tabla mas la coluimnas del buscar
    @FXML
    private TextField txtBuscarMascota;
    @FXML
    private TableView<Mascotas> tblMascotaBuscar;
    @FXML
    private TableColumn<Mascotas, Integer> colcodigoMascotaBuscar, colcodigoClienteBuscar;
    @FXML
    private TableColumn<Mascotas, String> colnombreMascotaBuscar;
    @FXML
    private TableColumn<Mascotas, String> colespecieBuscar;
    @FXML
    private TableColumn<Mascotas, String> colrazaBuscar;
    @FXML
    private TableColumn<Mascotas, String> colsexoBuscar;
    @FXML
    private TableColumn<Mascotas, String> colcolorBuscar;
    @FXML
    private TableColumn<Mascotas, Date> colfechaNacimientoBuscar;
    @FXML
    private TableColumn<Mascotas, Double> colpesoActualKgBuscar;
    // Para el eliminar
    @FXML
    private TextField txtEliminarMascota;

    // Para hacer el AgregarClienteView
    @FXML
    private TextField txtIngresarNombreMascota;
    @FXML
    private TextField txtIngresarEspecieMascota;
    @FXML
    private TextField txtIngresarRazaMascota;
    @FXML
    private DatePicker dpIngresarNacimientoMascota;
    @FXML
    private TextField txtIngresarColorMascota;
    @FXML
    private TextField txtIngresarPesoMascota;
    @FXML
    private TextField txtIngresarCodigoCliente;
    @FXML
    private ComboBox<String> cmbSexoMascota;

    // para hacer el modificar
    @FXML
    private TextField txtModificarCodigoMascota;
    @FXML
    private TextField txtModificarNombreMascota;
    @FXML
    private TextField txtModificarEspecieMascota;
    @FXML
    private TextField txtModificarRazaMascota;
    @FXML
    private DatePicker dpModificarNacimientoMascota;
    @FXML
    private TextField txtModificarColorMascota;
    @FXML
    private TextField txtModificarPesoMascota;
    @FXML
    private TextField txtModificarCodigoCliente;

    private ObservableList<Mascotas> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (tblMascota != null) {
            inicializarTablaListar();
            cargarDatos();
        }

        if (tblMascotaBuscar != null) {
            inicializarTablaBuscar();
        }
        cargarEnumMascota();
    }

    private void inicializarTablaListar() {
        colcodigoMascota.setCellValueFactory(new PropertyValueFactory<>("codigoMascota"));
        colnombreMascota.setCellValueFactory(new PropertyValueFactory<>("nombreMascota"));
        colespecie.setCellValueFactory(new PropertyValueFactory<>("especie"));
        colraza.setCellValueFactory(new PropertyValueFactory<>("raza"));
        colsexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        colfechaNacimiento.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colcolor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colpesoActualKg.setCellValueFactory(new PropertyValueFactory<>("pesoActualKg"));
        colcodigoCliente.setCellValueFactory(new PropertyValueFactory<>("codigoCliente"));

    }

    private void inicializarTablaBuscar() {
        colcodigoMascotaBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoMascota"));
        colnombreMascotaBuscar.setCellValueFactory(new PropertyValueFactory<>("nombreMascota"));
        colespecieBuscar.setCellValueFactory(new PropertyValueFactory<>("especie"));
        colrazaBuscar.setCellValueFactory(new PropertyValueFactory<>("raza"));
        colsexoBuscar.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        colfechaNacimientoBuscar.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colcolorBuscar.setCellValueFactory(new PropertyValueFactory<>("color"));
        colpesoActualKgBuscar.setCellValueFactory(new PropertyValueFactory<>("pesoActualKg"));
        colcodigoClienteBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoCliente"));
    }

    private void cargarEnumMascota() {
        ObservableList<String> enumCita = FXCollections.observableArrayList(
                "macho", "hembra");
        if (cmbSexoMascota != null) {
            cmbSexoMascota.setItems(enumCita);
        }
    }

    private void cargarDatos() {

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            lista.clear();
            String sql = "CALL sp_ListarMascotas();";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Mascotas(
                        rs.getInt("codigoMascota"),
                        rs.getString("nombreMascota"),
                        rs.getString("especie"),
                        rs.getString("raza"),
                        Mascotas.Sexo.valueOf(rs.getString("sexo")),
                        rs.getDate("fechaNacimiento"),
                        rs.getString("color"),
                        rs.getDouble("pesoActualKg"),
                        rs.getInt("codigoCliente")
                ));
            }
            tblMascota.setItems(lista);
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    @FXML
    private void onBuscarMascota(ActionEvent event) {
        ObservableList<Mascotas> mascotaBuscada = FXCollections.observableArrayList();

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareCall("CALL sp_BuscarMascota(?);");
            stmt.setInt(1, Integer.parseInt(txtBuscarMascota.getText()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                mascotaBuscada.add(new Mascotas(
                        rs.getInt("codigoMascota"),
                        rs.getString("nombreMascota"),
                        rs.getString("especie"),
                        rs.getString("raza"),
                        Mascotas.Sexo.valueOf(rs.getString("sexo")),
                        rs.getDate("fechaNacimiento"),
                        rs.getString("color"),
                        rs.getDouble("pesoActualKg"),
                        rs.getInt("codigoCliente")
                ));
            }
            if (mascotaBuscada.isEmpty()) {
                mostrarAlerta("No se encontro ninguna mascota.");
            }
            tblMascotaBuscar.setItems(mascotaBuscada);
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id de la mascota a bucar.");
        } catch (SQLException e) {
            mostrarAlerta("Error al buscar mascota: " + e.getMessage());
            e.printStackTrace();

        }
        txtBuscarMascota.clear();
    }

    @FXML
    private void onAgregarMascota(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            // comprovacion si esta vacio 
            if (txtIngresarNombreMascota.getText().isEmpty()
                    || txtIngresarEspecieMascota.getText().isEmpty()
                    || txtIngresarRazaMascota.getText().isEmpty()
                    || cmbSexoMascota.getValue() == null
                    || dpIngresarNacimientoMascota.getValue() == null
                    || txtIngresarColorMascota.getText().isEmpty()
                    || txtIngresarPesoMascota.getText().isEmpty()
                    || txtIngresarCodigoCliente.getText().isEmpty()) {
                mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
                return;
            }

            // mensaje si el aparetado de peso esta mal
            double peso;
            try {
                peso = Double.parseDouble(txtIngresarPesoMascota.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("El peso debe ser un numero valido tal como 5.1.");
                return;
            }

            // validacion del codigo cliente si esta bien ingresado
            int codigoCliente;
            try {
                codigoCliente = Integer.parseInt(txtIngresarCodigoCliente.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("El codigo del cliente debe ser un numero.");
                return;
            }

            String sql = "CALL sp_AgregarMascota(?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtIngresarNombreMascota.getText());
            stmt.setString(2, txtIngresarEspecieMascota.getText());
            stmt.setString(3, txtIngresarRazaMascota.getText());
            stmt.setString(4, cmbSexoMascota.getValue());
            stmt.setDate(5, java.sql.Date.valueOf(dpIngresarNacimientoMascota.getValue()));
            stmt.setString(6, txtIngresarColorMascota.getText());
            stmt.setDouble(7, peso); // aquí usamos el valor validado
            stmt.setInt(8, codigoCliente); // aquí también usamos el valor validado

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarAlerta("Mascota agregada correctamente.");
            } else {
                mostrarAlerta("No se pudo agregar la Mascota.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("Uno de los códigos no existe.");
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().toLowerCase().contains("fk")) {
                mostrarAlerta("Error: Uno de los códigos está mal o no existe.");
            } else {
                mostrarAlerta("Error al modificar la mascota: " + e.getMessage());
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al modificar la mascota: " + e.getMessage());
        }

        txtIngresarNombreMascota.clear();
        txtIngresarEspecieMascota.clear();
        txtIngresarRazaMascota.clear();
        cmbSexoMascota.setValue(null);
        dpIngresarNacimientoMascota.setValue(null);
        txtIngresarColorMascota.clear();
        txtIngresarPesoMascota.clear();
        txtIngresarCodigoCliente.clear();
    }

    @FXML
    private void onModificarMascota(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            if (txtModificarCodigoMascota.getText().isEmpty()
                    || txtModificarNombreMascota.getText().isEmpty()
                    || txtModificarEspecieMascota.getText().isEmpty()
                    || txtModificarRazaMascota.getText().isEmpty()
                    || cmbSexoMascota.getValue() == null
                    || dpModificarNacimientoMascota.getValue() == null
                    || txtModificarColorMascota.getText().isEmpty()
                    || txtModificarPesoMascota.getText().isEmpty()
                    || txtModificarCodigoCliente.getText().isEmpty()) {
                mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
                return;
            }

            int codigoMascota;
            try {
                codigoMascota = Integer.parseInt(txtModificarCodigoMascota.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("El codigo de la mascota debe ser un numero entero.");
                return;
            }

            // Validar código cliente
            int codigoCliente;
            try {
                codigoCliente = Integer.parseInt(txtModificarCodigoCliente.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("El codigo del cliente debe ser un numero entero.");
                return;
            }

            // Validar peso
            double peso;
            try {
                peso = Double.parseDouble(txtModificarPesoMascota.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("El peso debe ser un numero valido ejemplo: 5.1.");
                return;
            }

            String sql = "CALL sp_EditarMascota(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, codigoMascota);
            stmt.setString(2, txtModificarNombreMascota.getText());
            stmt.setString(3, txtModificarEspecieMascota.getText());
            stmt.setString(4, txtModificarRazaMascota.getText());
            stmt.setString(5, cmbSexoMascota.getValue());
            stmt.setDate(6, java.sql.Date.valueOf(dpModificarNacimientoMascota.getValue()));
            stmt.setString(7, txtModificarColorMascota.getText());
            stmt.setDouble(8, peso);
            stmt.setInt(9, codigoCliente);

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                mostrarAlerta("Mascota modificada correctamente.");
            } else {
                mostrarAlerta("No se pudo modificar la Mascota.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("Uno de los códigos no existe.");
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().toLowerCase().contains("fk")) {
                mostrarAlerta("Error: Uno de los códigos está mal o no existe.");
            } else {
                mostrarAlerta("Error al modificar la mascota: " + e.getMessage());
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al modificar la mascota: " + e.getMessage());
        }

        txtModificarCodigoMascota.clear();
        txtModificarNombreMascota.clear();
        txtModificarEspecieMascota.clear();
        txtModificarRazaMascota.clear();
        cmbSexoMascota.setValue(null);
        dpModificarNacimientoMascota.setValue(null);
        txtModificarColorMascota.clear();
        txtModificarPesoMascota.clear();
        txtModificarCodigoCliente.clear();
    }

    @FXML
    private void onEliminarMascota(ActionEvent event) {
        try {
            int id = Integer.parseInt(txtEliminarMascota.getText());
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement("CALL sp_EliminarMascota(?);");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int contador = rs.getInt("filasEliminadas");

                if (contador > 0) {
                    mostrarAlerta("Mascota eliminada correctamente.");
                } else {
                    mostrarAlerta("No se pudo eliminar la Mascota.");
                }
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id de la mascota a Eliminar.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtEliminarMascota.clear();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void irMenuMascotaAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuMascotaAdminView.fxml"));
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
    private void irMenuMascota(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuMascotaView.fxml"));
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
    private void irAgregarMascota(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/AgregarMascotaView.fxml"));
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
    private void irBuscarMascota(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/BuscarMascotaView.fxml"));
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
    private void irModificarMascota(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ModificarMascotaView.fxml"));
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
    private void irListarMascotas(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ListarMascotasView.fxml"));
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
    private void irEliminarMascota(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/EliminarMascotaView.fxml"));
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
    public void generarReporte() {
        JOptionPane.showMessageDialog(null, "El reporte está listo.");
        imprimirReceta();
    }

    public void imprimirReceta() {
        Map<String, Object> parametros = new HashMap<>();
        GenerarReporte.mostrarReporte("/org/jefrycruz/report/RecetaMascota.jasper","Receta de la Mascota",parametros);
    }
}
