<?php
$servername = "localhost";
$username = "root";
$password = ""; // Change this to your actual MySQL root password
$dbname = "grocery";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

if (isset($_GET['itemName'])) {
    $itemName = $_GET['itemName'];
    $sql = "SELECT status FROM grocery WHERE itemName='$itemName'";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        echo $row['status'];
    } else {
        echo "Not Found";
    }
} else {
    echo "Error";
}

$conn->close();
