package controllers;

import Dao.InventarioDAO;
import Models.Producto;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import javafx.scene.layout.VBox;

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
    TextField searchField;

    @FXML
    Button toggleAdvancedButton;
    @FXML
    VBox advancedFilterPanel;
    @FXML
    ComboBox<String> filterCategory;
    @FXML
    TextField filterQuantityMin;
    @FXML
    TextField filterQuantityMax;
    @FXML
    TextField filterPriceMin;
    @FXML
    TextField filterPriceMax;

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
    private FilteredList<Producto> filteredProductList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializar ComboBox
        invCategory.setItems(FXCollections.observableArrayList("Viveres en General", "Carne", "Pollo", "Pasta", "Arroz"));
        filterCategory.setItems(invCategory.getItems());

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

        filteredProductList = new FilteredList<>(productList, p -> true);
        InventoryTable.setItems(filteredProductList);

        // Live search + filtro avanzado en tiempo real
        searchField.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltros());
        filterCategory.valueProperty().addListener((obs, oldValue, newValue) -> aplicarFiltros());
        filterQuantityMin.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltros());
        filterQuantityMax.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltros());
        filterPriceMin.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltros());
        filterPriceMax.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltros());
    }

    @FXML
    private void toggleFiltrosAvanzados() {
        boolean mostrar = !advancedFilterPanel.isVisible();
        advancedFilterPanel.setVisible(mostrar);
        advancedFilterPanel.setManaged(mostrar);
        toggleAdvancedButton.setText(mostrar ? "Filtros avanzados ▴" : "Filtros avanzados ▾");
    }

    @FXML
    private void limpiarFiltrosAvanzados() {
        filterCategory.setValue(null);
        filterQuantityMin.clear();
        filterQuantityMax.clear();
        filterPriceMin.clear();
        filterPriceMax.clear();
    }

    // Mantiene el comportamiento del botón "Buscar" por si el equipo prefiere no depender solo del live search.
    @FXML
    private void buscarProducto() {
        aplicarFiltros();
    }

    // Combina texto libre + categoría + rango de cantidad + rango de precio en UN SOLO predicado.
    private void aplicarFiltros() {
        String textoFiltro = searchField.getText();
        String textoLower = (textoFiltro == null) ? "" : textoFiltro.trim().toLowerCase();

        String categoriaFiltro = filterCategory.getValue();
        String categoriaLower = (categoriaFiltro == null) ? "" : categoriaFiltro.trim().toLowerCase();

        Double cantidadMin = parseNumeroSeguro(filterQuantityMin.getText());
        Double cantidadMax = parseNumeroSeguro(filterQuantityMax.getText());
        Double precioMin = parseNumeroSeguro(filterPriceMin.getText());
        Double precioMax = parseNumeroSeguro(filterPriceMax.getText());

        filteredProductList.setPredicate(producto -> {
            // Criterio 1: código o nombre (texto libre)
            boolean coincideTexto = textoLower.isEmpty()
                    || (producto.getCodigo() != null && producto.getCodigo().toLowerCase().contains(textoLower))
                    || (producto.getNombre() != null && producto.getNombre().toLowerCase().contains(textoLower));

            // Criterio 2: categoría
            boolean coincideCategoria = categoriaLower.isEmpty()
                    || (producto.getCategoria() != null && producto.getCategoria().toLowerCase().contains(categoriaLower));

            // Criterio 3: rango de cantidad
            int cantidadProducto = producto.getCantidad();
            boolean coincideCantidadMin = (cantidadMin == null) || cantidadProducto >= cantidadMin;
            boolean coincideCantidadMax = (cantidadMax == null) || cantidadProducto <= cantidadMax;

            // Criterio 4: rango de precio
            double precioProducto = producto.getPrecioUnitario();
            boolean coincidePrecioMin = (precioMin == null) || precioProducto >= precioMin;
            boolean coincidePrecioMax = (precioMax == null) || precioProducto <= precioMax;

            return coincideTexto && coincideCategoria && coincideCantidadMin && coincideCantidadMax
                    && coincidePrecioMin && coincidePrecioMax;
        });
    }

    // Si el usuario escribe algo no numérico en los campos de rango, se ignora ese criterio (no rompe el filtro).
    private Double parseNumeroSeguro(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(texto.trim());
        } catch (NumberFormatException e) {
            return null;
        }
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
            alert.setContentText(currentProductId == 0 ? "¡Se ha registrado el producto exitosamente!" : "¡Se ha actualizado el producto exitosamente!");
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
}