package org.jefrycruz.controller;

import java.io.IOException;
import org.jefrycruz.models.Clientes;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javax.swing.JOptionPane;
import org.jefrycruz.db.Conexion;
import org.jefrycruz.report.GenerarReporte;

public class ClienteController {

    // Agregamos todos los elementos de los txt, btns o las columnas junto con la tabla de datos
    // tablas tbl
    // La tabla mas las columnas de solo el listar
    @FXML
    private TableView<Clientes> tblClientes;
    @FXML
    private TableColumn<Clientes, Integer> colcodigoCliente;
    @FXML
    private TableColumn<Clientes, String> colnombreCliente;
    @FXML
    private TableColumn<Clientes, String> colapellidoCliente;
    @FXML
    private TableColumn<Clientes, String> coltelefonoCliente;
    @FXML
    private TableColumn<Clientes, String> coldireccionCliente;
    @FXML
    private TableColumn<Clientes, String> colemailCliente;
    @FXML
    private TableColumn<Clientes, Date> colfechaRegistro;

    // La tabla mas la coluimnas del buscar
    @FXML
    private TextField txtBuscarClientes;
    @FXML
    private TextField txtBuscarClientesNombre;
    @FXML
    private TableView<Clientes> tblClientesBuscar;
    @FXML
    private TableColumn<Clientes, Integer> colcodigoClienteBuscar;
    @FXML
    private TableColumn<Clientes, String> colnombreClienteBuscar;
    @FXML
    private TableColumn<Clientes, String> colapellidoClienteBuscar;
    @FXML
    private TableColumn<Clientes, String> coltelefonoClienteBuscar;
    @FXML
    private TableColumn<Clientes, String> coldireccionClienteBuscar;
    @FXML
    private TableColumn<Clientes, String> colemailClienteBuscar;
    @FXML
    private TableColumn<Clientes, Date> colfechaRegistroBuscar;
    // Para el eliminar
    @FXML
    private TextField txtEliminarCliente;
    @FXML
    private TextField txtEliminarClienteNombre;

    // Para hacer el AgregarClienteView
    @FXML
    private TextField txtIngresarNombreCliente;
    @FXML
    private TextField txtIngresarApellidoCliente;
    @FXML
    private TextField txtIngresarTelefonoCliente;
    @FXML
    private TextField txtIngresarDireccionCliente;
    @FXML
    private TextField txtIngresarEmailCliente;
    @FXML
    private DatePicker dpFechaRegistroCliente;

    // para hacer el modificar
    @FXML
    private TextField txtModificarCodigoCliente;
    @FXML
    private TextField txtModificarNombreCliente;
    @FXML
    private TextField txtModificarApellidoCliente;
    @FXML
    private TextField txtModificarTelefonoCliente;
    @FXML
    private TextField txtModificarDireccionCliente;
    @FXML
    private TextField txtModificarEmailCliente;
    @FXML
    private DatePicker dpModificarRegistroCliente;

    //Creamos una observablelist ya que se automatica de clientes llamada lista la cual va a ase igual que el Fxcollection la cual se vincula 
    //con la tabla que tenemos en la vista de scene builder
    private ObservableList<Clientes> lista = FXCollections.observableArrayList();

    // Lo tenemos dentro del initialize para poder inicializar elementos y poder cargar datos en la tabla de scene builder
    // Los if son para ver si los elementos de la tabla junto con los datos no esten nullos porque si lo estarian nos tiraria error del nullpointereccepition
    @FXML
    public void initialize() {

        if (tblClientes != null) {
            inicializarTablaListar();
            cargarDatos();

        }

        if (tblClientesBuscar != null) {
            inicializarTablaBuscar();
        }
    }

