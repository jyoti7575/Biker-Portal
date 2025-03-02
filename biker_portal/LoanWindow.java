package com.mycompany.biker_portal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoanWindow {
    public void open() {
        JFrame frame = new JFrame("Bike Loan Calculator");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(30, 30, 30));

        JPanel panel = new JPanel();
        panel.setBackground(new Color(45, 45, 45));
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel amountLabel = new JLabel("Loan Amount:");
        JTextField amountField = new JTextField();

        JLabel rateLabel = new JLabel("Interest Rate (%):");
        JTextField rateField = new JTextField();

        JLabel termLabel = new JLabel("Loan Term (years):");
        JTextField termField = new JTextField();

        JLabel resultLabel = new JLabel("Monthly EMI:");
        JTextField resultField = new JTextField();
        resultField.setEditable(false);
        resultField.setBackground(new Color(60, 60, 60));
        resultField.setForeground(Color.WHITE);
        resultField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));

        JButton calculateButton = new JButton("Calculate");
        calculateButton.setBackground(new Color(70, 130, 180));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setFocusPainted(false);
        calculateButton.setFont(new Font("Segoe UI", Font.BOLD, 16));

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double principal = Double.parseDouble(amountField.getText());
                    double annualRate = Double.parseDouble(rateField.getText()) / 100 / 12;
                    int months = Integer.parseInt(termField.getText()) * 12;

                    double emi = (principal * annualRate * Math.pow(1 + annualRate, months)) /
                                 (Math.pow(1 + annualRate, months) - 1);

                    resultField.setText(String.format("%.2f", emi));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Styling Labels and Text Fields
        styleLabel(amountLabel);
        styleLabel(rateLabel);
        styleLabel(termLabel);
        styleLabel(resultLabel);
        styleTextField(amountField);
        styleTextField(rateField);
        styleTextField(termField);

        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(rateLabel);
        panel.add(rateField);
        panel.add(termLabel);
        panel.add(termField);
        panel.add(resultLabel);
        panel.add(resultField);
        panel.add(new JLabel());
        panel.add(calculateButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void styleLabel(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private void styleTextField(JTextField field) {
        field.setBackground(new Color(60, 60, 60));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
    }
}