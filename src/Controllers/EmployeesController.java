package controllers;

import Dao.EmpleadoDAO;
import Models.Empleado;
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

public class EmployeesController implements Initializable {

    private int currentEmployeeId = 0;

    @FXML
    Button saveButton;
    @FXML
    Button deleteButton;

    @FXML
    TextField empName;
    @FXML
    TextField empCI;
    @FXML
    TextField empPhone;
    @FXML
    TextField empRole;
    @FXML
    TextField empSalary;

    @FXML
    TextField searchField;

    @FXML
    Button toggleAdvancedButton;
    @FXML
    VBox advancedFilterPanel;
    @FXML
    ComboBox<String> filterCargo;
    @FXML
    TextField filterSalaryMin;
    @FXML
    TextField filterSalaryMax;

    @FXML
    TableView<Empleado> EmployeeTable;
    @FXML
    TableColumn<Empleado, Integer> ColId;
    @FXML
    TableColumn<Empleado, String> ColName;
    @FXML
    TableColumn<Empleado, String> ColCI;
    @FXML
    TableColumn<Empleado, String> ColPhone;
    @FXML
    TableColumn<Empleado, String> ColRole;
    @FXML
    TableColumn<Empleado, Double> ColSalary;
    @FXML
    TableColumn<Empleado, String> ColDate;

    ObservableList<Empleado> employeeList = FXCollections.observableArrayList();
    private FilteredList<Empleado> filteredEmployeeList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializar Columnas
        ColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColName.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        ColCI.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        ColPhone.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        ColRole.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        ColSalary.setCellValueFactory(new PropertyValueFactory<>("salario"));
        ColDate.setCellValueFactory(new PropertyValueFactory<>("fechaIngreso"));

