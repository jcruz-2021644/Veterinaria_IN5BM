package org.jefrycruz.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.jefrycruz.db.Conexion;
import org.jefrycruz.models.Facturas;
import org.jefrycruz.report.GenerarReporte;

/**
 *
 * @author PC
 */
public class FacturaController {

    // La tabla mas las columnas de solo el listar
    @FXML
    private TableView<Facturas> tblFactura;
    @FXML
    private TableColumn<Facturas, Integer> colcodigoFactura;
    @FXML
    private TableColumn<Facturas, Date> colfechaEmision;
    @FXML
    private TableColumn<Facturas, Double> coltotal;
    @FXML
    private TableColumn<Facturas, String> colmetodoPago;
    @FXML
    private TableColumn<Facturas, Integer> colcodigoCliente;
    @FXML
    private TableColumn<Facturas, Integer> colcodigoEmpleado;

    // La tabla mas la coluimnas del buscar
    @FXML
    private TextField txtBuscarFactura;
    @FXML
    private TextField txtImprimir;
    @FXML
    private TableView<Facturas> tblFacturaBuscar;
    @FXML
    private TableColumn<Facturas, Integer> colcodigoFacturaBuscar;
    @FXML
    private TableColumn<Facturas, Date> colfechaEmisionBuscar;
    @FXML
    private TableColumn<Facturas, Double> coltotalBuscar;
    @FXML
    private TableColumn<Facturas, String> colmetodoPagoBuscar;
    @FXML
    private TableColumn<Facturas, Integer> colcodigoClienteBuscar;
    @FXML
    private TableColumn<Facturas, Integer> colcodigoEmpleadoBuscar;
    // Para el eliminar
    @FXML
    private TextField txtEliminarFactura;

    @FXML
    private ComboBox<String> cmbMetodoPago;
    // Para hacer el AgregarClienteView
    @FXML
    private DatePicker dpIngresarFechaEmision;
    @FXML
    private TextField txtIngresarTotal;
    @FXML
    private TextField txtIngresarCodigoCliente;
    @FXML
    private TextField txtIngresarCodigoEmpleado;

    // para hacer el modificar
    @FXML
    private TextField txtModificarCodigoFactura;
    @FXML
    private DatePicker dpModificarFechaEmision;
    @FXML
    private TextField txtModificarTotal;
    @FXML
    private TextField txtModificarCodigoCliente;
    @FXML
    private TextField txtModificarCodigoEmpleado;
    private int codFacturaB;
    private boolean encontrada;
    private ObservableList<Facturas> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (tblFactura != null) {
            inicializarTablaListar();
            cargarDatos();
        }

