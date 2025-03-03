package com.mycompany.biker_portal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EventsWindow {
    public void open() {
        JFrame frame = new JFrame("Upcoming Bike Events");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(30, 30, 30));
        frame.setLayout(new BorderLayout(10, 10));

        // Header Label
        JLabel header = new JLabel("Upcoming Bike Events");
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(header, BorderLayout.NORTH);

        // Table column names
        String[] columnNames = {"Date", "Event Name", "Type", "Location"};

        // Sample event data
        Object[][] eventData = {
                {"13-14 October", "MTB Udhampur", "MTB", "Udhampur"},
                {"26-28 November", "Meraki 4.3 Noida-Jim Corbett", "MTB/Road", "Noida"},
                {"22 November", "Mid Night Cycling", "MTB/Road", "Mumbai"},
                {"22 November", "SoBo Circuit Midnight Cycling with Trikon", "MTB/Road", "Mumbai"},
                {"22 November", "Nilambur To Ooty Cycling", "MTB/Road", "Kerala"},
                {"26 November", "Pushkar 1000", "BRM", "Gwalior"},
                {"27 November", "Spartans - 400 K BRM", "BRM", "Vishakapatnam"},
                {"15 March 2025", "MotoGP Grand Prix", "Race", "Delhi"},
                {"25 April 2025", "India Bike Week", "Festival", "Goa"},
                {"10 May 2025", "Ducati Owners Meet", "Meetup", "Bangalore"},
                {"5 June 2025", "Harley-Davidson Annual Ride", "Ride", "Chennai"},
                {"20 July 2025", "Hero Riders Rally", "Rally", "Pune"}
        };

        // Table model
        DefaultTableModel model = new DefaultTableModel(eventData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };

        // Event Table with styling
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.setBackground(new Color(45, 45, 45));
        table.setForeground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(70, 70, 70));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.setGridColor(new Color(70, 70, 70)); // Grid line color

        // Scroll pane for table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
