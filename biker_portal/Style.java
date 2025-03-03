package com.mycompany.biker_portal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Style extends JFrame {
    private static final DatabaseManager dbManager = new DatabaseManager();

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Bike Types");
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout());
        // frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Dark background for the main frame
        frame.getContentPane().setBackground(new Color(18, 18, 18));

        JLabel titleLabel = new JLabel("Select Bike Type", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        // Slightly reduced top/bottom spacing
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        frame.add(titleLabel, BorderLayout.NORTH);

        try {
            List<String> types = dbManager.fetchDistinctValues("type");
            frame.add(createTypePanel(types), BorderLayout.CENTER);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error loading bike types: " + e.getMessage());
            e.printStackTrace();
        }

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Creates a 3-column grid of bike-type labels.
     */
    private static JPanel createTypePanel(List<String> types) {
        // 3 columns, small spacing
        JPanel typesPanel = new JPanel(new GridLayout(0, 3, 5, 5));
        typesPanel.setBackground(new Color(18, 18, 18));
        // Slightly reduced outer margin
        typesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (String type : types) {
            JLabel typeLabel = new JLabel(type, SwingConstants.CENTER);
            typeLabel.setFont(new Font("Arial", Font.BOLD, 16));
            typeLabel.setOpaque(true);
            typeLabel.setBackground(new Color(40, 40, 40));
            typeLabel.setForeground(Color.WHITE);
            typeLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            typeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addTypeHoverEffect(typeLabel);

            // Clicking on a label shows all bikes of this type
            typeLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    openTypeDetailsUI(type);
                }
            });

            typesPanel.add(typeLabel);
        }
        return typesPanel;
    }

    /**
     * Opens a new window displaying bikes for the given type.
     */
    private static void openTypeDetailsUI(String typeName) {
        JFrame typeFrame = new JFrame("Bikes in " + typeName);
        typeFrame.setSize(1200, 800);
        typeFrame.setLayout(new BorderLayout());
        // Dark background for the details frame
        typeFrame.getContentPane().setBackground(new Color(28, 28, 28));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(28, 28, 28));
        // Reduced margin around the content
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(typeName + " Bikes", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        // Slightly reduced spacing
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        // 3-column grid for bike cards, minimal spacing
        JPanel modelPanel = new JPanel(new GridLayout(0, 3, 5, 5));
        modelPanel.setBackground(new Color(28, 28, 28));

        // Fetch the bikes from DB
        ArrayList<Map<String, String>> models = dbManager.fetchModelsByType(typeName);

        for (Map<String, String> bike : models) {
            // Each bike is a panel with image (left) and details (right)
            JPanel bikePanel = new JPanel(new BorderLayout(5, 0));
            bikePanel.setBackground(new Color(38, 38, 38));
            bikePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));

            // (1) Image label on the left
            JLabel imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setOpaque(true);
            imageLabel.setBackground(new Color(38, 38, 38));

            String imagePath = bike.get("image_url");
            if (imagePath != null && !imagePath.isEmpty()) {
                ImageIcon icon = new ImageIcon(imagePath);
                // Scale the image to ~220x130
                Image scaledImage = icon.getImage().getScaledInstance(220, 130, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaledImage);
                imageLabel.setIcon(icon);
            } else {
                imageLabel.setText("<html><span style='color:#cccccc;'>No image available</span></html>");
            }

            // (2) Text details on the right
            String detailsHtml = "<html>"
                    + "<div style='background:#3a3a3a; border-radius:6px; padding:10px; color:#ecf0f1;'>"
                    + "<b style='font-size:15px; color:#ffffff;'>" + bike.get("model_name") + "</b><br>"
                    + "<span style='color:#cccccc;'>" + bike.get("brand") + "</span><br><br>"
                    + "<table style='width:100%; font-family: Arial; font-size:13px; color:#ecf0f1;'>"
                    + "<tr><td><b>Displacement:</b></td><td>" + bike.get("displacement") + "</td></tr>"
                    + "<tr><td><b>Power:</b></td><td>" + bike.get("power") + " HP</td></tr>"
                    + "<tr><td><b>Torque:</b></td><td>" + bike.get("torque") + " Nm</td></tr>"
                    + "<tr><td><b>Price:</b></td><td>₹" + bike.get("on_road_price") + "</td></tr>"
                    + "<tr><td><b>Mileage:</b></td><td>" + bike.get("fuel_consumption") + " km/l</td></tr>"
                    + "</table>"
                    + "</div>"
                    + "</html>";

            JLabel detailLabel = new JLabel(detailsHtml);
            detailLabel.setOpaque(true);
            detailLabel.setBackground(new Color(38, 38, 38));

            bikePanel.add(imageLabel, BorderLayout.WEST);
            bikePanel.add(detailLabel, BorderLayout.CENTER);

            // Hover effect on the entire card
            addDetailHoverEffect(bikePanel);

            // Add card to the modelPanel
            modelPanel.add(bikePanel);
        }

        JScrollPane scrollPane = new JScrollPane(modelPanel);
        scrollPane.getViewport().setBackground(new Color(28, 28, 28));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        typeFrame.add(contentPanel);
        typeFrame.setLocationRelativeTo(null);
        typeFrame.setVisible(true);
    }

    /**
     * Hover effect for the type labels.
     */
    private static void addTypeHoverEffect(JLabel label) {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setBackground(new Color(60, 60, 60));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                label.setBackground(new Color(40, 40, 40));
            }
        });
    }

    /**
     * Hover effect for the bike card (image + text).
     */
    private static void addDetailHoverEffect(JPanel panel) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(48, 48, 48));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(new Color(38, 38, 38));
            }
        });
    }
}
