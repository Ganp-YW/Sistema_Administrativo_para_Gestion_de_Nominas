package Models;

import Config.DBConn;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.ObservableList;


public class Proveedor {

    int id;
    String name;
    String document;
    String phoneNum;
    String rif;
    String empresa;
    String typeCharge;
    String preferedproducts;
    String lastPurchase; // Could be null for provider, maybe it's not needed, but keeping for compatibility if table had it
    String addedDate;

    public Proveedor(int id, String name, String document, String phoneNum, String rif, String empresa, String typeCharge, String preferedproducts, String lastPurchase, String addedDate) {
        this.id = id;
        this.name = name;
        this.document = document;
        this.phoneNum = phoneNum;
        this.rif = rif;
        this.empresa = empresa;
        this.typeCharge = typeCharge;
        this.preferedproducts = preferedproducts;
        this.lastPurchase = lastPurchase;
        this.addedDate = addedDate;
    }
    
    public Proveedor(){}

    // getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDocument() { return document; }
    public void setDocument(String document) { this.document = document; }
    public String getPhoneNum() { return phoneNum; }
    public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum; }
    public String getRif() { return rif; }
    public void setRif(String rif) { this.rif = rif; }
    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }
    public String getTypeCharge() { return typeCharge; }
    public void setTypeCharge(String typeCharge) { this.typeCharge = typeCharge; }
    public String getPreferedproducts() { return preferedproducts; }
    public void setPreferedproducts(String preferedproducts) { this.preferedproducts = preferedproducts; }
    public String getLastPurchase() { return lastPurchase; }
    public void setLastPurchase(String lastPurchase) { this.lastPurchase = lastPurchase; }
    public String getAddedDate() { return addedDate; }
    public void setAddedDate(String addedDate) { this.addedDate = addedDate; }

    // Metodo fillProviderList
    public static ObservableList<Proveedor> fillProviderList(ObservableList<Proveedor> lista) {
        String sql = "SELECT * FROM proveedores;";
        try (Connection conn = DBConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Proveedor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("cedula"),
                        rs.getString("telefono"),
                        rs.getString("rif"),
                        rs.getString("empresa"),
                        rs.getString("tipo_cobranza"),
                        rs.getString("inventario_preferido"),
                        "N/A", // lastPurchase isn't in table, so "N/A"
                        rs.getString("fecha_registro")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
