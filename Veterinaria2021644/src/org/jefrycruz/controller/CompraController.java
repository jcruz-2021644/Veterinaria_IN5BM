package org.jefrycruz.controller;

import java.io.IOException;
import java.sql.*;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jefrycruz.db.Conexion;
import org.jefrycruz.models.Compras;

public class CompraController {

    @FXML
    private TableView<Compras> tblCompra;
    @FXML
    private TableColumn<Compras, Integer> colcodigoCompra;
    @FXML
    private TableColumn<Compras, Date> colfechaCompra;
    @FXML
    private TableColumn<Compras, Double> coltotal;
    @FXML
    private TableColumn<Compras, String> coldetalle;
    @FXML
    private TableColumn<Compras, Integer> colcodigoProveedor;

    @FXML
    private ComboBox<String> cbmBuscarCompra;
    @FXML
    private TableView<Compras> tblCompraBuscar;
    @FXML
    private TableColumn<Compras, Integer> colcodigoCompraBuscar;
    @FXML
    private TableColumn<Compras, Date> colfechaCompraBuscar;
    @FXML
    private TableColumn<Compras, Double> coltotalBuscar;
    @FXML
    private TableColumn<Compras, String> coldetalleBuscar;
    @FXML
    private TableColumn<Compras, Integer> colcodigoProveedorBuscar;

    @FXML
    private ComboBox<String> cmbEliminarCompra;

    // Agregar
    @FXML
    private DatePicker dpIngresarFechaCompra;
    @FXML
    private TextField txtIngresarTotal;
    @FXML
    private TextField txtIngresarDetalle;
    @FXML
    private ComboBox<String> cmbIngresarCodigoProveedor;

    // Modificar
    @FXML
    private ComboBox<String> cmbModificarCodigoCompra;
    @FXML
    private DatePicker dpModificarFechaCompra;
    @FXML
    private TextField txtModificarTotal;
    @FXML
    private TextField txtModificarDetalle;
    @FXML
    private ComboBox<String> cmbModificarCodigoProveedor;

    private ObservableList<Compras> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (tblCompra != null) {
            inicializarTablaListar();
            cargarDatos();
        }

        if (tblCompraBuscar != null) {
            inicializarTablaBuscar();
        }

