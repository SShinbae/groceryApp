import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class groceryApp {
    private JFrame frame;
    private JTextArea textAreaData;
    private JTextField categoryNameField;
    private JTextField iconNameField;
    private JTextField tagsCountField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new groceryApp().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("Grocery Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.getContentPane().setLayout(null);

        JButton getFromWebServiceButton = new JButton("Item Grocery");
        getFromWebServiceButton.setBounds(270, 10, 200, 25);

        textAreaData = new JTextArea();
        textAreaData.setBounds(10, 45, 460, 150);
        textAreaData.setLineWrap(true);
        textAreaData.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textAreaData);
        scrollPane.setBounds(10, 45, 460, 150);

        JLabel categoryNameLabel = new JLabel("Item Category:");
        categoryNameLabel.setBounds(10, 245, 100, 25);

        categoryNameField = new JTextField();
        categoryNameField.setBounds(120, 245, 150, 25);

        JButton searchName = new JButton("Search Item Name");
        searchName.setBounds(280, 210, 190, 25);
        searchName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchCategory();
            }
        });

        JLabel iconNameLabel = new JLabel("Price (RM):");
        iconNameLabel.setBounds(10, 280, 100, 25);

        iconNameField = new JTextField();
        iconNameField.setBounds(120, 280, 150, 25);

        JLabel tagsCountLabel = new JLabel("Item Name:");
        tagsCountLabel.setBounds(10, 210, 100, 25);

        tagsCountField = new JTextField();
        tagsCountField.setBounds(120, 210, 150, 25);

        JButton saveToDatabaseButton = new JButton("Save To Database");
        saveToDatabaseButton.setBounds(10, 315, 460, 25);

        getFromWebServiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchDataFromTxt();
            }
        });

        searchName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchCategory();
            }
        });

        saveToDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String categoryName = categoryNameField.getText();
                String iconName = iconNameField.getText();
                String tagsCount = tagsCountField.getText();
                saveToDatabase(categoryName, iconName, tagsCount);
            }
        });

        frame.getContentPane().add(getFromWebServiceButton);
        frame.getContentPane().add(scrollPane);
        frame.getContentPane().add(categoryNameLabel);
        frame.getContentPane().add(categoryNameField);
        frame.getContentPane().add(searchName);
        frame.getContentPane().add(iconNameLabel);
        frame.getContentPane().add(iconNameField);
        frame.getContentPane().add(tagsCountLabel);
        frame.getContentPane().add(tagsCountField);
        frame.getContentPane().add(saveToDatabaseButton);

        frame.setVisible(true);
    }

    private void fetchDataFromTxt() {
        SwingUtilities.invokeLater(() -> {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\wanah\\eclipse-workspace\\groceryApp\\src\\grocery.txt"));
                StringBuilder data = new StringBuilder();
                String line;
                String itemName = tagsCountField.getText().trim(); // Retrieve item name entered by the user
                while ((line = reader.readLine()) != null) {
                    if (line.toLowerCase().contains("name:" + itemName.toLowerCase())) { // Check if the line contains the searched item name
                        String[] parts = line.split(",");
                        if (parts.length >= 3) {
                            // Reconstruct the string without the status
                            String namePart = parts[0]; // Name:Value
                            String categoryPart = parts[1]; // Category:Value
                            String pricePart = parts[2]; // Price:Value
                            data.append(namePart).append(",").append(categoryPart).append(",").append(pricePart).append("\n");
                        }
                    }
                }
                reader.close();

                if (data.length() > 0) {
                    textAreaData.setText(data.toString());
                } else {
                    textAreaData.setText("No matching item found.");
                }
            } catch (IOException e) {
                textAreaData.setText("Error: " + e.getMessage());
            }
        });
    }



    private void searchCategory() {
        SwingUtilities.invokeLater(() -> {
            String data = textAreaData.getText();

            // Retrieve item name entered by the user
            String itemName = tagsCountField.getText().trim();

            // Initialize variables to store category and price
            String category = "";
            String price = "";

            // Splitting the data into lines
            String[] lines = data.split("\\n");

            // Iterate through each line to find the item
            for (String line : lines) {
                // Splitting each line into parts using ","
                String[] parts = line.split(",");
                // Extracting name, category, and price from parts
                if (parts.length >= 3) {
                    String namePart = parts[0];
                    String categoryPart = parts[1];
                    String pricePart = parts[2];

                    // Extract name without label
                    String name = namePart.substring(namePart.indexOf(':') + 1).trim();

                    // Check if the item name matches
                    if (name.equalsIgnoreCase(itemName)) {
                        // Extract category and price without labels
                        category = categoryPart.substring(categoryPart.indexOf(':') + 1).trim();
                        price = pricePart.substring(pricePart.indexOf(':') + 1).trim();
                        break;
                    }
                }
            }

            // Set the item category and price in corresponding text fields
            categoryNameField.setText(category);
            iconNameField.setText(price);
        });
    }



    private void saveToDatabase(String categoryName, String iconName, String tagsCount) {
        // Implement your save to database logic here
        // This method is called when the user clicks the "Save To Database" button
    }
}
