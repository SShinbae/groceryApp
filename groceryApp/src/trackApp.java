import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class trackApp {

    private JFrame frame;
    private JTextField textField_item;
    private JTextField textField_status;

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
            }
        });
        btnCheck.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnCheck.setBounds(271, 58, 85, 28);
        frame.getContentPane().add(btnCheck);
    }

    /**
     * Get the status of an item from a file.
     */
    private String getItemStatus(String itemName) {
        File file = new File("C:\\Users\\wanah\\eclipse-workspace\\groceryApp\\src\\grocery.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                String name = data[0].split(":")[1].trim();
                if (name.equalsIgnoreCase(itemName.trim())) {
                    String status = data[3].split(":")[1].trim(); // Fetching the delivery status
                    return status; // Return "Delivered" or "Not Delivered"
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "File not found";
        }
        return "Not Found"; // If item is not in the list
    }
}
