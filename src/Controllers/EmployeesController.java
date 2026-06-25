package controllers;

import Dao.EmpleadoDAO;
import Models.Empleado;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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
        EmployeeTable.setItems(employeeList);
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
