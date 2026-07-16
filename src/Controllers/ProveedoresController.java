package controllers;

import Dao.ProveedorDAO;
import Models.Proveedor;
import java.net.URL;
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

public class ProveedoresController implements Initializable {

    private int currentProviderId = 0;

    @FXML
    Button saveButton;
    @FXML
    Button deleteButton;

    @FXML
    TextField providerName;
    @FXML
    TextField providerCI;
    @FXML
    TextField providerPhone;
    @FXML
    TextField providerRif;
    @FXML
    TextField providerBusiness;

    @FXML
    ComboBox<String> providerPayType;
    @FXML
    ComboBox<String> providerPrefItem;

    @FXML
    TextField searchField;
    @FXML
    Button toggleAdvancedButton;
    @FXML
    VBox advancedFilterPanel;
    @FXML
    ComboBox<String> filterPayType;

    @FXML
    TableView<Proveedor> ProviderTable;
    @FXML
    TableColumn<Proveedor, Integer> ColId;
    @FXML
    TableColumn<Proveedor, String> ColName;
    @FXML
    TableColumn<Proveedor, String> ColCI;
    @FXML
    TableColumn<Proveedor, String> ColTelf;
    @FXML
    TableColumn<Proveedor, String> ColRif;
    @FXML
    TableColumn<Proveedor, String> ColBusiness;
    @FXML
    TableColumn<Proveedor, String> ColPrefItem;
    @FXML
    TableColumn<Proveedor, String> ColPayType;
    @FXML
    TableColumn<Proveedor, String> ColLastBuy;
    @FXML
    TableColumn<Proveedor, String> ColAddedDate;

    ObservableList<Proveedor> providerList = FXCollections.observableArrayList();
    private FilteredList<Proveedor> filteredProviderList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializar ComboBoxes
        providerPayType.setItems(FXCollections.observableArrayList("Efectivo", "Transferencia", "Pago Mvil"));
        
        javafx.collections.ObservableList<Models.Producto> productosProv = javafx.collections.FXCollections.observableArrayList();
        Models.Producto.fillInventoryList(productosProv);
        javafx.collections.ObservableList<String> nombresProductosProv = javafx.collections.FXCollections.observableArrayList();
        for (Models.Producto p : productosProv) {
            nombresProductosProv.add(p.getNombre());
        }
        providerPrefItem.setItems(nombresProductosProv);

        // Inicializar Columnas
        ColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ColCI.setCellValueFactory(new PropertyValueFactory<>("document"));
        ColTelf.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        ColRif.setCellValueFactory(new PropertyValueFactory<>("rif"));
        ColBusiness.setCellValueFactory(new PropertyValueFactory<>("empresa"));
        ColPrefItem.setCellValueFactory(new PropertyValueFactory<>("preferedproducts"));
        ColPayType.setCellValueFactory(new PropertyValueFactory<>("typeCharge"));
        ColLastBuy.setCellValueFactory(new PropertyValueFactory<>("lastPurchase"));
        ColAddedDate.setCellValueFactory(new PropertyValueFactory<>("addedDate"));

