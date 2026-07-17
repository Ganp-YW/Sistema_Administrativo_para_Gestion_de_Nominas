package Models;

import Config.DBConn;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.ObservableList;

public class Empleado {

    public int id;
    public String nombre;
    public String cedula;
    public String telefono;
    public String cargo;
    public double salario;
    public String fechaIngreso;

    public Empleado(int id, String nombre, String cedula, String telefono, String cargo, double salario,
            String fechaIngreso) {
        this.id = id;
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.cargo = cargo;
        this.salario = salario;
        this.fechaIngreso = fechaIngreso;
    }

    public Empleado() {
    }

    // Metodo getId
    public int getId() {
        return id;
    }

    // Metodo setId
    public void setId(int id) {
        this.id = id;
    }

    // Metodo getNombre
    public String getNombre() {
        return nombre;
    }

    // Metodo setNombre
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Metodo getCedula
    public String getCedula() {
        return cedula;
    }

    // Metodo setCedula
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    // Metodo getTelefono
    public String getTelefono() {
        return telefono;
    }

    // Metodo setTelefono
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Metodo getCargo
    public String getCargo() {
        return cargo;
    }

    // Metodo setCargo
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    // Metodo getSalario
    public double getSalario() {
        return salario;
    }

    // Metodo setSalario
    public void setSalario(double salario) {
        this.salario = salario;
    }

    // Metodo getFechaIngreso
    public String getFechaIngreso() {
        return fechaIngreso;
    }

    // Metodo setFechaIngreso
    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    // Metodo fillEmployeeList
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
                        rs.getString("fecha_registro")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
