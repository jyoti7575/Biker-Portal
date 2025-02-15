package bikersportal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class Browsing extends JFrame {
    private JComboBox<String> brandDropdown, typeDropdown, priceDropdown;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public Browsing() {
        setTitle("Bike Browsing");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] brands = getUniqueValues("Brand");
        String[] types = getUniqueValues("type");
        System.out.println(types);
        String[] priceRanges = {"All", "Under 5000", "5000 - 10000", "Above 10000"};

        brandDropdown = createStyledDropdown(brands);
        typeDropdown = createStyledDropdown(types);
        priceDropdown = createStyledDropdown(priceRanges);

        brandDropdown.addActionListener(e -> loadBikeData());
        typeDropdown.addActionListener(e -> loadBikeData());
        priceDropdown.addActionListener(e -> loadBikeData());

        String[] columns = {"Brand", "Model", "Type", "Price"};
        tableModel = new DefaultTableModel(columns, 0);
        resultTable = new JTable(tableModel);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        resultTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { 
                int selectedRow = resultTable.getSelectedRow();
                if (selectedRow != -1) {
                    String selectedBike = tableModel.getValueAt(selectedRow, 1).toString();
                    new BikeDisplay(selectedBike);
                }
            }
        });

        loadBikeData(); 

        JScrollPane tableScrollPane = new JScrollPane(resultTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Available Bikes"));

        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("Brand:"));
        filterPanel.add(brandDropdown);
        filterPanel.add(new JLabel("Type:"));
        filterPanel.add(typeDropdown);
        filterPanel.add(new JLabel("Price:"));
        filterPanel.add(priceDropdown);
        filterPanel.setBackground(new Color(0, 130, 130));

        JButton backButton = new JButton("Back to Main Menu");
        styleBlackButton(backButton);
        backButton.addActionListener(e -> {
            dispose();
            new BikerPortal();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        bottomPanel.setBackground(new Color(0, 130, 130));

        add(filterPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.PAGE_END);

        setVisible(true);
    }

    private void loadBikeData() {
    tableModel.setRowCount(0); // Clear previous data

    String selectedBrand = brandDropdown.getSelectedItem().toString();
    String selectedType = typeDropdown.getSelectedItem().toString();
    String selectedPrice = priceDropdown.getSelectedItem().toString();

    try {
        // Base API URL
        String apiUrl = "https://motorcycles-by-api-ninjas.p.rapidapi.com/v1/motorcycles";
        ArrayList<String> params = new ArrayList<>();

        // Ensure at least one parameter is sent
        if (!selectedBrand.equals("All")) {
            params.add("make=" + selectedBrand);
        }
        if (!selectedType.equals("All")) {
            params.add("type=" + selectedType);
        }

        // If no filters are selected, use a default filter (e.g., "make=Honda")
        if (params.isEmpty()) {
            params.add("make=Honda"); // Change this default brand if needed
        }

        apiUrl += "?" + String.join("&", params);

        // API Request
        HttpResponse<String> response = Unirest.get(apiUrl)
                .header("x-rapidapi-key", "e052eec525msh4561551343249bdp131ea1jsn50124a4e6188")
                .header("x-rapidapi-host", "motorcycles-by-api-ninjas.p.rapidapi.com")
                .asString();

        // Debugging output
        System.out.println("Final API URL: " + apiUrl);
        System.out.println("Response Code: " + response.getStatus());
        System.out.println("Response Body: " + response.getBody());

        if (response.getStatus() == 200) {
            JSONArray bikesArray = new JSONArray(response.getBody());
            for (int i = 0; i < bikesArray.length(); i++) {
                JSONObject bike = bikesArray.getJSONObject(i);
                String brand = bike.optString("make", "Unknown");
                String model = bike.optString("model", "Unknown");
                String type = bike.optString("type", "Unknown");
                double price = bike.optDouble("price", 0);

                if (matchesPriceFilter(price, selectedPrice)) {
                    tableModel.addRow(new Object[]{brand, model, type, "₹" + price});
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to fetch bike data. Error Code: " + response.getStatus(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to fetch bike data.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private boolean matchesPriceFilter(double price, String filter) {
        switch (filter) {
            case "Under 5000": return price < 5000;
            case "5000 - 10000": return price >= 5000 && price <= 10000;
            case "Above 10000": return price > 10000;
            default: return true;
        }
    }

    private String[] getUniqueValues(String column) {
        ArrayList<String> values = new ArrayList<>();
        values.add("All");
        String query = "SELECT DISTINCT " + column + " FROM Bike";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                values.add(rs.getString(column));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return values.toArray(new String[0]);
    }

    private JComboBox<String> createStyledDropdown(String[] items) {
        JComboBox<String> dropdown = new JComboBox<>(items);
        dropdown.setBackground(Color.BLACK);
        dropdown.setForeground(Color.WHITE);
        dropdown.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return dropdown;
    }

    private void styleBlackButton(JButton button) {
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public static void main(String[] args) {
        new Browsing();
    }
}
