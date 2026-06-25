package Dao;

import Config.DBConn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import Models.Producto;

public class InventarioDAO {
    public boolean guardar(Producto producto) {
        String sql = "INSERT INTO inventario (codigo, nombre, categoria, cantidad, precio_unitario) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getCodigo());
            pstmt.setString(2, producto.getNombre());
            pstmt.setString(3, producto.getCategoria());
            pstmt.setInt(4, producto.getCantidad());
            pstmt.setDouble(5, producto.getPrecioUnitario());

            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean modificar(Producto producto) {
        String sql = "UPDATE inventario SET codigo=?, nombre=?, categoria=?, cantidad=?, precio_unitario=? WHERE id=?";
        try (Connection conn = DBConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getCodigo());
            pstmt.setString(2, producto.getNombre());
            pstmt.setString(3, producto.getCategoria());
            pstmt.setInt(4, producto.getCantidad());
            pstmt.setDouble(5, producto.getPrecioUnitario());
            pstmt.setInt(6, producto.getId());

            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Error al modificar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM inventario WHERE id=?";
        try (Connection conn = DBConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }
}
