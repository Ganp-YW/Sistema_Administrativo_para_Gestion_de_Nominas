/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import Config.DBConn;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.awt.Desktop;
import java.net.URI;
import javafx.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 *
 * @author joseh
 */
public class PrincipalView {

    @FXML
    private StackPane tabContainer;
    @FXML
    private Label lblInfo;
    @FXML
    private Label lblTotalProveedores;
    @FXML
    private Label lblTotalEmpleados;
    @FXML
    private Label lblTotalClientes;
    @FXML
    private VBox cardProveedores;
    @FXML
    private VBox cardEmpleados;
    @FXML
    private VBox cardClientes;

    private javafx.scene.Node defaultContent;

    // Se ejecuta automáticamente al cargar el FXML
    @FXML
    public void initialize() {

        ViewManager.init(tabContainer); // Inicializar ViewManager con el contenedor
        if (!tabContainer.getChildren().isEmpty()) {
            defaultContent = tabContainer.getChildren().get(0);
        }
        configurarEfectosHover();
        cargarResumen();

    }

    // Metodo configurarEfectosHover
    private void configurarEfectosHover() {
        agregarEfectoZoom(cardProveedores);
        agregarEfectoZoom(cardEmpleados);
        agregarEfectoZoom(cardClientes);
    }

    /**
     * Helper que aplica la transición de escala a un contenedor VBox.
     */
    private void agregarEfectoZoom(VBox tarjeta) {
        if (tarjeta == null) {
            return;
        }

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setRadius(10);
        shadow.setSpread(0.2);

        // Al entrar el mouse: se agranda un 5% (1.05) de forma fluida
        tarjeta.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), tarjeta);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();

            tarjeta.setEffect(shadow);
        });

        // Al salir el mouse: regresa a su tamaño original (1.0)
        tarjeta.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), tarjeta);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();

            tarjeta.setEffect(null);
        });
    }

    /**
     * Realiza un conteo progresivo animado en un Label desde 0 hasta el valor
     * final.
     */
    private void animarConteo(Label label, int valorFinal) {
        if (valorFinal <= 0) {
            label.setText("0");
            return;
        }

        Timeline timeline = new Timeline();
        int duracionMs = 800; // Duración total de la animación en milisegundos
        int pasos = Math.min(valorFinal, 30); // Máximo 30 pasos para no saturar la UI con números grandes
        int intervalo = duracionMs / pasos;

        for (int i = 0; i <= pasos; i++) {
            final int valorActual = (int) ((double) valorFinal * i / pasos);
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(i * intervalo),
                    e -> label.setText(String.valueOf(valorActual)));
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.play();
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

        cargarResumen();
    }

    // Auxiliar
    private void loadTab(String TabShortened, String TabInfo) {
        // Obtener tamaño disponible del contenedor
        double width = tabContainer.getWidth();
        double height = tabContainer.getHeight();

        if (width == 0) {
            width = 800;
        }
        if (height == 0) {
            height = 600;
        }

        // Cargar vista
        ViewManager.loadView(TabShortened, width, height);
        lblInfo.setText(TabInfo);
    }

    /**
     * Cuenta registros de una tabla en la base de datos
     *
     * @param
     * @return
     */
    private int contarRegistros(String nombreTabla) {
        int total = 0;
        String sql = "SELECT COUNT(*) AS total FROM " + nombreTabla;

        try (Connection conn = DBConn.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error en el motor de consulta para [" + nombreTabla + "]: " + e.getMessage());
        }
        return total;
    }

    // Metodo cargarResumen
    public void cargarResumen() {
        int totalEmpleados = contarRegistros("empleados");
        int totalClientes = contarRegistros("clientes");
        int totalProveedores = contarRegistros("proveedores");

        // lblTotalEmpleados.setText(String.valueOf(totalEmpleados));
        // lblTotalClientes.setText(String.valueOf(totalClientes));
        // lblTotalProveedores.setText(String.valueOf(totalProveedores));
        animarConteo(lblTotalEmpleados, totalEmpleados);
        animarConteo(lblTotalClientes, totalClientes);
        animarConteo(lblTotalProveedores, totalProveedores);

    }
}
