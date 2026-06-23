package org.jefrycruz.controller;

import java.io.IOException;
import org.jefrycruz.models.Proveedores;
import java.sql.Connection;
import java.sql.CallableStatement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.sql.ResultSet;
import javafx.scene.control.TextField;
import java.sql.PreparedStatement;
import javafx.scene.control.Alert;
import org.jefrycruz.db.Conexion;

public class ProveedorController {

    // La tabla mas las columnas de solo el listar
    @FXML
    private TableView<Proveedores> tblProveedores;
    @FXML
    private TableColumn<Proveedores, Integer> colcodigoProveedor;
    @FXML
    private TableColumn<Proveedores, String> colnombreProveedor;
    @FXML
    private TableColumn<Proveedores, String> coltelefonoProveedor;
    @FXML
    private TableColumn<Proveedores, String> coldireccionProveedor;
    @FXML
    private TableColumn<Proveedores, String> colcorreoProveedores;

    // La tabla mas la coluimnas del buscar
    @FXML
    private TextField txtBuscarProveedor;
    @FXML
    private TableView<Proveedores> tblProveedoresBuscar;
    @FXML
    private TableColumn<Proveedores, Integer> colcodigoProveedorBuscar;
    @FXML
    private TableColumn<Proveedores, String> colnombreProveedorBuscar;
    @FXML
    private TableColumn<Proveedores, String> coltelefonoProveedorBuscar;
    @FXML
    private TableColumn<Proveedores, String> coldireccionProveedorBuscar;
    @FXML
    private TableColumn<Proveedores, String> colcorreoProveedoresBuscar;
    // Para el elimnar
    @FXML
    private TextField txtEliminarProveedor;

    // Para agregarProveedor
    @FXML
    private TextField txtIngresarNombreProveedor;
    @FXML
    private TextField txtIngresarDireccionProveedor;
    @FXML
    private TextField txtIngresarTelefonoProveedor;
    @FXML
    private TextField txtIngresarCorreoProveedor;

    // Para modificarProveedor
    @FXML
    private TextField txtModificarCodigoProveedor;
    @FXML
    private TextField txtModificarNombreProveedor;
    @FXML
    private TextField txtModificarDireccionProveedor;
    @FXML
    private TextField txtModificarTelefonoProveedor;
    @FXML
    private TextField txtModificarCorreoProveedor;

    private ObservableList<Proveedores> lista = FXCollections.observableArrayList();

    // --- Inicializar Listar ---
    @FXML
    public void initialize() {
        if (tblProveedores != null) {
            inicializarTablaListar();
            cargarDatos();
        }

        if (tblProveedoresBuscar != null) {
            inicializarTablaBuscar();
        }

    }

    private void inicializarTablaListar() {
        colcodigoProveedor.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
        colnombreProveedor.setCellValueFactory(new PropertyValueFactory<>("nombreProveedor"));
        coltelefonoProveedor.setCellValueFactory(new PropertyValueFactory<>("telefonoProveedor"));
        coldireccionProveedor.setCellValueFactory(new PropertyValueFactory<>("direccionProveedor"));
        colcorreoProveedores.setCellValueFactory(new PropertyValueFactory<>("correoProveedores"));
    }

    private void inicializarTablaBuscar() {
        colcodigoProveedorBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
        colnombreProveedorBuscar.setCellValueFactory(new PropertyValueFactory<>("nombreProveedor"));
        coltelefonoProveedorBuscar.setCellValueFactory(new PropertyValueFactory<>("telefonoProveedor"));
        coldireccionProveedorBuscar.setCellValueFactory(new PropertyValueFactory<>("direccionProveedor"));
        colcorreoProveedoresBuscar.setCellValueFactory(new PropertyValueFactory<>("correoProveedores"));
    }

