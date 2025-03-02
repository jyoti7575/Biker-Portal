package com.mycompany.bikerportalf;

import javax.swing.*;
import java.awt.*;

public class MenuBar extends JFrame {
    public MenuBar() {
        // Set the title and layout for the frame
        setTitle("Bike Portal");
        setLayout(new BorderLayout());

        // Absolute path to the image
        String imagePath = "D:\\PROJECTS\\Biker's Portal\\BikerPortal\\src\\images\\logo.png";

        // Load and resize the image
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image image = originalIcon.getImage();
        Image resizedImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        // Create the top panel with GridBagLayout
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Left Panel - Logo and Title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel logoLabel = new JLabel(resizedIcon);
        JLabel titleLabel = new JLabel("Biker Portal");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        leftPanel.add(logoLabel);
        leftPanel.add(titleLabel);

        // Center Panel - Search Bar
        JPanel centerPanel = new JPanel(new FlowLayout());
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        centerPanel.add(searchField);

        // Right Panel - Menu Bar
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JMenuBar menuBar = new JMenuBar();

        JMenu browseMenu = new JMenu("Bikes");
        JMenuItem browseItem = new JMenuItem("Browse All Bikes");
        browseMenu.add(browseItem);

        JMenu compareMenu = new JMenu("Compare");
        JMenuItem compareItem = new JMenuItem("Compare Bikes");
        compareMenu.add(compareItem);

        menuBar.add(browseMenu);
        menuBar.add(compareMenu);
        rightPanel.add(menuBar);

        // Add Components to the Top Panel with GridBagLayout
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // Left Panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        topPanel.add(leftPanel, gbc);

        // Center Panel
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        topPanel.add(centerPanel, gbc);

        // Right Panel
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        topPanel.add(rightPanel, gbc);

        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.CYAN);
        contentPanel.add(new JLabel("Welcome to the Bike Portal"));

        // Add Panels to Frame
        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        // Frame Settings
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuBar().setVisible(true));
    }
}
