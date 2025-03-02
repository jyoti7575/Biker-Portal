package com.mycompany.biker_portal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class LocationWindow {
    private HashMap<String, String[]> distributors;
    private final Color BACKGROUND_COLOR = new Color(24, 24, 24);
    private final Color PANEL_COLOR = new Color(40, 40, 40);
    private final Color ACCENT_COLOR = new Color(0, 120, 215);

    public LocationWindow() {
        initializeDistributors();
    }

    private void initializeDistributors() {
        distributors = new HashMap<>();
        distributors.put("Mumbai", new String[]{"Hero MotoCorp - Andheri", "Ducati - Bandra", "Harley-Davidson - Lower Parel"});
        distributors.put("Delhi", new String[]{"Hero MotoCorp - Connaught Place", "Ducati - South Delhi", "Harley-Davidson - Noida"});
        distributors.put("Bangalore", new String[]{"Hero MotoCorp - Indiranagar", "Ducati - Koramangala", "Harley-Davidson - Whitefield"});
        distributors.put("Pune", new String[]{"Hero MotoCorp - Shivaji Nagar", "Ducati - Viman Nagar", "Harley-Davidson - Kalyani Nagar"});
    }

    public void open() {
        JFrame frame = createMainFrame();
        frame.add(createHeaderPanel(), BorderLayout.NORTH);
        frame.add(createSearchPanel(), BorderLayout.CENTER);
        frame.add(createCitiesPanel(), BorderLayout.SOUTH);
        frame.add(createLocationButtonPanel(), BorderLayout.EAST);
        frame.setVisible(true);
    }

    private JFrame createMainFrame() {
        JFrame frame = new JFrame("Select Your City");
        frame.setSize(650, 550);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(15, 15));
        frame.getContentPane().setBackground(BACKGROUND_COLOR);
        frame.setLocationRelativeTo(null);
        return frame;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JLabel header = new JLabel("Find Bike Distributors");
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setForeground(Color.WHITE);
        headerPanel.add(header);
        
        return headerPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(BACKGROUND_COLOR);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));

        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchField.setBackground(PANEL_COLOR);
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80), 2),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        searchPanel.add(searchField, gbc);

        return searchPanel;
    }

    private JPanel createCitiesPanel() {
        JPanel citiesPanel = new JPanel(new GridLayout(3, 3, 15, 15));
        citiesPanel.setBackground(BACKGROUND_COLOR);
        citiesPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 30, 30, 30),
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 2),
                "Popular Cities",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                Color.LIGHT_GRAY
            )
        ));

        String[] popularCities = {"Mumbai", "Delhi", "Bangalore", "Pune", "Hyderabad", "Ahmedabad", "Chennai", "Kolkata", "Chandigarh"};

        for (String city : popularCities) {
            JButton cityButton = createCityButton(city);
            citiesPanel.add(cityButton);
        }

        return citiesPanel;
    }

    private JButton createCityButton(String city) {
        JButton button = new JButton(city);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(PANEL_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        button.setFocusPainted(false);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PANEL_COLOR);
            }
        });

        button.addActionListener(e -> showDistributors(city));
        return button;
    }

    private JPanel createLocationButtonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));

        JButton detectLocation = new JButton("Detect My Location");
        detectLocation.setFont(new Font("Segoe UI", Font.BOLD, 14));
        detectLocation.setBackground(ACCENT_COLOR);
        detectLocation.setForeground(Color.WHITE);
        detectLocation.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR.darker(), 2),
            BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        detectLocation.setFocusPainted(false);
        detectLocation.addActionListener(e -> showDistributors("Mumbai"));

        panel.add(detectLocation);
        return panel;
    }

    private void showDistributors(String city) {
    JFrame distFrame = new JFrame("Distributors in " + city);
    distFrame.setSize(500, 300);
    distFrame.setLayout(new BorderLayout());
    distFrame.getContentPane().setBackground(BACKGROUND_COLOR);

    JLabel distHeader = new JLabel(city + " Distributors", SwingConstants.CENTER);
    distHeader.setFont(new Font("Segoe UI", Font.BOLD, 20));
    distHeader.setForeground(Color.WHITE);
    distHeader.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
    distFrame.add(distHeader, BorderLayout.NORTH);

    String[] availableDistributors = distributors.getOrDefault(city, new String[]{"No distributors available."});
    
    // Prepare data for JTable
    String[][] tableData = new String[availableDistributors.length][2];
    for (int i = 0; i < availableDistributors.length; i++) {
        String[] parts = availableDistributors[i].split(" - ");
        tableData[i][0] = parts[0]; // Brand Name
        tableData[i][1] = parts.length > 1 ? parts[1] : "N/A"; // Location
    }

    String[] columnNames = {"Brand", "Location"};
    
    JTable distributorTable = new JTable(tableData, columnNames);
    distributorTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    distributorTable.setRowHeight(30);
    distributorTable.setBackground(PANEL_COLOR);
    distributorTable.setForeground(Color.WHITE);
    distributorTable.setGridColor(new Color(80, 80, 80));
    distributorTable.setSelectionBackground(ACCENT_COLOR);
    distributorTable.setSelectionForeground(Color.WHITE);
    
    JScrollPane scrollPane = new JScrollPane(distributorTable);
    scrollPane.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 2));
    scrollPane.getViewport().setBackground(PANEL_COLOR);

    distFrame.add(scrollPane, BorderLayout.CENTER);
    distFrame.setLocationRelativeTo(null);
    distFrame.setVisible(true);
}

}