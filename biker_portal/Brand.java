package com.mycompany.biker_portal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Brand extends JFrame {
    private static DatabaseManager dbManager = new DatabaseManager();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Brand::createAndShowGUI);
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("🏍️ Bikers Portal - Brands");
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(18, 18, 18));

        JPanel brandPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        brandPanel.setBackground(new Color(24, 24, 24));

        ArrayList<String> brands = dbManager.fetchBrands();
        for (String brand : brands) {
            JLabel brandLabel = createStyledLabel(brand, new Color(50, 50, 50), Color.WHITE, 16);
            brandLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    openBrandUI(brand);
                }
            });
            brandPanel.add(brandLabel);
        }

        JScrollPane scrollPane = new JScrollPane(brandPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void openBrandUI(String brand) {
        JFrame brandFrame = new JFrame(brand);
        brandFrame.setSize(600, 400);
        brandFrame.setLayout(new BorderLayout());
        brandFrame.getContentPane().setBackground(new Color(18, 18, 18));

        JLabel titleLabel = createStyledLabel(brand, new Color(24, 24, 24), Color.ORANGE, 22);
        brandFrame.add(titleLabel, BorderLayout.NORTH);

        JPanel modelPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        modelPanel.setBackground(new Color(24, 24, 24));

        ArrayList<String> models = dbManager.fetchModels(brand);
        for (String model : models) {
            JLabel modelLabel = createStyledLabel(model, new Color(50, 50, 50), Color.WHITE, 14);
            modelLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    openBikeDetails(model);
                }
            });
            modelPanel.add(modelLabel);
        }

        JScrollPane modelScrollPane = new JScrollPane(modelPanel);
        modelScrollPane.setBorder(null);
        modelScrollPane.getVerticalScrollBar().setUnitIncrement(15);

        brandFrame.add(modelScrollPane, BorderLayout.CENTER);
        brandFrame.setLocationRelativeTo(null);
        brandFrame.setVisible(true);
    }

    private static void openBikeDetails(String modelName) {
        JFrame bikeFrame = new JFrame(modelName);
        bikeFrame.setSize(600, 500);
        bikeFrame.setLayout(new BorderLayout());
        bikeFrame.getContentPane().setBackground(new Color(18, 18, 18));

        JPanel bikePanel = fetchBikeDetails(modelName);

        if (bikePanel != null) {
            bikeFrame.add(bikePanel, BorderLayout.CENTER);
        } else {
            JLabel errorLabel = new JLabel("⚠️ Bike details not found!", SwingConstants.CENTER);
            errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            errorLabel.setForeground(Color.RED);
            bikeFrame.add(errorLabel, BorderLayout.CENTER);
        }

        bikeFrame.setLocationRelativeTo(null);
        bikeFrame.setVisible(true);
    }

    private static JPanel fetchBikeDetails(String modelName) {
        JPanel bikePanel = new JPanel(new BorderLayout(10, 10));
        bikePanel.setBackground(new Color(35, 35, 45));
        bikePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT b.name AS brand_name, m.* FROM models m " +
                           "JOIN brands b ON m.brand_id = b.id WHERE m.model_name = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, modelName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String imagePath = rs.getString("image_url");

                // 🔹 Display Bike Image
                ImageIcon imageIcon = new ImageIcon(imagePath);
                Image scaledImage = imageIcon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                bikePanel.add(imageLabel, BorderLayout.NORTH);

                // 🔹 Specifications Table
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

    private static JLabel createStyledLabel(String text, Color bgColor, Color fgColor, int fontSize) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        label.setOpaque(true);
        label.setBackground(bgColor);
        label.setForeground(fgColor);
        label.setPreferredSize(new Dimension(200, 50));
        addHoverEffect(label, bgColor);
        return label;
    }

    private static void addHoverEffect(JLabel label, Color defaultColor) {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setBackground(defaultColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setBackground(defaultColor);
            }
        });
    }
}
