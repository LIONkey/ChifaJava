package Clases;

import Conexion.Conectar;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class Inventario {

    private PreparedStatement PS;
    private ResultSet RS;
    private final Conectar CN;
    private DefaultTableModel DT;
   private final String SQL_SELECT_INVENTARIO = "SELECT inv_pro_codigo, pro_descripcion, inv_entradas, inv_salidas, inv_stock FROM tblInventario " +
    "INNER JOIN tblProducto ON inv_pro_codigo = tblProducto.pro_Codigo ";
   

 

    public Inventario() {
        PS = null;
        CN = new Conectar();
    }

    private DefaultTableModel setTitulosInventario() {
        DT = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        DT.addColumn("Código");
        DT.addColumn("Descripción");
        DT.addColumn("Entrada");
        DT.addColumn("Salida");
        DT.addColumn("Stock");
        return DT;
    }

    public DefaultTableModel getDatosInventario() {
        try {
            setTitulosInventario();

            // Prepara la consulta SQL y obtiene los resultados
            PS = CN.getConnection().prepareStatement(SQL_SELECT_INVENTARIO);
            RS = PS.executeQuery();

            // Itera a través de los resultados y agrega filas a la tabla
            while (RS.next()) {
                Object[] fila = new Object[5];
                fila[0] = RS.getString(1);
                fila[1] = RS.getString(2);
                fila[2] = RS.getInt(3);  // Imprime la cantidad de entrada
                fila[3] = RS.getInt(4);  // Imprime la cantidad de salida
                fila[4] = RS.getInt(5);  // Imprime el stock
                DT.addRow(fila);
            }

        } catch (SQLException e) {
            // Maneja cualquier error de SQL imprimiendo un mensaje
            System.err.println("Error al listar los datos: " + e.getMessage());
        } finally {
            // Limpia recursos y desconecta la base de datos
            try {
                if (RS != null) {
                    RS.close();
                }
                if (PS != null) {
                    PS.close();
                }
            } catch (SQLException ex) {
                // Maneja errores al cerrar los recursos
                System.err.println("Error al cerrar los recursos: " + ex.getMessage());
            }
            CN.desconectar();
        }
        return DT;
    }

}