        // Listener para selección de tabla
        EmployeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                currentEmployeeId = newSelection.getId();
                empName.setText(newSelection.getNombre());
                empCI.setText(newSelection.getCedula());
                empPhone.setText(newSelection.getTelefono());
                empRole.setText(newSelection.getCargo());
                empSalary.setText(String.valueOf(newSelection.getSalario()));
                saveButton.setText("Actualizar");
            }
        });

        employeeList = Empleado.fillEmployeeList(employeeList);

        filteredEmployeeList = new FilteredList<>(employeeList, p -> true);
        EmployeeTable.setItems(filteredEmployeeList);

        // Poblar el ComboBox de cargos con los valores ya existentes en la BD.
        // Es editable, así que el usuario puede escribir un cargo nuevo aunque no esté en la lista.
        EmpleadoDAO dao = new EmpleadoDAO();
        List<String> cargosExistentes = dao.obtenerCargosUnicos();
        filterCargo.setItems(FXCollections.observableArrayList(cargosExistentes));

        // Live search: el filtro se aplica en tiempo real mientras se escribe, sin necesidad del botón.
        searchField.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltros());
        filterCargo.valueProperty().addListener((obs, oldValue, newValue) -> aplicarFiltros());
        filterSalaryMin.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltros());
        filterSalaryMax.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltros());
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
        filterCargo.setValue(null);
        filterSalaryMin.clear();
        filterSalaryMax.clear();
        // Los listeners ya disparan aplicarFiltros() automáticamente al limpiar cada campo.
    }

    // Mantiene el comportamiento del botón "Buscar" por si el equipo prefiere no usar el live search.
    @FXML
    private void buscarEmpleado() {
        aplicarFiltros();
    }

    // Construye UN SOLO predicado combinando todos los criterios activos (texto libre + cargo + rango de salario).
    // Esto es clave: FilteredList solo acepta un predicado a la vez, así que cada criterio se combina con AND.
    private void aplicarFiltros() {
        String textoFiltro = searchField.getText();
        String textoLower = (textoFiltro == null) ? "" : textoFiltro.trim().toLowerCase();

        String cargoFiltro = filterCargo.getValue();
        String cargoLower = (cargoFiltro == null) ? "" : cargoFiltro.trim().toLowerCase();

        Double salarioMin = parseSalarioSeguro(filterSalaryMin.getText());
        Double salarioMax = parseSalarioSeguro(filterSalaryMax.getText());

        filteredEmployeeList.setPredicate(empleado -> {
            // Criterio 1: nombre o cédula (texto libre)
            boolean coincideTexto = textoLower.isEmpty()
                    || (empleado.getNombre() != null && empleado.getNombre().toLowerCase().contains(textoLower))
                    || (empleado.getCedula() != null && empleado.getCedula().toLowerCase().contains(textoLower));

            // Criterio 2: cargo (coincidencia parcial, para permitir escribir un cargo nuevo o parcial)
            boolean coincideCargo = cargoLower.isEmpty()
                    || (empleado.getCargo() != null && empleado.getCargo().toLowerCase().contains(cargoLower));

            // Criterio 3: rango de salario
            double salarioEmpleado = empleado.getSalario();
            boolean coincideMin = (salarioMin == null) || salarioEmpleado >= salarioMin;
            boolean coincideMax = (salarioMax == null) || salarioEmpleado <= salarioMax;

            return coincideTexto && coincideCargo && coincideMin && coincideMax;
        });
    }

    // Si el usuario escribe algo que no es un número válido en los campos de salario, lo ignoramos
    // en vez de tirar una excepción — así el filtro no se rompe mientras la persona sigue escribiendo.
    private Double parseSalarioSeguro(String texto) {
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
    private void saveEmployee() {
        String nom = empName.getText();
        String ced = empCI.getText();
        String tel = empPhone.getText();
        String cargo = empRole.getText();
        
        double salario = 0;
        try {
            salario = Double.parseDouble(empSalary.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Datos inválidos");
            alert.setContentText("El salario debe ser un número válido.");
            alert.showAndWait();
            return;
        }

        Empleado nuevoEmpleado = new Empleado(currentEmployeeId, nom, ced, tel, cargo, salario, "");
        EmpleadoDAO dao = new EmpleadoDAO();
        
        boolean res = false;
        if (currentEmployeeId == 0) {
            res = dao.guardar(nuevoEmpleado);
        } else {
            res = dao.modificar(nuevoEmpleado);
        }
        
        if (res) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Empleado procesado");
            alert.setContentText(currentEmployeeId == 0 ? "¡Se ha registrado el empleado exitosamente!" : "¡Se ha actualizado el empleado exitosamente!");
            alert.showAndWait();
            
            // Limpiar las cajas de texto
            empName.clear();
            empCI.clear();
            empPhone.clear();
            empRole.clear();
            empSalary.clear();
            
            currentEmployeeId = 0;
            saveButton.setText("Guardar");
            EmployeeTable.getSelectionModel().clearSelection();
            
            // Actualizar la tabla
            employeeList.clear();
            Empleado.fillEmployeeList(employeeList);

            // Refrescar el ComboBox de cargos por si se guardó un cargo nuevo que no estaba antes
            EmpleadoDAO cargoDao = new EmpleadoDAO();
            filterCargo.setItems(FXCollections.observableArrayList(cargoDao.obtenerCargosUnicos()));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Hubo un problema procesando el empleado.");
            alert.showAndWait();
        }
    }

    @FXML
    private void deleteEmployee() {
        Empleado selected = EmployeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ningún empleado seleccionado");
            alert.setContentText("Por favor selecciona un empleado de la tabla para eliminar.");
            alert.showAndWait();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmar eliminación");
        confirmation.setHeaderText("Eliminar empleado");
        confirmation.setContentText("¿Estás seguro de que quieres eliminar al empleado " + selected.getNombre() + "?");

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            EmpleadoDAO dao = new EmpleadoDAO();
            if (dao.eliminar(selected.getId())) {
                Alert finalMessage = new Alert(Alert.AlertType.INFORMATION);
                finalMessage.setTitle("Empleado Eliminado");
                finalMessage.setContentText("¡Se ha Eliminado el empleado exitosamente!");
                finalMessage.showAndWait();
                
                // Actualizar tabla
                employeeList.clear();
                Empleado.fillEmployeeList(employeeList);
                
                // Limpiar formulario si el empleado borrado estaba seleccionado
                if (currentEmployeeId == selected.getId()) {
                    empName.clear();
                    empCI.clear();
                    empPhone.clear();
                    empRole.clear();
                    empSalary.clear();
                    currentEmployeeId = 0;
                    saveButton.setText("Guardar");
                }
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setContentText("Hubo un problema al intentar eliminar el empleado.");
                error.showAndWait();
            }
        }
    }
}
