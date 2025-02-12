package bikerportal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class searching extends JFrame {
    private JTextField searchField;
    private JComboBox<String> fuelTypeDropdown, engineCapacityDropdown, priceDropdown;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    // ✅ Sample Bike Data (Model, Fuel Type, Engine Capacity, Price)
    private final String[][] bikeData = {
        {"Yamaha R1", "Petrol", "998cc", "$15,000"},
        {"Honda CBR600RR", "Petrol", "599cc", "$9,000"},
        {"Kawasaki KX250", "Petrol", "249cc", "$7,500"},
        {"Suzuki RMZ450", "Petrol", "450cc", "$6,000"},
        {"Giant Escape 3", "None", "Hybrid", "$4,000"},
        {"Harley Davidson Street 750", "Petrol", "749cc", "$12,000"},
        {"Revolt RV400", "Electric", "N/A", "$3,500"}
    };

    public searching() {
        setTitle("Bike Searching");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ✅ Search Field
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        styleBlackComponent(searchButton); // ✅ Apply black styling to search button

        // ✅ Filters
        String[] fuelTypes = {"All", "Petrol", "Electric", "Hybrid"};
        String[] engineCapacities = {"All", "Below 300cc", "300cc - 600cc", "Above 600cc"};
        String[] priceRanges = {"All", "Under $5000", "$5000 - $10000", "Above $10000"};

        fuelTypeDropdown = new JComboBox<>(fuelTypes);
        engineCapacityDropdown = new JComboBox<>(engineCapacities);
        priceDropdown = new JComboBox<>(priceRanges);

        // ✅ Apply Black Styling to Dropdowns
        styleBlackComponent(fuelTypeDropdown);
        styleBlackComponent(engineCapacityDropdown);
        styleBlackComponent(priceDropdown);

        // ✅ Table Setup
        String[] columns = {"Model Name", "Fuel Type", "Engine Capacity", "Price"};
        tableModel = new DefaultTableModel(columns, 0);
        resultTable = new JTable(tableModel);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // ✅ Search & Filter Panel (Top)
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchPanel.setBackground(new Color(0, 130, 130));

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // ✅ Filter Panel (Middle)
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());
        filterPanel.add(new JLabel("Fuel Type:"));
        filterPanel.add(fuelTypeDropdown);
        filterPanel.add(new JLabel("Engine Capacity:"));
        filterPanel.add(engineCapacityDropdown);
        filterPanel.add(new JLabel("Price:"));
        filterPanel.add(priceDropdown);

        // ✅ Table Panel (Bottom)
        JScrollPane tableScrollPane = new JScrollPane(resultTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Search Results"));

        // ✅ Back Button
        JButton backButton = new JButton("Back to Main Menu");
        styleBlackComponent(backButton); // ✅ Apply black styling to back button
        backButton.addActionListener(e -> {
            dispose(); // Close Searching Window
            new BikerPortal(); // Return to Main Menu
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(0, 130, 130));
        bottomPanel.add(backButton);

        // ✅ Event Listeners for Filtering
        searchButton.addActionListener(e -> filterBikes());
        fuelTypeDropdown.addActionListener(e -> filterBikes());
        engineCapacityDropdown.addActionListener(e -> filterBikes());
        priceDropdown.addActionListener(e -> filterBikes());

        // ✅ Add Components to Layout
        add(searchPanel, BorderLayout.NORTH);
        add(filterPanel, BorderLayout.CENTER);
        add(tableScrollPane, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.PAGE_END);

        filterBikes(); // ✅ Load Initial Data
        setVisible(true);
    }

    private void filterBikes() {
        String searchQuery = searchField.getText().toLowerCase();
        String selectedFuelType = (String) fuelTypeDropdown.getSelectedItem();
        String selectedEngineCapacity = (String) engineCapacityDropdown.getSelectedItem();
        String selectedPrice = (String) priceDropdown.getSelectedItem();

        tableModel.setRowCount(0); // ✅ Clear Table

        for (String[] bike : bikeData) {
            String model = bike[0].toLowerCase();
            String fuelType = bike[1];
            String engineCapacity = bike[2];
            String price = bike[3];

            // ✅ Apply Filters
            if (!searchQuery.isEmpty() && !model.contains(searchQuery)) continue;
            if (!selectedFuelType.equals("All") && !fuelType.equals(selectedFuelType)) continue;
            if (!selectedEngineCapacity.equals("All") && !matchesEngineCapacity(engineCapacity, selectedEngineCapacity)) continue;
            if (!selectedPrice.equals("All") && !matchesPrice(price, selectedPrice)) continue;

            // ✅ Add Matching Bike to Table
            tableModel.addRow(new String[]{bike[0], fuelType, engineCapacity, price});
        }
    }

    private boolean matchesEngineCapacity(String engine, String filter) {
        if (engine.equals("N/A")) return false;
        int cc = Integer.parseInt(engine.replaceAll("[^0-9]", ""));
        switch (filter) {
            case "Below 300cc": return cc < 300;
            case "300cc - 600cc": return cc >= 300 && cc <= 600;
            case "Above 600cc": return cc > 600;
            default: return true;
        }
    }

    private boolean matchesPrice(String price, String filter) {
        int priceValue = Integer.parseInt(price.replaceAll("[^0-9]", ""));
        switch (filter) {
            case "Under $5000": return priceValue < 5000;
            case "$5000 - $10000": return priceValue >= 5000 && priceValue <= 10000;
            case "Above $10000": return priceValue > 10000;
            default: return true;
        }
    }

    private void styleBlackComponent(JComponent component) {
        component.setBackground(Color.BLACK);
        component.setForeground(Color.WHITE);
        if (component instanceof JButton) {
            component.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
    }

    public static void main(String[] args) {
        new searching();
    }
}
