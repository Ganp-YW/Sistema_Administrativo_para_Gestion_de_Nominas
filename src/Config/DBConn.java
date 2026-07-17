/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Config;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;

/**
 *
 * @author joseh
 */
public class DBConn {

    private static Boolean initialized = false;
    private static EmbeddedPostgres pg = null;
    private static String URL = "";
    private static String USER = "postgres";
    private static String PASSWORD = "";

    // Metodo para inicializar la configuracion de base de datos embebida
    private static void initDBConf() {
        try {
            System.out.println("Iniciando PostgreSQL Embebido...");
            System.out.println("Si es la primera vez que se ejecuta en esta PC, esto puede demorar mientras se descargan los binarios.");
            
            File dataDir = new File(Path.of("").toAbsolutePath().toString(), "database_data");
            boolean isFirstRun = !dataDir.exists();
            
            pg = EmbeddedPostgres.builder()
                    .setDataDirectory(dataDir)
                    .setCleanDataDirectory(false)
                    .start();
            
            URL = pg.getJdbcUrl("postgres", "postgres");
            
            if (isFirstRun) {
                System.out.println("Primera ejecución detectada. Inicializando tablas...");
                executeInitSql();
            }
            
            initialized = true;
            System.out.println("Base de datos levantada con éxito en: " + URL);
        } catch (IOException e) {
            System.err.println("Error iniciando base de datos embebida: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Metodo para ejecutar el script de inicializacion SQL
    private static void executeInitSql() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            Path sqlPath = Path.of(Path.of("").toAbsolutePath().toString(), "init.sql");
            if (Files.exists(sqlPath)) {
                String sql = Files.readString(sqlPath);
                stmt.execute(sql);
                System.out.println("Tablas de init.sql creadas con éxito.");
            } else {
                System.err.println("No se encontró el archivo init.sql en la raíz del proyecto.");
            }
        } catch (Exception e) {
            System.err.println("Error ejecutando init.sql: " + e.getMessage());
        }
    }

    // Metodo para obtener una conexion a la base de datos
    public static Connection getConnection() {
        if (!initialized) {
            initDBConf();
        }
        
        Connection conn = null;
        try {
            if (URL != null && !URL.isEmpty()) {
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage());
        }
        return conn;
    }

    // Metodo main para probar la conexion
    public static void main(String[] args) {
        System.out.println("Prueba de Conexion a Embedded PostgreSQL:");

        try (Connection conn = DBConn.getConnection()) {
            if (conn == null) {
                System.out.println("La conexión falló.");
                return;
            }

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 'Conexion Exitosa' AS saludo");

            while (rs.next()) {
                System.out.println("Resultado de la BD: " + rs.getString("saludo"));
            }
        } catch (Exception e) {
            System.err.println("Error en la consulta: " + e.getMessage());
        }
    }
}
