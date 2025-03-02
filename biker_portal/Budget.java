package com.mycompany.biker_portal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;

public class Budget extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Budget::createAndShowGUI);
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("🏍 Bikers Portal - Budget");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // 🔹 Main Budget Panel
        JPanel budgetPanel = createBudgetPanel();
        frame.add(budgetPanel, BorderLayout.CENTER);

        frame.getContentPane().setBackground(new Color(18, 18, 18));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JPanel createBudgetPanel() {
        JPanel budgetPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        budgetPanel.setBackground(new Color(24, 24, 24));

        String[] budgetRanges = {"Upto ₹50,000", "₹50,000 - ₹1 Lakh", "₹1 Lakh - ₹2 Lakh", "Above ₹2 Lakh"};

        for (String range : budgetRanges) {
            JLabel budgetLabel = new JLabel(range, SwingConstants.CENTER);
            budgetLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
            budgetLabel.setOpaque(true);
            budgetLabel.setBackground(new Color(40, 40, 40));
            budgetLabel.setForeground(Color.WHITE);
            budgetLabel.setPreferredSize(new Dimension(160, 60));
            addHoverEffect(budgetLabel, new Color(60, 60, 60));

            budgetLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    openBudgetDetailsUI(range);
                }
            });

            budgetPanel.add(budgetLabel);
        }
        return budgetPanel;
    }

    private static void openBudgetDetailsUI(String budgetRange) {
        JFrame budgetFrame = new JFrame(budgetRange);
        budgetFrame.setSize(800, 600);
        budgetFrame.setLayout(new BorderLayout());
        budgetFrame.getContentPane().setBackground(new Color(18, 18, 18));

        // 🔹 Title
        JLabel titleLabel = new JLabel(budgetRange, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.ORANGE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        budgetFrame.add(titleLabel, BorderLayout.NORTH);

        // 🔹 Models Panel (3-Column Layout)
        JPanel modelPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        modelPanel.setBackground(new Color(24, 24, 24));

        ArrayList<String> models = fetchModelsByBudget(budgetRange);

        if (models.isEmpty()) {
            JLabel noDataLabel = new JLabel("No models found in this range.", SwingConstants.CENTER);
            noDataLabel.setForeground(Color.WHITE);
            modelPanel.add(noDataLabel);
        } else {
            for (String model : models) {
                JLabel modelLabel = new JLabel(model, SwingConstants.CENTER);
                modelLabel.setOpaque(true);
                modelLabel.setBackground(new Color(50, 50, 50));
                modelLabel.setForeground(Color.WHITE);
                modelLabel.setPreferredSize(new Dimension(200, 80));
                addHoverEffect(modelLabel, new Color(70, 70, 70));

                modelLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        showModelDetails(model);
                    }
                });

                modelPanel.add(modelLabel);
            }
        }

        JScrollPane scrollPane = new JScrollPane(modelPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);

        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(255, 87, 34));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> budgetFrame.dispose());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(18, 18, 18));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(backButton);

        budgetFrame.add(scrollPane, BorderLayout.CENTER);
        budgetFrame.add(bottomPanel, BorderLayout.SOUTH);
        budgetFrame.setLocationRelativeTo(null);
        budgetFrame.setVisible(true);
    }

    private static void showModelDetails(String modelName) {
        DatabaseManager dbManager = new DatabaseManager();
        Map<String, String> details = dbManager.fetchBikeDetails(modelName);

        JFrame detailsFrame = new JFrame("Details for " + modelName);
        detailsFrame.setSize(600, 400);
        detailsFrame.setLayout(new BorderLayout());
        detailsFrame.getContentPane().setBackground(new Color(24, 24, 24));

        JLabel titleLabel = new JLabel(modelName, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.ORANGE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        detailsFrame.add(titleLabel, BorderLayout.NORTH);

        // Convert Map to Table Data
        String[] columnNames = {"Specification", "Value"};
        String[][] tableData = new String[details.size()][2];
        int i = 0;
        for (Map.Entry<String, String> entry : details.entrySet()) {
            tableData[i][0] = entry.getKey();
            tableData[i][1] = entry.getValue();
            i++;
        }

        JTable table = new JTable(tableData, columnNames);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(50, 50, 50));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setBackground(new Color(30, 30, 30));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(80, 80, 80));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JButton closeButton = new JButton("Close");
        closeButton.setBackground(new Color(255, 87, 34));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeButton.addActionListener(e -> detailsFrame.dispose());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(24, 24, 24));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        bottomPanel.add(closeButton);

        detailsFrame.add(scrollPane, BorderLayout.CENTER);
        detailsFrame.add(bottomPanel, BorderLayout.SOUTH);
        detailsFrame.setLocationRelativeTo(null);
        detailsFrame.setVisible(true);
    }

    private static ArrayList<String> fetchModelsByBudget(String budgetRange) {
        DatabaseManager dbManager = new DatabaseManager();
        return dbManager.fetchModelsByBudget(budgetRange);
    }

    private static void addHoverEffect(JLabel label, Color hoverColor) {
        Color defaultColor = label.getBackground();
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setBackground(defaultColor);
            }
        });
    }
}
