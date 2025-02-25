package bikerportals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BikerPortals extends JFrame {

    public BikerPortals() {
        setTitle("BikersPortal");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.BLACK);

        String[] options = {"Compare", "Location", "Events", "Loan", "Articles"};
        JComboBox<String> dropdownMenu = new JComboBox<>(options);
        dropdownMenu.setPreferredSize(new Dimension(200, 30));
        dropdownMenu.setBackground(Color.DARK_GRAY);
        dropdownMenu.setForeground(Color.WHITE);
        dropdownMenu.setFont(new Font("Arial", Font.PLAIN, 16));

        dropdownMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) dropdownMenu.getSelectedItem();
                openPage(selectedOption);
            }
        });

        panel.add(dropdownMenu);
        add(panel);
        setVisible(true);
    }

    private void openPage(String option) {
        System.out.println("Option selected: " + option);
        switch (option) {
            case "Compare":
                new Compare();
                break;
            case "Location":
                new ShowroomLocator();
                break;
            case "Events":
                JOptionPane.showMessageDialog(this, "Opening Events Page...");
                break;
            case "Loan":
                System.out.println("Opening Loan Page...");
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Loan.main(new String[]{});
                    }
                });
                break;
            case "Articles":
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Articles.main(new String[]{});
                    }
                });
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unknown Option Selected");
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BikerPortals();
            }
        });
    }
}
