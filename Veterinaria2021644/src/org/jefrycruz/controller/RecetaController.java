package org.jefrycruz.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.jefrycruz.db.Conexion;
import org.jefrycruz.models.Recetas;
import org.jefrycruz.report.GenerarReporte;

public class RecetaController {

    // La tabla mas las columnas de solo el listar
    @FXML
    private TableView<Recetas> tblReceta;
    @FXML
    private TableColumn<Recetas, Integer> colcodigoReceta;
    @FXML
    private TableColumn<Recetas, String> coldosis;
    @FXML
    private TableColumn<Recetas, String> colfrecuencia;
    @FXML
    private TableColumn<Recetas, Integer> colduracionDosis;
    @FXML
    private TableColumn<Recetas, String> colindicaciones;
    @FXML
    private TableColumn<Recetas, Integer> colcodigoConsulta;
    @FXML
    private TableColumn<Recetas, Integer> colcodigoMedicamento;

    // La tabla mas la coluimnas del buscar
    @FXML
    private TextField txtBuscarReceta;
    @FXML
    private TableView<Recetas> tblRecetaBuscar;
    @FXML
    private TableColumn<Recetas, Integer> colcodigoRecetaBuscar;
    @FXML
    private TableColumn<Recetas, String> coldosisBuscar;
    @FXML
    private TableColumn<Recetas, String> colfrecuenciaBuscar;
    @FXML
    private TableColumn<Recetas, Integer> colduracionDosisBuscar;
    @FXML
    private TableColumn<Recetas, String> colindicacionesBuscar;
    @FXML
    private TableColumn<Recetas, Integer> colcodigoConsultaBuscar;
    @FXML
    private TableColumn<Recetas, Integer> colcodigoMedicamentoBuscar;
    // Para el eliminar
    @FXML
    private TextField txtEliminarReceta;

    // Para hacer el AgregarClienteView
    @FXML
    private TextField txtIngresarDosis;
    @FXML
    private TextField txtIngresarFrecuencia;
    @FXML
    private TextField txtIngresarDuracionDosis;
    @FXML
    private TextField txtIngresarIndicaciones;
    @FXML
    private TextField txtIngresarCodigoConsulta;
    @FXML
    private TextField txtIngresarCodigoMedicamento;

    // para hacer el modificar
    @FXML
    private TextField txtModificarCodigoReceta;
    @FXML
    private TextField txtModificarDosis;
    @FXML
    private TextField txtModificarFrecuencia;
    @FXML
    private TextField txtModificarDuracionDosis;
    @FXML
    private TextField txtModificarIndicaciones;
    @FXML
    private TextField txtModificarCodigoConsulta;
    @FXML
    private TextField txtModificarCodigoMedicamento;

    private ObservableList<Recetas> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (tblReceta != null) {
            inicializarTablaListar();
            cargarDatos();
        }

