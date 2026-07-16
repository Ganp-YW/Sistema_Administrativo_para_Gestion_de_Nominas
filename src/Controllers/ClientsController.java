/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import Models.Client;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import Models.Client;
import Dao.ClienteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author joseh
 */
public class ClientsController {

    private int currentClientId = 0;

    @FXML
    Button saveButton;
    @FXML
    Button deleteButton;

    @FXML
    TextField clientName;
    @FXML
    TextField clientCI;
    @FXML
    TextField clientPhone;
    @FXML
    TextField clientRif;
    @FXML
    TextField clientBusiness;

    @FXML
    ComboBox<String> clientPayType;
    @FXML
    ComboBox<String> clientPrefItem;

    // Table
    @FXML
    private TableView<Client> ClientTable;
    @FXML
    private TableColumn<Client, Integer> ColId;
    @FXML
    private TableColumn<Client, String> ColName;
    @FXML
    private TableColumn<Client, String> ColCI;
    @FXML
    private TableColumn<Client, String> ColTelf;
    @FXML
    private TableColumn<Client, String> ColRif;
    @FXML
    private TableColumn<Client, String> ColBusiness;
    @FXML
    private TableColumn<Client, String> ColPrefItem;
    @FXML
    private TableColumn<Client, String> ColPayType;
    @FXML
    private TableColumn<Client, String> ColLastBuy;
    @FXML
    private TableColumn<Client, String> ColAddedDate;
    @FXML
    private ObservableList<Client> clientList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Cargar datos para los select (ComboBox)
        clientPayType.setItems(
                FXCollections.observableArrayList("Efectivo", "Transferencia", "Pago Movil", "Crédito", "Divisas",
                        "Cheque", "Otro"));

        javafx.collections.ObservableList<Models.Producto> productos = javafx.collections.FXCollections
                .observableArrayList();
        Models.Producto.fillInventoryList(productos);
        javafx.collections.ObservableList<String> nombresProductos = javafx.collections.FXCollections
                .observableArrayList();
        for (Models.Producto p : productos) {
            nombresProductos.add(p.getNombre());
        }
        clientPrefItem.setItems(nombresProductos);

        ColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ColCI.setCellValueFactory(new PropertyValueFactory<>("document")); // Atributo se llama document
        ColTelf.setCellValueFactory(new PropertyValueFactory<>("phoneNum")); // Atributo se llama phoneNum
        ColRif.setCellValueFactory(new PropertyValueFactory<>("rif"));
        ColBusiness.setCellValueFactory(new PropertyValueFactory<>("empresa")); // Atributo se llama empresa
        ColPrefItem.setCellValueFactory(new PropertyValueFactory<>("preferedproducts"));
        ColPayType.setCellValueFactory(new PropertyValueFactory<>("typeCharge"));
        ColLastBuy.setCellValueFactory(new PropertyValueFactory<>("lastPurchase"));
        ColAddedDate.setCellValueFactory(new PropertyValueFactory<>("addedDate"));

        // Listener para selección de tabla
        ClientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                currentClientId = newSelection.getId();
                clientName.setText(newSelection.getName());
                clientCI.setText(newSelection.getDocument());
                clientPhone.setText(newSelection.getPhoneNum());
                clientRif.setText(newSelection.getRif());
                clientBusiness.setText(newSelection.getEmpresa());
                clientPayType.setValue(newSelection.getTypeCharge());
                clientPrefItem.setValue(newSelection.getPreferedproducts());
                saveButton.setText("Actualizar");
            }
        });

        clientList = Client.fillClientList(clientList);

        ClientTable.setItems(clientList);
    }

    @FXML
    private void saveClient() {
        // 1. Leer los datos de la vista
        String nom = clientName.getText();
        String ced = clientCI.getText();
        String tel = clientPhone.getText();
        String rif = clientRif.getText();
        String emp = clientBusiness.getText();
        String tipoCob = clientPayType.getValue(); // Si es un ComboBox
        String invPref = clientPrefItem.getValue();
        // 2. Crear el objeto con tu Modelo
        Client nuevoCliente = new Client(currentClientId, nom, ced, tel, rif, emp, tipoCob, invPref, "", "");
        // 3. Pasarlo al DAO para que haga la magia SQL
        ClienteDAO dao = new ClienteDAO();

        boolean res = false;
        if (currentClientId == 0) {
            res = dao.guardar(nuevoCliente);
        } else {
            res = dao.modificar(nuevoCliente);
        }

        if (res) {
            System.out.println("¡Cliente procesado correctamente!");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cliente procesado");
            alert.setContentText(currentClientId == 0 ? "¡Se ha registrado el cliente exitosamente!"
                    : "¡Se ha actualizado el cliente exitosamente!");
            alert.showAndWait();

            // Limpiar las cajas de texto
            clientName.clear();
            clientCI.clear();
            clientPhone.clear();
            clientRif.clear();
            clientBusiness.clear();
            clientPayType.setValue(null);
            clientPrefItem.setValue(null);

            currentClientId = 0;
            saveButton.setText("Guardar");
            ClientTable.getSelectionModel().clearSelection();

            // Actualizar la tabla
            clientList.clear();
            Client.fillClientList(clientList);
        } else {
            System.out.println("Error guardando el cliente.");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Hubo un error al intentar guardar el cliente en la base de datos.");
            alert.showAndWait();
        }
    }

    @FXML
    private void deleteClient() {
        Client selected = ClientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ningún cliente seleccionado");
            alert.setContentText("Por favor selecciona un cliente de la tabla para eliminar.");
            alert.showAndWait();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmar eliminación");
        confirmation.setHeaderText("Eliminar cliente");
        confirmation.setContentText("¿Estás seguro de que quieres eliminar al cliente " + selected.getName() + "?");

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            ClienteDAO dao = new ClienteDAO();
            if (dao.eliminar(selected.getId())) {
                Alert finalMessage = new Alert(Alert.AlertType.INFORMATION);
                finalMessage.setTitle("Cliente Eliminado");
                finalMessage.setContentText("¡Se ha Eliminado el cliente exitosamente!");
                finalMessage.showAndWait();

                // Actualizar tabla
                clientList.clear();
                Client.fillClientList(clientList);

                // Limpiar formulario si el cliente borrado estaba seleccionado en el form
                if (currentClientId == selected.getId()) {
                    clientName.clear();
                    clientCI.clear();
                    clientPhone.clear();
                    clientRif.clear();
                    clientBusiness.clear();
                    clientPayType.setValue(null);
                    clientPrefItem.setValue(null);
                    currentClientId = 0;
                    saveButton.setText("Guardar");
                }
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setContentText("Hubo un problema al intentar eliminar el cliente.");
                error.showAndWait();
            }
        }
    }
}
