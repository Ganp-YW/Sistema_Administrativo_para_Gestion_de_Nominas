/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import Config.DBConn;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;

/**
 *
 * @author FABIOPRO
 */
public class Client {

    int id;
    String name;
    String document;
    String phoneNum;
    String rif;
    String empresa;
    String typeCharge;
    String preferedproducts;
    String lastPurchase;
    String addedDate;

    public Client(int id, String name, String document, String phoneNum, String rif, String empresa, String typeCharge, String preferedproducts, String lastPurchase, String addedDate) {
        setCodigo(id);
        setName(name);
        setDocument(document);
        setPhoneNum(phoneNum);
        setRif(rif);
        setEmpresa(empresa);
        setTypeCharge(typeCharge);
        setPreferedproducts(preferedproducts);
        setLastPurchase(lastPurchase);
        setAddedDate(addedDate);
    }

    //setters
    public void setCodigo(int id) {
        this.id = id;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setRif(String rif) {
        this.rif = rif;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public void setTypeCharge(String typeCharge) {
        this.typeCharge = typeCharge;
    }

    public void setPreferedproducts(String preferedproducts) {
        this.preferedproducts = preferedproducts;
    }

    private void setLastPurchase(String lastPurchase) {
        this.lastPurchase = lastPurchase;
    }

    private void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    //getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRif() {
        return rif;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getPreferedproducts() {
        return preferedproducts;
    }

    public String getEmpresa() {
        return empresa;
    }

    public String getDocument() {
        return document;
    }

    public String getTypeCharge() {
        return typeCharge;
    }

    public String getLastPurchase() {
        return lastPurchase;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public static ObservableList<Client> fillClientList(ObservableList<Client> list) {
        try (Connection conn = DBConn.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet clients = st.executeQuery("SELECT * FROM public.clientes");

            while (clients.next()) {
                int id = Integer.parseInt(clients.getString(1));

                list.add(new Client(
                        id,
                        clients.getString(2), // nombre
                        clients.getString(3), // cedula
                        clients.getString(4), // telefono
                        clients.getString(5), // rif
                        clients.getString(6), // empresa
                        clients.getString(7), // tipo_cobranza
                        clients.getString(8), // inventario_preferido
                        "N/A",                // lastPurchase (no está en la tabla)
                        clients.getString(9)  // fecha_registro
                ));
            }
            return list;
        } catch (SQLException e) {
            System.err.println("Error obteniendo clientes: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
        return list;
    }
}
