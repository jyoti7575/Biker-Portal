package com.mycompany.biker_portal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class SearchResults {

    // Displays the search results in a 3-column grid of labels
    public static void displayResults(String searchText) {
        JFrame resultFrame = new JFrame("Search Bikes");
        resultFrame.setSize(800, 600);
        resultFrame.setLocationRelativeTo(null);
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a panel with a 3-column grid layout
        JPanel resultsPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        resultsPanel.setBackground(new Color(30, 30, 30));
        resultsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Query the database for matching models
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT model_name FROM models WHERE LOWER(model_name) LIKE ?")) {

            stmt.setString(1, "%" + searchText.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();

            boolean foundAny = false;
            while (rs.next()) {
                foundAny = true;
                String modelName = rs.getString("model_name");

                // Create a clickable label for each model
                JLabel modelLabel = new JLabel(modelName, SwingConstants.CENTER);
                modelLabel.setOpaque(true);
                modelLabel.setBackground(new Color(60, 60, 60));
                modelLabel.setForeground(Color.WHITE);
                modelLabel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(80, 80, 80), 2),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                modelLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

                // Hover effect for the label
                modelLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        modelLabel.setBackground(new Color(80, 80, 80));
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        modelLabel.setBackground(new Color(60, 60, 60));
                    }
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        showBikeDetails(modelName);
                    }
                });

                resultsPanel.add(modelLabel);
            }

            // If no matches, display a single label
            if (!foundAny) {
                JLabel noResultsLabel = new JLabel("No bikes found for: " + searchText, SwingConstants.CENTER);
                noResultsLabel.setOpaque(true);
                noResultsLabel.setForeground(Color.WHITE);
                noResultsLabel.setBackground(new Color(60, 60, 60));
                noResultsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                resultsPanel.add(noResultsLabel);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(resultFrame, "❌ Error fetching results!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Put resultsPanel inside a scroll pane for overflow
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));

        resultFrame.add(scrollPane);
        resultFrame.setVisible(true);
    }

    // Shows *all* columns of a selected bike model in a new window, plus brand name
    private static void showBikeDetails(String modelName) {
    JFrame detailsFrame = new JFrame("Bike Details: " + modelName);
    detailsFrame.setSize(800, 600);
    detailsFrame.setLocationRelativeTo(null);

    // Main content panel with BoxLayout for vertical stacking
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    contentPanel.setBackground(new Color(45, 45, 45));
    contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

    // Image panel with FlowLayout (centered)
    JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
    imagePanel.setBackground(contentPanel.getBackground());
    
    JLabel imageLabel = new JLabel();
    imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    imageLabel.setOpaque(true);
    imageLabel.setBackground(new Color(45, 45, 45));
    imagePanel.add(imageLabel);
    contentPanel.add(imagePanel);

    // Use JTextPane for better HTML rendering of details
    JTextPane detailsPane = new JTextPane();
    detailsPane.setContentType("text/html");
    detailsPane.setEditable(false);
    detailsPane.setBackground(contentPanel.getBackground());
    detailsPane.setForeground(Color.WHITE);
    detailsPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
    contentPanel.add(detailsPane);

    // Create scroll pane after adding all components
    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.getViewport().setBackground(contentPanel.getBackground());

    // Query for bike details, including brand name
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(
                 "SELECT m.*, b.name AS brandname " +
                 "FROM models m " +
                 "JOIN brands b ON m.brand_id = b.id " +
                 "WHERE LOWER(m.model_name) = LOWER(?)")) {

        stmt.setString(1, modelName);
        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            JOptionPane.showMessageDialog(detailsFrame,
                    "No details found for '" + modelName + "'",
                    "Not Found",
                    JOptionPane.WARNING_MESSAGE);
            detailsFrame.dispose();
            return;
        }

        // Build HTML structure for displaying all details
        StringBuilder html = new StringBuilder();
        html.append("<html><head><style>")
            .append("body { font-family: Arial; margin: 0; padding: 0; color: #fff; background-color: #2e2e2e; }")
            .append(".spec-table { width: 100%; border-collapse: collapse; margin-top: 15px; font-size: 13px; }")
            .append(".spec-table tr { border-bottom: 1px solid #555; }")
            .append(".spec-table td { padding: 6px 8px; text-align: left; }")
            .append(".spec-header { width: 140px; color: #ccc; }")
            .append("h2 { text-align: center; margin: 10px 0; color: #4CAF50; }")
            .append("</style></head><body>");
        html.append("<h2>").append(modelName).append("</h2>");

        // Handle image separately
        String imagePath = rs.getString("image_url");
        if (imagePath != null && !imagePath.isEmpty()) {
            ImageIcon icon = new ImageIcon(imagePath);
            // Scale the image to maintain aspect ratio (max width 400, height 250)
            int maxWidth = 400, maxHeight = 250;
            Image scaled = icon.getImage().getScaledInstance(maxWidth, maxHeight, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        } else {
            imageLabel.setText("<html><center><span style='color:#cccccc;'>No image available</span></center></html>");
        }

        // Build a table of all columns (skip brand_id and image_url)
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();
        html.append("<table class='spec-table'>");
        for (int i = 1; i <= columnCount; i++) {
            String colName = meta.getColumnName(i).toLowerCase();
            if (colName.equals("brand_id") || colName.equals("image_url"))
                continue;
            String val = rs.getString(i);
            if (val == null || val.trim().isEmpty())
                val = "N/A";
            // Format displacement if needed
            if (colName.equals("displacement")) {
                String numeric = val.replaceAll("[^0-9.]", "");
                val = numeric.isEmpty() ? "N/A" : numeric + "cc";
            }
            if (colName.equals("brandname"))
                colName = "brand";
            html.append("<tr>")
                .append("<td class='spec-header'><b>").append(colName).append("</b></td>")
                .append("<td>").append(val).append("</td>")
                .append("</tr>");
        }
        html.append("</table>");
        html.append("</body></html>");

        detailsPane.setText(html.toString());

    } catch (SQLException e) {
        System.err.println("❌ SQL Error: " + e.getMessage());
        e.printStackTrace();
        JOptionPane.showMessageDialog(detailsFrame,
                "❌ Error fetching details!\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    detailsFrame.add(scrollPane, BorderLayout.CENTER);
    detailsFrame.setVisible(true);
}

}
