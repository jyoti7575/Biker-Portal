package com.mycompany.biker_portal;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class Displacement extends JFrame {

    // 1) Define fixed displacement ranges
    private static final Map<String, int[]> DISPLACEMENT_RANGES = new LinkedHashMap<>();
    static {
        DISPLACEMENT_RANGES.put("100 - 150cc", new int[]{100, 150});
        DISPLACEMENT_RANGES.put("150 - 200cc", new int[]{150, 200});
        DISPLACEMENT_RANGES.put("200 - 300cc", new int[]{200, 300});
        DISPLACEMENT_RANGES.put("300 - 400cc", new int[]{300, 400});
        DISPLACEMENT_RANGES.put("400 - 600cc", new int[]{400, 600});
        DISPLACEMENT_RANGES.put("600 - 900cc", new int[]{600, 900});
        DISPLACEMENT_RANGES.put("900 - 1200cc", new int[]{900, 1200});
        DISPLACEMENT_RANGES.put("1200cc & Above", new int[]{1200, Integer.MAX_VALUE});
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Displacement::createAndShowGUI);
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Bikers Portal - Displacement");
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30)); // Dark background

        // 2) Create a panel with range labels
        JPanel displacementPanel = createDisplacementPanel();
        frame.add(displacementPanel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Creates a grid of labels, one for each range
    private static JPanel createDisplacementPanel() {
        JPanel displacementPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        displacementPanel.setBackground(new Color(30, 30, 30));

        // For each range, create a label
        for (String range : DISPLACEMENT_RANGES.keySet()) {
            JLabel label = new JLabel(range, SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setOpaque(true);
            label.setBackground(new Color(60, 63, 65));
            label.setForeground(Color.WHITE);
            label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));
            label.setPreferredSize(new Dimension(180, 60));
            addHoverEffect(label);

            // On click, show bikes that fall into that range
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showBikesInRange(range);
                }
            });

            displacementPanel.add(label);
        }

        return displacementPanel;
    }

    // 3) On label click, fetch all bikes and filter them by range
    private static void showBikesInRange(String rangeKey) {
        int[] range = DISPLACEMENT_RANGES.get(rangeKey);
        int min = range[0];
        int max = range[1];

        JFrame frame = new JFrame("Bikes in " + rangeKey);
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30)); // Dark background

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        // 4) Define columns with Model Name first
        model.addColumn("Model Name");
        model.addColumn("Brand");
        model.addColumn("Displacement (cc)");
        model.addColumn("Power (HP)");
        model.addColumn("Showroom Price (₹)");
        model.addColumn("On Road Price (₹)");

        // Dark theme for table
        table.setBackground(new Color(45, 45, 45));
        table.setForeground(Color.WHITE);
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(70, 70, 70));
        table.setSelectionForeground(Color.WHITE);

        // Customize table header
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(50, 50, 50));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));

        // Center-align table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        // Fetch all bikes from DB (or just relevant columns)
        String sql = "SELECT m.model_name, b.name AS brand, m.displacement, m.power, m.showroom_price, m.on_road_price "
                   + "FROM models m "
                   + "JOIN brands b ON m.brand_id = b.id ";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Parse displacement from strings like "124.8cc" or "NA"
                String dispStr = rs.getString("displacement");
                double dispValue = extractNumericDisplacement(dispStr);

                // Check if it falls within [min, max]
                if (dispValue >= min && dispValue <= max) {
                    // Extract power from strings like "47 HP"
                    String powerStr = rs.getString("power");
                    float powerVal = extractNumericPower(powerStr);

                    model.addRow(new Object[]{
                            rs.getString("model_name"),   // Model Name first
                            rs.getString("brand"),
                            dispStr,                      // Displacement raw string
                            powerVal,
                            rs.getInt("showroom_price"),
                            rs.getInt("on_road_price")
                    });
                }
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "No bikes found in this range.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(table);
        // Dark scroll pane background
        scrollPane.getViewport().setBackground(new Color(45, 45, 45));
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Extracts numeric displacement from strings like "108.4cc" or "NA".
     * Returns 0 if parsing fails or if it's "NA".
     */
    private static double extractNumericDisplacement(String displacement) {
        if (displacement == null) return 0;
        try {
            // Remove all non-digit and non-decimal characters
            String numeric = displacement.replaceAll("[^0-9.]", "");
            if (numeric.isEmpty()) return 0;
            return Double.parseDouble(numeric);
        } catch (NumberFormatException e) {
            return 0; // fallback if we can't parse
        }
    }

    /**
     * Extracts numeric power from strings like "47 HP" or "5.9HP".
     * Returns 0 if parsing fails.
     */
    private static float extractNumericPower(String power) {
        if (power == null) return 0f;
        try {
            String numeric = power.replaceAll("[^0-9.]", "");
            if (numeric.isEmpty()) return 0f;
            return Float.parseFloat(numeric);
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    // Hover effect for each label
    private static void addHoverEffect(JLabel label) {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setBackground(new Color(90, 93, 95));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setBackground(new Color(60, 63, 65));
            }
        });
    }
}
