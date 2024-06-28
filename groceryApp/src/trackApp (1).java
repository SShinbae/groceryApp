import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.swing.JOptionPane;

public class trackApp {

    private JFrame frame;
    private JTextField textField_item;
    private JTextField textField_status;
    private JButton btnDelivered;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    trackApp window = new trackApp();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public trackApp() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame("Track Order");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblItem = new JLabel("Item Name:");
        lblItem.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblItem.setBounds(25, 35, 95, 13);
        frame.getContentPane().add(lblItem);

        textField_item = new JTextField();
        textField_item.setColumns(12);
        textField_item.setBounds(25, 58, 115, 28);
        frame.getContentPane().add(textField_item);

        JLabel lblstatus = new JLabel("Status:");
        lblstatus.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblstatus.setBounds(25, 134, 95, 13);
        frame.getContentPane().add(lblstatus);

        textField_status = new JTextField();
        textField_status.setBounds(25, 157, 115, 28);
        frame.getContentPane().add(textField_status);
        textField_status.setColumns(10);

        JButton btnCheck = new JButton("Check");
        btnCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String itemName = textField_item.getText();
                String status = getItemStatus(itemName);
                textField_status.setText(status);
                if ("Delivered".equalsIgnoreCase(status)) {
                    btnDelivered.setEnabled(false);
                } else {
                    btnDelivered.setEnabled(true);
                }
            }
        });
        btnCheck.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnCheck.setBounds(271, 58, 85, 28);
        frame.getContentPane().add(btnCheck);

        btnDelivered = new JButton("Delivered");
        btnDelivered.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnDelivered.setBounds(271, 157, 115, 28);
        btnDelivered.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String itemName = textField_item.getText();
                updateItemStatus(itemName, "Delivered");
                textField_status.setText("Delivered");
                btnDelivered.setEnabled(false);
            }
        });
        frame.getContentPane().add(btnDelivered);
    }

    /**
     * Get the status of an item from the database.
     */
    private String getItemStatus(String itemName) {
        try {
            // Construct URL to your PHP script (adjust the path as needed)
            String phpScriptUrl = "http://localhost/groceryApp/retrieve.php?itemName=" + URLEncoder.encode(itemName, "UTF-8");

            // Open connection to the URL
            URL url = new URL(phpScriptUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            // Check for successful response
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                return "Error: Server returned status " + responseCode;
            }

            // Read the response content
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            StringBuilder response = new StringBuilder();
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            conn.disconnect();

            String rawStatus = response.toString().trim();
            if (rawStatus.equals("Not Found")) {
                return "Not Found";
            } else if (rawStatus.equals("Error")) {
                return "Error: Could not retrieve status";
            }

            return rawStatus;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error getting status: " + e.getMessage();
        }
    }

    /**
     * Update the status of an item in the database.
     */
    private void updateItemStatus(String itemName, String status) {
        try {
            // Construct URL to your PHP script (adjust the path as needed)
            String phpScriptUrl = "http://localhost/groceryApp/update_status.php";

            // Open connection to the URL
            URL url = new URL(phpScriptUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Create the POST parameters
            String urlParameters = "itemName=" + URLEncoder.encode(itemName, "UTF-8") +
                                   "&status=" + URLEncoder.encode(status, "UTF-8");

            // Send the POST request
            OutputStream os = conn.getOutputStream();
            os.write(urlParameters.getBytes());
            os.flush();
            os.close();

            // Check for successful response
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + responseCode);
            }

            // Read the response content
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            StringBuilder response = new StringBuilder();
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            conn.disconnect();

            JOptionPane.showMessageDialog(frame, response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error updating status: " + e.getMessage());
        }
    }
}
