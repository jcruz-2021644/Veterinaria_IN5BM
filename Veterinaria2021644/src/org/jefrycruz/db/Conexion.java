package org.jefrycruz.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class Conexion {

    private static Conexion instancia;
    private Connection conexion;
    private boolean credencialesInvalidas = false;
    private boolean mensajeMostrado = false;

    /*Declaramos las variables por defaul para que las verifique despues*/
    private String url = "jdbc:mysql://localhost:3306/DBVeterinaria2025?useSSL=false&allowPublicKeyRetrieval=true";
    private String user = "quintom";
    private String password = "admin";

    private Conexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = DriverManager.getConnection(url, user, password);
            credencialesInvalidas = false;
            mensajeMostrado = false;
            System.out.println("Conexion establecida correctamente.");
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el driver JDBC.");
        } catch (SQLException e) {
            if ("28000".equals(e.getSQLState()) && e.getErrorCode() == 1045) {
                System.out.println("Error: Usuario o clave de acceso de la base de datos incorrectos.");
                credencialesInvalidas = true;
            } else {
                System.out.println("Error al conectar a la base de datos.");
            }
        }
    }

    public static synchronized Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public Connection getConexion() {
        if (credencialesInvalidas) {
            if (!mensajeMostrado) {
                System.out.println("No se podrá reconectar porque las credenciales son incorrectas.");
                mensajeMostrado = true;
            }
            return null;
        }

        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(url, user, password);
                System.out.println("Conexión reestablecida correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al reestablecer la conexión: " + e.getMessage());
            return null;
        }

        return conexion;
    }

    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexion.");
        }
    }

}
