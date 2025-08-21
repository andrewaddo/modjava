# Database Schema Analysis

This document outlines the database schema for the `shopping-cart` application, reverse-engineered from `mysql_query.sql`.

## Schema Overview

The database consists of the following tables:

*   `product`: Stores product information.
*   `orders`: Records order details, linking products to specific orders.
*   `user`: Manages user accounts.
*   `transactions`: Logs payment transactions.
*   `user_demand`: Tracks user demand for out-of-stock products.
*   `usercart`: Stores items currently in a user's shopping cart.

## Table Details

### `product`

Stores details about each product available in the store.

| Column    | Type          | Constraints      | Description                               |
| :-------- | :------------ | :--------------- | :---------------------------------------- |
| `pid`     | `VARCHAR(45)` | `PRIMARY KEY`    | Unique product identifier.                |
| `pname`   | `VARCHAR(100)`| `NULL`           | Product name.                             |
| `ptype`   | `VARCHAR(20)` | `NULL`           | Product type/category (e.g., 'mobile', 'laptop'). |
| `pinfo`   | `VARCHAR(350)`| `NULL`           | Detailed product information.             |
| `pprice`  | `DECIMAL(12,2)`| `NULL`           | Product price.                            |
| `pquantity`| `INT`         | `NULL`           | Available quantity of the product.        |
| `image`   | `LONGBLOB`    | `NULL`           | Binary data for product image.            |

### `orders`

Records individual product items within an order. An order can contain multiple products.

| Column    | Type          | Constraints      | Description                               |
| :-------- | :------------ | :--------------- | :---------------------------------------- |
| `orderid` | `VARCHAR(45)` | `PRIMARY KEY`    | Identifier for the overall order.         |
| `prodid`  | `VARCHAR(45)` | `PRIMARY KEY`, `FOREIGN KEY` (`product`) | Product identifier. |
| `quantity`| `INT`         | `NULL`           | Quantity of the product in this order item. |
| `amount`  | `DECIMAL(10,2)`| `NULL`           | Total amount for this specific product quantity in the order. |
| `shipped` | `INT`         | `NOT NULL`, `DEFAULT 0` | Shipping status (0 for not shipped, 1 for shipped). |

**Foreign Key:**
*   `prodid` references `product(pid)`

### `user`

Stores user registration details.

| Column    | Type          | Constraints      | Description                               |
| :-------- | :------------ | :--------------- | :---------------------------------------- |
| `email`   | `VARCHAR(60)` | `PRIMARY KEY`    | User's email address (used as unique identifier). |
| `name`    | `VARCHAR(30)` | `NULL`           | User's full name.                         |
| `mobile`  | `BIGINT`      | `NULL`           | User's mobile number.                     |
| `address` | `VARCHAR(250)`| `NULL`           | User's shipping address.                  |
| `pincode` | `INT`         | `NULL`           | Postal code for the address.              |
| `password`| `VARCHAR(20)` | `NULL`           | User's password (Note: In a real application, this should be hashed). |

### `transactions`

Records payment transactions, linking them to users and orders.

| Column    | Type          | Constraints      | Description                               |
| :-------- | :------------ | :--------------- | :---------------------------------------- |
| `transid` | `VARCHAR(45)` | `PRIMARY KEY`, `FOREIGN KEY` (`orders`) | Unique transaction identifier, also links to `orderid`. |
| `username`| `VARCHAR(60)` | `NULL`, `FOREIGN KEY` (`user`) | User's email who made the transaction. |
| `time`    | `DATETIME`    | `NULL`           | Timestamp of the transaction.             |
| `amount`  | `DECIMAL(10,2)`| `NULL`           | Total amount of the transaction.          |

**Foreign Keys:**
*   `username` references `user(email)`
*   `transid` references `orders(orderid)` (This implies a 1-to-1 relationship between a transaction and an order, where `transid` is also the `orderid`).

### `user_demand`

Tracks user requests for products that are currently out of stock.

| Column    | Type          | Constraints      | Description                               |
| :-------- | :------------ | :--------------- | :---------------------------------------- |
| `username`| `VARCHAR(60)` | `PRIMARY KEY`, `FOREIGN KEY` (`user`) | User's email who demanded the product. |
| `prodid`  | `VARCHAR(45)` | `PRIMARY KEY`, `FOREIGN KEY` (`product`) | Product identifier that is in demand. |
| `quantity`| `INT`         | `NULL`           | Quantity of the product demanded by the user. |

**Foreign Keys:**
*   `username` references `user(email)`
*   `prodid` references `product(pid)`

### `usercart`

Stores items that users have added to their shopping carts.

| Column    | Type          | Constraints      | Description                               |
| :-------- | :------------ | :--------------- | :---------------------------------------- |
| `username`| `VARCHAR(60)` | `NULL`, `FOREIGN KEY` (`user`) | User's email whose cart this item belongs to. |
| `prodid`  | `VARCHAR(45)` | `NULL`, `FOREIGN KEY` (`product`) | Product identifier in the cart. |
| `quantity`| `INT`         | `NULL`           | Quantity of the product in the cart.      |

**Foreign Keys:**
*   `username` references `user(email)`
*   `prodid` references `product(pid)`

## Relationships

The following relationships exist between the tables:

*   **`product`** is referenced by:
    *   `orders` (one-to-many: one product can be in many order items)
    *   `user_demand` (one-to-many: one product can be demanded by many users)
    *   `usercart` (one-to-many: one product can be in many user carts)

*   **`user`** is referenced by:
    *   `transactions` (one-to-many: one user can have many transactions)
    *   `user_demand` (one-to-many: one user can demand many products)
    *   `usercart` (one-to-many: one user can have many items in their cart)

*   **`orders`** is referenced by:
    *   `transactions` (one-to-one: one order has one transaction, where `transid` in `transactions` is also `orderid` in `orders`).

This concludes the database schema analysis.