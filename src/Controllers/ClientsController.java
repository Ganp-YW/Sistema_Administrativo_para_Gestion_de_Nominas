/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.Client;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
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
        // TODO: Cargar datos para los select (ComboBox) 
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

        clientList = Client.fillClientList(clientList);

        ClientTable.setItems(clientList);
    }

    @FXML
    private void saveClient() {
        String name = clientName.getText();
        String ci = clientCI.getText();

        String payType = (String) clientPayType.getValue();
        System.out.println(payType);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cliente registrado");
        alert.setContentText("¡Se ha registrado el cliente!");
        alert.showAndWait();
    }

    @FXML
    private void deleteClient() {
        Alert finalMessage = new Alert(Alert.AlertType.INFORMATION);
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Código si presionó OK
            finalMessage.setTitle("Cliente Eliminado");
            finalMessage.setContentText("¡Se ha Eliminado el cliente!");
            finalMessage.showAndWait();
        } else {
            // Código si canceló o cerró
            finalMessage.setTitle("Cliente no eliminado");
            finalMessage.setContentText("¡No se ha eliminado el cliente!");
            finalMessage.showAndWait();
        }
    }
}
