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

    public int id;
    public String name;
    public String document;
    public String phoneNum;
    public String rif;
    public String empresa;
    public String typeCharge;
    public String preferedproducts;
    public String lastPurchase;
    public String addedDate;

    public Client(int id, String name, String document, String phoneNum, String rif, String empresa, String typeCharge,
            String preferedproducts, String lastPurchase, String addedDate) {
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

    public Client() {
    }

    // setters
    public void setCodigo(int id) {
        this.id = id;
    }

    // Metodo setName
    public void setName(String Name) {
        this.name = Name;
    }

    // Metodo setDocument
    public void setDocument(String document) {
        this.document = document;
    }

    // Metodo setPhoneNum
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    // Metodo setRif
    public void setRif(String rif) {
        this.rif = rif;
    }

    // Metodo setEmpresa
    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    // Metodo setTypeCharge
    public void setTypeCharge(String typeCharge) {
        this.typeCharge = typeCharge;
    }

    // Metodo setPreferedproducts
    public void setPreferedproducts(String preferedproducts) {
        this.preferedproducts = preferedproducts;
    }

    // Metodo setLastPurchase
    private void setLastPurchase(String lastPurchase) {
        this.lastPurchase = lastPurchase;
    }

    // Metodo setAddedDate
    private void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    // getters
    public int getId() {
        return id;
    }

    // Metodo getName
    public String getName() {
        return name;
    }

    // Metodo getRif
    public String getRif() {
        return rif;
    }

    // Metodo getPhoneNum
    public String getPhoneNum() {
        return phoneNum;
    }

    // Metodo getPreferedproducts
    public String getPreferedproducts() {
        return preferedproducts;
    }

    // Metodo getEmpresa
    public String getEmpresa() {
        return empresa;
    }

    // Metodo getDocument
    public String getDocument() {
        return document;
    }

    // Metodo getTypeCharge
    public String getTypeCharge() {
        return typeCharge;
    }

    // Metodo getLastPurchase
    public String getLastPurchase() {
        return lastPurchase;
    }

    // Metodo getAddedDate
    public String getAddedDate() {
        return addedDate;
    }

    // Metodo fillClientList
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
                        "N/A", // lastPurchase (no está en la tabla)
                        clients.getString(9) // fecha_registro
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
