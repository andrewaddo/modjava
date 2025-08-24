# Database Schema

This document outlines the database schema for the `shopping-cart` application, as inferred from the `shopping-cart/databases/mysql_query.sql` file.

## Tables

### `product`

Stores information about the products available in the store.

| Column      | Type          | Description                                   |
|-------------|---------------|-----------------------------------------------|
| `pid`       | VARCHAR(45)   | The unique identifier for the product.        |
| `name`     | VARCHAR(100)  | The name of the product.                      |
| `type`     | VARCHAR(20)   | The type or category of the product.          |
| `info`     | VARCHAR(350)  | A description of the product.                 |
| `price`    | DECIMAL(12,2) | The price of the product.                     |
| `quantity` | INT           | The quantity of the product in stock.         |
| `image`     | LONGBLOB      | The product image.                            |

### `orders`

Stores information about customer orders.

| Column    | Type          | Description                                         |
|-----------|---------------|-----------------------------------------------------|
| `orderid` | VARCHAR(45)   | The unique identifier for the order.                |
| `prodid`  | VARCHAR(45)   | The ID of the product that was ordered.             |
| `quantity`| INT           | The quantity of the product that was ordered.       |
| `amount`  | DECIMAL(10,2) | The total amount for this part of the order.        |
| `shipped` | INT           | A flag indicating whether the order has been shipped. |

### `user`

Stores information about registered users.

| Column    | Type          | Description                             |
|-----------|---------------|-----------------------------------------|
| `email`   | VARCHAR(60)   | The user's email address (primary key). |
| `name`    | VARCHAR(30)   | The user's name.                        |
| `mobile`  | BIGINT        | The user's mobile phone number.         |
| `address` | VARCHAR(250)  | The user's shipping address.            |
| `pincode` | INT           | The user's postal code.                 |
| `password`| VARCHAR(20)   | The user's password.                    |
| `role`    | VARCHAR(20)   | The user's role.                        |

### `transactions`

Stores information about financial transactions.

| Column     | Type          | Description                                      |
|------------|---------------|--------------------------------------------------|
| `transid`  | VARCHAR(45)   | The unique identifier for the transaction.       |
| `username` | VARCHAR(60)   | The email of the user who made the transaction.  |
| `time`     | DATETIME      | The date and time of the transaction.            |
| `amount`   | DECIMAL(10,2) | The amount of the transaction.                   |

### `user_demand`

Stores information about products that users have requested to be notified about when they are back in stock.

| Column     | Type        | Description                                  |
|------------|-------------|----------------------------------------------|
| `username` | VARCHAR(60) | The email of the user making the demand.     |
| `prodid`   | VARCHAR(45) | The ID of the product that is in demand.     |
| `quantity` | INT         | The quantity of the product that is in demand. |

### `usercart`

Stores the items in a user's shopping cart.

| Column     | Type        | Description                               |
|------------|-------------|-------------------------------------------|
| `username` | VARCHAR(60) | The email of the user who owns the cart.  |
| `prodid`   | VARCHAR(45) | The ID of the product in the cart.        |
| `quantity` | INT         | The quantity of the product in the cart.  |