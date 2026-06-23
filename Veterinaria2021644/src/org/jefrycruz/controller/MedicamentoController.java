package org.jefrycruz.controller;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.jefrycruz.models.Medicamentos;

/**
 *
 * @author informatica
 */
public class MedicamentoController {
// La tabla mas las columnas de solo el listar

    @FXML
    private TableView<Medicamentos> tblMedicamento;
    @FXML
    private TableColumn<Medicamentos, Integer> colcodigoMedicamento;
    @FXML
    private TableColumn<Medicamentos, String> colnombre;
    @FXML
    private TableColumn<Medicamentos, String> coldescripcion;
    @FXML
    private TableColumn<Medicamentos, Integer> colstock;
    @FXML
    private TableColumn<Medicamentos, Double> colprecioUnitario;
    @FXML
    private TableColumn<Medicamentos, Date> colfechaVencimiento;
    @FXML
    private TableColumn<Medicamentos, Integer> colcodigoProveedor;

    // La tabla mas la coluimnas del buscar
    @FXML
    private TextField txtBuscarMedicamento;
    @FXML
    private TableView<Medicamentos> tblMedicamentoBuscar;
    @FXML
    private TableColumn<Medicamentos, Integer> colcodigoMedicamentoBuscar;
    @FXML
    private TableColumn<Medicamentos, String> colnombreBuscar;
    @FXML
    private TableColumn<Medicamentos, String> coldescripcionBuscar;
    @FXML
    private TableColumn<Medicamentos, Integer> colstockBuscar;
    @FXML
    private TableColumn<Medicamentos, Double> colprecioUnitarioBuscar;
    @FXML
    private TableColumn<Medicamentos, Date> colfechaVencimientoBuscar;
    @FXML
    private TableColumn<Medicamentos, Integer> colcodigoProveedorBuscar;
    // Para el eliminar
    @FXML
    private TextField txtEliminarMedicamento;

    // Para hacer el AgregarClienteView
    @FXML
    private TextField txtIngresarNombre;
    @FXML
    private TextField txtIngresarDescripcion;
    @FXML
    private TextField txtIngresarStock;
    @FXML
    private TextField txtIngresarPrecioUnitario;
    @FXML
    private DatePicker dpIngresarFechaVencimiento;
    @FXML
    private TextField txtIngresarCodigoProveedor;

    // para hacer el modificar
    @FXML
    private TextField txtModificarCodigoMedicamento;
    @FXML
    private TextField txtModificarNombre;
    @FXML
    private TextField txtModificarDescripcion;
    @FXML
    private TextField txtModificarStock;
    @FXML
    private TextField txtModificarPrecioUnitario;
    @FXML
    private DatePicker dpModificarFechaVencimiento;
    @FXML
    private TextField txtModificarCodigoProveedor;

    private ObservableList<Medicamentos> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (tblMedicamento != null) {
            inicializarTablaListar();
            cargarDatos();
        }