        if (tblFacturaBuscar != null) {
            inicializarTablaBuscar();
        }
        cargarEnumsFactura();
    }

    private void inicializarTablaListar() {
        colcodigoFactura.setCellValueFactory(new PropertyValueFactory<>("codigoFactura"));
        colfechaEmision.setCellValueFactory(new PropertyValueFactory<>("fechaEmision"));
        coltotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colmetodoPago.setCellValueFactory(new PropertyValueFactory<>("metodoPago"));
        colcodigoCliente.setCellValueFactory(new PropertyValueFactory<>("codigoCliente"));
        colcodigoEmpleado.setCellValueFactory(new PropertyValueFactory<>("codigoEmpleado"));

    }

    private void inicializarTablaBuscar() {
        colcodigoFacturaBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoFactura"));
        colfechaEmisionBuscar.setCellValueFactory(new PropertyValueFactory<>("fechaEmision"));
        coltotalBuscar.setCellValueFactory(new PropertyValueFactory<>("total"));
        colmetodoPagoBuscar.setCellValueFactory(new PropertyValueFactory<>("metodoPago"));
        colcodigoClienteBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoCliente"));
        colcodigoEmpleadoBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoEmpleado"));
    }

    private void cargarDatos() {

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            lista.clear();
            String sql = "CALL sp_ListarFacturas();";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Facturas(
                        rs.getInt("codigoFactura"),
                        rs.getDate("fechaEmision"),
                        rs.getDouble("total"),
                        Facturas.Pago.valueOf(rs.getString("metodoPago")),
                        rs.getInt("codigoCliente"),
                        rs.getInt("codigoEmpleado")
                ));
            }
            tblFactura.setItems(lista);
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    private void cargarEnumsFactura() {
        ObservableList<String> enumsFactura = FXCollections.observableArrayList(
                "Efectivo", "Tarjeta", "Transferencia");
        if (cmbMetodoPago != null) {
            cmbMetodoPago.setItems(enumsFactura);
        }
    }

    @FXML
    private void onBuscarFactura(ActionEvent event) {
        ObservableList<Facturas> facturaBuscada = FXCollections.observableArrayList();

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareCall("CALL sp_BuscarFactura(?);");
            stmt.setInt(1, Integer.parseInt(txtBuscarFactura.getText()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                facturaBuscada.add(new Facturas(
                        rs.getInt("codigoFactura"),
                        rs.getDate("fechaEmision"),
                        rs.getDouble("total"),
                        Facturas.Pago.valueOf(rs.getString("metodoPago")),
                        rs.getInt("codigoCliente"),
                        rs.getInt("codigoEmpleado")
                ));
            }
            if (facturaBuscada.isEmpty()) {
                mostrarAlerta("No se encontro ninguna factura.");
            }
            tblFacturaBuscar.setItems(facturaBuscada);
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id de la factura a bucar.");
        } catch (SQLException e) {
            mostrarAlerta("Error al buscar factura: " + e.getMessage());
            e.printStackTrace();

        }
        txtBuscarFactura.clear();
    }

    @FXML
    private void onAgregarFactura(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            if (dpIngresarFechaEmision == null
                    || txtIngresarTotal.getText().isEmpty()
                    || cmbMetodoPago.getValue() == null
                    || txtIngresarCodigoCliente.getText().isEmpty()
                    || txtIngresarCodigoEmpleado.getText().isEmpty()) {
                mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
                return;
            }

            int codCliente;
            try {
                codCliente = Integer.parseInt(txtIngresarCodigoCliente.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Ingrese el codigo cliente correctamente");
                return;
            }

            int codEmpleado;
            try {
                codEmpleado = Integer.parseInt(txtIngresarCodigoEmpleado.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Ingrese el codigo empleado correctamente");
                return;
            }

            String sql = "CALL sp_AgregarFactura(?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDate(1, java.sql.Date.valueOf(dpIngresarFechaEmision.getValue()));
            stmt.setString(2, txtIngresarTotal.getText());
            stmt.setString(3, cmbMetodoPago.getValue());
            stmt.setString(4, txtIngresarCodigoCliente.getText());
            stmt.setString(5, txtIngresarCodigoEmpleado.getText());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarAlerta("Factura agregada correctamente.");
            } else {
                mostrarAlerta("No se pudo agregar la Factura.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("Uno de los dos codigos no existe.");
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().toLowerCase().contains("fk")) {
                mostrarAlerta("Error: Uno de los dos codigos esta mal no existe.");
            } else {
                mostrarAlerta("Error al agregar la factura: " + e.getMessage());
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al agregar la factura: " + e.getMessage());
        }
        dpIngresarFechaEmision.setValue(null);
        txtIngresarTotal.clear();
        cmbMetodoPago.setValue(null);
        txtIngresarCodigoCliente.clear();
        txtIngresarCodigoEmpleado.clear();
    }

    @FXML
    private void onModificarFactura(ActionEvent event) {

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            if (txtModificarCodigoFactura.getText().isEmpty()
                    || dpModificarFechaEmision == null
                    || txtModificarTotal.getText().isEmpty()
                    || cmbMetodoPago == null
                    || txtModificarCodigoCliente.getText().isEmpty()
                    || txtModificarCodigoEmpleado.getText().isEmpty()) {
                mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
                return;
            }
            int codFactura;
            try {
                codFactura = Integer.parseInt(txtModificarCodigoFactura.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Ingrese el codigo factura correctamente");
                return;
            }
            int codCliente;
            try {
                codCliente = Integer.parseInt(txtModificarCodigoCliente.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Ingrese el codigo cliente correctamente");
                return;
            }

            int codEmpleado;
            try {
                codEmpleado = Integer.parseInt(txtModificarCodigoEmpleado.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Ingrese el codigo empleado correctamente");
                return;
            }

            String sql = "CALL sp_EditarFactura(?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, Integer.parseInt(txtModificarCodigoFactura.getText()));
            stmt.setDate(2, java.sql.Date.valueOf(dpModificarFechaEmision.getValue()));
            stmt.setString(3, txtModificarTotal.getText());
            stmt.setString(4, cmbMetodoPago.getValue());
            stmt.setString(5, txtModificarCodigoCliente.getText());
            stmt.setString(6, txtModificarCodigoEmpleado.getText());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                mostrarAlerta("Factura modificada correctamente.");
            } else {
                mostrarAlerta("No se pudo modificar la factura.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarAlerta("Uno de los dos codigos no existe.");
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().toLowerCase().contains("fk")) {
                mostrarAlerta("Error: Uno de los dos codigos esta mal no existe.");
            } else {
                mostrarAlerta("Error al modificar la factura: " + e.getMessage());
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al modificar la factura: " + e.getMessage());
        }

        txtModificarCodigoFactura.clear();
        dpModificarFechaEmision.setValue(null);
        txtModificarTotal.clear();
        cmbMetodoPago.setValue(null);
        txtModificarCodigoCliente.clear();
        txtModificarCodigoEmpleado.clear();
    }

    @FXML
    private void onEliminarFactura(ActionEvent event) {

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            int id = Integer.parseInt(txtEliminarFactura.getText());

            PreparedStatement stmt = conn.prepareStatement("CALL sp_EliminarFactura(?);");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int contador = rs.getInt("filasEliminadas");

                if (contador > 0) {
                    mostrarAlerta("Factura eliminada correctamente.");
                } else {
                    mostrarAlerta("No se pudo eliminar la Factura.");
                }
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id de la factura a Eliminar.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtEliminarFactura.clear();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
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
    private void irBuscarFactura(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/BuscarFacturaView.fxml"));
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
    private void irImprimirFactura(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/GenerarFacturaView.fxml"));
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
    private void irAgregarFactura(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/AgregarFacturaView.fxml"));
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
    private void irListarFactura(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ListarFacturaView.fxml"));
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
    private void irModificarFactura(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ModificarFacturaView.fxml"));
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
    private void irEliminarFactura(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/EliminarFacturaView.fxml"));
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
    private void irMenuFactura(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuFacturaView.fxml"));
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
    private void irMenuFacturaAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuFacturaAdminView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("JCruz-2021644");
        stage.show();
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));

        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    //Reportes 
    @FXML
    private void buscarImprimirFactura(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(" call sp_FacturaAtencion(?);");
            int codigo = Integer.parseInt(txtImprimir.getText());
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                mostrarAlerta("Factura encontrada.");
                encontrada = true;
                codFacturaB = codigo;
            } else {
                mostrarAlerta("Factura no encontrada.");
                encontrada = false;
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("El código debe ser un número válido.");
        } catch (SQLException e) {
            mostrarAlerta("Error al buscar factura.");
            e.printStackTrace();
        }
    }

    @FXML
    public void generarReporteBuscado() {
        try {
            // Puedes mostrar un mensaje si quieres
            JOptionPane.showMessageDialog(null, "Generando reporte...");
            imprimirReporteBuscado();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void imprimirReporteBuscado() {
        Map<String, Object> parametros = new HashMap<>();

        try {
            // Cargar la imagen como BufferedImage
            InputStream hoja = getClass().getResourceAsStream("/org/jefrycruz/image/Hoja_membretada.jpg");
            if (hoja == null) {
                System.out.println("No se encontró la imagen del logo en /org/jefrycruz/image/Hoja_membretada.jpg");
                parametros.put("HojaMembretada", null);
            } else {
                BufferedImage hojaImagen = ImageIO.read(hoja);
                parametros.put("HojaMembretada", hojaImagen);
                hoja.close();
            }

            // Parámetro del código de factura
            parametros.put("codigoFactura", String.valueOf(codFacturaB));

            GenerarReporte.mostrarReporte(
                    "/org/jefrycruz/report/FacturaAtendida.jasper",
                    "Reporte Factura",
                    parametros
            );
        } catch (Exception e) {
            System.out.println("Error al generar el reporte: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
    public void imprimirReporteBuscado() {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("HojaMembretada", "/org/jefrycruz/image/Hoja_membretada.jpg");
        try {
            InputStream hoja = getClass().getResourceAsStream("/org/jefrycruz/image/Hoja_membretada.jpg");
            if (hoja == null) {
                System.out.println("No se encontró la imagen del logo en /org/jefrycruz/image/Hoja_membretada.jpg");
            } else {
                BufferedImage hojaImagen = ImageIO.read(hoja);
                parametros.put("HojaMembretada", hojaImagen);
            }

            // Asegúrate de que codFacturaB haya sido asignado correctamente antes
            parametros.put("codigoFactura", codFacturaB);

            GenerarReporte.mostrarReporte(
                    "/org/jefrycruz/report/FacturaAtendida.jasper",
                    "Reporte Factura",
                    parametros
            );

        } catch (Exception e) {
            System.out.println("Error al generar el reporte: " + e.getMessage());
            e.printStackTrace();
        }
    }
     */
    @FXML
    public void generarReporte() {
        Facturas facturaSeleccionada = tblFactura.getSelectionModel().getSelectedItem();
        if (facturaSeleccionada != null) {
            int factura = facturaSeleccionada.getCodigoFactura();
            imprimirReporte(factura);
            return;
        }
        JOptionPane.showMessageDialog(null, "Su reporte está listo");
    }

    public void imprimirReporte(int codFacturaB) {
        Map parametros = new HashMap();
        parametros.put("_codigoFactura", codFacturaB);

        try {
            if (!parametros.containsKey("SubReporteEmpleadoParaFactura")) {
                InputStream SubReporteEmpleado = GenerarReporte.class.getResourceAsStream("/org/jefrycruz/report/SubReporteEmpleadoParaFactura.jasper");
                if (SubReporteEmpleado == null) {
                    System.out.println("No se encontro la SubReporte para el reporte Factura, direccion actual: /org/jefrycruz/report/SubReporteEmpleadoParaFactura.jasper");
                }
                parametros.put("SubReporteEmpleado", SubReporteEmpleado);
            }

            if (!parametros.containsKey("SubReporteClienteParaFactura")) {
                InputStream SubReporteCliente = GenerarReporte.class.getResourceAsStream("/org/jefrycruz/report/SubReporteClienteParaFactura.jasper");
                if (SubReporteCliente == null) {
                    System.out.println("No se encontro la SubReporte para el reporte Factura, direccion actual: /org/jefrycruz/report/SubReporteClienteParaFactura.jasper");
                }
                parametros.put("SubReporteCliente", SubReporteCliente);
            }
            if (!parametros.containsKey("HojaMembretada")) {
                InputStream hoja = getClass().getResourceAsStream("/org/jefrycruz/image/Hoja_membretada.jpg");
                if (hoja == null) {
                    System.out.println("No se encontró la imagen del logo en /org/jefrycruz/image/Hoja_membretada.jpg");
                } else {
                    // Convertir InputStream a BufferedImage (java.awt.Image)
                    BufferedImage hojaImagen = ImageIO.read(hoja);
                    parametros.put("HojaMembretada", hojaImagen);
                }
            }

            GenerarReporte.mostrarReporte(
                    "/org/jefrycruz/report/ReporteFacturaConSubReportes.jasper",
                    "Reporte Factura",
                    parametros
            );

        } catch (Exception e) {
            System.out.println("Error al generar el reporte: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
