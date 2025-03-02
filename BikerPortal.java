package bikerportal;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BikerPortal extends JFrame {

    private JTextField bike1Field;
    private JTextField bike2Field;
    private JTextArea comparisonArea;
    private JLabel bikeImageLabel1;
    private JLabel bikeImageLabel2;
    private JPanel detailsPanel;

    private Map<String, Map<String, String>> bikeData = new HashMap<>();

    public BikerPortal() {
        setTitle("Bike Comparison Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Input Panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        bike1Field = new JTextField(20);
        bike2Field = new JTextField(20);
        JButton compareButton = new JButton("Compare");
        JButton singleBikeButton = new JButton("Check Single Bike");

        inputPanel.add(new JLabel("Bike 1:"));
        inputPanel.add(bike1Field);
        inputPanel.add(new JLabel("Bike 2:"));
        inputPanel.add(bike2Field);
        inputPanel.add(compareButton);
        inputPanel.add(singleBikeButton);

        add(inputPanel, BorderLayout.NORTH);

        // 2. Center Panel for Images and Details
        JPanel centerPanel = new JPanel(new BorderLayout());

        // 3. Bike Images Area
        JPanel imagePanel = new JPanel(new GridLayout(1, 2));
        bikeImageLabel1 = new JLabel("", SwingConstants.CENTER);
        bikeImageLabel2 = new JLabel("", SwingConstants.CENTER);
        imagePanel.add(bikeImageLabel1);
        imagePanel.add(bikeImageLabel2);
        centerPanel.add(imagePanel, BorderLayout.NORTH);

        // 4. Details Area
        detailsPanel = new JPanel(new BorderLayout());
        comparisonArea = new JTextArea(20, 50);
        comparisonArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(comparisonArea);
        detailsPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(detailsPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // 5. Compare Button Action
        compareButton.addActionListener(e -> {
            String bike1Name = bike1Field.getText().trim().toLowerCase();
            String bike2Name = bike2Field.getText().trim().toLowerCase();
            compareBikes(bike1Name, bike2Name);
        });

        // 6. Single Bike Button Action
        singleBikeButton.addActionListener(e -> {
            String bikeName = bike1Field.getText().trim().toLowerCase();
            checkSingleBike(bikeName);
        });

        // 7. Sample Bike Data (REPLACE THIS WITH YOUR DATA LOADING)
        bikeData.put("honda cb shine", createBikeData("Honda CB Shine", "124cc", "10.7 hp", "Drum", "107 kg", "₹78,000", "₹85,000", "65 kmpl", "C:\\Users\\Kashish\\Desktop\\2018-honda-cb-shine-sp.jpg"));
        bikeData.put("hero splendor plus", createBikeData("Hero Splendor Plus", "97.2cc", "7.9 hp", "Drum", "110 kg", "₹70,000", "₹75,000", "70 kmpl", "C:\\Users\\Kashish\\Desktop\\hero splender.jpg"));
        bikeData.put("bajaj pulsar 150", createBikeData("Bajaj Pulsar 150", "149.5cc", "14 hp", "Disc", "140 kg", "₹1.1 lakh", "₹1.2 lakh", "45 kmpl", "C:\\Users\\Kashish\\Desktop\\pulsar.jpg"));
        bikeData.put("yamaha r15 v4", createBikeData("Yamaha R15 V4", "155cc", "18.4 hp", "Disc", "142 kg", "₹1.8 lakh", "₹1.9 lakh", "40 kmpl", "C:\\Users\\Kashish\\Desktop\\Yamaha-R15-V4-Black.jpg"));
        bikeData.put("harley davidson street 750", createBikeData("Harley Davidson Street 750", "749cc", "53 hp", "Disc", "233 kg", "₹5.34 lakh", "₹5.5 lakh", "20 kmpl", "C:\\Users\\Kashish\\Desktop\\Harley-Davidson-Street-750-.jpg"));
        bikeData.put("royal enfield bullet 350", createBikeData("Royal Enfield Bullet 350", "346cc", "19.1 hp", "Disc", "191 kg", "₹1.5 lakh", "₹1.6 lakh", "37 kmpl", "C:\\Users\\Kashish\\Desktop\\royal enfield.jpg"));
        bikeData.put("mercedes benz amg motorcycle", createBikeData("Mercedes-Benz AMG Motorcycle", "999cc", "215 hp", "Disc", "205 kg", "₹25 lakh", "₹27 lakh", "15 kmpl", "C:\\Users\\Kashish\\Desktop\\mercedes bike.jpg"));
        bikeData.put("harley davidson x440", createBikeData("Harley Davidson X440", "440cc", "27 hp", "Disc", "190 kg", "₹2.5 lakh", "₹2.7 lakh", "35 kmpl", "C:\\Users\\Kashish\\Desktop\\Harley-Davidson-X440.jpg"));

        pack();
        setVisible(true);
    }

    private Map<String, String> createBikeData(String name, String engine, String power, String brakes, String weight, String price, String roadPrice, String mileage, String imagePath) {
        Map<String, String> data = new HashMap<>();
        data.put("Name", name);
        data.put("Engine", engine);
        data.put("Power", power);
        data.put("Brakes", brakes);
        data.put("Weight", weight);
        data.put("Price", price);
        data.put("Road Price", roadPrice);
        data.put("Mileage", mileage);
        data.put("ImagePath", imagePath);
        return data;
    }

    private void compareBikes(String bike1Name, String bike2Name) {
        Map<String, String> bike1Data = bikeData.get(bike1Name);
        Map<String, String> bike2Data = bikeData.get(bike2Name);

        if (bike1Data == null || bike2Data == null) {
            comparisonArea.setText("One or both bikes not found.");
            return;
        }

        StringBuilder comparisonText = new StringBuilder();

        // Header row
        comparisonText.append(String.format("%-15s %-25s %-25s\n", "Feature", bike1Data.get("Name"), bike2Data.get("Name")));
        comparisonText.append("-------------------------------------------------------------\n");

        // Data rows
        for (String feature : bike1Data.keySet()) {
            if (!feature.equals("ImagePath")) {
                comparisonText.append(String.format("%-15s %-25s %-25s\n", feature, bike1Data.get(feature), bike2Data.get(feature)));
            }
        }

        comparisonArea.setText(comparisonText.toString());

        // Display images
        displayImage(bikeImageLabel1, bike1Data.get("ImagePath"));
        displayImage(bikeImageLabel2, bike2Data.get("ImagePath"));
    }

    private void checkSingleBike(String bikeName) {
        Map<String, String> bikeData = this.bikeData.get(bikeName);

        if (bikeData == null) {
            comparisonArea.setText("Bike not found.");
            return;
        }

        StringBuilder bikeDetails = new StringBuilder();

        // Header
        bikeDetails.append(String.format("%-15s %-25s\n", "Feature", bikeData.get("Name")));
        bikeDetails.append("--------------------------------------------\n");

        // Data rows
        for (String feature : bikeData.keySet()) {
            if (!feature.equals("ImagePath")) {
                bikeDetails.append(String.format("%-15s %-25s\n", feature, bikeData.get(feature)));
            }
        }

        comparisonArea.setText(bikeDetails.toString());

        // Display image
        displayImage(bikeImageLabel1, bikeData.get("ImagePath"));
        bikeImageLabel2.setIcon(null); // Clear second image
    }

    private void displayImage(JLabel label, String imagePath) {
        ImageIcon imageIcon = new ImageIcon(imagePath);
        Image image = imageIcon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(image));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BikerPortal());
    }
}
