package controllers;

import Dao.InventarioDAO;
import Models.Producto;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class InventoryController implements Initializable {

    private int currentProductId = 0;

    @FXML
    Button saveButton;
    @FXML
    Button deleteButton;

    @FXML
    TextField invCode;
    @FXML
    TextField invName;
    @FXML
    ComboBox<String> invCategory;
    @FXML
    TextField invQuantity;
    @FXML
    TextField invPrice;

    @FXML
    TableView<Producto> InventoryTable;
    @FXML
    TableColumn<Producto, Integer> ColId;
    @FXML
    TableColumn<Producto, String> ColCode;
    @FXML
    TableColumn<Producto, String> ColName;
    @FXML
    TableColumn<Producto, String> ColCategory;
    @FXML
    TableColumn<Producto, Integer> ColQuantity;
    @FXML
    TableColumn<Producto, Double> ColPrice;
    @FXML
    TableColumn<Producto, String> ColDate;

    ObservableList<Producto> productList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializar ComboBox
        invCategory.setItems(FXCollections.observableArrayList("Viveres en General", "Carne", "Pollo", "Pasta", "Arroz",
                "Lacteos", "Otro"));

        // Inicializar Columnas
        ColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColCode.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        ColName.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        ColCategory.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        ColQuantity.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        ColPrice.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        ColDate.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));

        // Listener para selección de tabla
        InventoryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                currentProductId = newSelection.getId();
                invCode.setText(newSelection.getCodigo());
                invName.setText(newSelection.getNombre());
                invCategory.setValue(newSelection.getCategoria());
                invQuantity.setText(String.valueOf(newSelection.getCantidad()));
                invPrice.setText(String.valueOf(newSelection.getPrecioUnitario()));
                saveButton.setText("Actualizar");
            }
        });

        productList = Producto.fillInventoryList(productList);
        InventoryTable.setItems(productList);
    }

    @FXML
    private void saveProduct() {
        String code = invCode.getText();
        String nom = invName.getText();
        String cat = invCategory.getValue();

        int cant = 0;
        double precio = 0;

        try {
            cant = Integer.parseInt(invQuantity.getText());
            precio = Double.parseDouble(invPrice.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Datos inválidos");
            alert.setContentText("La cantidad debe ser entera y el precio debe ser un número válido.");
            alert.showAndWait();
            return;
        }

        Producto nuevoProducto = new Producto(currentProductId, code, nom, cat, cant, precio, "");
        InventarioDAO dao = new InventarioDAO();

        boolean res = false;
        if (currentProductId == 0) {
            res = dao.guardar(nuevoProducto);
        } else {
            res = dao.modificar(nuevoProducto);
        }

        if (res) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Producto procesado");
            alert.setContentText(currentProductId == 0 ? "¡Se ha registrado el producto exitosamente!"
                    : "¡Se ha actualizado el producto exitosamente!");
            alert.showAndWait();

            // Limpiar las cajas de texto
            invCode.clear();
            invName.clear();
            invCategory.setValue(null);
            invQuantity.clear();
            invPrice.clear();

            currentProductId = 0;
            saveButton.setText("Guardar");
            InventoryTable.getSelectionModel().clearSelection();

            // Actualizar la tabla
            productList.clear();
            Producto.fillInventoryList(productList);
            ViewManager.clearCache();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Hubo un problema procesando el producto.");
            alert.showAndWait();
        }
    }

    @FXML
    private void deleteProduct() {
        Producto selected = InventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ningún producto seleccionado");
            alert.setContentText("Por favor selecciona un producto de la tabla para eliminar.");
            alert.showAndWait();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmar eliminación");
        confirmation.setHeaderText("Eliminar producto");
        confirmation.setContentText("¿Estás seguro de que quieres eliminar el producto " + selected.getNombre() + "?");

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            InventarioDAO dao = new InventarioDAO();
            if (dao.eliminar(selected.getId())) {
                Alert finalMessage = new Alert(Alert.AlertType.INFORMATION);
                finalMessage.setTitle("Producto Eliminado");
                finalMessage.setContentText("¡Se ha Eliminado el producto exitosamente!");
                finalMessage.showAndWait();

                // Actualizar tabla
                productList.clear();
                Producto.fillInventoryList(productList);
                ViewManager.clearCache();

                // Limpiar formulario si el producto borrado estaba seleccionado
                if (currentProductId == selected.getId()) {
                    invCode.clear();
                    invName.clear();
                    invCategory.setValue(null);
                    invQuantity.clear();
                    invPrice.clear();
                    currentProductId = 0;
                    saveButton.setText("Guardar");
                }
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setContentText("Hubo un problema al intentar eliminar el producto.");
                error.showAndWait();
            }
        }
    }

    @FXML
    private void buscarProducto() {
        // Implementar búsqueda
    }

    @FXML
    private void toggleFiltrosAvanzados() {
        // Implementar visibilidad de filtros
    }

    @FXML
    private void limpiarFiltrosAvanzados() {
        // Implementar limpieza de filtros
    }
}
