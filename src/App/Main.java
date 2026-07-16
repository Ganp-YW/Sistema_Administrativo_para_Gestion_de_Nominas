package App;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author joseh
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void init() throws Exception {
        // Inicializar la base de datos (y descargar binarios si es necesario) 
        // ANTES de cargar la interfaz. Esto previene errores de vistas que 
        // asumen que la base de datos ya está lista.
        Config.DBConn.getConnection();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Views/PrincipalView.fxml"));
        Scene scene = new Scene(root);
        
        scene.getStylesheets().add(getClass().getResource("/Styles/table.css").toExternalForm());
        primaryStage.setTitle("Sistema Administrativo para Gestion de Nominas");
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(768);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//import javafx.scene.text.Text;
//import javafx.scene.text.TextFlow;
//import javafx.stage.Stage;
//import javafx.collections.FXCollections;
//import java.util.Optional;
//
//public class Main extends Application {
//    
//    // Declarar componentes como variables de instancia para poder acceder a ellos
//    private TextField txtNombre;
//    private PasswordField txtPassword;
//    private TextArea txtComentario;
//    private TableView<String[]> tablaUsuarios;
//    private ProgressBar progressBar;
//    private ComboBox<String> comboPaises;
//    private Slider sliderVolumen;
//    private Label lblSliderValor;
//    
//    @Override
//    public void start(Stage primaryStage) {
//        // Crear panel principal con scroll
//        ScrollPane scrollPane = new ScrollPane();
//        scrollPane.setFitToWidth(true);
//        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
//        
//        // Contenedor principal vertical
//        VBox mainContainer = new VBox(20);
//        mainContainer.setPadding(new Insets(20));
//        mainContainer.setStyle("-fx-background-color: #f5f5f5;");
//        
//        // 1. TÍTULO PRINCIPAL
//        Label titulo = new Label("🎯 SISTEMA COMPLETO EN UN SOLO ARCHIVO");
//        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
//        titulo.setTextFill(Color.web("#2c3e50"));
//        
//        // 2. SECCIÓN DE FORMULARIO
//        VBox seccionFormulario = crearSeccion("📝 FORMULARIO DE REGISTRO");
//        
//        GridPane formulario = new GridPane();
//        formulario.setVgap(10);
//        formulario.setHgap(10);
//        formulario.setPadding(new Insets(15));
//        
//        // Campos del formulario
//        txtNombre = new TextField();
//        txtNombre.setPromptText("Ingrese su nombre completo");
//        txtNombre.setPrefWidth(300);
//        
//        txtPassword = new PasswordField();
//        txtPassword.setPromptText("Ingrese su contraseña");
//        
//        txtComentario = new TextArea();
//        txtComentario.setPromptText("Comentarios adicionales...");
//        txtComentario.setPrefRowCount(3);
//        txtComentario.setWrapText(true);
//        
//        // Agregar al grid
//        formulario.add(new Label("Nombre:"), 0, 0);
//        formulario.add(txtNombre, 1, 0);
//        formulario.add(new Label("Contraseña:"), 0, 1);
//        formulario.add(txtPassword, 1, 1);
//        formulario.add(new Label("Comentario:"), 0, 2);
//        formulario.add(txtComentario, 1, 2);
//        
//        // Botones del formulario
//        HBox botonesForm = new HBox(10);
//        Button btnGuardar = new Button("💾 Guardar");
//        btnGuardar.setStyle(getEstiloBotonPrimario());
//        btnGuardar.setOnAction(e -> guardarFormulario());
//        
//        Button btnLimpiar = new Button("🧹 Limpiar");
//        btnLimpiar.setStyle(getEstiloBotonSecundario());
//        btnLimpiar.setOnAction(e -> limpiarFormulario());
//        
//        botonesForm.getChildren().addAll(btnGuardar, btnLimpiar);
//        formulario.add(botonesForm, 1, 3);
//        
//        seccionFormulario.getChildren().add(formulario);
//        
//        // 3. SECCIÓN DE CONTROLES
//        VBox seccionControles = crearSeccion("🎛️ CONTROLES DIVERSOS");
//        
//        HBox controles = new HBox(20);
//        controles.setPadding(new Insets(15));
//        
//        // CheckBox y RadioButtons
//        VBox opciones = new VBox(10);
//        CheckBox checkTerminos = new CheckBox("Acepto los términos y condiciones");
//        CheckBox checkNotificaciones = new CheckBox("Recibir notificaciones por email");
//        checkNotificaciones.setSelected(true);
//        
//        ToggleGroup grupoRadio = new ToggleGroup();
//        RadioButton radio1 = new RadioButton("Opción 1");
//        radio1.setToggleGroup(grupoRadio);
//        RadioButton radio2 = new RadioButton("Opción 2");
//        radio2.setToggleGroup(grupoRadio);
//        radio2.setSelected(true);
//        RadioButton radio3 = new RadioButton("Opción 3");
//        radio3.setToggleGroup(grupoRadio);
//        
//        opciones.getChildren().addAll(checkTerminos, checkNotificaciones, 
//                                      new Separator(), radio1, radio2, radio3);
//        
//        // ComboBox y Slider
//        VBox selectores = new VBox(15);
//        
//        comboPaises = new ComboBox<>();
//        comboPaises.getItems().addAll("México", "España", "Argentina", "Colombia", "Chile");
//        comboPaises.setPromptText("Seleccione un país");
//        comboPaises.setPrefWidth(200);
//        
//        // Slider
//        VBox sliderBox = new VBox(5);
//        sliderVolumen = new Slider(0, 100, 50);
//        sliderVolumen.setShowTickLabels(true);
//        sliderVolumen.setShowTickMarks(true);
//        sliderVolumen.setMajorTickUnit(25);
//        sliderVolumen.setBlockIncrement(10);
//        
//        lblSliderValor = new Label("Volumen: 50%");
//        sliderVolumen.valueProperty().addListener((obs, oldVal, newVal) -> {
//            lblSliderValor.setText(String.format("Volumen: %.0f%%", newVal));
//        });
//        
//        sliderBox.getChildren().addAll(new Label("Control de volumen:"), sliderVolumen, lblSliderValor);
//        
//        selectores.getChildren().addAll(
//            new Label("País:"), comboPaises,
//            new Separator(),
//            sliderBox
//        );
//        
//        controles.getChildren().addAll(opciones, selectores);
//        seccionControles.getChildren().add(controles);
//        
//        // 4. SECCIÓN DE TABLA
//        VBox seccionTabla = crearSeccion("📊 TABLA DE USUARIOS");
//        tablaUsuarios = crearTabla();
//        seccionTabla.getChildren().add(tablaUsuarios);
//        
//        // Botones para la tabla
//        HBox botonesTabla = new HBox(10);
//        Button btnAgregar = new Button("➕ Agregar Usuario");
//        btnAgregar.setStyle(getEstiloBotonPrimario());
//        btnAgregar.setOnAction(e -> agregarUsuarioTabla());
//        
//        Button btnEliminar = new Button("🗑️ Eliminar Seleccionado");
//        btnEliminar.setStyle(getEstiloBotonPeligro());
//        btnEliminar.setOnAction(e -> eliminarUsuarioTabla());
//        
//        botonesTabla.getChildren().addAll(btnAgregar, btnEliminar);
//        seccionTabla.getChildren().add(botonesTabla);
//        
//        // 5. SECCIÓN DE IMÁGENES
//        VBox seccionImagenes = crearSeccion("🖼️ IMÁGENES Y MEDIA");
//        
//        HBox contenedorImagenes = new HBox(20);
//        contenedorImagenes.setPadding(new Insets(15));
//        
//        // Imagen 1
//        VBox imgBox1 = new VBox(10);
//        ImageView imagen1 = new ImageView();
//        imagen1.setFitWidth(150);
//        imagen1.setFitHeight(150);
//        imagen1.setPreserveRatio(true);
//        imagen1.setStyle("-fx-border-color: #ddd; -fx-border-width: 2px; -fx-border-radius: 10px;");
//        
//        Button btnCargarImg = new Button("📁 Cargar Imagen");
//        btnCargarImg.setOnAction(e -> cargarImagen(imagen1));
//        
//        imgBox1.getChildren().addAll(
//            new Label("Imagen desde botón:"),
//            imagen1,
//            btnCargarImg
//        );
//        
//        // Imagen 2 - Botón con icono
//        Button btnConIcono = new Button("👤 Perfil");
//        btnConIcono.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; " +
//                           "-fx-padding: 10px 20px; -fx-background-radius: 5px;");
//        
//        contenedorImagenes.getChildren().addAll(imgBox1, btnConIcono);
//        seccionImagenes.getChildren().add(contenedorImagenes);
//        
//        // 6. SECCIÓN DE PROGRESS
//        VBox seccionProgress = crearSeccion("⏳ BARRAS DE PROGRESO");
//        
//        VBox progressContainer = new VBox(15);
//        progressContainer.setPadding(new Insets(15));
//        
//        progressBar = new ProgressBar(0.3);
//        progressBar.setPrefWidth(400);
//        
//        ProgressIndicator progressIndicator = new ProgressIndicator(0.6);
//        
//        HBox botonesProgress = new HBox(10);
//        Button btnIncrementar = new Button("➕ Incrementar");
//        btnIncrementar.setOnAction(e -> incrementarProgress());
//        
//        Button btnReset = new Button("🔄 Resetear");
//        btnReset.setOnAction(e -> resetProgress());
//        
//        botonesProgress.getChildren().addAll(btnIncrementar, btnReset);
//        
//        progressContainer.getChildren().addAll(
//            new Label("Progress Bar:"),
//            progressBar,
//            new Label("Progress Indicator:"),
//            progressIndicator,
//            botonesProgress
//        );
//        
//        seccionProgress.getChildren().add(progressContainer);
//        
//        // 7. SECCIÓN DE MENÚS
//        VBox seccionMenus = crearSeccion("🍔 MENÚS Y DIÁLOGOS");
//        
//        // Barra de menú
//        MenuBar menuBar = new MenuBar();
//        
//        Menu menuArchivo = new Menu("Archivo");
//        MenuItem itemNuevo = new MenuItem("Nuevo");
//        MenuItem itemAbrir = new MenuItem("Abrir");
//        MenuItem itemSalir = new MenuItem("Salir");
//        itemSalir.setOnAction(e -> primaryStage.close());
//        
//        menuArchivo.getItems().addAll(itemNuevo, itemAbrir, new SeparatorMenuItem(), itemSalir);
//        
//        Menu menuEditar = new Menu("Editar");
//        menuEditar.getItems().addAll(new MenuItem("Copiar"), new MenuItem("Pegar"));
//        
//        Menu menuAyuda = new Menu("Ayuda");
//        MenuItem itemAcerca = new MenuItem("Acerca de");
//        itemAcerca.setOnAction(e -> mostrarAcercaDe());
//        
//        menuAyuda.getItems().add(itemAcerca);
//        menuBar.getMenus().addAll(menuArchivo, menuEditar, menuAyuda);
//        
//        // Botones de diálogo
//        HBox botonesDialogo = new HBox(10);
//        Button btnInfo = new Button("ℹ️ Información");
//        btnInfo.setOnAction(e -> mostrarDialogoInfo());
//        
//        Button btnError = new Button("❌ Error");
//        btnError.setOnAction(e -> mostrarDialogoError());
//        
//        Button btnConfirm = new Button("✅ Confirmar");
//        btnConfirm.setOnAction(e -> mostrarDialogoConfirmacion());
//        
//        botonesDialogo.getChildren().addAll(btnInfo, btnError, btnConfirm);
//        
//        seccionMenus.getChildren().addAll(menuBar, botonesDialogo);
//        
//        // 8. SECCIÓN DE TEXTO ENRIQUECIDO
//        VBox seccionTexto = crearSeccion("🔠 TEXTO ENRIQUECIDO");
//        
//        TextFlow textFlow = new TextFlow();
//        Text texto1 = new Text("Texto en ");
//        texto1.setFill(Color.BLACK);
//        
//        Text texto2 = new Text("negrita ");
//        texto2.setFill(Color.BLACK);
//        texto2.setFont(Font.font("Arial", FontWeight.BOLD, 14));
//        
//        Text texto3 = new Text("y en ");
//        texto3.setFill(Color.BLACK);
//        
//        Text texto4 = new Text("cursiva");
//        texto4.setFill(Color.BLACK);
//        texto4.setFont(Font.font("Arial", FontWeight.BOLD, 14));
//        
//        Text texto5 = new Text(". También podemos usar ");
//        texto5.setFill(Color.BLACK);
//        
//        Text texto6 = new Text("colores");
//        texto6.setFill(Color.RED);
//        
//        textFlow.getChildren().addAll(texto1, texto2, texto3, texto4, texto5, texto6);
//        
//        Hyperlink link = new Hyperlink("Este es un enlace clickeable");
//        link.setOnAction(e -> mostrarMensaje("Enlace clickeado!"));
//        
//        seccionTexto.getChildren().addAll(textFlow, link);
//        
//        // 9. SECCIÓN DE TABS
//        VBox seccionTabs = crearSeccion("📑 PESTAÑAS (TABPANE)");
//        
//        TabPane tabPane = new TabPane();
//        
//        Tab tab1 = new Tab("Perfil");
//        tab1.setClosable(false);
//        VBox tab1Content = new VBox(10);
//        tab1Content.setPadding(new Insets(15));
//        tab1Content.getChildren().addAll(
//            new Label("Nombre: Usuario Demo"),
//            new Label("Email: demo@ejemplo.com"),
//            new Label("Rol: Administrador")
//        );
//        tab1.setContent(tab1Content);
//        
//        Tab tab2 = new Tab("Configuración");
//        tab2.setClosable(false);
//        VBox tab2Content = new VBox(10);
//        tab2Content.setPadding(new Insets(15));
//        tab2Content.getChildren().addAll(
//            new CheckBox("Modo oscuro"),
//            new CheckBox("Notificaciones por email"),
//            new CheckBox("Autoguardado")
//        );
//        tab2.setContent(tab2Content);
//        
//        tabPane.getTabs().addAll(tab1, tab2);
//        seccionTabs.getChildren().add(tabPane);
//        
//        // 10. PIE DE PÁGINA
//        HBox footer = new HBox();
//        footer.setAlignment(Pos.CENTER);
//        footer.setPadding(new Insets(20));
//        footer.setStyle("-fx-background-color: #2c3e50;");
//        
//        Label footerText = new Label("© 2024 Sistema JavaFX - Todos los componentes en un solo archivo");
//        footerText.setTextFill(Color.WHITE);
//        footer.getChildren().add(footerText);
//        
//        // Agregar todas las secciones al contenedor principal
//        mainContainer.getChildren().addAll(
//            titulo,
//            seccionFormulario,
//            seccionControles,
//            seccionTabla,
//            seccionImagenes,
//            seccionProgress,
//            seccionMenus,
//            seccionTexto,
//            seccionTabs,
//            footer
//        );
//        
//        scrollPane.setContent(mainContainer);
//        
//        // Crear escena
//        Scene scene = new Scene(scrollPane, 900, 700);
//        
//        // Configurar stage
//        primaryStage.setTitle("Sistema JavaFX - Todo en Main.java");
//        primaryStage.setScene(scene);
//        primaryStage.setMinWidth(800);
//        primaryStage.setMinHeight(600);
//        primaryStage.show();
//    }
//    
//    // ============================================
//    // MÉTODOS AUXILIARES
//    // ============================================
//    
//    private VBox crearSeccion(String titulo) {
//        VBox seccion = new VBox(10);
//        seccion.setPadding(new Insets(15));
//        seccion.setStyle("-fx-background-color: white; -fx-border-color: #ddd; " +
//                        "-fx-border-width: 1px; -fx-border-radius: 8px;");
//        
//        Label lblTitulo = new Label(titulo);
//        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 16));
//        lblTitulo.setTextFill(Color.web("#2c3e50"));
//        
//        seccion.getChildren().add(lblTitulo);
//        return seccion;
//    }
//    
//    private String getEstiloBotonPrimario() {
//        return "-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
//               "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; " +
//               "-fx-padding: 10px 20px; -fx-background-radius: 5px;";
//    }
//    
//    private String getEstiloBotonSecundario() {
//        return "-fx-background-color: #6c757d; -fx-text-fill: white; " +
//               "-fx-font-weight: bold; -fx-padding: 10px 20px; -fx-background-radius: 5px;";
//    }
//    
//    private String getEstiloBotonPeligro() {
//        return "-fx-background-color: #e74c3c; -fx-text-fill: white; " +
//               "-fx-font-weight: bold; -fx-padding: 10px 20px; -fx-background-radius: 5px;";
//    }
//    
//    private TableView<String[]> crearTabla() {
//        TableView<String[]> tabla = new TableView<>();
//        tabla.setPrefHeight(200);
//        
//        // Columnas
//        TableColumn<String[], String> colId = new TableColumn<>("ID");
//        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[0]));
//        colId.setPrefWidth(50);
//        
//        TableColumn<String[], String> colNombre = new TableColumn<>("Nombre");
//        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[1]));
//        colNombre.setPrefWidth(150);
//        
//        TableColumn<String[], String> colEmail = new TableColumn<>("Email");
//        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[2]));
//        colEmail.setPrefWidth(200);
//        
//        TableColumn<String[], String> colRol = new TableColumn<>("Rol");
//        colRol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[3]));
//        colRol.setPrefWidth(100);
//        
//        tabla.getColumns().addAll(colId, colNombre, colEmail, colRol);
//        
//        // Datos iniciales
//        tabla.getItems().addAll(
//            new String[]{"1", "Juan Pérez", "juan@email.com", "Admin"},
//            new String[]{"2", "María García", "maria@email.com", "Usuario"},
//            new String[]{"3", "Carlos López", "carlos@email.com", "Editor"}
//        );
//        
//        return tabla;
//    }
//    
//    // ============================================
//    // MANEJADORES DE EVENTOS
//    // ============================================
//    
//    private void guardarFormulario() {
//        String nombre = txtNombre.getText();
//        String password = txtPassword.getText();
//        String comentario = txtComentario.getText();
//        String pais = comboPaises.getValue();
//        
//        if (nombre.isEmpty() || password.isEmpty()) {
//            mostrarMensaje("Por favor, complete todos los campos obligatorios");
//            return;
//        }
//        
//        String mensaje = String.format(
//            "Datos guardados:\n" +
//            "Nombre: %s\n" +
//            "País: %s\n" +
//            "Comentario: %s",
//            nombre, 
//            pais != null ? pais : "No seleccionado",
//            comentario.isEmpty() ? "Ninguno" : comentario
//        );
//        
//        mostrarMensaje(mensaje);
//    }
//    
//    private void limpiarFormulario() {
//        txtNombre.clear();
//        txtPassword.clear();
//        txtComentario.clear();
//        comboPaises.getSelectionModel().clearSelection();
//    }
//    
//    private void agregarUsuarioTabla() {
//        int nuevoId = tablaUsuarios.getItems().size() + 1;
//        String[] nuevoUsuario = {
//            String.valueOf(nuevoId),
//            "Usuario " + nuevoId,
//            "usuario" + nuevoId + "@email.com",
//            "Nuevo"
//        };
//        tablaUsuarios.getItems().add(nuevoUsuario);
//        mostrarMensaje("Usuario agregado con ID: " + nuevoId);
//    }
//    
//    private void eliminarUsuarioTabla() {
//        int selectedIndex = tablaUsuarios.getSelectionModel().getSelectedIndex();
//        if (selectedIndex >= 0) {
//            tablaUsuarios.getItems().remove(selectedIndex);
//            mostrarMensaje("Usuario eliminado");
//        } else {
//            mostrarMensaje("Seleccione un usuario para eliminar");
//        }
//    }
//    
//    private void cargarImagen(ImageView imageView) {
//        // En un caso real, aquí abrirías un FileChooser
//        // Por ahora, mostramos un mensaje
//        mostrarMensaje("Función para cargar imagen activada\n(En un caso real, abriría un explorador de archivos)");
//        
//        // Simulación: cambiar a imagen de ejemplo
//        imageView.setStyle("-fx-background-color: #3498db; -fx-border-color: #2980b9; " +
//                          "-fx-border-width: 2px; -fx-border-radius: 10px;");
//        Label placeholder = new Label("Imagen\nCargada");
//        placeholder.setTextFill(Color.WHITE);
//        placeholder.setFont(Font.font("Arial", FontWeight.BOLD, 16));
//        placeholder.setAlignment(Pos.CENTER);
//        
//        StackPane stackPane = new StackPane(placeholder);
//        stackPane.setPrefSize(150, 150);
//        stackPane.setStyle("-fx-background-color: #3498db; -fx-background-radius: 10px;");
//        
//        // Necesitamos crear un Scene para mostrar el StackPane
//        // En este caso, solo actualizamos el estilo
//        imageView.setStyle("-fx-background-color: #3498db; -fx-background-radius: 10px;");
//    }
//    
//    private void incrementarProgress() {
//        double current = progressBar.getProgress();
//        if (current < 1.0) {
//            progressBar.setProgress(current + 0.1);
//        }
//    }
//    
//    private void resetProgress() {
//        progressBar.setProgress(0.0);
//    }
//    
//    private void mostrarAcercaDe() {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Acerca de");
//        alert.setHeaderText("Sistema JavaFX - Todo en Main.java");
//        alert.setContentText("Versión 1.0\n\nEste sistema demuestra cómo crear " +
//                           "una interfaz completa usando solo código Java,\n" +
//                           "sin necesidad de archivos FXML separados.\n\n" +
//                           "© 2024 - Ejemplo Educativo");
//        alert.showAndWait();
//    }
//    
//    private void mostrarDialogoInfo() {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Información");
//        alert.setHeaderText("Operación exitosa");
//        alert.setContentText("Los datos se han procesado correctamente.");
//        alert.showAndWait();
//    }
//    
//    private void mostrarDialogoError() {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Error");
//        alert.setHeaderText("Ocurrió un error");
//        alert.setContentText("No se pudo completar la operación. Intente nuevamente.");
//        alert.showAndWait();
//    }
//    
//    private void mostrarDialogoConfirmacion() {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Confirmación");
//        alert.setHeaderText("¿Está seguro?");
//        alert.setContentText("Esta acción no se puede deshacer.");
//        
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.isPresent() && result.get() == ButtonType.OK) {
//            mostrarMensaje("Acción confirmada");
//        }
//    }
//    
//    private void mostrarMensaje(String mensaje) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Mensaje");
//        alert.setHeaderText(null);
//        alert.setContentText(mensaje);
//        alert.showAndWait();
//    }
//    
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
