package Models;

import Config.DBConn;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.ObservableList;

public class Empleado {

    int id;
    String nombre;
    String cedula;
    String telefono;
    String cargo;
    double salario;
    String fechaIngreso;

    public Empleado(int id, String nombre, String cedula, String telefono, String cargo, double salario, String fechaIngreso) {
        this.id = id;
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.cargo = cargo;
        this.salario = salario;
        this.fechaIngreso = fechaIngreso;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }
    public String getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(String fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public static ObservableList<Empleado> fillEmployeeList(ObservableList<Empleado> lista) {
        String sql = "SELECT * FROM empleados;";
        try (Connection conn = DBConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Empleado(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("cedula"),
                        rs.getString("telefono"),
                        rs.getString("cargo"),
                        rs.getDouble("salario_base"),
                        rs.getString("fecha_registro")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
