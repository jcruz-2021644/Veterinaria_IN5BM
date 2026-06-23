package org.jefrycruz.controller;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jefrycruz.db.Conexion;
import org.jefrycruz.models.Empleados;

public class EmpleadoController {

    // La tabla mas las columnas de solo el listar
    @FXML
    private TableView<Empleados> tblEmpleado;
    @FXML
    private TableColumn<Empleados, Integer> colcodigoEmpleado;
    @FXML
    private TableColumn<Empleados, String> colnombreEmpleado;
    @FXML
    private TableColumn<Empleados, String> colapellidoEmpleado;
    @FXML
    private TableColumn<Empleados, String> colcargo;
    @FXML
    private TableColumn<Empleados, String> coltelefonoEmpleado;
    @FXML
    private TableColumn<Empleados, String> colcorreoEmpleado;

    // La tabla mas la coluimnas del buscar
    @FXML
    private TextField txtBuscarEmpleado;
    @FXML
    private TableView<Empleados> tblEmpleadoBuscar;
    @FXML
    private TableColumn<Empleados, Integer> colcodigoEmpleadoBuscar;
    @FXML
    private TableColumn<Empleados, String> colnombreEmpleadoBuscar;
    @FXML
    private TableColumn<Empleados, String> colapellidoEmpleadoBuscar;
    @FXML
    private TableColumn<Empleados, String> colcargoBuscar;
    @FXML
    private TableColumn<Empleados, String> coltelefonoEmpleadoBuscar;
    @FXML
    private TableColumn<Empleados, String> colcorreoEmpleadoBuscar;

    // Para eliminar
    @FXML
    private TextField txtEliminarEmpleado;

    // Para agregar
    @FXML
    private TextField txtIngresarNombreEmpleado;
    @FXML
    private TextField txtIngresarApellidoEmpleado;
    @FXML
    private TextField txtIngresarCargoEmpleado;
    @FXML
    private TextField txtIngresarTelefonoEmpleado;
    @FXML
    private TextField txtIngresarCorreoEmpleado;

    // Para Modificar
    @FXML
    private TextField txtModificarCodigoEmpleado;
    @FXML
    private TextField txtModificarNombreEmpleado;
    @FXML
    private TextField txtModificarApellidoEmpleado;
    @FXML
    private TextField txtModificarCargoEmpleado;
    @FXML
    private TextField txtModificarTelefonoEmpleado;
    @FXML
    private TextField txtModificarCorreoEmpleado;

    private ObservableList<Empleados> lista = FXCollections.observableArrayList();

    // --- Inicializar Listar ---
    @FXML
    public void initialize() {
        if (tblEmpleado != null) {
            inicializarTablaListar();
            cargarDatos();
        }

        if (tblEmpleadoBuscar != null) {
            inicializarTablaBuscar();
        }

    }

    private void inicializarTablaListar() {
        colcodigoEmpleado.setCellValueFactory(new PropertyValueFactory<>("codigoEmpleado"));
        colnombreEmpleado.setCellValueFactory(new PropertyValueFactory<>("nombreEmpleado"));
        colapellidoEmpleado.setCellValueFactory(new PropertyValueFactory<>("apellidoEmpleado"));
        colcargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        coltelefonoEmpleado.setCellValueFactory(new PropertyValueFactory<>("telefonoEmpleado"));
        colcorreoEmpleado.setCellValueFactory(new PropertyValueFactory<>("correoEmpleado"));
    }

    private void inicializarTablaBuscar() {
        colcodigoEmpleadoBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoEmpleado"));
        colnombreEmpleadoBuscar.setCellValueFactory(new PropertyValueFactory<>("nombreEmpleado"));
        colapellidoEmpleadoBuscar.setCellValueFactory(new PropertyValueFactory<>("apellidoEmpleado"));
        colcargoBuscar.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        coltelefonoEmpleadoBuscar.setCellValueFactory(new PropertyValueFactory<>("telefonoEmpleado"));
        colcorreoEmpleadoBuscar.setCellValueFactory(new PropertyValueFactory<>("correoEmpleado"));
    }

