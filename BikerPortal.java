package bikerportal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BikerPortal extends JFrame {
    
    protected JButton browseButton, searchButton; // Changed from private to protected
    
    public BikerPortal() {
        
        setTitle("Biker's Portal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(0, 130, 130));
        topPanel.setPreferredSize(new Dimension(800, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 1;

        // Left Panel (Logo & Title)
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

        // Center Panel (Search Bar)
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        centerPanel.setOpaque(false);
        JTextField searchField = new JTextField(20);
        searchButton = new JButton("Search"); // ✅ Make searchButton accessible

        Color buttonColor = new Color(0, 0, 0);
        Color textColor = Color.WHITE;

        searchButton.setBackground(buttonColor);
        searchButton.setForeground(textColor);
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        centerPanel.add(searchField);
        centerPanel.add(searchButton);

        // Right Panel (Compare & Browse Buttons)
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);

        JButton compareButton = new JButton("Compare");
        browseButton = new JButton("Browse"); // ✅ Initialize browseButton

        JButton[] topPanelButtons = {compareButton, browseButton, searchButton};
        for (JButton button : topPanelButtons) {
            button.setBackground(buttonColor);
            button.setForeground(textColor);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        rightPanel.add(compareButton);
        rightPanel.add(browseButton);

        // Add Components to Top Panel
        gbc.gridx = 0; gbc.anchor = GridBagConstraints.WEST;
        topPanel.add(leftPanel, gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.CENTER;
        topPanel.add(centerPanel, gbc);

        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST;
        topPanel.add(rightPanel, gbc);

        // Grid Panel (2x4 Layout)
        JPanel gridPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gridPanel.setBackground(Color.BLACK);

        String[] bikeTypes = {
            "Road Bike", "Mountain Bike", "Hybrid Bike", "Cruiser Bike",
            "BMX Bike", "Folding Bike", "Electric Bike", "Touring Bike"
        };

        for (String bike : bikeTypes) {
            JButton bikeButton = new JButton(bike);
            gridPanel.add(bikeButton);
        }

        add(topPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);

        // ✅ Add Action Listeners for Buttons
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Browsing(); // ✅ Open Browsing Window
                dispose(); // ✅ Close Parent Window
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new searching(); // ✅ Open Searching Window
                dispose(); // ✅ Close Parent Window
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new BikerPortal();
    }
}
