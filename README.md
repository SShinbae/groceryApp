# Grocery Management and Order Tracking System

This project consists of two integrated applications to streamline grocery management and order tracking:

**1. MainApp (Grocery Management System):**

* **Purpose:** Manages grocery items, including searching, displaying details, and saving to a database.
* **Features:**
    * Fetches item data from a local text file.
    * Allows searching items by name.
    * Displays item details (name, category, price).
    * Saves item data to a database via a middleware.
* **Architecture:** Interacts with the user and communicates with the middleware for database operations.

**2. trackApp (Track Order):**

* **Purpose:** Enables users to track the status of their orders.
* **Features:**
    * Tracks order status by item name.
    * Updates order status to "Delivered" via the middleware.
    * Disables the "Delivered" button for delivered items.
* **Architecture:** Provides a user interface and communicates with the middleware to retrieve and update order status.

## Architecture Diagram


![architecture image](https://github.com/SShinbae/groceryApp/assets/80872201/e74d5484-2d11-40d3-9ed0-7ed17e2c0bf8)

## Middleware Endpoints

* **MainApp:**
    * `http://localhost/DAD/groceryApp/save_to_database.php` (POST): Saves item data.
* **trackApp:**
    * `http://localhost/DAD/groceryApp/retrieve.php` (GET): Retrieves order status.
    * `http://localhost/DAD/groceryApp/update_status.php` (POST): Updates order status.

## Middleware Functions

* `save_to_database.php`: Inserts item data into the database.
* `retrieve.php`: Fetches order status from the database.
* `update_status.php`: Updates order status in the database.

## Database Schema

**Grocery Table**

| Column Name | Data Type | Description                                |
|-------------|----------|--------------------------------------------|
| itemName    | string   | Name of the item (e.g., "Apple", "Milk")     |
| category    | string   | Category of the item (e.g., "Fruit", "Dairy") |
| price       | decimal  | Price of the item (e.g., 1.25, 3.99)         |
| status      | string   | Order status ("Not Delivered", "Delivered")   |
