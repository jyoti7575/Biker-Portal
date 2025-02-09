
package bikersportal;

import java.sql.*;

public class DBConnection{
    static String url = "jdbc:mysql:///bikers_db";
    static String user = "root";
    static String pass = "Haggu@123";
    
    public static Connection connect() throws SQLException{
        try{
            return DriverManager.getConnection(url,user,pass);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
