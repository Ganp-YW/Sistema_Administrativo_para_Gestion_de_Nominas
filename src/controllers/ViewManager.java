/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

/**
 *
 * @author joseh
 */
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewManager {

    // Contenedor principal donde se muestran las vistas
    private static StackPane container;

    // Cache de vistas ya cargadas
    private static Map<String, Parent> viewCache = new HashMap<>();

    // Controlador actual
    private static Object actualController;

    // Inicializar con el contenedor
    public static void init(StackPane principalContainer) {
        container = principalContainer;
    }

    // Metodo loadView
    public static void loadView(String viewName, double width, double height) {
        try {
            HBox view = getView(viewName);

            // vista.setPrefHeight(height);
            // vista.setPrefWidth(width);

            container.getChildren().clear();
            container.getChildren().add(view);
        } catch (IOException e) {
            System.err.println("Error cargando vista: " + viewName);
            e.printStackTrace();
        }
    }

    // Obtener vista de cache o cargarla
    private static HBox getView(String view) throws IOException {
        if (viewCache.containsKey(view)) {
            return (HBox) viewCache.get(view);
        }

        String routeFXML = "/Views/" + view + "View.fxml";
        FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(routeFXML));
        HBox View = loader.load();

        actualController = loader.getController();

        viewCache.put(view, View);

        return View;
    }

    // Obtener el controlador actual
    public static Object getControladorActual() {
        return actualController;
    }

    // Metodo clearCache
    public static void clearCache() {
        viewCache.clear();
    }
}