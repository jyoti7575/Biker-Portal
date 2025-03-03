package com.mycompany.biker_portal;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Map;

public class BikeDetails extends JFrame {
    private static DatabaseManager dbManager = new DatabaseManager();

    public BikeDetails(String modelName) {
        setTitle("Bike Details - " + modelName);
        setSize(800, 600);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(18, 18, 18));

        // Fetch bike details from the database
        Map<String, String> bikeDetails = dbManager.fetchBikeDetails(modelName);

        // Top: Bike Name
        JLabel titleLabel = new JLabel(modelName, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.ORANGE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Center: Image and Details
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(24, 24, 24));

        // Bike Image
        String imagePath = bikeDetails.getOrDefault("image_url", "");
        if (!imagePath.isEmpty()) {
            File imgFile = new File(imagePath);
            System.out.println("Image path: " + imgFile.getAbsolutePath()); // Debug statement
            if (imgFile.exists()) {
                System.out.println("Image file exists."); // Debug statement
                ImageIcon bikeImage = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH));
                JLabel imageLabel = new JLabel(bikeImage);
                imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                centerPanel.add(imageLabel, BorderLayout.WEST);
            } else {
                System.out.println("Image file does not exist."); // Debug statement
                showErrorPlaceholder(centerPanel);
            }
        } else {
            System.out.println("Image path is empty."); // Debug statement
            showErrorPlaceholder(centerPanel);
        }

        // Bike Specifications
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        detailsPanel.setBackground(new Color(24, 24, 24));

        // Add bike specs dynamically
        for (Map.Entry<String, String> entry : bikeDetails.entrySet()) {
            if (!entry.getKey().equals("image_url")) {
                JLabel keyLabel = createStyledLabel(entry.getKey() + ":", new Color(24, 24, 24), Color.LIGHT_GRAY);
                JLabel valueLabel = createStyledLabel(entry.getValue(), new Color(24, 24, 24), Color.WHITE);
                detailsPanel.add(keyLabel);
                detailsPanel.add(valueLabel);
            }
        }

        centerPanel.add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Placeholder image for errors
    private void showErrorPlaceholder(JPanel panel) {
        JLabel placeholder = new JLabel("Image Not Available", SwingConstants.CENTER);
        placeholder.setForeground(Color.RED);
        placeholder.setFont(new Font("Segoe UI", Font.BOLD, 16));
        placeholder.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(placeholder, BorderLayout.WEST);
    }

    private JLabel createStyledLabel(String text, Color bgColor, Color fgColor) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setOpaque(true);
        label.setBackground(bgColor);
        label.setForeground(fgColor);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return label;
    }
}