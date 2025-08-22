# Database Schema Analysis for `shopping-cart` Application

This document outlines the database schema for the legacy `shopping-cart` application, derived from the `mysql_query.sql` file.

## Tables and Their Relationships

### 1. `product` Table
- **Purpose**: Stores details about products available in the e-commerce platform.
- **Columns**:
    - `pid` (VARCHAR(45)): Primary Key, unique product identifier.
    - `pname` (VARCHAR(100)): Product name.
    - `ptype` (VARCHAR(20)): Product type/category.
    - `pinfo` (VARCHAR(350)): Product description/information.
    - `pprice` (DECIMAL(12,2)): Product price.
    - `pquantity` (INT): Available stock quantity.
    - `image` (LONGBLOB): Product image (stored as BLOB).

### 2. `orders` Table
- **Purpose**: Records individual product items within an order. An order can consist of multiple product entries.
- **Columns**:
    - `orderid` (VARCHAR(45)): Part of Composite Primary Key, identifies a specific order.
    - `prodid` (VARCHAR(45)): Part of Composite Primary Key, identifies the product within the order.
    - `quantity` (INT): Quantity of the product in this order entry.
    - `amount` (DECIMAL(10,2)): Total amount for this product entry (quantity * price).
    - `shipped` (INT): Shipping status (0 for not shipped, 1 for shipped).
- **Relationships**:
    - `prodid` is a Foreign Key referencing `product.pid`.

### 3. `user` Table
- **Purpose**: Stores user registration and profile information.
- **Columns**:
    - `email` (VARCHAR(60)): Primary Key, unique user email (used as username).
    - `name` (VARCHAR(30)): User's full name.
    - `mobile` (BIGINT): User's mobile number.
    - `address` (VARCHAR(250)): User's shipping address.
    - `pincode` (INT): User's address pincode.
    - `password` (VARCHAR(20)): User's login password.

### 4. `transactions` Table
- **Purpose**: Records payment transactions for orders.
- **Columns**:
    - `transid` (VARCHAR(45)): Primary Key, unique transaction identifier.
    - `username` (VARCHAR(60)): Email of the user who made the transaction.
    - `time` (DATETIME): Timestamp of the transaction.
    - `amount` (DECIMAL(10,2)): Total amount of the transaction.
- **Relationships**:
    - `username` is a Foreign Key referencing `user.email`.
    - `transid` is a Foreign Key referencing `orders.orderid` (implies a 1-to-1 relationship between a transaction and an order, or one transaction covers one order).

### 5. `user_demand` Table
- **Purpose**: Tracks user demand for specific products, likely for back-in-stock notifications.
- **Columns**:
    - `username` (VARCHAR(60)): Part of Composite Primary Key, identifies the user.
    - `prodid` (VARCHAR(45)): Part of Composite Primary Key, identifies the product.
    - `quantity` (INT): The quantity the user is demanding.
- **Relationships**:
    - `username` is a Foreign Key referencing `user.email`.
    - `prodid` is a Foreign Key referencing `product.pid`.

### 6. `usercart` Table
- **Purpose**: Stores items currently in a user's shopping cart.
- **Columns**:
    - `username` (VARCHAR(60)): Identifies the user whose cart it is.
    - `prodid` (VARCHAR(45)): Identifies the product in the cart.
    - `quantity` (INT): Quantity of the product in the cart.
- **Relationships**:
    - `username` is a Foreign Key referencing `user.email`.
    - `prodid` is a Foreign Key referencing `product.pid`.

## Initial Data
The `mysql_query.sql` also contains `INSERT` statements to populate these tables with initial data, including sample products, a sample order, guest and admin users, a transaction, user demand, and a user cart entry.
