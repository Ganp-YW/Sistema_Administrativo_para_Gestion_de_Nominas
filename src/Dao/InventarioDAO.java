package Dao;

import Config.DBConn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import Models.Producto;

public class InventarioDAO {

    // Devuelve las categorías ya usadas (sin repetir), para poblar el ComboBox del filtro avanzado.
    public List<String> obtenerCategoriasUnicas() {
        List<String> categorias = new ArrayList<>();
        String sql = "SELECT DISTINCT categoria FROM inventario WHERE categoria IS NOT NULL ORDER BY categoria";
        try (Connection conn = DBConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                categorias.add(rs.getString("categoria"));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener categorías únicas: " + e.getMessage());
        }
        return categorias;
    }

    // Metodo para guardar un producto
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

    // Metodo para modificar un producto
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

    // Metodo para eliminar un producto
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
