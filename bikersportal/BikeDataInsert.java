package bikersportal;

import java.sql.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BikeDataInsert {
    public static void insertBike(String name, String brand, double showroomPrice, double onRoadPrice,
                                  int engineCapacity, String fuelType, String imagePath) {
        String query = "INSERT INTO Bike (Name, Brand, Showroom_price, On_road_price, Engine_capacity, Fuel_type, Bike_Image) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             FileInputStream fis = new FileInputStream(new File(imagePath))) {

            pstmt.setString(1, name);
            pstmt.setString(2, brand);
            pstmt.setDouble(3, showroomPrice);
            pstmt.setDouble(4, onRoadPrice);
            pstmt.setInt(5, engineCapacity);
            pstmt.setString(6, fuelType);
            pstmt.setBinaryStream(7, fis, (int) new File(imagePath).length());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Bike data inserted successfully!");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        insertBike("Yamaha r22", "bmww", 15000.00, 16000.00, 998, "desel", "E:/java-project/BikersPortal/src/imgs/bikes/a1.jpg");
    }
}