    private void cargarDatos() {

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            lista.clear();
            String sql = "CALL sp_ListarEmpleados();";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Empleados(
                        rs.getInt("codigoEmpleado"),
                        rs.getString("nombreEmpleado"),
                        rs.getString("apellidoEmpleado"),
                        rs.getString("cargo"),
                        rs.getString("telefonoEmpleado"),
                        rs.getString("correoEmpleado")
                ));
            }

            tblEmpleado.setItems(lista);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAgregarEmpleado(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            if (txtIngresarNombreEmpleado.getText().isEmpty()
                    || txtIngresarApellidoEmpleado.getText().isEmpty()
                    || txtIngresarCargoEmpleado.getText().isEmpty()
                    || txtIngresarTelefonoEmpleado.getText().isEmpty()
                    || txtIngresarCorreoEmpleado.getText().isEmpty()) {
                mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
                return;
            }

            String sql = "CALL sp_AgregarEmpleado(?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtIngresarNombreEmpleado.getText());
            stmt.setString(2, txtIngresarApellidoEmpleado.getText());
            stmt.setString(3, txtIngresarCargoEmpleado.getText());
            stmt.setString(4, txtIngresarTelefonoEmpleado.getText());
            stmt.setString(5, txtIngresarCorreoEmpleado.getText());
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarAlerta("Empleado agregado correctamente.");
            } else {
                mostrarAlerta("No se pudo agregar el empleado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al agregar Empleado: " + e.getMessage());
        }

        txtIngresarNombreEmpleado.clear();
        txtIngresarApellidoEmpleado.clear();
        txtIngresarCargoEmpleado.clear();
        txtIngresarTelefonoEmpleado.clear();
        txtIngresarCorreoEmpleado.clear();
    }

    @FXML
    private void onModificarEmpleado(ActionEvent event) {

        if (txtModificarCodigoEmpleado.getText().isEmpty()
                || txtModificarNombreEmpleado.getText().isEmpty()
                || txtModificarApellidoEmpleado.getText().isEmpty()
                || txtModificarCargoEmpleado.getText().isEmpty()
                || txtModificarTelefonoEmpleado.getText().isEmpty()
                || txtModificarCorreoEmpleado.getText().isEmpty()) {
            mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
            return;
        }
        int codEmpleado;
        try {
            codEmpleado = Integer.parseInt(txtModificarCodigoEmpleado.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Ingrese el codigo empleado correctamente");
            return;
        }
        // Conectamos la base de datos con el conectar()
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            String sql = "CALL sp_EditarEmpleado(?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, Integer.parseInt(txtModificarCodigoEmpleado.getText()));
            stmt.setString(2, txtModificarNombreEmpleado.getText());
            stmt.setString(3, txtModificarApellidoEmpleado.getText());
            stmt.setString(4, txtModificarCargoEmpleado.getText());
            stmt.setString(5, txtModificarTelefonoEmpleado.getText());
            stmt.setString(6, txtModificarCorreoEmpleado.getText());

            int filasAfectadas = stmt.executeUpdate();
            // igual el menssaje si se realizo bien o hay un error
            if (filasAfectadas > 0) {
                mostrarAlerta("Empleado modificado correctamente.");
            } else {
                mostrarAlerta("No se pudo modificar el Empleado revisa tu id o los parametros.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id de el Empleado a modificar.");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al modificar el Empleado: " + e.getMessage());
        }

        txtModificarCodigoEmpleado.clear();
        txtModificarNombreEmpleado.clear();
        txtModificarApellidoEmpleado.clear();
        txtModificarCargoEmpleado.clear();
        txtModificarTelefonoEmpleado.clear();
        txtModificarCorreoEmpleado.clear();

    }

    @FXML
    private void onEliminarEmpleado(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            int id = Integer.parseInt(txtEliminarEmpleado.getText());

            PreparedStatement stmt = conn.prepareStatement("CALL sp_EliminarEmpleado(?);");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int contador = rs.getInt("filasEliminadas");

                if (contador > 0) {
                    mostrarAlerta("Empleado eliminado correctamente.");
                } else {
                    mostrarAlerta("No se pudo eliminar la Cita.");
                }
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id de la cita a Eliminar.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtEliminarEmpleado.clear();

    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void onBuscarEmpleado(ActionEvent event) {
        //otra bvez creamos otra lista en la cual almacenaremos y acutalizaremos los datos 
        ObservableList<Empleados> empleadoBuscado = FXCollections.observableArrayList();

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            // Hacemos la coneccion con el conectar() con base de datos y llamamos el sp
            PreparedStatement stmt = conn.prepareStatement("CALL sp_BuscarEmpleado(?);");
            // pues estoy realizando un casteo de lo que ingreso en el txt lo paso a int porque como int tengo el id
            stmt.setInt(1, Integer.parseInt(txtBuscarEmpleado.getText()));
            // Ejecuto la consulta y guardo los valores en el result tes rs 

            ResultSet rs = stmt.executeQuery();

            // Reccorre la fila del resul set 
            while (rs.next()) {
                // Crea el objeto de Clientes que es lo que se agrega a la lista de clienteBuscado 

                empleadoBuscado.add(new Empleados(
                        rs.getInt("codigoEmpleado"),
                        rs.getString("nombreEmpleado"),
                        rs.getString("apellidoEmpleado"),
                        rs.getString("cargo"),
                        rs.getString("telefonoEmpleado"),
                        rs.getString("correoEmpleado")
                ));

            }

            if (empleadoBuscado.isEmpty()) {
                mostrarAlerta("No se encontro ninguna empleado.");
            }
            tblEmpleadoBuscar.setItems(empleadoBuscado);
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id del empleado a bucar.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        txtBuscarEmpleado.clear();

    }

    @FXML
    private void irMenuEmpleadoAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuEmpleadoAdminView.fxml"));
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
    private void irAgregarEmpleado(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/AgregarEmpleadoView.fxml"));
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
    private void irListarEmpleado(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ListarEmpleadosView.fxml"));
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
    private void irBuscarEmpleado(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/BuscarEmpleadoView.fxml"));
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
    private void irModificarEmpleado(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ModificarEmpleadoView.fxml"));
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
    private void irEliminarEmpleado(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/EliminarEmpleadoView.fxml"));
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
