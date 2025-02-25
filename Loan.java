
package bikerportals;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Loan extends JFrame {
    private JTextField loanAmountField, interestRateField;
    private JRadioButton emiAdvance, emiArrears;
    private JButton calculateButton;
    private JTable emiTable;

    public Loan() {
        setTitle("EMI Calculator");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

      
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel title = new JLabel("EMI Calculator - Calculate Your Bike Loan EMI");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(title, BorderLayout.NORTH);
        JLabel description = new JLabel("<html>Bike loan EMI calculation was never this easy. Just select the bike you wish to avail a loan for and which city do you wish to purchase the bike. Options to change loan amount, interest rate, tenure and much more.</html>");
        topPanel.add(description, BorderLayout.CENTER);

        
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(new JLabel("Loan Amount (₹):"));
        loanAmountField = new JTextField("100000");
        inputPanel.add(loanAmountField);
        inputPanel.add(new JLabel("Rate Of Interest (%):"));
        interestRateField = new JTextField("10");
        inputPanel.add(interestRateField);

        inputPanel.add(new JLabel("EMI Type:"));
        JPanel emiTypePanel = new JPanel();
        emiAdvance = new JRadioButton("EMI in Advance", true);
        emiArrears = new JRadioButton("EMI in Arrears");
        ButtonGroup emiTypeGroup = new ButtonGroup();
        emiTypeGroup.add(emiAdvance);
        emiTypeGroup.add(emiArrears);
        emiTypePanel.add(emiAdvance);
        emiTypePanel.add(emiArrears);
        inputPanel.add(emiTypePanel);

     
        calculateButton = new JButton("Calculate EMI");
        inputPanel.add(calculateButton);
        inputPanel.add(new JLabel()); // Placeholder for grid alignment

       
        String[] columnNames = {"Months", "EMI", ""};
        Object[][] data = {
            {12, "₹ 8719/-", "View Chart"},
            {24, "₹ 4576/-", "View Chart"},
            {36, "₹ 3200/-", "View Chart"},
            {48, "₹ 2515/-", "View Chart"},
            {60, "₹ 2107/-", "View Chart"},
            {72, "₹ 1837/-", "View Chart"},
            {84, "₹ 1646/-", "View Chart"}
        };
        emiTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(emiTable);

      
        add(topPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                JOptionPane.showMessageDialog(null, "EMI Calculation logic will go here.");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Loan().setVisible(true);
            }
        });
    }
}
