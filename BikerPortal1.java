package bikerportal1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class BikerPortal1 extends JFrame {

    private JTextField bike1Field;
    private JTextField bike2Field;
    private JTextArea comparisonArea;

    private Map<String, Map<String, String>> bikeData = new HashMap<>();

    public BikerPortal1() {
        setTitle("Bike Comparison Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Input Panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        bike1Field = new JTextField(20);
        bike2Field = new JTextField(20);
        JButton compareButton = new JButton("Compare");

        inputPanel.add(new JLabel("Bike 1:")); // Label directly added
        inputPanel.add(bike1Field);
        inputPanel.add(new JLabel("Bike 2:"));
        inputPanel.add(bike2Field);
        inputPanel.add(compareButton);

        add(inputPanel, BorderLayout.NORTH);

        // 2. Comparison Area
        comparisonArea = new JTextArea(20, 50);
        comparisonArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(comparisonArea);
        add(scrollPane, BorderLayout.CENTER);

        // 3. Compare Button Action
        compareButton.addActionListener(e -> { // Lambda expression for brevity
            String bike1Name = bike1Field.getText().trim().toLowerCase();
            String bike2Name = bike2Field.getText().trim().toLowerCase();
            compareBikes(bike1Name, bike2Name);
        });

        // 4. Sample Bike Data (REPLACE THIS WITH YOUR DATA LOADING)
        bikeData.put("honda cb shine", createBikeData("Honda CB Shine", "124cc", "10.7 hp", "Drum", "107 kg", "₹78,000"));
        bikeData.put("hero splendor plus", createBikeData("Hero Splendor Plus", "97.2cc", "7.9 hp", "Drum", "110 kg", "₹70,000"));
        bikeData.put("bajaj pulsar 150", createBikeData("Bajaj Pulsar 150", "149.5cc", "14 hp", "Disc", "140 kg", "₹1.1 lakh"));
        bikeData.put("yamaha r15 v4", createBikeData("Yamaha R15 V4", "155cc", "18.4 hp", "Disc", "142 kg", "₹1.8 lakh"));

        pack();
        setVisible(true);
    }

    // Helper function to create bike data maps
    private Map<String, String> createBikeData(String name, String engine, String power, String brakes, String weight, String price) {
        Map<String, String> data = new HashMap<>();
        data.put("Name", name);
        data.put("Engine", engine);
        data.put("Power", power);
        data.put("Brakes", brakes);
        data.put("Weight", weight);
        data.put("Price", price);
        return data;
    }

    private void compareBikes(String bike1Name, String bike2Name) {
        Map<String, String> bike1Data = bikeData.get(bike1Name);
        Map<String, String> bike2Data = bikeData.get(bike2Name);

        if (bike1Data == null || bike2Data == null) {
            comparisonArea.setText("One or both bikes not found.");
            return;
        }

        // Using StringBuilder for efficiency
        StringBuilder comparisonText = new StringBuilder();

        // Header row
        comparisonText.append(String.format("%-25s %-25s %-25s\n", "Feature", bike1Data.get("Name"), bike2Data.get("Name")));
        comparisonText.append("----------------------------------------------------------------------\n");

        // Data rows
        for (String feature : bike1Data.keySet()) {
            comparisonText.append(String.format("%-25s %-25s %-25s\n", feature, bike1Data.get(feature), bike2Data.get(feature)));
        }

        comparisonArea.setText(comparisonText.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BikerPortal1());
    }
}
