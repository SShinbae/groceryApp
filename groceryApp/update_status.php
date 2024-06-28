<?php
$servername = "localhost";
$username = "root";
$password = ""; // Change this to your actual MySQL root password
$dbname = "grocery";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $itemName = $_POST['itemName'];
    $status = $_POST['status'];

    $sql = "UPDATE grocery SET status='$status' WHERE itemName='$itemName'";

    if ($conn->query($sql) === TRUE) {
        echo "Status updated successfully";
    } else {
        echo "Error updating status: " . $conn->error;
    }

    $conn->close();
} else {
    echo "Invalid request method.";
}
