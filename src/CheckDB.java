import Config.DBConn;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class CheckDB {
    public static void main(String[] args) {
        try (Connection conn = DBConn.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM public.clientes LIMIT 1");
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            System.out.println("Column count: " + columnCount);
            for (int i = 1; i <= columnCount; i++) {
                System.out.println("Column " + i + ": " + rsmd.getColumnName(i) + " (" + rsmd.getColumnTypeName(i) + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
