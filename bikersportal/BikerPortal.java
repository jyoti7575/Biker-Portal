package bikersportal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BikerPortal extends JFrame {
    
    protected JButton browseButton, searchButton;

    public BikerPortal() {
        setTitle("Biker's Portal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

       
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(0, 130, 130));
        topPanel.setPreferredSize(new Dimension(800, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 1;

        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setOpaque(false);

        ImageIcon originalIcon = new ImageIcon("D:/PROJECTS/Biker's Portal/logo.png");
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);

        JLabel logoLabel = new JLabel(resizedIcon);
        JLabel titleLabel = new JLabel("Biker's Portal");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        leftPanel.add(logoLabel);
        leftPanel.add(titleLabel);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        centerPanel.setOpaque(false);
        JTextField searchField = new JTextField(20);
        searchButton = new JButton("Search");
        Color buttonColor = new Color(0, 0, 0);
        Color textColor = Color.WHITE;
        searchButton.setBackground(buttonColor);
        searchButton.setForeground(textColor);
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        centerPanel.add(searchField);
        centerPanel.add(searchButton);
       
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);
        JButton compareButton = new JButton("Compare");
    
        compareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] bikeNames = fetchBikeNames();
                
                if (bikeNames == null || bikeNames.length < 2) {
                    JOptionPane.showMessageDialog(null, "Not enough bikes in the database to compare!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JComboBox<String> bike1Dropdown = new JComboBox<>(bikeNames);
                JComboBox<String> bike2Dropdown = new JComboBox<>(bikeNames);

                JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
                panel.add(new JLabel("Select First Bike:"));
                panel.add(bike1Dropdown);
                panel.add(new JLabel("Select Second Bike:"));
                panel.add(bike2Dropdown);

                int result = JOptionPane.showConfirmDialog(null, panel, "Select Bikes for Comparison", JOptionPane.OK_CANCEL_OPTION);
                
                if (result == JOptionPane.OK_OPTION) {
                    String bike1 = (String) bike1Dropdown.getSelectedItem();
                    String bike2 = (String) bike2Dropdown.getSelectedItem();

                    if (!bike1.equals(bike2)) {
                        new Comparison(bike1, bike2);
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select two different bikes!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        browseButton = new JButton("Browse");

        JButton addbikes = new JButton("Add Bikes");

        JButton[] topPanelButtons = {compareButton, browseButton, searchButton, addbikes};
        for (JButton button : topPanelButtons) {
            button.setBackground(buttonColor);
            button.setForeground(textColor);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        rightPanel.add(compareButton);
        rightPanel.add(browseButton);

        
        gbc.gridx = 0; gbc.anchor = GridBagConstraints.WEST;
        topPanel.add(leftPanel, gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.CENTER;
        topPanel.add(centerPanel, gbc);

        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST;
        topPanel.add(rightPanel, gbc);

        
        JPanel gridPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gridPanel.setBackground(new Color(228,228,228));

        String[] bikeImages = {
            "E:/java-project/BikersPortal/src/imgs/bikes/yamaha_r15.jpg",
            "E:/java-project/BikersPortal/src/imgs/bikes/kawasaki_ninja300.jpg",
            "E:/java-project/BikersPortal/src/imgs/bikes/suzuki_hayabusa.jpg",
            "E:/java-project/BikersPortal/src/imgs/bikes/ducati_panigale_v4.jpg",
            "E:/java-project/BikersPortal/src/imgs/bikes/bmw_s1000rr.jpg",
            "E:/java-project/BikersPortal/src/imgs/bikes/honda_cbr650r.jpg",
            "E:/java-project/BikersPortal/src/imgs/bikes/ktm_rc390.jpg",
            "E:/java-project/BikersPortal/src/imgs/bikes/hero_xpulse200.jpg"
        };

        for (String imagePath : bikeImages) {
            JButton bikeButton = createImageButton(imagePath);
            gridPanel.add(bikeButton);
        }

        add(topPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);

        // ✅ Add Action Listeners for Buttons
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Browsing();
                dispose();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new searching();
                dispose();
            }
        });

        setVisible(true);
    }

    
    private String[] fetchBikeNames() {
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT Name FROM Bike");
             ResultSet rs = pstmt.executeQuery()) {

            List<String> bikeList = new ArrayList<>();
            while (rs.next()) {
                bikeList.add(rs.getString("Name"));
            }

            return bikeList.toArray(new String[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[]{};
        }
    }

   
    private JButton createImageButton(String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(img);

        JButton button = new JButton(resizedIcon);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        return button;
    }

    public static void main(String[] args) {
        new BikerPortal();
    }
}
