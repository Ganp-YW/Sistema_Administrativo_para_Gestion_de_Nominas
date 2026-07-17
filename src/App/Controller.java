/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

/**
 *
 * @author joseh
 */
import java.io.IOException;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.stage.*;

public class Controller {

    @FXML
    private Label label;
    
    @FXML
    private Button clientsTabButton;
    
    // Metodo openClientsTab
    public void openClientsTab() throws IOException{
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Se ha pulsado el boton");
        alert.setContentText("¡Entrando al tab de clientes!");
        alert.show();
        
        // 1. Cargar el nuevo FXML
        Parent ClientView = FXMLLoader.load(
            getClass().getResource("/Views/ClientsView.fxml")
        );
        
        // 2. Crear nueva ventana
        Stage nuevaVentana = new Stage();
        nuevaVentana.setTitle("Vista Clientes");
        nuevaVentana.setScene(new Scene(ClientView, 900, 600));
        
        // 3. Mostrar
        nuevaVentana.show();
        
        // Opcional: Cerrar ventana actual
         ((Stage) clientsTabButton.getScene().getWindow()).close();
    }

    // Metodo initialize
    public void initialize() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
    }
    }