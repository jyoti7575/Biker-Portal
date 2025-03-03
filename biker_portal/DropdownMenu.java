package com.mycompany.biker_portal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DropdownMenu extends JPanel {
    private JComboBox<String> dropdownMenu;

    public DropdownMenu() {
        setLayout(new BorderLayout());
        setBackground(new Color(24, 24, 24));

        String[] options = {"Compare", "Location", "Loan", "Events", "Articles"};
        dropdownMenu = new JComboBox<>(options);
        dropdownMenu.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dropdownMenu.setBackground(new Color(36, 36, 36));
        dropdownMenu.setForeground(Color.WHITE);

        dropdownMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSelection(dropdownMenu.getSelectedItem().toString());
            }
        });

        add(dropdownMenu, BorderLayout.CENTER);
    }

    private void handleSelection(String selection) {
        switch (selection) {
            case "Compare":
                new CompareWindow().open();
                break;
            case "Location":
                new LocationWindow().open();
                break;
            case "Loan":
                new LoanWindow().open();
                break;
            case "Events":
                new EventsWindow().open();
                break;
            case "Articles":
                new ArticlesWindow().open();
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unknown selection!");
        }
    }
}