    //y lo meto en el inicializartablalistar porque asi puedo agregar aqui mismo los elementos qeu se seterarab en la tabla
    private void inicializarTablaListar() {
        //unimos el codigoCliente osea la columna con el atributo de la base dedatos codigoCliente el cual con el 
        //propety calues factory seteara el valor de codigo cliente en el codigoCliente de la tabla de scene builder
        colcodigoCliente.setCellValueFactory(new PropertyValueFactory<>("codigoCliente"));
        colnombreCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        colapellidoCliente.setCellValueFactory(new PropertyValueFactory<>("apellidoCliente"));
        coltelefonoCliente.setCellValueFactory(new PropertyValueFactory<>("telefonoCliente"));
        coldireccionCliente.setCellValueFactory(new PropertyValueFactory<>("direccionCliente"));
        colemailCliente.setCellValueFactory(new PropertyValueFactory<>("emailCliente"));
        colfechaRegistro.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));

    }

    private void inicializarTablaBuscar() {
        colcodigoClienteBuscar.setCellValueFactory(new PropertyValueFactory<>("codigoCliente"));
        colnombreClienteBuscar.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        colapellidoClienteBuscar.setCellValueFactory(new PropertyValueFactory<>("apellidoCliente"));
        coltelefonoClienteBuscar.setCellValueFactory(new PropertyValueFactory<>("telefonoCliente"));
        coldireccionClienteBuscar.setCellValueFactory(new PropertyValueFactory<>("direccionCliente"));
        colemailClienteBuscar.setCellValueFactory(new PropertyValueFactory<>("emailCliente"));
        colfechaRegistroBuscar.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));
    }

    private void cargarDatos() {

        // establecemso la coneccion que tiene a la base de datos para asi con el callableStatement poder llamar el sp 
        // y de ultimo ejecutamos la consulta que seria el sp y obtenesmos el resultado en el resulset
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            //pues limpia la lista que seria el observable list que esta arriba
            lista.clear();
            // mandamos a llamar el procedimiento almacenado 
            String sql = "CALL sp_ListarClientes();";
            CallableStatement stmt = conn.prepareCall(sql);
            ResultSet rs = stmt.executeQuery();
            // Nos ayuda a recorrer los atributos que estan en la vase de datos para
            // para asi seterarlos 

            while (rs.next()) {
                lista.add(new Clientes(
                        rs.getInt("codigoCliente"),
                        rs.getString("nombreCliente"),
                        rs.getString("apellidoCliente"),
                        rs.getString("telefonoCliente"),
                        rs.getString("direccionCliente"),
                        rs.getString("emailCliente"),
                        rs.getDate("fechaRegistro")
                ));
            }

            // Le agregamos los items que estan dentro de la lista que es lo que acabamos de settear
            // arriba lo cual esta uardado en el lista del obserbable list
            tblClientes.setItems(lista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBuscarClientes(ActionEvent event) {
        //otra bvez creamos otra lista en la cual almacenaremos y acutalizaremos los datos 
        ObservableList<Clientes> clienteBuscado = FXCollections.observableArrayList();

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            // Hacemos la coneccion con el conectar() con base de datos y llamamos el sp
            PreparedStatement stmt = conn.prepareStatement("CALL sp_BuscarCliente(?);");
            // pues estoy realizando un casteo de lo que ingreso en el txt lo paso a int porque como int tengo el id
            stmt.setInt(1, Integer.parseInt(txtBuscarClientes.getText()));
            // Ejecuto la consulta y guardo los valores en el result tes rs 

            ResultSet rs = stmt.executeQuery();

            // Reccorre la fila del resul set 
            while (rs.next()) {
                // Crea el objeto de Clientes que es lo que se agrega a la lista de clienteBuscado 

                clienteBuscado.add(new Clientes(
                        rs.getInt("codigoCliente"),
                        rs.getString("nombreCliente"),
                        rs.getString("apellidoCliente"),
                        rs.getString("telefonoCliente"),
                        rs.getString("direccionCliente"),
                        rs.getString("emailCliente"),
                        rs.getDate("fechaRegistro")
                ));

            }
            if (clienteBuscado.isEmpty()) {
                mostrarAlerta("No se encontro ningun Cliente.");
            }
            tblClientesBuscar.setItems(clienteBuscado);
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id del cliente a bucar.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        txtBuscarClientes.clear();
    }

    @FXML
    private void onAgregarCliente(ActionEvent event) {
        // Conectamos la base de datos con el conectar() 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            if (txtIngresarNombreCliente.getText().isEmpty()
                    || txtIngresarApellidoCliente.getText().isEmpty()
                    || txtIngresarTelefonoCliente.getText().isEmpty()
                    || txtIngresarDireccionCliente.getText().isEmpty()
                    || txtIngresarEmailCliente.getText().isEmpty()
                    || dpFechaRegistroCliente == null) {
                mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
                return;
            }

            // llamamos el sp
            String sql = "CALL sp_AgregarCliente(?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // implementamos para poder agregar los valores que se escriben ene l txt 
            stmt.setString(1, txtIngresarNombreCliente.getText());
            stmt.setString(2, txtIngresarApellidoCliente.getText());
            stmt.setString(3, txtIngresarTelefonoCliente.getText());
            stmt.setString(4, txtIngresarDireccionCliente.getText());
            stmt.setString(5, txtIngresarEmailCliente.getText());
            stmt.setDate(6, java.sql.Date.valueOf(dpFechaRegistroCliente.getValue()));

            // Declaramos las filas y las ejecutamos como una consulta de update
            int filasAfectadas = stmt.executeUpdate();

            //Mensajes para decir si fue agregado o tubo un error
            if (filasAfectadas > 0) {
                // el mostrar alerta es para una libreria que nos ayuda tipo para mandar un mini ventan para que nos diga
                // que ele cliente fue agregado correctamente
                mostrarAlerta("Cliente agregado correctamente.");

            } else {
                mostrarAlerta("No se pudo agregar el cliente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al agregar cliente: " + e.getMessage());
        }
        txtIngresarNombreCliente.clear();
        txtIngresarApellidoCliente.clear();
        txtIngresarTelefonoCliente.clear();
        txtIngresarDireccionCliente.clear();
        txtIngresarEmailCliente.clear();
        dpFechaRegistroCliente.setValue(null);
    }

    @FXML
    private void onModificarCliente(ActionEvent event) {

        if (txtModificarCodigoCliente.getText().isEmpty()
                || txtModificarNombreCliente.getText().isEmpty()
                || txtModificarApellidoCliente.getText().isEmpty()
                || txtModificarTelefonoCliente.getText().isEmpty()
                || txtModificarDireccionCliente.getText().isEmpty()
                || txtModificarEmailCliente.getText().isEmpty()
                || dpModificarRegistroCliente == null) {
            mostrarAlerta("Por favor, complete todos los campos antes de continuar.");
            return;
        }

        int codCliente;
        try {
            codCliente = Integer.parseInt(txtModificarCodigoCliente.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("El codigo cliente tiene que ser un numero entero");
            return;
        }

        // Conectamos la base de datos con el conectar()
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            String sql = "CALL sp_EditarCliente(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // buscamos por el int para el id del clientes y asi editarlo
            stmt.setInt(1, Integer.parseInt(txtModificarCodigoCliente.getText()));
            stmt.setString(2, txtModificarNombreCliente.getText());
            stmt.setString(3, txtModificarApellidoCliente.getText());
            stmt.setString(4, txtModificarTelefonoCliente.getText());
            stmt.setString(5, txtModificarDireccionCliente.getText());
            stmt.setString(6, txtModificarEmailCliente.getText());
            stmt.setDate(7, java.sql.Date.valueOf(dpModificarRegistroCliente.getValue()));

            int filasAfectadas = stmt.executeUpdate();
            // igual el menssaje si se realizo bien o hay un error
            if (filasAfectadas > 0) {
                mostrarAlerta("Cliente modificado correctamente.");
            } else {
                mostrarAlerta("No se pudo modificar el cliente.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id de el Cliente a modificar.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al modificar la Cliente: " + e.getMessage());
        }

        txtModificarCodigoCliente.clear();
        txtModificarNombreCliente.clear();
        txtModificarApellidoCliente.clear();
        txtModificarTelefonoCliente.clear();
        txtModificarDireccionCliente.clear();
        txtModificarEmailCliente.clear();
        dpModificarRegistroCliente.setValue(null);
    }

    @FXML
    private void onEliminarCliente(ActionEvent event) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            int id = Integer.parseInt(txtEliminarCliente.getText());

            PreparedStatement stmt = conn.prepareStatement("CALL sp_EliminarCliente(?);");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int contador = rs.getInt("filasEliminadas");

                if (contador > 0) {
                    mostrarAlerta("Cliente eliminado correctamente.");
                } else {
                    mostrarAlerta("No se pudo eliminar el Cliente.");
                }
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente el numero de id de el Cliente a Eliminar.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtEliminarCliente.clear();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /*
    @FXML
    private void buscarClientePorNombre(ActionEvent event) {
        ObservableList<Clientes> clienteBuscado = FXCollections.observableArrayList();

        if (txtBuscarClientesNombre.getText().isEmpty()) {
            mostrarAlerta("Por favor, ingrese el nombre del cliente a buscar.");
            return;
        }
        try (Connection conn = conectar()) {
            PreparedStatement stmt = conn.prepareStatement("CALL sp_BuscarClientePorNombre(?)");
            stmt.setString(1, txtBuscarClientesNombre.getText());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                clienteBuscado.add(new Clientes(
                        rs.getInt("codigoCliente"),
                        rs.getString("nombreCliente"),
                        rs.getString("apellidoCliente"),
                        rs.getString("telefonoCliente"),
                        rs.getString("direccionCliente"),
                        rs.getString("emailCliente"),
                        rs.getDate("fechaRegistro")
                ));
            }
            if (clienteBuscado.isEmpty()) {
                mostrarAlerta("No se encontro ningun Cliente.");
            }
            TablaClientesBuscar.setItems(clienteBuscado);
        } catch (NumberFormatException e) {
            mostrarAlerta("Porfavor ingresar correctamente nombre del cliente a bucar.");
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    @FXML
    private void onEliminarClientePorNombre(ActionEvent event) {
        String nombre = txtEliminarClienteNombre.getText().trim();

        if (nombre.isEmpty()) {
            mostrarAlerta("Por favor, ingrese el nombre del cliente a eliminar.");
            return;
        }

        try (Connection conn = conectar()) {
            String sql = "CALL sp_EliminarClientePorNombre(?);";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int filasEliminadas = rs.getInt("filasEliminadas"); 

                if (filasEliminadas > 0) {
                    mostrarAlerta("Cliente eliminado correctamente.");
                } else {
                    mostrarAlerta("No se encontro ningun cliente con ese nombre.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error al eliminar cliente: " + e.getMessage());
        }
    }
     */
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
    private void irListarCliente(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ListarClientesView.fxml"));
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
    private void irEliminarCliente(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/EliminarClienteView.fxml"));
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
    private void irMenuPrincipal(ActionEvent event) throws IOException {
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
    private void irAgregarCliente(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/AgregarCliente.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Vista Cliente JCruz-2021644");
        stage.show();
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));

        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irModificarCliente(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/ModificarCliente.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Vista Cliente JCruz-2021644");
        stage.show();
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));

        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irBuscarCliente(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/BuscarClienteView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Vista Cliente JCruz-2021644");
        stage.show();
        stage.getIcons().add(new Image("/org/jefrycruz/image/LogoApp.png"));

        // muestra la siguiente escena
        ((Stage) (((javafx.scene.Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void irMenuClienteAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuClienteAdminView.fxml"));
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
    private void irMenuCliente(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jefrycruz/view/MenuClienteView.fxml"));
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

        JOptionPane.showMessageDialog(null, "Si resporte esta listo");
        imprimirCliente();
    }

    @FXML
    public void imprimirCliente() {
        Map<String, Object> parametros = new HashMap<>();
        GenerarReporte.mostrarReporte("ReporteCliente.jasper", "Reporte Cliente", parametros);
    }
}
