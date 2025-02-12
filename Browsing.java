package bikerportal;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;

public class Browsing extends JFrame {
    private JComboBox<String> brandDropdown, typeDropdown, priceDropdown;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JLabel bikeImageLabel, specLabel;

    public Browsing() {
        setTitle("Bike Browsing");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ✅ Dropdown Filters
        String[] brands = {"All", "Yamaha", "Honda", "Kawasaki", "Suzuki", "Harley", "Revolt"};
        String[] types = {"All", "Sport", "Dirt", "Cruiser", "Electric"};
        String[] priceRanges = {"All", "Under $5000", "$5000 - $10000", "Above $10000"};

        brandDropdown = createStyledDropdown(brands);
        typeDropdown = createStyledDropdown(types);
        priceDropdown = createStyledDropdown(priceRanges);

        // ✅ Table for Bike Listings
        String[] columns = {"Brand", "Model", "Type", "Price"};
        tableModel = new DefaultTableModel(columns, 0);
        resultTable = new JTable(tableModel);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.setBackground(Color.WHITE);
        resultTable.setOpaque(true);

        // ✅ Style Table Header
        JTableHeader tableHeader = resultTable.getTableHeader();
        tableHeader.setBackground(Color.BLACK);
        tableHeader.setForeground(Color.WHITE);

        // ✅ Center Align Table Content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < resultTable.getColumnCount(); i++) {
            resultTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // ✅ Image & Specifications Panel
        bikeImageLabel = new JLabel();
        bikeImageLabel.setPreferredSize(new Dimension(250, 200));

        specLabel = new JLabel("Select a bike to see details");
        specLabel.setVerticalAlignment(JLabel.TOP);

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Bike Details"));
        detailsPanel.add(bikeImageLabel, BorderLayout.WEST);
        detailsPanel.add(specLabel, BorderLayout.CENTER);
        detailsPanel.setBackground(new Color(0,130,130));

        // ✅ Top Panel (Filters)
        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("Brand:"));
        filterPanel.add(brandDropdown);
        filterPanel.add(new JLabel("Type:"));
        filterPanel.add(typeDropdown);
        filterPanel.add(new JLabel("Price:"));
        filterPanel.add(priceDropdown);
        filterPanel.setBackground(new Color(0,130,130));

        // ✅ Scroll Pane
        JScrollPane tableScrollPane = new JScrollPane(resultTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Available Bikes"));
        tableScrollPane.getViewport().setBackground(Color.WHITE);

        // ✅ Back Button
        JButton backButton = new JButton("Back to Main Menu");
        styleBlackButton(backButton);
        backButton.addActionListener(e -> {
            dispose();
            new BikerPortal();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        bottomPanel.setBackground(new Color(0,130,130));

        // ✅ Apply Background Color to JFrame Content Pane
        getContentPane().setBackground(Color.WHITE);

        // ✅ Add Components to Layout
        add(filterPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(detailsPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.PAGE_END);

        setVisible(true);
    }

    private JComboBox<String> createStyledDropdown(String[] items) {
        JComboBox<String> dropdown = new JComboBox<>(items);
        dropdown.setBackground(Color.BLACK);
        dropdown.setForeground(Color.WHITE);
        dropdown.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return dropdown;
    }

    private void styleBlackButton(JButton button) {
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public static void main(String[] args) {
        new Browsing();
    }
}
