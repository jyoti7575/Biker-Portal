/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bikersportal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author daya3
 */
public class Comparison {
    public static void getComparison() {
        try (Connection con = DBConnection.connect();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM comparison")) { 

            System.out.println("\n === compariosn ===");
            while (rs.next()) {
                System.out.println("Bike1 ID: " + rs.getInt("bike1_id")
                        + "vs Bike2 id: " + rs.getInt("bike2_id")
                        + ", Performance: " + rs.getString("performance_diff"));
            }
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
}
