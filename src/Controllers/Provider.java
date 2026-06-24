/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

/**
 *
 * @author joseh
 */
public class Provider {
        @FXML
    Button saveButton;
    @FXML
    Button deleteButton;

    @FXML
    private void init(){ 
        // TODO: Cargar datos para los select (ComboBox) 
    }
    
    @FXML
    private void saveProvider() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Proveedor registrado");
        alert.setContentText("¡Se ha registrado el proveedor!");
        alert.showAndWait();
    }

    @FXML
    private void deleteProvider() {
        Alert finalMessage = new Alert(Alert.AlertType.INFORMATION);
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Código si presionó OK
            finalMessage.setTitle("Proveedor Eliminado");
            finalMessage.setContentText("¡Se ha Eliminado el proveedor!");
            finalMessage.showAndWait();
        } else {
            // Código si canceló o cerró
            finalMessage.setTitle("Proveedor no eliminado");
            finalMessage.setContentText("¡No se ha eliminado el proveedor!");
            finalMessage.showAndWait();
        }
    }
}
