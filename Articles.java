package bikerportals;

import javax.swing.*;
import java.awt.*;

public class Articles {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Bike News");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.LIGHT_GRAY);

        JLabel titleLabel = new JLabel("Bike News");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("Read bike news of all models in India. All latest bikes have a number of news about its launch, updates, specs, comparisons and more.");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField searchField = new JTextField();
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 15));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5));
        String[] buttonLabels = {"NEWS", "REVIEWS", "SPECIAL REPORTS", "IMAGES", "VIDEOS"};
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            buttonPanel.add(button);
        }

        JPanel newsPanel = new JPanel();
        newsPanel.setLayout(new GridLayout(1, 3));
        newsPanel.add(createNewsItem("Ducati DesertX Discovery India launch tomorrow", "By Rishabh Bhaskar", "7 hours ago"));
        newsPanel.add(createNewsItem("Royal Enfield Guerrilla 450 gets new Peix Bronze colour, priced at Rs. 2.49 lakh", "By Rishabh Bhaskar", "8 hours ago"));
        newsPanel.add(createNewsItem("Bajaj Auto likely to pump Rs. 1,360 crore into KTM", "By Pratheek Kunder", "11 hours ago"));

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(descLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(searchField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(newsPanel);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static JPanel createNewsItem(String title, String author, String time) {
        JPanel newsItemPanel = new JPanel();
        newsItemPanel.setLayout(new BoxLayout(newsItemPanel, BoxLayout.Y_AXIS));
        newsItemPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel authorLabel = new JLabel(author);
        authorLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        authorLabel.setForeground(Color.GRAY);

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        timeLabel.setForeground(Color.LIGHT_GRAY);

        newsItemPanel.add(titleLabel);
        newsItemPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        newsItemPanel.add(authorLabel);
        newsItemPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        newsItemPanel.add(timeLabel);

        return newsItemPanel;
    }
}
