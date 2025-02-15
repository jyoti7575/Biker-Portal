package bikersportal;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Comparison extends JFrame {
    private JLabel bikeImageLabel1, bikeImageLabel2;
    private JLabel[] bike1Labels, bike2Labels;
    private String bike1, bike2;

    public Comparison(String bike1, String bike2) {
        this.bike1 = bike1;
        this.bike2 = bike2;

        setTitle("Bike Comparison - " + bike1 + " vs " + bike2);
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        //   Background Panel
        JPanel backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setBackground(new Color(25, 25, 25));

        //   Image Panel
        JPanel imagePanel = new JPanel(new GridLayout(1, 2, 20, 0));
        imagePanel.setBackground(new Color(30, 30, 30));

        bikeImageLabel1 = createImageLabel();
        bikeImageLabel2 = createImageLabel();
        imagePanel.add(bikeImageLabel1);
        imagePanel.add(bikeImageLabel2);

        //   Specification Panel
        JPanel specPanel = new JPanel(new GridLayout(6, 3, 10, 10));
        specPanel.setBackground(new Color(40, 40, 40));
        specPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        //   Labels for Comparison
        String[] attributes = {"Name", "Brand", "Price", " Engine", "Fuel Type"};
        bike1Labels = new JLabel[attributes.length];
        bike2Labels = new JLabel[attributes.length];

        for (int i = 0; i < attributes.length; i++) {
            JLabel attrLabel = createStyledLabel(attributes[i], Color.ORANGE);
            bike1Labels[i] = createStyledLabel("-", Color.WHITE);
            bike2Labels[i] = createStyledLabel("-", Color.WHITE);

            specPanel.add(bike1Labels[i]);
            specPanel.add(attrLabel);
            specPanel.add(bike2Labels[i]);
        }

        //   Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(25, 25, 25));

        JButton closeButton = new JButton("Close");
        styleButton(closeButton);
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(closeButton);

        //   Adding Components
        backgroundPanel.add(imagePanel, BorderLayout.NORTH);
        backgroundPanel.add(specPanel, BorderLayout.CENTER);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(backgroundPanel);
        loadBikeDetails();
        setVisible(true);
    }

    private void loadBikeDetails() {
        String query = "SELECT * FROM Bike WHERE Name = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt1 = conn.prepareStatement(query);
             PreparedStatement pstmt2 = conn.prepareStatement(query)) {

            pstmt1.setString(1, bike1);
            pstmt2.setString(1, bike2);

            ResultSet rs1 = pstmt1.executeQuery();
            ResultSet rs2 = pstmt2.executeQuery();

            if (rs1.next()) updateBikeInfo(rs1, bike1Labels, bikeImageLabel1);
            if (rs2.next()) updateBikeInfo(rs2, bike2Labels, bikeImageLabel2);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateBikeInfo(ResultSet rs, JLabel[] labels, JLabel imageLabel) throws SQLException {
        labels[0].setText(rs.getString("Name"));
        labels[1].setText(rs.getString("Brand"));
        labels[2].setText("₹" + rs.getDouble("Showroom_price"));
        labels[3].setText(rs.getInt("Engine_capacity") + "cc");
        labels[4].setText(rs.getString("Fuel_type"));

        byte[] imgBytes = rs.getBytes("Bike_Image");
        if (imgBytes != null) {
            ImageIcon icon = new ImageIcon(imgBytes);
            Image img = icon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } else {
            imageLabel.setText("❌ No Image");
        }
    }

    private JLabel createStyledLabel(String text, Color color) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(color);
        label.setOpaque(true);
        label.setBackground(new Color(50, 50, 50));
        label.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return label;
    }

    private JLabel createImageLabel() {
        JLabel label = new JLabel("Loading...", JLabel.CENTER);
        label.setPreferredSize(new Dimension(300, 200));
        label.setOpaque(true);
        label.setBackground(Color.BLACK);
        label.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true));
        return label;
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(200, 0, 0));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
    }
}