        if (tblMedicamentoBuscar != null) {
            inicializarTablaBuscar();
        }
    }

    private void inicializarTablaListar() {
        colcodigoMedicamento.setCellValueFactory(new PropertyValueFactory<>("codigoMedicamento"));
        colnombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        coldescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colstock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colprecioUnitario.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colfechaVencimiento.setCellValueFactory(new PropertyValueFactory<>("fechaVencimiento"));
        colcodigoProveedor.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));

    }

    private void inicializarTablaBuscar() {
        colcodigoMedicamentoBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoMedicamento"));
        colnombreBuscar.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        coldescripcionBuscar.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colstockBuscar.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colprecioUnitarioBuscar.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colfechaVencimientoBuscar.setCellValueFactory(new PropertyValueFactory<>("fechaVencimiento"));
        colcodigoProveedorBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
    }

    private void cargarDatos() {

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            lista.clear();
            String sql = "CALL sp_ListarMedicamentos();";
            CallableStatement cstmt = conn.prepareCall(sql);
            ResultSet rs = cstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Medicamentos(
                        rs.getInt("codigoMedicamento"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getInt("stock"),
                        rs.getDouble("precioUnitario"),
                        rs.getDate("fechaVencimiento"),
                        rs.getInt("codigoProveedor")
                ));
            }
            tblMedicamento.setItems(lista);
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    @FXML
    private void onBuscarMedicamento(ActionEvent event) {
        ObservableList<Medicamentos> medicamentoBuscado = FXCollections.observableArrayList();

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareCall("CALL sp_BuscarMedicamento(?);");
            stmt.setInt(1, Integer.parseInt(txtBuscarMedicamento.getText()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                medicamentoBuscado.add(new Medicamentos(
                        rs.getInt("codigoMedicamento"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getInt("stock"),
                        rs.getDouble("precioUnitario"),
                        rs.getDate("fechaVencimiento"),
                        rs.getInt("codigoProveedor")
                ));
            }
            if (medicamentoBuscado.isEmpty()) {
                mostrarAlerta("No se encontro ningun Medicamento.");
            }
            tblMedicamentoBuscar.setItems(medicamentoBuscado);
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id de el Medicamento a bucar.");
        } catch (SQLException e) {
            mostrarAlerta("Error al buscar Medicamento: " + e.getMessage());
            e.printStackTrace();

        }
        txtBuscarMedicamento.clear();
    }

    @FXML
    private void onAgregarMedicamento(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            if (txtIngresarNombre.getText().isEmpty()
                    || txtIngresarDescripcion.getText().isEmpty()
                    || txtIngresarStock.getText().isEmpty()
                    || txtIngresarPrecioUnitario.getText().isEmpty()
                    || dpIngresarFechaVencimiento == null
                    || txtIngresarCodigoProveedor.getText().isEmpty()) {
                mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
                return;
            }

            int codProveedor;
            try {
                codProveedor = Integer.parseInt(txtIngresarCodigoProveedor.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Ingrese correctamente el codigo proveedor");
                return;
            }

            String sql = "CALL sp_AgregarMedicamento( ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtIngresarNombre.getText());
            stmt.setString(2, txtIngresarDescripcion.getText());
            stmt.setString(3, txtIngresarStock.getText());
            stmt.setString(4, txtIngresarPrecioUnitario.getText());
            stmt.setDate(5, java.sql.Date.valueOf(dpIngresarFechaVencimiento.getValue()));
            stmt.setString(6, txtIngresarCodigoProveedor.getText());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarAlerta("Medicamento agregado correctamente.");
            } else {
                mostrarAlerta("No se pudo agregar el Medicamento.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("Uno de los dos codigos no existe.");
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().toLowerCase().contains("fk")) {
                mostrarAlerta("Error: Uno de los dos codigos esta mal no existe.");
            } else {
                mostrarAlerta("Error al modificar el medicamento: " + e.getMessage());
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al modificar el medicamento: " + e.getMessage());
        }
        txtIngresarNombre.clear();
        txtIngresarDescripcion.clear();
        txtIngresarStock.clear();
        txtIngresarPrecioUnitario.clear();
        dpIngresarFechaVencimiento.setValue(null);
        txtIngresarCodigoProveedor.clear();

    }

    @FXML
    private void onModificarMedicamento(ActionEvent event) {

        if (txtModificarCodigoMedicamento.getText().isEmpty()
                || txtModificarNombre.getText().isEmpty()
                || txtModificarDescripcion.getText().isEmpty()
                || txtModificarStock.getText().isEmpty()
                || txtModificarPrecioUnitario.getText().isEmpty()
                || dpModificarFechaVencimiento == null
                || txtModificarCodigoProveedor.getText().isEmpty()) {
            mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
            return;
        }

        int codMedicamento;
        try {
            codMedicamento = Integer.parseInt(txtModificarCodigoMedicamento.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Ingrese correctamente el codigo Medicamento");
            return;
        }

        int codProveedor;
        try {
            codProveedor = Integer.parseInt(txtModificarCodigoProveedor.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Ingrese correctamente el codigo proveedor");
            return;
        }

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            String sql = "CALL sp_EditarMedicamento(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, Integer.parseInt(txtModificarCodigoMedicamento.getText()));
            stmt.setString(2, txtModificarNombre.getText());
            stmt.setString(3, txtModificarDescripcion.getText());
            stmt.setString(4, txtModificarStock.getText());
            stmt.setString(5, txtModificarPrecioUnitario.getText());
            stmt.setDate(6, java.sql.Date.valueOf(dpModificarFechaVencimiento.getValue()));
            stmt.setString(7, txtModificarCodigoProveedor.getText());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                mostrarAlerta("Medicamento modificado correctamente.");
            } else {
                mostrarAlerta("No se pudo modificar el Medicamento.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("Uno de los dos codigos no existe.");
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().toLowerCase().contains("fk")) {
                mostrarAlerta("Error: Uno de los dos codigos esta mal no existe.");
            } else {
                mostrarAlerta("Error al modificar el medicamento: " + e.getMessage());
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al modificar el medicamento: " + e.getMessage());
        }

        txtModificarCodigoMedicamento.clear();
        txtModificarNombre.clear();
        txtModificarDescripcion.clear();
        txtModificarStock.clear();
        txtModificarPrecioUnitario.clear();
        dpModificarFechaVencimiento.setValue(null);
        txtModificarCodigoProveedor.clear();

    }

    @FXML
    private void onEliminarMedicamento(ActionEvent event) {
        try {
            int id = Integer.parseInt(txtEliminarMedicamento.getText());
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement("CALL sp_EliminarMedicamento(?);");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int contador = rs.getInt("filasEliminadas");

                if (contador > 0) {
                    mostrarAlerta("Medicamento eliminado correctamente.");
                } else {
                    mostrarAlerta("No se pudo eliminar el Medicamento.");
                }
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id del medicamento a Eliminar.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtEliminarMedicamento.clear();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void irMenuMedicamento(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuMedicamentosView.fxml"));
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
    private void irMenuMedicamentoAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuMedicamentosAdminView.fxml"));
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
    private void irListarMedicamento(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ListarMedicamentosView.fxml"));
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
    private void irAgregarMedicamento(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/AgregarMedicamentoView.fxml"));
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
    private void irBuscarMedicamento(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/BuscarMedicamentoView.fxml"));
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
    private void irModificarMedicamento(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ModificarMedicamentoView.fxml"));
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
    private void irEliminarMedicamento(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/EliminarMedicamentoView.fxml"));
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
