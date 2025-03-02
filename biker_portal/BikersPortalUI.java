package com.mycompany.biker_portal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BikersPortalUI {
    private static String currentUser = null; // Tracks the logged-in user

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BikersPortalUI::launchApp);
    }

    // Start the application with login check
    private static void launchApp() {
        JFrame frame = new JFrame("\uD83C\uDFCD️ Bikers Portal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(18, 18, 18)); // Dark background

        // Prompt login first
        showLoginForm(frame);

        // Only show the app if the user is logged in
        if (currentUser != null) {
            JPanel headerPanel = createHeaderPanel(frame);
            frame.add(headerPanel, BorderLayout.NORTH);

            JPanel heroPanel = createHeroPanel();
            frame.add(heroPanel, BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } else {
            frame.dispose();
        }
    }

    private static void showLoginForm(JFrame frame) {
        LoginPanel loginPanel = new LoginPanel(frame);
        boolean isLoggedIn = loginPanel.showLoginForm();

        if (isLoggedIn) {
            currentUser = loginPanel.getLoggedInUser();
        }
    }

    private static JPanel createHeaderPanel(JFrame frame) {
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setBackground(new Color(24, 24, 24));
    headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

    JLabel logoLabel = new JLabel("BikersPortal");
    logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
    logoLabel.setForeground(new Color(255, 87, 34));
    JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    logoPanel.setBackground(new Color(24, 24, 24));
    logoPanel.add(logoLabel);
    headerPanel.add(logoPanel, BorderLayout.WEST);

    JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
    centerPanel.setBackground(new Color(24, 24, 24));

    JPanel searchPanel = new JPanel(new BorderLayout());
    searchPanel.setBackground(new Color(24, 24, 24));

    JTextField searchField = new JTextField();
    searchField.setPreferredSize(new Dimension(300, 40));
    searchField.setBackground(new Color(36, 36, 36));
    searchField.setForeground(Color.WHITE);
    searchField.setCaretColor(Color.WHITE);
    searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(64, 64, 64), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));

    JButton searchButton = new JButton("Search");
    searchButton.setBackground(new Color(255, 87, 34));
    searchButton.setForeground(Color.WHITE);
    searchButton.setFocusPainted(false);
    searchButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

    searchButton.addActionListener(e -> {
        String searchText = searchField.getText().trim();
        if (!searchText.isEmpty()) {
            SearchResults.displayResults(searchText);
        } else {
            JOptionPane.showMessageDialog(frame, "Please enter a search term!", "Search", JOptionPane.WARNING_MESSAGE);
        }
    });

    searchPanel.add(searchField, BorderLayout.CENTER);
    searchPanel.add(searchButton, BorderLayout.EAST);

    centerPanel.add(searchPanel);

    DropdownMenu dropdownMenu = new DropdownMenu();
    centerPanel.add(dropdownMenu);

    headerPanel.add(centerPanel, BorderLayout.CENTER);

    JButton logoutButton = createRoundedButton(" Logout", new Color(244, 67, 54));
    logoutButton.setPreferredSize(new Dimension(100, 40));
    logoutButton.addActionListener(e -> {
        currentUser = null;
        frame.dispose();
        launchApp();
    });

    JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    logoutPanel.setBackground(new Color(24, 24, 24));
    logoutPanel.add(logoutButton);
    headerPanel.add(logoutPanel, BorderLayout.EAST);

    return headerPanel;
}


    private static JPanel createHeroPanel() {
        JPanel heroPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(18, 18, 18), getWidth(), getHeight(), new Color(36, 36, 36));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        heroPanel.setPreferredSize(new Dimension(1200, 400));
        heroPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel heroText = new JLabel("FIND THE RIGHT BIKE");
        heroText.setFont(new Font("Segoe UI", Font.BOLD, 42));
        heroText.setForeground(Color.WHITE);
        heroPanel.add(heroText, gbc);

        gbc.gridy++;
        JLabel subText = new JLabel("Comprehensive Information on Bikes at Your Fingertips");
        subText.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        subText.setForeground(Color.LIGHT_GRAY);
        heroPanel.add(subText, gbc);

        gbc.gridy++;
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        welcomeLabel.setForeground(Color.ORANGE);
        heroPanel.add(welcomeLabel, gbc);

        gbc.gridy++;
        JPanel buttonPanel = createCategoryButtonsPanel();
        heroPanel.add(buttonPanel, gbc);

        return heroPanel;
    }

    private static JPanel createCategoryButtonsPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonPanel.setBackground(new Color(36, 36, 36));

        JButton brandButton = createCategoryButton("Brand", new Color(255, 87, 34));
        brandButton.addActionListener(e -> SwingUtilities.invokeLater(Brand::createAndShowGUI));

        JButton budgetButton = createCategoryButton("Budget", new Color(76, 175, 80));
        budgetButton.addActionListener(e -> SwingUtilities.invokeLater(Budget::createAndShowGUI));

        JButton displacementButton = createCategoryButton("Displacement", new Color(33, 150, 243));
        displacementButton.addActionListener(e -> SwingUtilities.invokeLater(Displacement::createAndShowGUI));

        JButton styleButton = createCategoryButton("Style", new Color(156, 39, 176));
        styleButton.addActionListener(e -> SwingUtilities.invokeLater(Style::createAndShowGUI));

        buttonPanel.add(brandButton);
        buttonPanel.add(budgetButton);
        buttonPanel.add(displacementButton);
        buttonPanel.add(styleButton);

        return buttonPanel;
    }

    private static JButton createRoundedButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(color.darker(), 2));
        addHoverEffect(button, color);
        return button;
    }

    private static JButton createCategoryButton(String text, Color color) {
        JButton button = createRoundedButton(text, color);
        button.setPreferredSize(new Dimension(220, 60));
        return button;
    }

    private static void addHoverEffect(JButton button, Color color) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
    }
}