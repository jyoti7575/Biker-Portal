package com.mycompany.biker_portal;

import com.mycompany.biker_portal.folder.PlaceholderPasswordField;
import com.mycompany.biker_portal.folder.PlaceholderTextField;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginPanel {
    private JFrame parentFrame;
    private String loggedInUser;

    // Constructor
    public LoginPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public boolean showLoginForm() {
        JDialog loginDialog = new JDialog(parentFrame, "Sign in - Biker's Portal", true);
        loginDialog.setSize(600, 600);
        loginDialog.setLayout(new GridBagLayout());
        loginDialog.setLocationRelativeTo(parentFrame);
        loginDialog.getContentPane().setBackground(new Color(30, 30, 30)); // Dark background

        JPanel panel = createPanel();
        GridBagConstraints gbc = createGridBagConstraints();

        // Logo
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel logoLabel = new JLabel("Biker's Portal");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        logoLabel.setForeground(new Color(66, 133, 244));
        panel.add(logoLabel, gbc);

        // Heading
        gbc.gridy = 1;
        JLabel signInLabel = new JLabel("Sign in");
        signInLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        signInLabel.setForeground(Color.WHITE);
        panel.add(signInLabel, gbc);

        gbc.gridy = 2;
        JLabel subtitle = new JLabel("Use your Biker's Portal Account");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.LIGHT_GRAY);
        panel.add(subtitle, gbc);

        // Fields
        PlaceholderTextField emailField = createStyledTextField("Email");
        PlaceholderPasswordField passwordField = createStyledPasswordField("Enter your password");

        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(emailField, gbc);

        gbc.gridy = 4;
        panel.add(passwordField, gbc);

        // Forgot Password
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel forgotLabel = new JLabel("Forgot password?");
        forgotLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotLabel.setForeground(new Color(66, 133, 244));
        forgotLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handleForgotPassword();
            }
        });
        panel.add(forgotLabel, gbc);

        // Next Button
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton nextBtn = createStyledButton("Next", new Color(66, 133, 244), Color.WHITE);

        final boolean[] loginSuccessful = {false};

        nextBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginDialog, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    String query = "SELECT * FROM users WHERE email = ? AND password = ?";
                    try (var pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, email);
                        pstmt.setString(2, password);
                        var resultSet = pstmt.executeQuery();

                        if (resultSet.next()) {
                            loggedInUser = email;
                            JOptionPane.showMessageDialog(loginDialog, "Login Successful! Welcome, " + resultSet.getString("name"));
                            loginSuccessful[0] = true;
                            loginDialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(loginDialog, "Invalid email or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(loginDialog, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(nextBtn, gbc);

        // Create Account Button
        gbc.gridy = 7;
        JButton createAccountBtn = createStyledButton("Create account", Color.DARK_GRAY, new Color(66, 133, 244));
        createAccountBtn.setBorder(BorderFactory.createLineBorder(new Color(66, 133, 244), 1));
        createAccountBtn.addActionListener(e -> {
            loginDialog.dispose();
            new RegisterPanel(parentFrame).showRegisterForm();
        });
        panel.add(createAccountBtn, gbc);

        loginDialog.add(panel);
        loginDialog.setVisible(true);
        return loginSuccessful[0];
    }

    private void handleForgotPassword() {
        String email = JOptionPane.showInputDialog(parentFrame, "Enter your registered email:");
        if (email == null || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "Email cannot be empty.");
            return;
        }

        String adminCode = JOptionPane.showInputDialog(parentFrame, "Enter admin-provided reset code:");
        if (adminCode != null && adminCode.equals("123")) {
            String newPassword = JOptionPane.showInputDialog(parentFrame, "Enter new password:");
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    String updateQuery = "UPDATE users SET password = ? WHERE email = ?";
                    try (var pstmt = conn.prepareStatement(updateQuery)) {
                        pstmt.setString(1, newPassword);
                        pstmt.setString(2, email);
                        int rowsUpdated = pstmt.executeUpdate();

                        if (rowsUpdated > 0) {
                            JOptionPane.showMessageDialog(parentFrame, "Password updated successfully!");
                        } else {
                            JOptionPane.showMessageDialog(parentFrame, "Email not found!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(parentFrame, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Password cannot be empty.");
            }
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Invalid admin code.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getLoggedInUser() {
        return loggedInUser;
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

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return button;
    }

    private PlaceholderTextField createStyledTextField(String placeholder) {
        PlaceholderTextField textField = new PlaceholderTextField(placeholder);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        textField.setForeground(Color.WHITE);
        textField.setBackground(new Color(60, 63, 65));
        textField.setCaretColor(new Color(66, 133, 244));
        return textField;
    }

    private PlaceholderPasswordField createStyledPasswordField(String placeholder) {
        PlaceholderPasswordField passwordField = new PlaceholderPasswordField(placeholder);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        passwordField.setForeground(Color.WHITE);
        passwordField.setBackground(new Color(60, 63, 65));
        passwordField.setCaretColor(new Color(66, 133, 244));
        return passwordField;
    }
}
