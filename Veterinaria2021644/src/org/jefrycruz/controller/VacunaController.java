package org.jefrycruz.controller;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
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
import org.jefrycruz.models.Vacunas;

public class VacunaController {

    // La tabla mas las columnas de solo el listar
    @FXML
    private TableView<Vacunas> tblVacuna;
    @FXML
    private TableColumn<Vacunas, Integer> colcodigoVacuna;
    @FXML
    private TableColumn<Vacunas, String> colnombreVacuna;
    @FXML
    private TableColumn<Vacunas, String> coldescipcion;
    @FXML
    private TableColumn<Vacunas, String> coldosis;
    @FXML
    private TableColumn<Vacunas, String> colfrecuenciaMeses;

    // La tabla mas la coluimnas del buscar
    @FXML
    private TextField txtBuscarVacuna;
    @FXML
    private TableView<Vacunas> tblVacunaBuscar;
    @FXML
    private TableColumn<Vacunas, Integer> colcodigoVacunaBuscar;
    @FXML
    private TableColumn<Vacunas, String> colnombreVacunaBuscar;
    @FXML
    private TableColumn<Vacunas, String> coldescipcionBuscar;
    @FXML
    private TableColumn<Vacunas, String> coldosisBuscar;
    @FXML
    private TableColumn<Vacunas, String> colfrecuanciaMesesBuscar;

    // Para eliminar
    @FXML
    private TextField txtEliminarVacuna;

    // Para agregar
    @FXML
    private TextField txtIngresarNombreVacuna;
    @FXML
    private TextField txtIngresarDescripcionVacuna;
    @FXML
    private TextField txtIngresarDosisVacuna;
    @FXML
    private TextField txtIngresarFrecuaneciaMesesVacuna;

    // Para Modificar
    @FXML
    private TextField txtModificarCodigoVacuna;
    @FXML
    private TextField txtModificarNombreVacuna;
    @FXML
    private TextField txtModificarDescripcionVacuna;
    @FXML
    private TextField txtModificarDosisVacuna;
    @FXML
    private TextField txtModificarFrecuaneciaMesesVacuna;

    private ObservableList<Vacunas> lista = FXCollections.observableArrayList();

    // --- Inicializar Listar ---
    @FXML
    public void initialize() {
        if (tblVacuna != null) {
            inicializarTablaListar();
            cargarDatos();
        }

        if (tblVacunaBuscar != null) {
            inicializarTablaBuscar();
        }

    }

    private void inicializarTablaListar() {
        colcodigoVacuna.setCellValueFactory(new PropertyValueFactory<>("codigoVacuna"));
        colnombreVacuna.setCellValueFactory(new PropertyValueFactory<>("nombreVacuna"));
        coldescipcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        coldosis.setCellValueFactory(new PropertyValueFactory<>("dosis"));
        colfrecuenciaMeses.setCellValueFactory(new PropertyValueFactory<>("frecuenciaMeses"));
    }

    private void inicializarTablaBuscar() {
        colcodigoVacunaBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoVacuna"));
        colnombreVacunaBuscar.setCellValueFactory(new PropertyValueFactory<>("nombreVacuna"));
        coldescipcionBuscar.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        coldosisBuscar.setCellValueFactory(new PropertyValueFactory<>("dosis"));
        colfrecuanciaMesesBuscar.setCellValueFactory(new PropertyValueFactory<>("frecuenciaMeses"));
    }

