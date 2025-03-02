package bikersportal;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import org.json.JSONObject;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONArray;

public class BikeDisplay extends JFrame {
    private JLabel bikeImageLabel;
    private JPanel detailsPanel;
    
    // ✅ Google API Key & Search Engine ID
    private static final String GOOGLE_API_KEY = "AIzaSyDkL2TW_HBNEfxFbDzSUcykDJET5hUBNfQ";
    private static final String GOOGLE_CSE_ID = "0285f728c99f14e0c";

    public BikeDisplay(String bikeModel) {
        setTitle("🏍 Bike Details - " + bikeModel);
        setSize(650, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 30));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ✅ Image Panel (Top)
        bikeImageLabel = new JLabel("🖼️ Loading Image...", JLabel.CENTER);
        bikeImageLabel.setPreferredSize(new Dimension(600, 250));
        bikeImageLabel.setOpaque(true);
        bikeImageLabel.setBackground(Color.BLACK);
        bikeImageLabel.setForeground(Color.WHITE);
        bikeImageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(bikeImageLabel, BorderLayout.NORTH);

        // ✅ Details Panel (Scrollable)
        detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(new Color(50, 50, 50));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(detailsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ✅ Close Button
        JButton closeButton = new JButton("❌ Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(Color.RED);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(30, 30, 30));
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);

        // ✅ Fetch Bike Details & Image
        loadBikeDetails(bikeModel);
        loadBikeImage(bikeModel);

        setVisible(true);
    }

    private void loadBikeDetails(String bikeModel) {
        try {
            String apiUrl = "https://motorcycles-by-api-ninjas.p.rapidapi.com/v1/motorcycles?model=" + bikeModel.replace(" ", "%20");

            HttpResponse<String> response = Unirest.get(apiUrl)
                .header("x-rapidapi-key", "e052eec525msh4561551343249bdp131ea1jsn50124a4e6188")
                .header("x-rapidapi-host", "motorcycles-by-api-ninjas.p.rapidapi.com")
                .asString();

            if (response.getStatus() != 200) {
                JOptionPane.showMessageDialog(this, "⚠️ Failed to fetch bike details. Error Code: " + response.getStatus(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JSONArray bikesArray = new JSONArray(response.getBody().trim());
            if (bikesArray.length() == 0) {
                JOptionPane.showMessageDialog(this, "ℹ️ No bike data found!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JSONObject bike = bikesArray.getJSONObject(0);

            for (String key : bike.keySet()) {
                addBikeDetail(key.replace("_", " ").toUpperCase() + ":", bike.optString(key, "N/A"));
            }

            revalidate();
            repaint();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Failed to fetch bike details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBikeImage(String bikeModel) {
        new Thread(() -> {
            try {
                String searchQuery = bikeModel + " bike";
                String imageUrl = fetchImageFromGoogle(searchQuery);

                if (imageUrl != null) {
                    BufferedImage img = ImageIO.read(new URL(imageUrl));
                    ImageIcon icon = new ImageIcon(img.getScaledInstance(600, 250, Image.SCALE_SMOOTH));
                    bikeImageLabel.setText(""); // Remove "Loading Image..."
                    bikeImageLabel.setIcon(icon);
                } else {
                    bikeImageLabel.setText("🚫 Image Not Found");
                }
            } catch (Exception e) {
                bikeImageLabel.setText("🚫 Error Loading Image");
                e.printStackTrace();
            }
        }).start();
    }

    private String fetchImageFromGoogle(String query) {
        try {
            String apiUrl = "https://www.googleapis.com/customsearch/v1?q=" + query.replace(" ", "%20") + 
                            "&cx=" + GOOGLE_CSE_ID + "&searchType=image&key=" + GOOGLE_API_KEY + "&num=1";

            HttpResponse<String> response = Unirest.get(apiUrl).asString();

            if (response.getStatus() == 200) {
                JSONObject json = new JSONObject(response.getBody());
                JSONArray items = json.getJSONArray("items");
                if (items.length() > 0) {
                    return items.getJSONObject(0).getString("link");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addBikeDetail(String key, String value) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel keyLabel = new JLabel("📌 " + key);
        keyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        keyLabel.setForeground(new Color(255, 180, 0));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        valueLabel.setForeground(Color.WHITE);

        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(new Color(60, 60, 60));
        rowPanel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1, true));
        rowPanel.add(keyLabel, BorderLayout.WEST);
        rowPanel.add(valueLabel, BorderLayout.EAST);

        gbc.gridx = 0;
        gbc.gridy = detailsPanel.getComponentCount();
        detailsPanel.add(rowPanel, gbc);
    }
}
