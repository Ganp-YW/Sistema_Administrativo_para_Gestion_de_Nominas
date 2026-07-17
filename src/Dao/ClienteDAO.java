/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Config.DBConn;
import Models.Client;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ClienteDAO {
    // Metodo guardar
    public boolean guardar(Client cliente) {
        String sql = "INSERT INTO clientes (nombre, cedula, telefono, rif, empresa, tipo_cobranza, inventario_preferido) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConn.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getName());
            pstmt.setString(2, cliente.getDocument());
            pstmt.setString(3, cliente.getPhoneNum());
            pstmt.setString(4, cliente.getRif());
            pstmt.setString(5, cliente.getEmpresa());
            pstmt.setString(6, cliente.getTypeCharge());
            pstmt.setString(7, cliente.getPreferedproducts());

            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar cliente: " + e.getMessage());
            return false;
        }
    }

    // Metodo modificar
    public boolean modificar(Client cliente) {
        String sql = "UPDATE clientes SET nombre=?, cedula=?, telefono=?, rif=?, empresa=?, tipo_cobranza=?, inventario_preferido=? WHERE id=?";
        try (Connection conn = DBConn.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getName());
            pstmt.setString(2, cliente.getDocument());
            pstmt.setString(3, cliente.getPhoneNum());
            pstmt.setString(4, cliente.getRif());
            pstmt.setString(5, cliente.getEmpresa());
            pstmt.setString(6, cliente.getTypeCharge());
            pstmt.setString(7, cliente.getPreferedproducts());
            pstmt.setInt(8, cliente.getId());

            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Error al modificar cliente: " + e.getMessage());
            return false;
        }
    }

    // Metodo eliminar
    public boolean eliminar(int id) {
        String sql = "DELETE FROM clientes WHERE id=?";
        try (Connection conn = DBConn.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }
}
