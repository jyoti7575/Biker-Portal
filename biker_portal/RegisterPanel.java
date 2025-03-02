package com.mycompany.biker_portal;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.mycompany.biker_portal.DatabaseConnection;
import com.mycompany.biker_portal.folder.PlaceholderTextField;
import com.mycompany.biker_portal.folder.PlaceholderPasswordField;

public class RegisterPanel {
    private JFrame parentFrame;

    // Constructor
    public RegisterPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public void showRegisterForm() {
        JDialog registerDialog = new JDialog(parentFrame, "Create your Biker's Portal Account", true);
        registerDialog.setSize(500, 700);
        registerDialog.setLayout(new BorderLayout());
        registerDialog.setLocationRelativeTo(parentFrame);
        registerDialog.getContentPane().setBackground(new Color(34, 34, 34));

        JPanel panel = createPanel();
        GridBagConstraints gbc = createGridBagConstraints();

        // Logo (Placeholder)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel logoLabel = new JLabel("Biker's Portal");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        logoLabel.setForeground(new Color(66, 133, 244));
        panel.add(logoLabel, gbc);

        // Heading
        gbc.gridy = 1;
        JLabel signUpLabel = new JLabel("Create your Biker's Portal Account");
        signUpLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        signUpLabel.setForeground(Color.WHITE);
        panel.add(signUpLabel, gbc);

        // Subtitle
        gbc.gridy = 2;
        JLabel subtitle = new JLabel("One account. All biking adventures.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.LIGHT_GRAY);
        panel.add(subtitle, gbc);

        // Registration Fields
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 3;
        gbc.insets = new Insets(20, 20, 10, 20);
        PlaceholderTextField nameField = createStyledTextField("Name");
        panel.add(nameField, gbc);

        gbc.gridy = 4;
        PlaceholderTextField emailField = createStyledTextField("Email");
        panel.add(emailField, gbc);

        gbc.gridy = 5;
        PlaceholderPasswordField passwordField = createStyledPasswordField("Password");
        panel.add(passwordField, gbc);

        gbc.gridy = 6;
        PlaceholderPasswordField confirmPasswordField = createStyledPasswordField("Confirm Password");
        panel.add(confirmPasswordField, gbc);

        // Register Button
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 20, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        JButton registerBtn = createStyledButton("Register");

        // Register Button Action
        registerBtn.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(registerDialog, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(registerDialog, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (registerUser(name, email, password)) {
                    JOptionPane.showMessageDialog(registerDialog, "Registration Successful!");
                    registerDialog.dispose();
                    new LoginPanel(parentFrame).showLoginForm(); // Open login window after successful registration
                } else {
                    JOptionPane.showMessageDialog(registerDialog, "Registration Failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(registerBtn, gbc);

        registerDialog.add(panel, BorderLayout.CENTER);
        registerDialog.setVisible(true);
    }

    private boolean registerUser(String name, String email, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO USERS (name, email, password) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            System.err.println("Database Error: " + ex.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    private JPanel createPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        return panel;
    }

    private GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        return gbc;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(66, 133, 244));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }

    private PlaceholderTextField createStyledTextField(String placeholder) {
        PlaceholderTextField textField = new PlaceholderTextField(placeholder);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setForeground(Color.WHITE);
        textField.setBorder(new CompoundBorder(
                new LineBorder(new Color(100, 100, 100), 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        textField.setBackground(new Color(60, 60, 60));
        return textField;
    }

    private PlaceholderPasswordField createStyledPasswordField(String placeholder) {
        PlaceholderPasswordField passwordField = new PlaceholderPasswordField(placeholder);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setForeground(Color.WHITE);
        passwordField.setBorder(new CompoundBorder(
                new LineBorder(new Color(100, 100, 100), 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        passwordField.setBackground(new Color(60, 60, 60));
        return passwordField;
    }
}
