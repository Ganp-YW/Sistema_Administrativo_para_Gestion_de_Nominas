/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 * @author joseh
 */

public class DBConn {

    private static Boolean initialized = false;
    private static Path confRoute = Path.of(Path.of("").toAbsolutePath().toString(), "src", "Config","config.properties");
    private static String HOST = "";
    private static String PORT = "";
    private static String DB = "";
    private static String USER = "";
    private static String PASSWORD = "";
    private static String URL = "";

    private static void initDBConf() throws FileNotFoundException, IOException {
        DBConn.initialized = true;

        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(confRoute.toString())) {
            prop.load(input);
        }
        
        DBConn.HOST = prop.getProperty("db.HOST");
        DBConn.PORT = prop.getProperty("db.PORT");
        DBConn.DB = prop.getProperty("db.DB");
        DBConn.USER = prop.getProperty("db.USER");
        DBConn.PASSWORD = prop.getProperty("db.PASSWORD");
        DBConn.URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB;
    }

    public static Connection getConnection() throws IOException {
        if (!initialized) initDBConf();
            
        Connection conn = null;
        
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            try {
                System.err.println("Error al conectar: " + e.toString());
                System.err.println("Reintendando con conexion provisional");
                conn = DriverManager.getConnection("jdbc:postgresql://host:5432/db", "user", "pass"); // Conexion provicional
            } catch (SQLException err) {
                System.err.println(err.toString());
                System.err.println("\nError al conectar: " + err.getMessage());
            }
        }
        return conn;
    }

    public static void main(String[] args) {
        System.out.println("Prueba de Conexion:");

        try (Connection conn = DBConn.getConnection()) {
            if (conn == null) {
                return;
            }

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 'Conexion Exitosa' AS saludo");

            while (rs.next()) {
                System.out.println("Resultado de la BD: " + rs.getString("saludo"));
            }

//            try-with-resources cierra la conexion automaticamente           
//            rs.close();
//            stmt.close();
//            conn.close(); 
        } catch (Exception e) {
            System.err.println("Error en la consulta: " + e.getMessage());
        }

    }
}
