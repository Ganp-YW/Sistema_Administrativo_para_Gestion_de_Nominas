/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import java.io.IOException;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.*;
import javafx.stage.*;
/**
 *
 * @author joseh
 */
public class PrincipalView {
        @FXML
    private StackPane tabContainer;
    
    @FXML
    private Label lblInfo;
    
    // Se ejecuta automáticamente al cargar el FXML
    @FXML
    public void initialize() {
        ViewManager.init(tabContainer); // Inicializar ViewManager con el contenedor
        
        loadClientsTab();   // Cargar primera vista por defecto
    }
    
    @FXML
    private void loadClientsTab() {
        loadTab("Clients","Vista: Clientes");
    }
    
    @FXML
    private void loadProvidersTab() {
        loadTab("Provider","Vista: Proveedores");
    }
    
    @FXML
    private void cargarTab3() {
    }
    
    
    // Auxiliar
    private void loadTab(String TabShortened, String TabInfo){
        // Obtener tamaño disponible del contenedor
        double width = tabContainer.getWidth();
        double height = tabContainer.getHeight();
        
        if (width == 0) width = 800;
        if (height == 0) height = 600;
        
        // Cargar vista
        ViewManager.loadView(TabShortened, width, height);
        lblInfo.setText(TabInfo);
    }
}
