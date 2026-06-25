package Dao;

import Config.DBConn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import Models.Proveedor;

public class ProveedorDAO {
    public boolean guardar(Proveedor proveedor) {
        String sql = "INSERT INTO proveedores (nombre, cedula, telefono, rif, empresa, tipo_cobranza, inventario_preferido) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, proveedor.getName());
            pstmt.setString(2, proveedor.getDocument());
            pstmt.setString(3, proveedor.getPhoneNum());
            pstmt.setString(4, proveedor.getRif());
            pstmt.setString(5, proveedor.getEmpresa());
            pstmt.setString(6, proveedor.getTypeCharge());
            pstmt.setString(7, proveedor.getPreferedproducts());

            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar proveedor: " + e.getMessage());
            return false;
        }
    }

    public boolean modificar(Proveedor proveedor) {
        String sql = "UPDATE proveedores SET nombre=?, cedula=?, telefono=?, rif=?, empresa=?, tipo_cobranza=?, inventario_preferido=? WHERE id=?";
        try (Connection conn = DBConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, proveedor.getName());
            pstmt.setString(2, proveedor.getDocument());
            pstmt.setString(3, proveedor.getPhoneNum());
            pstmt.setString(4, proveedor.getRif());
            pstmt.setString(5, proveedor.getEmpresa());
            pstmt.setString(6, proveedor.getTypeCharge());
            pstmt.setString(7, proveedor.getPreferedproducts());
            pstmt.setInt(8, proveedor.getId());

            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Error al modificar proveedor: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM proveedores WHERE id=?";
        try (Connection conn = DBConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar proveedor: " + e.getMessage());
            return false;
        }
    }
}
