# Database Schema Analysis

This document outlines the database schema for the `shopping-cart` application, reverse-engineered from `mysql_query.sql`.

## Tables

### `product`
Stores information about products available in the store.
- `pid` (VARCHAR(45)): Primary Key, unique product identifier.
- `pname` (VARCHAR(100)): Name of the product.
- `ptype` (VARCHAR(20)): Type or category of the product (e.g., 'mobile', 'laptop', 'tv', 'camera', 'speaker', 'tablet', 'cooler', 'fan').
- `pinfo` (VARCHAR(350)): Detailed information or description of the product.
- `pprice` (DECIMAL(12,2)): Price of the product.
- `pquantity` (INT): Available quantity of the product in stock.
- `image` (LONGBLOB): Binary data for the product image.

### `orders`
Records individual order items. An order can consist of multiple products.
- `orderid` (VARCHAR(45)): Primary Key, unique order identifier.
- `prodid` (VARCHAR(45)): Primary Key, Foreign Key referencing `product.pid`. Identifies the product in the order.
- `quantity` (INT): Quantity of the specific product in this order item.
- `amount` (DECIMAL(10,2)): Total amount for this specific product quantity in the order.
- `shipped` (INT): Status of shipment (0 for not shipped, 1 for shipped).

### `user`
Stores user registration and profile information.
- `email` (VARCHAR(60)): Primary Key, unique email address of the user, used as username.
- `name` (VARCHAR(30)): Full name of the user.
- `mobile` (BIGINT): Mobile number of the user.
- `address` (VARCHAR(250)): Shipping/billing address of the user.
- `pincode` (INT): Pincode of the user's address.
- `password` (VARCHAR(20)): User's account password.

### `transactions`
Records payment transactions for orders.
- `transid` (VARCHAR(45)): Primary Key, unique transaction identifier. Also acts as a Foreign Key referencing `orders.orderid`.
- `username` (VARCHAR(60)): Foreign Key referencing `user.email`. Identifies the user who made the transaction.
- `time` (DATETIME): Timestamp of the transaction.
- `amount` (DECIMAL(10,2)): Total amount of the transaction.

### `user_demand`
Tracks user demand for out-of-stock products or back-in-stock notifications.
- `username` (VARCHAR(60)): Primary Key, Foreign Key referencing `user.email`.
- `prodid` (VARCHAR(45)): Primary Key, Foreign Key referencing `product.pid`.
- `quantity` (INT): The quantity the user is demanding.

### `usercart`
Stores items currently in a user's shopping cart.
- `username` (VARCHAR(60)): Foreign Key referencing `user.email`.
- `prodid` (VARCHAR(45)): Foreign Key referencing `product.pid`.
- `quantity` (INT): Quantity of the product in the user's cart.

## Relationships

- `orders.prodid` -> `product.pid` (One-to-Many: one product can be in many order items)
- `transactions.username` -> `user.email` (Many-to-One: one user can have many transactions)
- `transactions.transid` -> `orders.orderid` (One-to-One: each transaction corresponds to one order)
- `user_demand.username` -> `user.email` (Many-to-Many: users can demand many products, products can be demanded by many users)
- `user_demand.prodid` -> `product.pid` (Many-to-Many: users can demand many products, products can be demanded by many users)
- `usercart.username` -> `user.email` (Many-to-Many: users can have many products in their cart, products can be in many users' carts)
- `usercart.prodid` -> `product.pid` (Many-to-Many: users can have many products in their cart, products can be in many users' carts)
