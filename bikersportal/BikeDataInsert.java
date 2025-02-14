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
        insertBike("Yamaha R15", "Yamaha", 150000.00, 160000.00, 155, "Petrol", "E:/java-project/BikersPortal/src/imgs/bikes/yamaha_r15.jpg");
insertBike("Kawasaki Ninja 300", "Kawasaki", 330000.00, 350000.00, 296, "Petrol", "E:/java-project/BikersPortal/src/imgs/bikes/kawasaki_ninja300.jpg");
insertBike("Suzuki Hayabusa", "Suzuki", 1680000.00, 1750000.00, 1340, "Petrol", "E:/java-project/BikersPortal/src/imgs/bikes/suzuki_hayabusa.jpg");
insertBike("Ducati Panigale V4", "Ducati", 2400000.00, 2550000.00, 1103, "Petrol", "E:/java-project/BikersPortal/src/imgs/bikes/ducati_panigale_v4.jpg");
insertBike("BMW S1000RR", "BMW", 2200000.00, 2300000.00, 999, "Petrol", "E:/java-project/BikersPortal/src/imgs/bikes/bmw_s1000rr.jpg");
insertBike("Honda CBR 650R", "Honda", 920000.00, 970000.00, 649, "Petrol", "E:/java-project/BikersPortal/src/imgs/bikes/honda_cbr650r.jpg");
insertBike("KTM RC 390", "KTM", 320000.00, 350000.00, 373, "Petrol", "E:/java-project/BikersPortal/src/imgs/bikes/ktm_rc390.jpg");
insertBike("Royal Enfield Classic 350", "Royal Enfield", 210000.00, 225000.00, 349, "Petrol", "E:/java-project/BikersPortal/src/imgs/bikes/royal_enfield_classic350.jpg");
insertBike("Bajaj Dominar 400", "Bajaj", 220000.00, 235000.00, 373, "Petrol", "E:/java-project/BikersPortal/src/imgs/bikes/bajaj_dominar400.jpg");
insertBike("Harley Davidson Iron 883", "Harley Davidson", 1050000.00, 1120000.00, 883, "Petrol", "E:/java-project/BikersPortal/src/imgs/bikes/harley_iron883.jpg");
insertBike("Triumph Street Triple", "Triumph", 980000.00, 1050000.00, 765, "Petrol", "E:/java-project/BikersPortal/src/imgs/bikes/triumph_street_triple.jpg");
insertBike("Hero Xpulse 200", "Hero", 160000.00, 175000.00, 199, "Petrol", "E:/java-project/BikersPortal/src/imgs/bikes/hero_xpulse200.jpg");
insertBike("TVS Apache RR310", "TVS", 270000.00, 285000.00, 312, "Petrol", "E:/java-project/BikersPortal/src/imgs/bikes/tvs_apache_rr310.jpg");
insertBike("Kawasaki Z900", "Kawasaki", 920000.00, 950000.00, 948, "Petrol", "E:/java-project/BikersPortal/src/imgs/bikes/kawasaki_z900.jpg");
insertBike("Ducati Monster 821", "Ducati", 1120000.00, 1180000.00, 821, "Petrol", "E:/java-project/BikersPortal/src/imgs/bikes/ducati_monster821.jpg");

    }
}