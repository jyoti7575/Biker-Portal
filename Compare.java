package bikerportals;

import javax.swing.*;
import java.awt.*;

public class Compare extends JFrame {
    public Compare() {
        
        setTitle("Compare Bikes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        getContentPane().setBackground(new Color(243, 244, 246));

       
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        JLabel title = new JLabel("Compare Bikes", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(title);
        container.add(Box.createRigidArea(new Dimension(0, 10)));

      
        JTextArea description = new JTextArea(
            "Are you confused between multiple bikes to choose from? Not sure what features should you compare? Don't worry, bike comparison was never so easy. BikeWale brings you an amazing tool! Compare Bikes for bike comparison based on prices, mileage, power, performance, and 100s of other features. Compare your favorite bikes to choose the one that suits your needs. Compare multiple bikes at once to find the best one."
        );
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEditable(false);
        description.setBackground(new Color(243, 244, 246));
        description.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(description);
        container.add(Box.createRigidArea(new Dimension(0, 20)));

        
        JPanel bikePanel = new JPanel();
        bikePanel.setLayout(new GridLayout(1, 4, 10, 10));
        String[] bikes = {"Select Bike 1", "VS", "Select Bike 2"};

        for (String bike : bikes) {
            JButton bikeButton = new JButton(bike);
            bikeButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
            bikePanel.add(bikeButton);
        }

        container.add(bikePanel);
        container.add(Box.createRigidArea(new Dimension(0, 20)));

       
        JButton compareButton = new JButton("Compare");
        compareButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        compareButton.setBackground(new Color(220, 38, 38));
        compareButton.setForeground(Color.WHITE);
        compareButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(compareButton);
        container.add(Box.createRigidArea(new Dimension(0, 20)));

        
        JLabel popularComparisons = new JLabel("Popular Comparisons", SwingConstants.CENTER);
        popularComparisons.setFont(new Font("SansSerif", Font.BOLD, 20));
        popularComparisons.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(popularComparisons);
        container.add(Box.createRigidArea(new Dimension(0, 10)));

        
        JPanel popularPanel = new JPanel();
        popularPanel.setLayout(new GridLayout(3, 2, 10, 10));
        String[] popularBikes = {
            "Royal Enfield Hunter 350", "TVS Ronin", "Royal Enfield Classic 350", "Honda CB350", "Yamaha MT 15 V2", 
            "Yamaha R15 V4" 
        };

        for (String bike : popularBikes) {
            JPanel bikeCard = new JPanel();
            bikeCard.setLayout(new BoxLayout(bikeCard, BoxLayout.Y_AXIS));
            bikeCard.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            bikeCard.setBackground(Color.WHITE);

            JLabel bikeLabel = new JLabel(bike, SwingConstants.CENTER);
            bikeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            bikeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            bikeCard.add(bikeLabel);

            JLabel bikePrice = new JLabel("₹ Price", SwingConstants.CENTER);
            bikePrice.setFont(new Font("SansSerif", Font.PLAIN, 14));
            bikePrice.setForeground(Color.GRAY);
            bikePrice.setAlignmentX(Component.CENTER_ALIGNMENT);
            bikeCard.add(bikePrice);

            JButton bikeCompareButton = new JButton("Compare");
            bikeCompareButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
            bikeCompareButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            bikeCard.add(bikeCompareButton);

            popularPanel.add(bikeCard);
        }

        container.add(popularPanel);
        add(container);
        setVisible(true);
    }
}
