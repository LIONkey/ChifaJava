package Clases;

import Conexion.Conectar;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Productos {

    private PreparedStatement PS;
    private ResultSet RS;
    private final Conectar CN;
    private DefaultTableModel DT;
    private final String SQL_INSERT_PRODUCTOS = "INSERT INTO tblProducto (pro_Codigo, pro_Descripcion) VALUES (?, ?)";
    private final String SQL_SELECT_PRODUCTOS = "SELECT pro_Codigo, pro_Descripcion FROM tblProducto";

    public Productos() {
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
        return DT;
    }

    public DefaultTableModel getDatosProductos() {
        try {
            setTitulosProductos();
            PS = CN.getConnection().prepareStatement(SQL_SELECT_PRODUCTOS);
            RS = PS.executeQuery();
            Object[] fila = new Object[2];
            while (RS.next()) {
                fila[0] = RS.getString(1);
                fila[1] = RS.getString(2);
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

    public int registrarProducto(String codigo, String descripcion) {
        int res = 0;
        try {
            PS = CN.getConnection().prepareStatement(SQL_INSERT_PRODUCTOS);
            PS.setString(1, codigo);
            PS.setString(2, descripcion);
            res = PS.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "Producto registrado con éxito.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo registrar el producto.");
            System.err.println("Error al registrar el producto." + e.getMessage());
        } finally {
            PS = null;
            CN.desconectar();
        }
        return res;
    }

    public void insertarProductoInventario(String codigoProducto) {
        int res;
        try {
            PS = CN.getConnection().prepareStatement("EXEC NUEVO_PRODUCTO ?");
            PS.setString(1, codigoProducto);
            res = PS.executeUpdate(); // Utiliza executeUpdate para una instrucción de inserción.
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "Producto insertado en la tabla inventario con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar registro en la tabla inventario." + e.getMessage());
        } finally {
            PS = null;
            CN.desconectar();
        }
    }

    public int verificarCodigoInventario(String codigo) {
        int res = 0;
        try {
            PS = CN.getConnection().prepareStatement("SELECT count(inv_pro_codigo) from tblInventario where inv_pro_codigo='" + codigo + "'");
            RS = PS.executeQuery();

            while (RS.next()) {
                res = RS.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error al devolver cantidad de registros." + e.getMessage());
        } finally {
            PS = null;
            CN.desconectar();
        }
        return res;
    }

    public int actualizarProducto(String codigo, String descripcion, String codigo_old) {
        String SQL = "UPDATE producto SET pro_Codigo='" + codigo + "',pro_Descripcion='" + descripcion + "' WHERE pro_Codigo='" + codigo_old + "'";
        int res = 0;
        try {
            PS = CN.getConnection().prepareStatement(SQL);
            res = PS.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "Producto actualizado con éxito");
            }
        } catch (SQLException e) {
            System.err.println("Error al modificar los datos del cliente." + e.getMessage());
        } finally {
            PS = null;
            CN.desconectar();
        }
        return res;
    }

    public int eliminarProducto(String codigo) {
        int res = 0;
        try {
            // Primero, elimina las entradas relacionadas en tblEntrada
            String SQL_DELETE_ENTRADAS = "DELETE FROM tblEntrada WHERE ProductoCodigo = ?";
            PS = CN.getConnection().prepareStatement(SQL_DELETE_ENTRADAS);
            PS.setString(1, codigo);
            int entradasEliminadas = PS.executeUpdate();

            // Ahora, elimina el producto de tblProducto
            String SQL_DELETE_PRODUCTO = "DELETE FROM tblProducto WHERE pro_Codigo = ?";
            PS = CN.getConnection().prepareStatement(SQL_DELETE_PRODUCTO);
            PS.setString(1, codigo);
            int productoEliminado = PS.executeUpdate();

            if (productoEliminado > 0) {
                JOptionPane.showMessageDialog(null, "Producto eliminado con éxito");
            }

            res = productoEliminado;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No es posible eliminar el producto.");
            System.err.println("Error al eliminar producto." + e.getMessage());
        } finally {
            PS = null;
            CN.desconectar();
        }
        return res;
    }

}
