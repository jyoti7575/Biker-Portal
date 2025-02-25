
package bikerportals;
import javax.swing.*;
import java.awt.*;

class ShowroomLocator extends JFrame {
    public ShowroomLocator() {
        setTitle("Showroom Locator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Showroom Locator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        JLabel infoLabel = new JLabel("<html>Looking to buy a new bike? You have come to the right place. "
                + "BikeWale’s dealer locator helps you find authorized dealers for more than 25 motorcycle and scooter brands in India. "
                + "We have listed over 7000 authorized bike dealers located in 1000+ cities in India. "
                + "Get contact information, full address and google map direction of the nearest dealer around you.</html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(infoLabel);

        JPanel searchPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Find Bike Dealers in your City"));

        JLabel brandLabel = new JLabel("Brand");
        searchPanel.add(brandLabel);

        JComboBox<String> brandComboBox = new JComboBox<>(new String[]{"Select Your Brand"});
        searchPanel.add(brandComboBox);

        JLabel cityLabel = new JLabel("City");
        searchPanel.add(cityLabel);

        JComboBox<String> cityComboBox = new JComboBox<>(new String[]{"Select City"});
        searchPanel.add(cityComboBox);

        panel.add(searchPanel);

        JPanel brandPanel = new JPanel(new GridLayout(2, 5, 10, 10));
        brandPanel.setBorder(BorderFactory.createTitledBorder("Explore Showroom by Brand"));

        addBrand(brandPanel, "Royal Enfield", "");
        addBrand(brandPanel, "Bajaj", "");
        addBrand(brandPanel, "Honda", "");
        addBrand(brandPanel, "TVS", "");
        addBrand(brandPanel, "Yamaha", "");
        addBrand(brandPanel, "Hero", "");
        addBrand(brandPanel, "KTM", "");
        addBrand(brandPanel, "Kawasaki", "");
        addBrand(brandPanel, "Suzuki", "");
        addBrand(brandPanel, "Triumph", "");

        panel.add(brandPanel);

        add(panel);
        setVisible(true);
    }

    private void addBrand(JPanel panel, String name, String imageUrl) {
        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel brandImage = new JLabel(new ImageIcon(imageUrl));
        brandImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        brandPanel.add(brandImage);

        JLabel brandName = new JLabel(name);
        brandName.setFont(new Font("Arial", Font.PLAIN, 14));
        brandName.setAlignmentX(Component.CENTER_ALIGNMENT);
        brandPanel.add(brandName);

        panel.add(brandPanel);
    }
}
