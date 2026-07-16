package Dao;

import Config.DBConn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import Models.Empleado;

public class EmpleadoDAO {
    public boolean guardar(Empleado empleado) {
        String sql = "INSERT INTO empleados (nombre, cedula, telefono, cargo, salario_base) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, empleado.getNombre());
            pstmt.setString(2, empleado.getCedula());
            pstmt.setString(3, empleado.getTelefono());
            pstmt.setString(4, empleado.getCargo());
            pstmt.setDouble(5, empleado.getSalario());

            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar empleado: " + e.getMessage());
            return false;
        }
    }

    public boolean modificar(Empleado empleado) {
        String sql = "UPDATE empleados SET nombre=?, cedula=?, telefono=?, cargo=?, salario_base=? WHERE id=?";
        try (Connection conn = DBConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, empleado.getNombre());
            pstmt.setString(2, empleado.getCedula());
            pstmt.setString(3, empleado.getTelefono());
            pstmt.setString(4, empleado.getCargo());
            pstmt.setDouble(5, empleado.getSalario());
            pstmt.setInt(6, empleado.getId());

            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Error al modificar empleado: " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminar(int id) {
        String sql = "DELETE FROM empleados WHERE id=?";
        try (Connection conn = DBConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar empleado: " + e.getMessage());
            return false;
        }
    }
}
