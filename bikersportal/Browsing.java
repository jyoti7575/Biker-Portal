package bikersportal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Browsing extends JFrame {
    private JComboBox<String> brandDropdown, typeDropdown, priceDropdown;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JLabel bikeImageLabel, specLabel;

    public Browsing() {
        setTitle("Bike Browsing");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ✅ Fetch dynamic filters
        String[] brands = getUniqueValues("Brand");
        String[] types = getUniqueValues("Fuel_type");
        String[] priceRanges = {"All", "Under $5000", "$5000 - $10000", "Above $10000"};

        brandDropdown = createStyledDropdown(brands);
        typeDropdown = createStyledDropdown(types);
        priceDropdown = createStyledDropdown(priceRanges);

        brandDropdown.addActionListener(e -> loadBikeData());
        typeDropdown.addActionListener(e -> loadBikeData());
        priceDropdown.addActionListener(e -> loadBikeData());

        // ✅ Table for Bike Listings
        String[] columns = {"Brand", "Model", "Type", "Price"};
        tableModel = new DefaultTableModel(columns, 0);
        resultTable = new JTable(tableModel);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.setBackground(Color.WHITE);
        resultTable.setOpaque(true);

        loadBikeData(); // Load data from database

        // ✅ Scroll Pane
        JScrollPane tableScrollPane = new JScrollPane(resultTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Available Bikes"));

        // ✅ Image & Specifications Panel
        bikeImageLabel = new JLabel();
        bikeImageLabel.setPreferredSize(new Dimension(250, 200));

        specLabel = new JLabel("Select a bike to see details");
        specLabel.setVerticalAlignment(JLabel.TOP);

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Bike Details"));
        detailsPanel.add(bikeImageLabel, BorderLayout.WEST);
        detailsPanel.add(specLabel, BorderLayout.CENTER);
        detailsPanel.setBackground(new Color(0,130,130));

        // ✅ Top Panel (Filters)
        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("Brand:"));
        filterPanel.add(brandDropdown);
        filterPanel.add(new JLabel("Type:"));
        filterPanel.add(typeDropdown);
        filterPanel.add(new JLabel("Price:"));
        filterPanel.add(priceDropdown);
        filterPanel.setBackground(new Color(0,130,130));

        // ✅ Back Button
        JButton backButton = new JButton("Back to Main Menu");
        styleBlackButton(backButton);
        backButton.addActionListener(e -> {
            dispose();
            new BikerPortal();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        bottomPanel.setBackground(new Color(0,130,130));

        // ✅ Apply Background Color to JFrame Content Pane
        getContentPane().setBackground(Color.WHITE);

        // ✅ Add Components to Layout
        add(filterPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(detailsPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.PAGE_END);

        setVisible(true);
    }

    private void loadBikeData() {
        tableModel.setRowCount(0); // Clear table before fetching data
        String selectedBrand = brandDropdown.getSelectedItem().toString();
        String selectedType = typeDropdown.getSelectedItem().toString();
        String selectedPrice = priceDropdown.getSelectedItem().toString();
        
        String query = "SELECT Brand, Name, Fuel_type, Showroom_price FROM Bike WHERE 1=1";
        if (!selectedBrand.equals("All")) query += " AND Brand = '" + selectedBrand + "'";
        if (!selectedType.equals("All")) query += " AND Fuel_type = '" + selectedType + "'";
        if (selectedPrice.equals("Under $5000")) query += " AND Showroom_price < 5000";
        else if (selectedPrice.equals("$5000 - $10000")) query += " AND Showroom_price BETWEEN 5000 AND 10000";
        else if (selectedPrice.equals("Above $10000")) query += " AND Showroom_price > 10000";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String brand = rs.getString("Brand");
                String model = rs.getString("Name");
                String type = rs.getString("Fuel_type");
                double price = rs.getDouble("Showroom_price");
                tableModel.addRow(new Object[]{brand, model, type, "$" + price});
            }
        } catch (SQLException e) {
            e.printStackTrace();
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