        if (tblRecetaBuscar != null) {
            inicializarTablaBuscar();
        }
    }

    private void inicializarTablaListar() {
        colcodigoReceta.setCellValueFactory(new PropertyValueFactory<>("codigoReceta"));
        coldosis.setCellValueFactory(new PropertyValueFactory<>("dosis"));
        colfrecuencia.setCellValueFactory(new PropertyValueFactory<>("frecuencia"));
        colduracionDosis.setCellValueFactory(new PropertyValueFactory<>("duracionDosis"));
        colindicaciones.setCellValueFactory(new PropertyValueFactory<>("indicaciones"));
        colcodigoConsulta.setCellValueFactory(new PropertyValueFactory<>("codigoConsulta"));
        colcodigoMedicamento.setCellValueFactory(new PropertyValueFactory<>("codigoMedicamento"));

    }

    private void inicializarTablaBuscar() {
        colcodigoRecetaBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoReceta"));
        coldosisBuscar.setCellValueFactory(new PropertyValueFactory<>("dosis"));
        colfrecuenciaBuscar.setCellValueFactory(new PropertyValueFactory<>("frecuencia"));
        colduracionDosisBuscar.setCellValueFactory(new PropertyValueFactory<>("duracionDosis"));
        colindicacionesBuscar.setCellValueFactory(new PropertyValueFactory<>("indicaciones"));
        colcodigoConsultaBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoConsulta"));
        colcodigoMedicamentoBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoMedicamento"));
    }

    private void cargarDatos() {

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            lista.clear();
            String sql = "CALL sp_ListarRecetas();";
            CallableStatement cstmt = conn.prepareCall(sql);
            ResultSet rs = cstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Recetas(
                        rs.getInt("codigoReceta"),
                        rs.getString("dosis"),
                        rs.getString("frecuencia"),
                        rs.getInt("duracionDosis"),
                        rs.getString("indicaciones"),
                        rs.getInt("codigoConsulta"),
                        rs.getInt("codigoMedicamento")
                ));
            }
            tblReceta.setItems(lista);
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    @FXML
    private void onBuscarReceta(ActionEvent event) {
        ObservableList<Recetas> recetaBuscada = FXCollections.observableArrayList();

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareCall("CALL sp_BuscarReceta(?);");
            stmt.setInt(1, Integer.parseInt(txtBuscarReceta.getText()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                recetaBuscada.add(new Recetas(
                        rs.getInt("codigoReceta"),
                        rs.getString("dosis"),
                        rs.getString("frecuencia"),
                        rs.getInt("duracionDosis"),
                        rs.getString("indicaciones"),
                        rs.getInt("codigoConsulta"),
                        rs.getInt("codigoMedicamento")
                ));
            }
            if (recetaBuscada.isEmpty()) {
                mostrarAlerta("No se encontro ninguna receta.");
            }
            tblRecetaBuscar.setItems(recetaBuscada);
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id de la receta a bucar.");
        } catch (SQLException e) {
            mostrarAlerta("Error al buscar receta: " + e.getMessage());
            e.printStackTrace();

        }
        txtBuscarReceta.clear();
    }

    @FXML
    private void onAgregarReceta(ActionEvent event) {
        if (txtIngresarDosis.getText().isEmpty()
                || txtIngresarFrecuencia.getText().isEmpty()
                || txtIngresarDuracionDosis.getText().isEmpty()
                || txtIngresarIndicaciones.getText().isEmpty()
                || txtIngresarCodigoConsulta.getText().isEmpty()
                || txtIngresarCodigoMedicamento.getText().isEmpty()) {
            mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
            return;
        }

        int duracionDosis;
        int codigoConsulta;
        int codigoMedicamento;

        try {
            duracionDosis = Integer.parseInt(txtIngresarDuracionDosis.getText());
            codigoConsulta = Integer.parseInt(txtIngresarCodigoConsulta.getText());
            codigoMedicamento = Integer.parseInt(txtIngresarCodigoMedicamento.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Por favor, asegúrese de ingresar solo números en duración y códigos.");
            return;
        }

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            String sql = "CALL sp_AgregarReceta(?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, txtIngresarDosis.getText());
            stmt.setString(2, txtIngresarFrecuencia.getText());
            stmt.setInt(3, duracionDosis);
            stmt.setString(4, txtIngresarIndicaciones.getText());
            stmt.setInt(5, codigoConsulta);
            stmt.setInt(6, codigoMedicamento);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarAlerta("Receta agregada correctamente.");
            } else {
                mostrarAlerta("No se pudo agregar la receta.");
            }

            // Limpiar los campos
            txtIngresarDosis.clear();
            txtIngresarFrecuencia.clear();
            txtIngresarDuracionDosis.clear();
            txtIngresarIndicaciones.clear();
            txtIngresarCodigoConsulta.clear();
            txtIngresarCodigoMedicamento.clear();

        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("Uno de los dos códigos no existe.");
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().toLowerCase().contains("fk")) {
                mostrarAlerta("Error: Uno de los dos códigos está mal o no existe.");
            } else {
                mostrarAlerta("Error al agregar la receta: " + e.getMessage());
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al agregar la receta: " + e.getMessage());
        }
    }

    @FXML
    private void onModificarReceta(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            if (txtModificarCodigoReceta.getText().isEmpty()
                    || txtModificarDosis.getText().isEmpty()
                    || txtModificarFrecuencia.getText().isEmpty()
                    || txtModificarDuracionDosis.getText().isEmpty()
                    || txtModificarIndicaciones.getText().isEmpty()
                    || txtModificarCodigoConsulta.getText().isEmpty()
                    || txtModificarCodigoMedicamento.getText().isEmpty()) {
                mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
                return;
            }

            int codigoReceta;
            int duracionDosis;
            int codigoConsulta;
            int codigoMedicamento;
            try {
                codigoReceta = Integer.parseInt(txtModificarCodigoReceta.getText());
                codigoConsulta = Integer.parseInt(txtIngresarCodigoConsulta.getText());
                codigoMedicamento = Integer.parseInt(txtIngresarCodigoMedicamento.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Los codigos tiene que ser un numero.");
                return;
            }

            try {
                duracionDosis = Integer.parseInt(txtModificarDuracionDosis.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("La duración de la dosis debe ser un numero entero.");
                return;
            }

            String sql = "CALL sp_EditarReceta(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, codigoReceta);
            stmt.setString(2, txtModificarDosis.getText());
            stmt.setString(3, txtModificarFrecuencia.getText());
            stmt.setInt(4, duracionDosis); // CORREGIDO: ahora es entero
            stmt.setString(5, txtModificarIndicaciones.getText());
            stmt.setString(6, txtModificarCodigoConsulta.getText());
            stmt.setString(7, txtModificarCodigoMedicamento.getText());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                mostrarAlerta("Receta modificada correctamente.");
            } else {
                mostrarAlerta("No se pudo modificar la Receta.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("Uno de los dos códigos no existe.");
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().toLowerCase().contains("fk")) {
                mostrarAlerta("Error: Uno de los dos códigos está mal o no existe.");
            } else {
                mostrarAlerta("Error al modificar la receta: " + e.getMessage());
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al modificar la receta: " + e.getMessage());
        }

        // Limpiar campos
        txtModificarCodigoReceta.clear();
        txtModificarDosis.clear();
        txtModificarFrecuencia.clear();
        txtModificarDuracionDosis.clear();
        txtModificarIndicaciones.clear();
        txtModificarCodigoConsulta.clear();
        txtModificarCodigoMedicamento.clear();
    }

    @FXML
    private void onEliminarReceta(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();

            int id = Integer.parseInt(txtEliminarReceta.getText());

            PreparedStatement stmt = conn.prepareStatement("CALL sp_EliminarReceta(?);");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int contador = rs.getInt("filasEliminadas");

                if (contador > 0) {
                    mostrarAlerta("Receta eliminada correctamente.");
                } else {
                    mostrarAlerta("No se pudo eliminar la Receta.");
                }
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id de la receta a Eliminar.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtEliminarReceta.clear();

    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void irMenuReceta(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuRecetaView.fxml"));
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
    private void irMenuRecetaAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuRecetaAdminView.fxml"));
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
    private void irBuscarReceta(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/BuscarRecetaView.fxml"));
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
    private void irImprimirReceta(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/GenerarRecetaView.fxml"));
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
        stage.setTitle("Veterinaria JCruz-2021644");
        stage.show();
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));

        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irAgregarReceta(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/AgregarRecetaView.fxml"));
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
    private void irModificarReceta(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ModificarRecetaView.fxml"));
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
    private void irListarReceta(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ListarRecetasView.fxml"));
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
    private void irEliminarReceta(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/EliminarRecetaView.fxml"));
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
            imprimirReceta();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void imprimirReceta() {
        Map<String, Object> parametros = new HashMap<>();
        try {
            InputStream hoja = getClass().getResourceAsStream("/org/jefrycruz/image/Hoja_membretada.jpg");
            if (hoja == null) {
                System.out.println("No se encontró la imagen del logo en /org/jefrycruz/image/Hoja_membretada.jpg");
            } else {
                // Convertir InputStream a BufferedImage (java.awt.Image)
                BufferedImage hojaImagen = ImageIO.read(hoja);
                parametros.put("HojaMembretada", hojaImagen);
            }

            GenerarReporte.mostrarReporte(
                    "/org/jefrycruz/report/RecetaMascota.jasper",
                    "Receta de la mascota",
                    parametros
            );

        } catch (Exception e) {
            System.out.println("Error al cargar la imagen o generar reporte: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
