# 🎬 Guion para Presentación en Video (Máx 10 minutos)

## Introducción (0:00 - 1:00)
**Narrador/Presentador:** "Hola a todos, hoy les presentaremos nuestro Sistema Administrativo para Gestión de Nóminas e Inventario. Este proyecto está desarrollado en Java siguiendo la arquitectura Modelo-Vista-Controlador (MVC), lo cual nos permite tener el código ordenado, mantenible y escalable. A continuación, explicaremos su funcionamiento interno dividido en tres partes fundamentales."

---

## Parte 1: Clases, Tipos de datos, Colecciones y Frameworks (1:00 - 4:00)
**Ubicación en el código:** Paquetes `Models`, `Controllers` y librerías importadas.

**Presentador:**
- **Librerías y Frameworks:** "Para la interfaz gráfica utilizamos el framework **JavaFX** junto con archivos `.fxml` (diseñados en SceneBuilder) y estilos `.css`. Esto nos permite separar completamente la lógica visual del código Java. También utilizamos la librería de **Zonky Embedded Postgres** para la base de datos."
- **Clases y Atributos:** "En el paquete `Models` tenemos las clases principales que representan nuestro negocio, por ejemplo: `Client`, `Empleado`, `Producto` y `Proveedor`. En estas clases declaramos los **atributos** (como nombre, cédula, teléfono) utilizando el encapsulamiento."
- **Tipos de datos:** "Hacemos uso de tipos de datos primitivos de Java como `int` para los IDs y objetos `String` para textos."
- **Listas y Arrays:** "En lugar de usar arrays estáticos tradicionales, implementamos **ObservableList** de JavaFX (una lista dinámica especializada). Esto es vital porque nos permite vincular la lista directamente a las tablas de la interfaz. Cuando agregamos o modificamos un registro en la lista, la tabla en pantalla se actualiza automáticamente."
- **Herencia y Polimorfismo:** "Aplicamos herencia principalmente al extender clases del framework de JavaFX, como nuestra clase principal que hereda de `Application`. Además, aplicamos polimorfismo al sobreescribir (usando `@Override`) métodos clave como `start()` o `init()`, dotándolos del comportamiento específico que nuestro sistema requiere."

---

## Parte 2: Base de Datos y Patrón DAO (4:00 - 7:00)
**Ubicación en el código:** Paquetes `Config` (clase `DBConn`) y `Dao`.

**Presentador:**
- **La Base de Datos:** "En este proyecto no obligamos al usuario a instalar un gestor de bases de datos externo. Usamos **PostgreSQL Embebido**. La magia de esto ocurre en la clase `DBConn` dentro del paquete `Config`. Esta clase se encarga de descargar silenciosamente los binarios de Postgres la primera vez, crear el directorio `database_data`, e inicializar las tablas a través de nuestro archivo `init.sql`."
- **Conexiones (JDBC):** "Usamos la API JDBC tradicional (`java.sql.Connection`, `Statement`, `ResultSet`). La clase `DBConn` aplica un patrón de diseño Singleton básico para entregarnos siempre la misma conexión abierta a la base de datos."
- **Código de Base de Datos (DAO):** "Para no ensuciar la interfaz gráfica con código SQL, creamos el paquete `Dao` (Data Access Object). Aquí tenemos clases como `ClienteDAO` o `ProveedorDAO`. Cada una de ellas contiene los métodos para insertar, actualizar, eliminar o listar registros usando sentencias SQL puras (`INSERT INTO`, `SELECT`, `UPDATE`). Así, si el día de mañana queremos cambiar de base de datos, solo modificamos los DAO sin tocar el resto del sistema."

---

## Parte 3: Clase Main, Algoritmo Principal y Ejecución (7:00 - 9:30)
**Ubicación en el código:** Paquete `App` (clase `Main`) y `Controllers.PrincipalView`.

**Presentador:**
- **Clase Main:** "Todo sistema necesita un punto de entrada. En nuestro caso, está en la clase `Main` del paquete `App`. Funciona como el cerebro de arranque."
- **Algoritmo Principal:** 
  1. "Primero, al ejecutarse, se llama al método `init()`. Aquí nos conectamos a la base de datos (con `DBConn`) antes de siquiera dibujar la ventana, previniendo errores de carga."
  2. "Luego, JavaFX llama automáticamente al método `start(Stage)`. Aquí usamos la clase `FXMLLoader` para leer nuestro archivo `PrincipalView.fxml`, le aplicamos nuestra hoja de estilos (`table.css`), le asignamos el tamaño a la ventana principal y finalmente la mostramos en pantalla con `primaryStage.show()`."
- **Ejecución en Tiempo Real:** "Una vez que arranca, el control pasa a `PrincipalView` y a nuestra clase utilitaria `ViewManager`. Estas clases se quedan a la escucha de eventos (clicks del usuario). Cuando se hace clic en 'Empleados' o 'Inventario', el sistema dinámicamente inyecta y cambia las vistas dentro del contenedor central sin tener que abrir múltiples ventanas molestas, garantizando una experiencia fluida y en tiempo real."

## Despedida (9:30 - 10:00)
**Presentador:** "Como pueden ver, gracias a los principios de Programación Orientada a Objetos, el patrón MVC y el uso de un modelo DAO, tenemos un software robusto, escalable y muy profesional. ¡Muchas gracias por su atención!"
