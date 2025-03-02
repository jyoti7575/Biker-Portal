package com.mycompany.biker_portal;

import java.sql.*;
import java.util.*;

public class DatabaseManager {

    // Fetch all brands from the database
    public ArrayList<String> fetchBrands() {
        ArrayList<String> brands = new ArrayList<>();
        String sql = "SELECT name FROM brands";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                brands.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch brands: " + e.getMessage());
        }
        return brands;
    }

    // Fetch models for a specific brand
    public ArrayList<String> fetchModels(String brandName) {
        ArrayList<String> models = new ArrayList<>();
        String sql = "SELECT model_name FROM models m JOIN brands b ON m.brand_id = b.id WHERE b.name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, brandName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                models.add(rs.getString("model_name"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch models: " + e.getMessage());
        }
        return models;
    }

    // Fetch details for a specific bike model
    public Map<String, String> fetchBikeDetails(String modelName) {
        Map<String, String> bikeDetails = new HashMap<>();
        String query = "SELECT * FROM models WHERE model_name = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, modelName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    // Store keys in lower-case for consistency in the UI
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i).toLowerCase();
                        String value = rs.getString(i);
                        bikeDetails.put(columnName, value != null ? value : "N/A");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch bike details: " + e.getMessage());
        }
        return bikeDetails;
    }
    
    // Fetch models based on budget range
    public ArrayList<String> fetchModelsByBudget(String budgetRange) {
        ArrayList<String> models = new ArrayList<>();
        String sql = "";

        switch (budgetRange) {
            case "Upto ₹50,000":
                sql = "SELECT model_name FROM models WHERE showroom_price <= 50000";
                break;
            case "₹50,000 - ₹1 Lakh":
                sql = "SELECT model_name FROM models WHERE showroom_price > 50000 AND showroom_price <= 100000";
                break;
            case "₹1 Lakh - ₹2 Lakh":
                sql = "SELECT model_name FROM models WHERE showroom_price > 100000 AND showroom_price <= 200000";
                break;
            case "Above ₹2 Lakh":
                sql = "SELECT model_name FROM models WHERE showroom_price > 200000";
                break;
            default:
                return models;
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                models.add(rs.getString("model_name"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch models by budget: " + e.getMessage());
        }
        return models;
    }

    // Fetch models based on bike type, including all details
    public ArrayList<Map<String, String>> fetchModelsByType(String type) {
        ArrayList<Map<String, String>> models = new ArrayList<>();
        String sql = "SELECT m.*, b.name AS brand FROM models m " +
                     "JOIN brands b ON m.brand_id = b.id " +
                     "WHERE m.type = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, type);
            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                while (rs.next()) {
                    Map<String, String> row = new HashMap<>();
                    // Convert column names to lower-case
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        row.put(metaData.getColumnName(i).toLowerCase(), rs.getString(i));
                    }
                    models.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return models;
    }

    // Fetch distinct values for a given column
    public List<String> fetchDistinctValues(String column) throws SQLException {
        List<String> values = new ArrayList<>();
        String sql = "SELECT DISTINCT " + column + " FROM models ORDER BY " + column;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                values.add(rs.getString(1));
            }
        }
        return values;
    }

    // Fetch displacement ranges and format them correctly
    public ArrayList<String> fetchDisplacementRanges() {
        ArrayList<String> displacementRanges = new ArrayList<>();
        String sql = "SELECT DISTINCT displacement FROM models ORDER BY CAST(displacement AS FLOAT) ASC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            ArrayList<Integer> displacements = new ArrayList<>();
            while (rs.next()) {
                String displacementStr = rs.getString("displacement").replaceAll("[^0-9.]", "");
                try {
                    int displacement = (int) Double.parseDouble(displacementStr);
                    displacements.add(displacement);
                } catch (NumberFormatException e) {
                    System.err.println("❌ Skipping invalid displacement: " + displacementStr);
                }
            }
            displacementRanges = createDisplacementRanges(displacements);
        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch displacement ranges: " + e.getMessage());
        }
        return displacementRanges;
    }

    // Generate proper displacement ranges
    private ArrayList<String> createDisplacementRanges(ArrayList<Integer> displacements) {
        ArrayList<String> ranges = new ArrayList<>();
        if (displacements.isEmpty()) return ranges;

        int step = 50; // 50cc range
        int min = displacements.get(0) / step * step;
        int max = min + step;

        for (int displacement : displacements) {
            if (displacement > max) {
                ranges.add(min + "-" + max + "cc");
                min = max;
                max += step;
            }
        }
        ranges.add(min + "-" + max + "cc");
        return ranges;
    }

    // Fetch models based on displacement range
    public ArrayList<String> fetchModelsByDisplacement(String displacementRange) {
        ArrayList<String> models = new ArrayList<>();
        String sql = "SELECT model_name FROM models WHERE displacement BETWEEN ? AND ?";

        String[] rangeParts = displacementRange.split("-");
        if (rangeParts.length != 2) return models;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, Integer.parseInt(rangeParts[0].trim()));
            stmt.setInt(2, Integer.parseInt(rangeParts[1].trim()));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                models.add(rs.getString("model_name"));
            }
        } catch (SQLException | NumberFormatException e) {
            System.err.println("❌ Failed to fetch models by displacement: " + e.getMessage());
        }
        return models;
    }
    
    // Fetch models related to "Arch" motorcycles
public ArrayList<String> fetchArchModels() {
    ArrayList<String> models = new ArrayList<>();
    String sql = "SELECT model_name FROM models WHERE model_name LIKE ? OR brand_id = (SELECT id FROM brands WHERE name = 'Arch Motorcycle')";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement stmt = connection.prepareStatement(sql)) {

        stmt.setString(1, "%Arch%");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            models.add(rs.getString("model_name"));
        }
    } catch (SQLException e) {
        System.err.println("❌ Failed to fetch Arch models: " + e.getMessage());
    }
    return models;
}



    
}
