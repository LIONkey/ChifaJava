package Clases;

import Conexion.Conectar;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class BuscarProductos {

    private PreparedStatement PS;
    private ResultSet RS;
    private final Conectar CN;
    private DefaultTableModel DT;
    private final String SQL_SELECT_PRODUCTOS = "SELECT pro_Codigo, pro_Descripcion, inv_stock\n"
            + "FROM tblProducto\n"
            + "INNER JOIN tblInventario ON pro_Codigo = inv_pro_codigo\n"
            + "ORDER BY pro_Codigo ASC";

    public BuscarProductos() {
        PS = null;
        CN = new Conectar();
    }

    private DefaultTableModel setTitulosProductos() {
        DT = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        DT.addColumn("Código");
        DT.addColumn("Descripción");
        DT.addColumn("Stock Actual");

        return DT;
    }

    public DefaultTableModel getDatosProductos() {
        try {
            setTitulosProductos();
            PS = CN.getConnection().prepareStatement(SQL_SELECT_PRODUCTOS);
            RS = PS.executeQuery();
            Object[] fila = new Object[4];
            while (RS.next()) {
                fila[0] = RS.getString(1);
                fila[1] = RS.getString(2);
                fila[2] = RS.getInt(3);
                DT.addRow(fila);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los datos." + e.getMessage());
        } finally {
            PS = null;
            RS = null;
            CN.desconectar();
        }
        return DT;
    }

    public DefaultTableModel getDatoP(int crt, String inf) {
        String SQL = "SELECT pro_Codigo, pro_Descripcion, inv_stock FROM tblProducto "
                + "INNER JOIN tblInventario ON pro_Codigo = inv_pro_codigo "
                + "WHERE ";

        if (crt == 2) {
            SQL += "pro_Codigo LIKE ?";
        } else {
            SQL += "pro_Descripcion LIKE ?";
        }

        try {
            setTitulosProductos();
            PS = CN.getConnection().prepareStatement(SQL);
            PS.setString(1, inf + "%");  // Agregar el '%' al valor de búsqueda
            RS = PS.executeQuery();
            Object[] fila = new Object[3];  // Reducir el tamaño del arreglo
            while (RS.next()) {
                fila[0] = RS.getString(1);
                fila[1] = RS.getString(2);
                fila[2] = RS.getInt(3);
                DT.addRow(fila);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los datos: " + e.getMessage());
        } finally {
            PS = null;
            RS = null;
            CN.desconectar();
        }
        return DT;
    }

}
