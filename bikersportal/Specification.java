package bikersportal;
import java.sql.*;

public class Specification {
    public static void getSpecifications() {
        try (Connection con = DBConnection.connect();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM specifications")) { 

            System.out.println("\n === Specifications ===");
            while (rs.next()) {
                System.out.println("Bike ID: " + rs.getInt("bike_id")
                        + ", Mileage: " + rs.getString("mileage")
                        + ", Power: " + rs.getString("power"));
            }
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
}