    private void cargarDatos() {

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            lista.clear();
            String sql = "CALL sp_ListarVacunas();";
            CallableStatement cstmt = conn.prepareCall(sql);
            ResultSet rs = cstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Vacunas(
                        rs.getInt("codigoVacuna"),
                        rs.getString("nombreVacuna"),
                        rs.getString("descripcion"),
                        rs.getString("dosis"),
                        rs.getInt("frecuenciaMeses")
                ));
            }

            tblVacuna.setItems(lista);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAgregarVacuna(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            if (txtIngresarNombreVacuna.getText().isEmpty()
                    || txtIngresarDescripcionVacuna.getText().isEmpty()
                    || txtIngresarDosisVacuna.getText().isEmpty()
                    || txtIngresarFrecuaneciaMesesVacuna.getText().isEmpty()) {
                mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
                return;
            }

            int frecuencia;
            try {
                frecuencia = Integer.parseInt(txtIngresarFrecuaneciaMesesVacuna.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Profavor ingresar correctamente el Frecuencia es un entero!!!");
                return;
            }

            String sql = "CALL sp_AgregarVacuna(?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtIngresarNombreVacuna.getText());
            stmt.setString(2, txtIngresarDescripcionVacuna.getText());
            stmt.setString(3, txtIngresarDosisVacuna.getText());
            stmt.setString(4, txtIngresarFrecuaneciaMesesVacuna.getText());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarAlerta("Vacuna agregado correctamente.");
            } else {
                mostrarAlerta("No se pudo agregar la vacuna.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Ingrese correctamente lo que se le pide");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al agregar vacuna: " + e.getMessage());
        }
        txtIngresarNombreVacuna.clear();
        txtIngresarDescripcionVacuna.clear();
        txtIngresarDosisVacuna.clear();
        txtIngresarFrecuaneciaMesesVacuna.clear();

    }

    @FXML
    private void onModificarVacuna(ActionEvent event) {

        // Conectamos la base de datos con el conectar()
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            if (txtModificarCodigoVacuna.getText().isEmpty()
                    || txtModificarNombreVacuna.getText().isEmpty()
                    || txtModificarDescripcionVacuna.getText().isEmpty()
                    || txtModificarDosisVacuna.getText().isEmpty()
                    || txtModificarFrecuaneciaMesesVacuna.getText().isEmpty()) {
                mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
                return;
            }

            int codVacuna;
            try {
                codVacuna = Integer.parseInt(txtModificarCodigoVacuna.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Profavor ingresar correctamente el codigo de vacuna es un entero");
                return;
            }

            int frecuencia;
            try {
                frecuencia = Integer.parseInt(txtModificarFrecuaneciaMesesVacuna.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Profavor ingresar correctamente el Frecuencia es un entero!!!");
                return;
            }

            String sql = "CALL sp_EditarVacuna(?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, Integer.parseInt(txtModificarCodigoVacuna.getText()));
            stmt.setString(2, txtModificarNombreVacuna.getText());
            stmt.setString(3, txtModificarDescripcionVacuna.getText());
            stmt.setString(4, txtModificarDosisVacuna.getText());
            stmt.setString(5, txtModificarFrecuaneciaMesesVacuna.getText());

            int filasAfectadas = stmt.executeUpdate();
            // igual el menssaje si se realizo bien o hay un error
            if (filasAfectadas > 0) {
                mostrarAlerta("Vacuna modificada correctamente.");
                irListarVacuna(event);
            } else {
                mostrarAlerta("No se pudo modificar la vacuna revisa tus id o parametros.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Ingrese correctamente lo que se le pide");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al modificar la vacuna: " + e.getMessage());
        }

        txtModificarCodigoVacuna.clear();
        txtModificarNombreVacuna.clear();
        txtModificarDescripcionVacuna.clear();
        txtModificarDosisVacuna.clear();
        txtModificarFrecuaneciaMesesVacuna.clear();
    }

    @FXML
    private void onEliminarVacuna(ActionEvent event) {

        try {
            int id = Integer.parseInt(txtEliminarVacuna.getText());
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement("CALL sp_EliminarVacuna(?);");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int contador = rs.getInt("filasEliminadas");

                if (contador > 0) {
                    mostrarAlerta("Vacuna eliminada correctamente.");
                } else {
                    mostrarAlerta("No se pudo eliminar la Vacuna.");
                }
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id de la Vacuna a Eliminar.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtEliminarVacuna.clear();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void onBuscarVacuna(ActionEvent event) {
        //otra bvez creamos otra lista en la cual almacenaremos y acutalizaremos los datos 
        ObservableList<Vacunas> vacunaBuscada = FXCollections.observableArrayList();

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            // Hacemos la coneccion con el conectar() con base de datos y llamamos el sp
            PreparedStatement stmt = conn.prepareStatement("CALL sp_BuscarVacuna(?);");
            // pues estoy realizando un casteo de lo que ingreso en el txt lo paso a int porque como int tengo el id
            stmt.setInt(1, Integer.parseInt(txtBuscarVacuna.getText()));
            // Ejecuto la consulta y guardo los valores en el result tes rs 

            ResultSet rs = stmt.executeQuery();

            // Reccorre la fila del resul set 
            while (rs.next()) {
                // Crea el objeto de Clientes que es lo que se agrega a la lista de clienteBuscado 

                vacunaBuscada.add(new Vacunas(
                        rs.getInt("codigoVacuna"),
                        rs.getString("nombreVacuna"),
                        rs.getString("descripcion"),
                        rs.getString("dosis"),
                        rs.getInt("frecuenciaMeses")
                ));

            }
            // Acutalizamos lo calores de la tabla de Clientes en e buscar
            if (vacunaBuscada.isEmpty()) {
                mostrarAlerta("No se encontro ninguna vacuna.");
            }
            tblVacunaBuscar.setItems(vacunaBuscada);
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id de la vacuna a bucar.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtBuscarVacuna.clear();

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
    private void irAgregarVacuna(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/AgregarVacunaView.fxml"));
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
    private void irListarVacuna(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ListarVacunasView.fxml"));
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
    private void irBuscarVacuna(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/BuscarVacunaView.fxml"));
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
    private void irModificarVacuna(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ModificarVacunaView.fxml"));
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
    private void irEliminarVacuna(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/EliminarVacunaView.fxml"));
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
    private void irMenuVacunaAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuVacunaAdminView.fxml"));
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
