/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.io.IOException;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.*;
import javafx.stage.*;
import java.awt.Desktop;
import java.net.URI;
import javafx.event.ActionEvent;

/**
 *
 * @author joseh
 */
public class PrincipalView {
    @FXML
    private StackPane tabContainer;

    @FXML
    private Label lblInfo;

    private javafx.scene.Node defaultContent;

    // Se ejecuta automáticamente al cargar el FXML
    @FXML
    public void initialize() {
        ViewManager.init(tabContainer); // Inicializar ViewManager con el contenedor
        if (!tabContainer.getChildren().isEmpty()) {
            defaultContent = tabContainer.getChildren().get(0);
        }
    }

    @FXML
    void abrirRepositorioGithub(ActionEvent event) {
        // Reemplaza esta URL con el enlace directo a tu repositorio
        String url = "https://github.com/Ganp-YW/Sistema_Administrativo_para_Gestion_de_Nominas";

        try {
            // Verifica si el sistema soporta la acción de abrir el navegador
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                System.out.println("La función de abrir el navegador no está soportada en este sistema.");
            }
        } catch (Exception e) {
            System.err.println("Error al intentar abrir el enlace: " + e.getMessage());
            // Opcional: Aquí puedes añadir un Alert de JavaFX para notificar al usuario del
            // error
        }
    }

    @FXML
    private void loadClientsTab() {
        loadTab("Clients", "Vista: Clientes");
    }

    @FXML
    private void loadProvidersTab() {
        loadTab("Provider", "Vista: Proveedores");
    }

    @FXML
    private void loadEmployeesTab() {
        loadTab("Employees", "Vista: Empleados");
    }

    @FXML
    private void loadInventoryTab() {
        loadTab("Inventory", "Vista: Inventario");
    }

    @FXML
    private void loadDefaultTab() {
        tabContainer.getChildren().clear();
        if (defaultContent != null) {
            tabContainer.getChildren().add(defaultContent);
        }
        lblInfo.setText("Listo. Sistema iniciado.");
    }

    // Auxiliar
    private void loadTab(String TabShortened, String TabInfo) {
        // Obtener tamaño disponible del contenedor
        double width = tabContainer.getWidth();
        double height = tabContainer.getHeight();

        if (width == 0)
            width = 800;
        if (height == 0)
            height = 600;

        // Cargar vista
        ViewManager.loadView(TabShortened, width, height);
        lblInfo.setText(TabInfo);
    }
}
