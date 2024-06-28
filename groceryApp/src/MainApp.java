import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainApp {
    private JFrame frame;
    private JTextArea textAreaData;
    private JTextField categoryNameField;
    private JTextField iconNameField;
    private JTextField tagsCountField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp().createAndShowGUI());
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
                saveToDatabase(tagsCount, categoryName, iconName);
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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\wanah\\git\\groceryApp\\groceryApp\\src\\grocery.txt"));
                    StringBuilder data = new StringBuilder();
                    String line;
                    String itemName = tagsCountField.getText().trim(); // Retrieve item name entered by the user
                    while ((line = reader.readLine()) != null) {
                        if (line.toLowerCase().contains("name:" + itemName.toLowerCase())) { // Check if the line contains the searched item name
                            data.append(line).append("\n");
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
            }
        });
    }

    private void searchCategory() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String data = textAreaData.getText();

                // Retrieve item name entered by the user
                String itemName = tagsCountField.getText();

                // Initialize variables to store category and price
                String category = "";
                String price = "";

                // Splitting the data into lines
                String[] lines = data.split("\\n");

                // Iterate through each line to find the item
                for (String line : lines) {
                    // Splitting each line into parts using ","
                    String[] parts = line.split(",");
                    // Check if parts length is at least 3 to ensure it has name, category, and price
                    if (parts.length >= 3) {
                        // Extracting name, category, and price from parts
                        String name = parts[0].substring(parts[0].indexOf(":") + 1);
                        category = parts[1].substring(parts[1].indexOf(":") + 1);
                        price = parts[2].substring(parts[2].indexOf(":") + 1);

                        // Check if the item name matches
                        if (name.equalsIgnoreCase(itemName)) {
                            // Exit the loop if found
                            break;
                        }
                    }
                }

                // Set the item category and price in corresponding text fields
                categoryNameField.setText(category);
                iconNameField.setText(price);
            }
        });
    }

    private void saveToDatabase(String itemName, String category, String price) {
        try {
            String urlParameters = "itemName=" + URLEncoder.encode(itemName, "UTF-8")
                    + "&category=" + URLEncoder.encode(category, "UTF-8")
                    + "&price=" + URLEncoder.encode(price, "UTF-8");

            URL url = new URL("http://localhost/DAD/groceryApp/save_to_database.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            OutputStream os = conn.getOutputStream();
            os.write(urlParameters.getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Show success message
                JOptionPane.showMessageDialog(frame, response.toString(), "Record Saved Successfully", JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.println("POST request not worked");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
