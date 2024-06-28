<?php
$servername = "localhost";
$username = "root";
$password = ""; // Change this to your actual MySQL root password
$dbname = "grocery";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Ensure all expected POST keys are present
    if (isset($_POST['itemName']) && isset($_POST['category']) && isset($_POST['price'])) {
        $itemName = $_POST['itemName'];
        $category = $_POST['category'];
        $price = $_POST['price'];

        $sql = "INSERT INTO grocery (itemName, category, price, status) VALUES ('$itemName', '$category', '$price', 'Not Delivered')";

        if ($conn->query($sql) === TRUE) {
            echo "New record created successfully";
        } else {
            echo "Error: " . $sql . "<br>" . $conn->error;
        }
    } else {
        echo "Error: Missing required POST parameters.";
    }

    $conn->close();
} else {
    echo "Invalid request method.";
}