        // Listener para selección de tabla
        ProviderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                currentProviderId = newSelection.getId();
                providerName.setText(newSelection.getName());
                providerCI.setText(newSelection.getDocument());
                providerPhone.setText(newSelection.getPhoneNum());
                providerRif.setText(newSelection.getRif());
                providerBusiness.setText(newSelection.getEmpresa());
                providerPayType.setValue(newSelection.getTypeCharge());
                providerPrefItem.setValue(newSelection.getPreferedproducts());
                saveButton.setText("Actualizar");
            }
        });

        providerList = Proveedor.fillProviderList(providerList);

        filteredProviderList = new FilteredList<>(providerList, p -> true);
        ProviderTable.setItems(filteredProviderList);

        // Mismo catálogo cerrado que usa el formulario de Proveedores (sin "Crédito", a diferencia de Clientes)
        filterPayType.setItems(FXCollections.observableArrayList("Efectivo", "Transferencia", "Pago Mvil"));

        // Live search + filtro avanzado en tiempo real
        searchField.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltros());
        filterPayType.valueProperty().addListener((obs, oldValue, newValue) -> aplicarFiltros());
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
        filterPayType.setValue(null);
    }

    // Mantiene el botón "Buscar" por compatibilidad, aunque el live search ya cubre el mismo trabajo.
    @FXML
    private void buscarProveedor() {
        aplicarFiltros();
    }

    // Combina texto libre (nombre/cédula) + tipo de cobranza en un solo predicado.
    private void aplicarFiltros() {
        String textoFiltro = searchField.getText();
        String textoLower = (textoFiltro == null) ? "" : textoFiltro.trim().toLowerCase();

        String tipoCobranzaFiltro = filterPayType.getValue();

        filteredProviderList.setPredicate(proveedor -> {
            boolean coincideTexto = textoLower.isEmpty()
                    || (proveedor.getName() != null && proveedor.getName().toLowerCase().contains(textoLower))
                    || (proveedor.getDocument() != null && proveedor.getDocument().toLowerCase().contains(textoLower));

            boolean coincideCobranza = (tipoCobranzaFiltro == null)
                    || tipoCobranzaFiltro.equals(proveedor.getTypeCharge());

            return coincideTexto && coincideCobranza;
        });
    }

    @FXML
    private void saveProvider() {
        String nom = providerName.getText();
        String ced = providerCI.getText();
        String tel = providerPhone.getText();
        String rif = providerRif.getText();
        String emp = providerBusiness.getText();
        String tipoCob = providerPayType.getValue();
        String invPref = providerPrefItem.getValue();

        Proveedor nuevoProveedor = new Proveedor(currentProviderId, nom, ced, tel, rif, emp, tipoCob, invPref, "", "");
        ProveedorDAO dao = new ProveedorDAO();
        
        boolean res = false;
        if (currentProviderId == 0) {
            res = dao.guardar(nuevoProveedor);
        } else {
            res = dao.modificar(nuevoProveedor);
        }
        
        if (res) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Proveedor procesado");
            alert.setContentText(currentProviderId == 0 ? "¡Se ha registrado el proveedor exitosamente!" : "¡Se ha actualizado el proveedor exitosamente!");
            alert.showAndWait();
            
            // Limpiar las cajas de texto
            providerName.clear();
            providerCI.clear();
            providerPhone.clear();
            providerRif.clear();
            providerBusiness.clear();
            providerPayType.setValue(null);
            providerPrefItem.setValue(null);
            
            currentProviderId = 0;
            saveButton.setText("Guardar");
            ProviderTable.getSelectionModel().clearSelection();
            
            // Actualizar la tabla
            providerList.clear();
            Proveedor.fillProviderList(providerList);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Hubo un problema procesando el proveedor.");
            alert.showAndWait();
        }
    }

    @FXML
    private void deleteProvider() {
        Proveedor selected = ProviderTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ningún proveedor seleccionado");
            alert.setContentText("Por favor selecciona un proveedor de la tabla para eliminar.");
            alert.showAndWait();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmar eliminación");
        confirmation.setHeaderText("Eliminar proveedor");
        confirmation.setContentText("¿Estás seguro de que quieres eliminar al proveedor " + selected.getName() + "?");

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            ProveedorDAO dao = new ProveedorDAO();
            if (dao.eliminar(selected.getId())) {
                Alert finalMessage = new Alert(Alert.AlertType.INFORMATION);
                finalMessage.setTitle("Proveedor Eliminado");
                finalMessage.setContentText("¡Se ha Eliminado el proveedor exitosamente!");
                finalMessage.showAndWait();
                
                // Actualizar tabla
                providerList.clear();
                Proveedor.fillProviderList(providerList);
                
                // Limpiar formulario si el proveedor borrado estaba seleccionado en el form
                if (currentProviderId == selected.getId()) {
                    providerName.clear();
                    providerCI.clear();
                    providerPhone.clear();
                    providerRif.clear();
                    providerBusiness.clear();
                    providerPayType.setValue(null);
                    providerPrefItem.setValue(null);
                    currentProviderId = 0;
                    saveButton.setText("Guardar");
                }
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setContentText("Hubo un problema al intentar eliminar el proveedor.");
                error.showAndWait();
            }
        }
    }
}
