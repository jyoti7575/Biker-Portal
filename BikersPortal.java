
package bikersportal;

//import com.sun.jdi.connect.spi.Connection;
import java.sql.*;



public class BikersPortal {

   
    public static void main(String[] args) throws Exception   {
        
        DBConnection db = new DBConnection();
        System.out.println(db);
        
    }
    
}


//create tables in sql

//
//CREATE TABLE Bike (
//    Bike_id INT PRIMARY KEY AUTO_INCREMENT,
//    Name VARCHAR(100) NOT NULL,
//    Brand VARCHAR(100) NOT NULL,
//    Showroom_price DECIMAL(10,2) NOT NULL,
//    On_road_price DECIMAL(10,2) NOT NULL,
//    Engine_capacity INT NOT NULL,
//    Fuel_type VARCHAR(50) NOT NULL
//);


//CREATE TABLE specifications (
//    spec_id INT PRIMARY KEY AUTO_INCREMENT,
//    Bike_id INT,
//    Mileage VARCHAR(50),
//    Power VARCHAR(50),
//    Fuel_consumption VARCHAR(50),
//    Maintenance_tip TEXT,
//    FOREIGN KEY (Bike_id) REFERENCES bikes(bike_id) ON DELETE CASCADE
//);



//CREATE TABLE Comparisons (
//    comp_id INT PRIMARY KEY AUTO_INCREMENT,
//    Bike1_id INT,
//    Bike2_id INT,
//    Performance_diff VARCHAR(100),
//    Cost_diff VARCHAR(100),
//    Mileage_diff VARCHAR(100),
//    FOREIGN KEY (Bike1_id) REFERENCES bike(bike_id) ON DELETE CASCADE,
//    FOREIGN KEY (Bike2_id) REFERENCES bike(bike_id) ON DELETE CASCADE
//);