        AgregarCodigoProveedorBox();
        AgregarCodigoCompraBox();
    }

    private void inicializarTablaListar() {
        colcodigoCompra.setCellValueFactory(new PropertyValueFactory<>("codigoCompra"));
        colfechaCompra.setCellValueFactory(new PropertyValueFactory<>("fechaCompra"));
        coltotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        coldetalle.setCellValueFactory(new PropertyValueFactory<>("detalle"));
        colcodigoProveedor.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
    }

    private void inicializarTablaBuscar() {
        colcodigoCompraBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoCompra"));
        colfechaCompraBuscar.setCellValueFactory(new PropertyValueFactory<>("fechaCompra"));
        coltotalBuscar.setCellValueFactory(new PropertyValueFactory<>("total"));
        coldetalleBuscar.setCellValueFactory(new PropertyValueFactory<>("detalle"));
        colcodigoProveedorBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
    }

    private void AgregarCodigoProveedorBox() {
        ObservableList<String> proveedores = FXCollections.observableArrayList();
        String sql = "select codigoProveedor from Proveedores";

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                proveedores.add(String.valueOf(rs.getInt("codigoProveedor")));
            }

            if (cmbModificarCodigoProveedor != null) {
                cmbModificarCodigoProveedor.setItems(proveedores);
            }

            if (cmbIngresarCodigoProveedor != null) {
                cmbIngresarCodigoProveedor.setItems(proveedores);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar proveedores: " + e.getMessage());
        }
    }

    private void AgregarCodigoCompraBox() {
        ObservableList<String> compras = FXCollections.observableArrayList();
        String sql = "select codigoCompra from Compras";

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                compras.add(String.valueOf(rs.getInt("codigoCompra")));
            }

            if (cmbEliminarCompra != null) {
                cmbEliminarCompra.setItems(compras);
            }
            if (cmbModificarCodigoCompra != null) {
                cmbModificarCodigoCompra.setItems(compras);
            }
            if (cbmBuscarCompra != null) {
                cbmBuscarCompra.setItems(compras);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar proveedores: " + e.getMessage());
        }
    }

    private void cargarDatos() {

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            lista.clear();
            String sql = "CALL sp_ListarCompras();";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Compras(
                        rs.getInt("codigoCompra"),
                        rs.getDate("fechaCompra"),
                        rs.getDouble("total"),
                        rs.getString("detalle"),
                        rs.getInt("codigoProveedor")
                ));
            }
            tblCompra.setItems(lista);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBuscarCompra(ActionEvent event) {
        ObservableList<Compras> compraBuscada = FXCollections.observableArrayList();

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareCall("CALL sp_BuscarCompra(?);");
            stmt.setInt(1, Integer.parseInt(cbmBuscarCompra.getValue()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                compraBuscada.add(new Compras(
                        rs.getInt("codigoCompra"),
                        rs.getDate("fechaCompra"),
                        rs.getDouble("total"),
                        rs.getString("detalle"),
                        rs.getInt("codigoProveedor")
                ));
            }
            if (compraBuscada.isEmpty()) {
                mostrarAlerta("No se encontro ninguna compra");
            }
            tblCompraBuscar.setItems(compraBuscada);
        } catch (NumberFormatException e) {
            mostrarAlerta("Por favor ingrese el id de la compra.");
        } catch (SQLException e) {
            mostrarAlerta("Error al buscar compra: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onAgregarCompra(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            if (dpIngresarFechaCompra.getValue() == null
                    || txtIngresarTotal.getText().isEmpty()
                    || txtIngresarDetalle.getText().isEmpty()
                    || cmbIngresarCodigoProveedor.getValue() == null) {
                mostrarAlerta("Por favor, complete todos los campos.");
                return;
            }

            String sql = "CALL sp_AgregarCompra(?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDate(1, java.sql.Date.valueOf(dpIngresarFechaCompra.getValue()));
            stmt.setString(2, txtIngresarTotal.getText());
            stmt.setString(3, txtIngresarDetalle.getText());
            stmt.setString(4, cmbIngresarCodigoProveedor.getValue());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                mostrarAlerta("Compra agregada correctamente.");
            } else {
                mostrarAlerta("No se pudo agregar la compra.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("El codigo del proveedor no existe.");
        } catch (SQLException e) {
            mostrarAlerta("Error SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            mostrarAlerta("Error general: " + e.getMessage());
            e.printStackTrace();
        }

        dpIngresarFechaCompra.setValue(null);
        txtIngresarTotal.clear();
        txtIngresarDetalle.clear();
        cmbIngresarCodigoProveedor.setValue(null);
    }

    @FXML
    private void onModificarCompra(ActionEvent event) {
        try{
            Connection conn = Conexion.getInstancia().getConexion();
            if (cmbModificarCodigoCompra.getValue() == null
                    || dpModificarFechaCompra.getValue() == null
                    || txtModificarTotal.getText().isEmpty()
                    || txtModificarDetalle.getText().isEmpty()
                    || cmbModificarCodigoProveedor.getValue() == null) {
                mostrarAlerta("Por favor, complete todos los campos.");
                return;
            }

            String sql = "CALL sp_EditarCompra(?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, cmbModificarCodigoCompra.getValue());
            stmt.setDate(2, java.sql.Date.valueOf(dpModificarFechaCompra.getValue()));
            stmt.setString(3, txtModificarTotal.getText());
            stmt.setString(4, txtModificarDetalle.getText());
            stmt.setString(5, cmbModificarCodigoProveedor.getValue());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarAlerta("Compra modificada correctamente.");
            } else {
                mostrarAlerta("No se pudo modificar la compra.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("El codigo es incorrecto");
        } catch (SQLException e) {
            mostrarAlerta("Error en la base de datos: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            mostrarAlerta("Error general: " + e.getMessage());
            e.printStackTrace();
        }

        cmbModificarCodigoCompra.setValue(null);
        dpModificarFechaCompra.setValue(null);
        txtModificarTotal.clear();
        txtModificarDetalle.clear();
        cmbModificarCodigoProveedor.setValue(null);
    }

    @FXML
    private void onEliminarCompra(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            int id = Integer.parseInt(cmbEliminarCompra.getValue());
            PreparedStatement stmt = conn.prepareStatement("CALL sp_EliminarCompra(?);");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt("filasEliminadas") > 0) {
                mostrarAlerta("Compra eliminada correctamente.");
            } else {
                mostrarAlerta("No se pudo eliminar la compra.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Ingresa el id correcto porfavor.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al eliminar la compra.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
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
    private void irAgregarCompra(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/AgregarCompraView.fxml"));
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
    private void irListarCompra(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ListarComprasView.fxml"));
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
    private void irBuscarCompra(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/BuscarCompraView.fxml"));
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
    private void irModificarCompra(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ModificarCompraView.fxml"));
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
    private void irEliminarCompra(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/EliminarCompraView.fxml"));
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
    private void irMenuCompraAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuCompraAdminView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("JCruz-2021644");
        stage.show();
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));

        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }
}
