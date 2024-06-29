package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Conectar {

    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String USER = "usersql";
    private static final String PASSWORD = "root";
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=dbInventario;encrypt=false;autoReconnect=true";

    private Connection conn;

    public Conectar() {
        conn = null;
    }

    public Connection getConnection() {
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error al conectar con la base de datos", JOptionPane.ERROR_MESSAGE);
            // Lanza una excepción personalizada o devuelve null en lugar de System.exit(0);
            return null;
        }
    }

    public void desconectar() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error al cerrar la conexión con la base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}
