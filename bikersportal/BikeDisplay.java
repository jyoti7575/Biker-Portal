package bikersportal;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class BikeDisplay extends JFrame {
    private JLabel bikeImageLabel, bikeNameLabel, brandLabel, priceLabel, engineLabel, fuelLabel;

    public BikeDisplay(String bikeName) {
        setTitle("Bike Details - " + bikeName);
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ✅ Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.DARK_GRAY);

        // ✅ Image Panel (Top)
        bikeImageLabel = new JLabel("Loading Image...", JLabel.CENTER);
        bikeImageLabel.setPreferredSize(new Dimension(500, 250));
        bikeImageLabel.setOpaque(true);
        bikeImageLabel.setBackground(Color.BLACK);
        mainPanel.add(bikeImageLabel, BorderLayout.NORTH);

        // ✅ Details Panel (Below Image)
        JPanel detailsPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        detailsPanel.setBackground(Color.LIGHT_GRAY);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ✅ Styled Labels
        bikeNameLabel = createStyledLabel("Name: ");
        brandLabel = createStyledLabel("Brand: ");
        priceLabel = createStyledLabel("Price: ");
        engineLabel = createStyledLabel("Engine: ");
        fuelLabel = createStyledLabel("Fuel Type: ");

        detailsPanel.add(bikeNameLabel);
        detailsPanel.add(brandLabel);
        detailsPanel.add(priceLabel);
        detailsPanel.add(engineLabel);
        detailsPanel.add(fuelLabel);

        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        // ✅ Close Button
        JButton closeButton = new JButton("Close");
        closeButton.setBackground(Color.RED);
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.add(closeButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadBikeDetails(bikeName);

        setVisible(true);
    }

    private void loadBikeDetails(String bikeName) {
        String query = "SELECT * FROM Bike WHERE Name = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, bikeName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                bikeNameLabel.setText("Name: " + rs.getString("Name"));
                brandLabel.setText("Brand: " + rs.getString("Brand"));
                priceLabel.setText("Price:  ₹" + rs.getDouble("Showroom_price"));
                engineLabel.setText("Engine: " + rs.getInt("Engine_capacity") + "cc");
                fuelLabel.setText("Fuel Type: " + rs.getString("Fuel_type"));

                // ✅ Load Image
                byte[] imgBytes = rs.getBytes("Bike_Image");
                if (imgBytes != null) {
                    ImageIcon icon = new ImageIcon(imgBytes);
                    Image img = icon.getImage().getScaledInstance(500, 250, Image.SCALE_SMOOTH);
                    bikeImageLabel.setIcon(new ImageIcon(img));
                    bikeImageLabel.setText(""); // Remove text placeholder
                } else {
                    bikeImageLabel.setText("No Image Available");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.BLACK);
        return label;
    }
}
