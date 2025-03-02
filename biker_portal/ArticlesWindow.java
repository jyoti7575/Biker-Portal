package com.mycompany.biker_portal;

import javax.swing.*;
import java.awt.*;

public class ArticlesWindow {
    public void open() {
        JFrame frame = new JFrame("Bike Articles");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(30, 30, 30)); // Dark theme
        frame.setLayout(new BorderLayout(10, 10));

        // Header
        JLabel header = new JLabel("Latest Bike Articles");
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setForeground(Color.WHITE);
        frame.add(header, BorderLayout.NORTH);

        // Main panel with grid layout for articles (2 columns)
        JPanel articlesPanel = new JPanel(new GridLayout(0, 2, 20, 20)); // 2 columns with spacing
        articlesPanel.setBackground(new Color(30, 30, 30)); // Dark background
        articlesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add article cards with images
        articlesPanel.add(createArticleCard(
                "Ducati DesertX Discovery India launch tomorrow",
                "Rishabh Bhaskar", "5 hours ago",
                "E:/java-project/Biker_portal/src/main/java/com/mycompany/biker_portal/imgs/kawasaki_z650.jpeg"
        ));
        articlesPanel.add(createArticleCard(
                "Royal Enfield Guerrilla 450 gets new Peix Bronze colour",
                "Rishabh Bhaskar", "7 hours ago",
                "E:/java-project/Biker_portal/src/main/java/com/mycompany/biker_portal/imgs/kawasaki_z650.jpeg"
        ));
        articlesPanel.add(createArticleCard(
                "Bajaj Auto likely to pump Rs. 1,360 crore into KTM",
                "Pratheek Kunder", "10 hours ago",
                "E:/java-project/Biker_portal/src/main/java/com/mycompany/biker_portal/imgs/kawasaki_z650.jpeg"
        ));
        articlesPanel.add(createArticleCard(
                "Top 10 Bikes for Long Rides",
                "Auto Expert", "1 day ago",
                "E:/java-project/Biker_portal/src/main/java/com/mycompany/biker_portal/imgs/kawasaki_z650.jpeg"
        ));
        articlesPanel.add(createArticleCard(
                "Best Bike Accessories for 2025",
                "Moto Guru", "2 days ago",
                "E:/java-project/Biker_portal/src/main/java/com/mycompany/biker_portal/imgs/kawasaki_z650.jpeg"
        ));
        articlesPanel.add(createArticleCard(
                "Electric Bikes: Are They Worth It?",
                "E-Ride Specialist", "3 days ago",
                "E:/java-project/Biker_portal/src/main/java/com/mycompany/biker_portal/imgs/kawasaki_z650.jpeg"
        ));

        // Scroll pane for better navigation
        JScrollPane scrollPane = new JScrollPane(articlesPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createArticleCard(String title, String author, String time, String imagePath) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(new Color(45, 45, 45)); // Dark gray card background
        card.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1, true));

        // Load image
        JLabel imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(400, 200, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(img));
        imageLabel.setPreferredSize(new Dimension(400, 200));

        // Article details
        JLabel titleLabel = new JLabel("<html><b style='color:white;'>" + title + "</b></html>");
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel authorLabel = new JLabel("By " + author);
        authorLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        authorLabel.setForeground(new Color(180, 180, 180)); // Lighter gray

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setForeground(new Color(180, 180, 180)); // Lighter gray

        // Adding content to the card
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(new Color(45, 45, 45)); // Match card background
        textPanel.add(titleLabel);
        textPanel.add(authorLabel);
        textPanel.add(timeLabel);

        card.add(imageLabel, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }
}
