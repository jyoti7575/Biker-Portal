package com.mycompany.biker_portal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CompareWindow {
    private JFrame frame;
    private JComboBox<String> bike1Dropdown;
    private JComboBox<String> bike2Dropdown;

    public void open() {
        frame = new JFrame("Compare Bikes");
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(25, 25, 35));

        JPanel selectionPanel = new JPanel(new GridBagLayout());
        selectionPanel.setBackground(new Color(35, 35, 45));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        bike1Dropdown = createStyledDropdown();
        bike2Dropdown = createStyledDropdown();

        JLabel bike1Label = createStyledLabel("Select Bike 1:");
        JLabel bike2Label = createStyledLabel("Select Bike 2:");

        gbc.gridx = 0;
        gbc.gridy = 0;
        selectionPanel.add(bike1Label, gbc);
        gbc.gridx = 1;
        selectionPanel.add(bike1Dropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        selectionPanel.add(bike2Label, gbc);
        gbc.gridx = 1;
        selectionPanel.add(bike2Dropdown, gbc);

        JButton compareButton = createStyledButton("Start Comparison");
        compareButton.addActionListener(e -> displayComparison());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        selectionPanel.add(compareButton, gbc);

        frame.add(selectionPanel, BorderLayout.NORTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JComboBox<String> createStyledDropdown() {
        JComboBox<String> dropdown = new JComboBox<>(fetchAllBikeModels().toArray(new String[0]));
        dropdown.setBackground(new Color(50, 50, 60));
        dropdown.setForeground(Color.WHITE);
        dropdown.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dropdown.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 110), 1));
        dropdown.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return dropdown;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.LIGHT_GRAY);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        return label;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 150, 255));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private ArrayList<String> fetchAllBikeModels() {
        ArrayList<String> models = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT model_name FROM models";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                models.add(rs.getString("model_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return models;
    }

    private void displayComparison() {
    String selectedBike1 = (String) bike1Dropdown.getSelectedItem();
    String selectedBike2 = (String) bike2Dropdown.getSelectedItem();

    if (selectedBike1 == null || selectedBike2 == null || selectedBike1.equals(selectedBike2)) {
        JOptionPane.showMessageDialog(frame, "Please select two different bikes.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    JPanel bike1Panel = fetchBikeDetails(selectedBike1);
    JPanel bike2Panel = fetchBikeDetails(selectedBike2);

    if (bike1Panel != null && bike2Panel != null) {
        JFrame comparisonFrame = new JFrame("Comparison: " + selectedBike1 + " vs " + selectedBike2);
        comparisonFrame.setSize(1200, 700);
        comparisonFrame.setLayout(new GridLayout(1, 3, 0, 0)); // Removed spacing
        comparisonFrame.getContentPane().setBackground(new Color(25, 25, 35));

        JLabel vsLabel = new JLabel("VS");
        vsLabel.setForeground(new Color(255, 90, 90));
        vsLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        vsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        comparisonFrame.add(bike1Panel);
        comparisonFrame.add(vsLabel);
        comparisonFrame.add(bike2Panel);

        comparisonFrame.setLocationRelativeTo(null);
        comparisonFrame.setVisible(true);
    } else {
        JOptionPane.showMessageDialog(frame, "Error fetching bike details.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private JPanel fetchBikeDetails(String modelName) {
        JPanel bikePanel = new JPanel(new BorderLayout(10, 10));
        bikePanel.setBackground(new Color(35, 35, 45));
        bikePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT b.name AS brand_name, m.* FROM models m JOIN brands b ON m.brand_id = b.id WHERE m.model_name = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, modelName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String imagePath = rs.getString("image_url");
                ImageIcon imageIcon = new ImageIcon(imagePath);
                Image scaledImage = imageIcon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                bikePanel.add(imageLabel, BorderLayout.NORTH);

                // Table model
                DefaultTableModel tableModel = new DefaultTableModel();
                tableModel.addColumn("Specification");
                tableModel.addColumn("Value");

                tableModel.addRow(new Object[]{"Brand", rs.getString("brand_name")});
                tableModel.addRow(new Object[]{"Model", rs.getString("model_name")});

                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    String columnName = rs.getMetaData().getColumnName(i);
                    String value = rs.getString(i);
                    if (!columnName.equalsIgnoreCase("id") &&
                        !columnName.equalsIgnoreCase("brand_id") &&
                        !columnName.equalsIgnoreCase("image_url") &&
                        !columnName.equalsIgnoreCase("model_name")) {
                        
                        tableModel.addRow(new Object[]{columnName.replace("_", " "), (value != null ? value : "N/A")});
                    }
                }

                JTable bikeTable = new JTable(tableModel);
                bikeTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                bikeTable.setForeground(Color.WHITE);
                bikeTable.setBackground(new Color(35, 35, 45));
                bikeTable.setRowHeight(25);

                JScrollPane scrollPane = new JScrollPane(bikeTable);
                scrollPane.setBorder(BorderFactory.createEmptyBorder());
                bikePanel.add(scrollPane, BorderLayout.CENTER);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return bikePanel;
    }
}