    private void cargarDatos() {

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            lista.clear();
            String sql = "CALL sp_ListarProveedores();";
            CallableStatement cstmt = conn.prepareCall(sql);
            ResultSet rs = cstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Proveedores(
                        rs.getInt("codigoProveedor"),
                        rs.getString("nombreProveedor"),
                        rs.getString("direccionProveedor"),
                        rs.getString("telefonoProveedor"),
                        rs.getString("correoProveedor")
                ));
            }

            tblProveedores.setItems(lista);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAgregarProveedor(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            if (txtIngresarNombreProveedor.getText().isEmpty()
                    || txtIngresarDireccionProveedor.getText().isEmpty()
                    || txtIngresarTelefonoProveedor.getText().isEmpty()
                    || txtIngresarCorreoProveedor.getText().isEmpty()) {
                mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
                return;
            }

            String sql = "CALL sp_AgregarProveedor(?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtIngresarNombreProveedor.getText());
            stmt.setString(2, txtIngresarDireccionProveedor.getText());
            stmt.setString(3, txtIngresarTelefonoProveedor.getText());
            stmt.setString(4, txtIngresarCorreoProveedor.getText());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarAlerta("Proveedor agregado correctamente.");
            } else {
                mostrarAlerta("No se pudo agregar el Proveedor.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al agregar proveedor: " + e.getMessage());
        }

        txtIngresarNombreProveedor.clear();
        txtIngresarDireccionProveedor.clear();
        txtIngresarTelefonoProveedor.clear();
        txtIngresarCorreoProveedor.clear();
    }

    @FXML
    private void onModificarProveedor(ActionEvent event) {

        // Conectamos la base de datos con el conectar()
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            if (txtModificarCodigoProveedor.getText().isEmpty()
                    || txtModificarNombreProveedor.getText().isEmpty()
                    || txtModificarDireccionProveedor.getText().isEmpty()
                    || txtModificarTelefonoProveedor.getText().isEmpty()
                    || txtModificarCorreoProveedor.getText().isEmpty()) {
                mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
                return;
            }
            int codProveedor;
            try {
                codProveedor = Integer.parseInt(txtModificarCodigoProveedor.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Ingresar el codigo de proveedor correspondiente");
                return;
            }

            String sql = "CALL sp_EditarProveedor( ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // buscamos por el int para el id del clientes y asi editarlo
            stmt.setInt(1, Integer.parseInt(txtModificarCodigoProveedor.getText()));
            stmt.setString(2, txtModificarNombreProveedor.getText());
            stmt.setString(3, txtModificarDireccionProveedor.getText());
            stmt.setString(4, txtModificarTelefonoProveedor.getText());
            stmt.setString(5, txtModificarCorreoProveedor.getText());

            int filasAfectadas = stmt.executeUpdate();
            // igual el menssaje si se realizo bien o hay un error
            if (filasAfectadas > 0) {
                mostrarAlerta("Proveedor modificado correctamente.");
            } else {
                mostrarAlerta("No se pudo modificar el proveedor.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al modificar proveedor: " + e.getMessage());
        }
        txtModificarCodigoProveedor.clear();
        txtModificarNombreProveedor.clear();
        txtModificarDireccionProveedor.clear();
        txtModificarTelefonoProveedor.clear();
        txtModificarCorreoProveedor.clear();
    }

    @FXML
    private void onEliminarProveedor(ActionEvent event) {
        try {
            int id = Integer.parseInt(txtEliminarProveedor.getText());
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement("CALL sp_EliminarProveedor(?);");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int contador = rs.getInt("filasEliminadas");

                if (contador > 0) {
                    mostrarAlerta("Proveedor eliminado correctamente.");
                } else {
                    mostrarAlerta("No se pudo eliminar al Proveedor.");

                }
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id del proveedor a Eliminar.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void onBuscarProveedor(ActionEvent event) {
        ObservableList<Proveedores> proveedorBuscado = FXCollections.observableArrayList();

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement("CALL sp_BuscarProveedor(?);");
            stmt.setInt(1, Integer.parseInt(txtBuscarProveedor.getText()));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                proveedorBuscado.add(new Proveedores(
                        rs.getInt("codigoProveedor"),
                        rs.getString("nombreProveedor"),
                        rs.getString("direccionProveedor"),
                        rs.getString("telefonoProveedor"),
                        rs.getString("correoProveedor")
                ));
            }
            if (proveedorBuscado.isEmpty()) {
                mostrarAlerta("No se encontro ninguna Proveedor.");
            }
            tblProveedoresBuscar.setItems(proveedorBuscado);
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id del proveedor a bucar.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    private void irAgregarProveedor(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/AgregarProveedorView.fxml"));
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
    private void irListarProveedor(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ListarProveedoresView.fxml"));
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
    private void irBuscarProveedor(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/BuscarProveedorView.fxml"));
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
    private void irModificarProveedor(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ModificarProveedorView.fxml"));
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
    private void irEliminarProveedor(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/EliminarProveedorView.fxml"));
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
    private void irMenuProveedorAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuProveedoresAdminView.fxml"));
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
