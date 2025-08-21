# Database Schema

This document outlines the database schema for the `shopping-cart` application, reverse-engineered from `mysql_query.sql`.

## Tables

### `product`

Stores information about products available in the store.

| Column    | Type        | Description                               |
| :-------- | :---------- | :---------------------------------------- |
| `pid`     | VARCHAR(45) | Primary Key, Unique Product Identifier    |
| `pname`   | VARCHAR(100)| Product Name                              |
| `ptype`   | VARCHAR(20) | Product Type (e.g., mobile, laptop, tv)   |
| `pinfo`   | VARCHAR(350)| Product Information/Description           |
| `pprice`  | DECIMAL(12,2)| Product Price                             |
| `pquantity`| INT         | Available Quantity of the Product         |
| `image`   | LONGBLOB    | Product Image (Binary Data)               |

```sql
CREATE TABLE IF NOT EXISTS `shopping-cart`.`product` (
  `pid` VARCHAR(45) NOT NULL,
  `pname` VARCHAR(100) NULL DEFAULT NULL,
  `ptype` VARCHAR(20) NULL DEFAULT NULL,
  `pinfo` VARCHAR(350) NULL DEFAULT NULL,
  `pprice` DECIMAL(12,2) NULL DEFAULT NULL,
  `pquantity` INT NULL DEFAULT NULL,
  `image` LONGBLOB NULL DEFAULT NULL,
  PRIMARY KEY (`pid`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
```

### `orders`

Records details of customer orders.

| Column    | Type        | Description                               |
| :-------- | :---------- | :---------------------------------------- |
| `orderid` | VARCHAR(45) | Primary Key, Unique Order Identifier      |
| `prodid`  | VARCHAR(45) | Primary Key, Foreign Key to `product.pid` |
| `quantity`| INT         | Quantity of the product in this order     |
| `amount`  | DECIMAL(10,2)| Total amount for this product in the order|
| `shipped` | INT         | Shipping status (0 for not shipped, 1 for shipped) |

```sql
CREATE TABLE IF NOT EXISTS `shopping-cart`.`orders` (
  `orderid` VARCHAR(45) NOT NULL,
  `prodid` VARCHAR(45) NOT NULL,
  `quantity` INT NULL DEFAULT NULL,
  `amount` DECIMAL(10,2) NULL DEFAULT NULL,
  `shipped` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`orderid`, `prodid`),
  INDEX `productid_idx` (`prodid` ASC) VISIBLE,
  CONSTRAINT `productid`
    FOREIGN KEY (`prodid`)
    REFERENCES `shopping-cart`.`product` (`pid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
```

### `user`

Stores user account information.

| Column    | Type        | Description                               |
| :-------- | :---------- | :---------------------------------------- |
| `email`   | VARCHAR(60) | Primary Key, User's Email Address         |
| `name`    | VARCHAR(30) | User's Full Name                          |
| `mobile`  | BIGINT      | User's Mobile Number                      |
| `address` | VARCHAR(250)| User's Shipping Address                   |
| `pincode` | INT         | User's Pincode/Zip Code                   |
| `password`| VARCHAR(20) | User's Account Password                   |

```sql
CREATE TABLE IF NOT EXISTS `shopping-cart`.`user` (
  `email` VARCHAR(60) NOT NULL,
  `name` VARCHAR(30) NULL DEFAULT NULL,
  `mobile` BIGINT NULL DEFAULT NULL,
  `address` VARCHAR(250) NULL DEFAULT NULL,
  `pincode` INT NULL DEFAULT NULL,
  `password` VARCHAR(20) NULL DEFAULT NULL,
  PRIMARY KEY (`email`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
```

### `transactions`

Logs all payment transactions.

| Column    | Type        | Description                               |
| :-------- | :---------- | :---------------------------------------- |
| `transid` | VARCHAR(45) | Primary Key, Unique Transaction Identifier|
| `username`| VARCHAR(60) | Foreign Key to `user.email`               |
| `time`    | DATETIME    | Timestamp of the transaction              |
| `amount`  | DECIMAL(10,2)| Transaction Amount                        |

```sql
CREATE TABLE IF NOT EXISTS `shopping-cart`.`transactions` (
  `transid` VARCHAR(45) NOT NULL,
  `username` VARCHAR(60) NULL DEFAULT NULL,
  `time` DATETIME NULL DEFAULT NULL,
  `amount` DECIMAL(10,2) NULL DEFAULT NULL,
  PRIMARY KEY (`transid`),
  INDEX `truserid_idx` (`username` ASC) VISIBLE,
  CONSTRAINT `truserid`
    FOREIGN KEY (`username`)
    REFERENCES `shopping-cart`.`user` (`email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `transorderid`
    FOREIGN KEY (`transid`)
    REFERENCES `shopping-cart`.`orders` (`orderid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
```

### `user_demand`

Tracks products for which users have expressed demand (e.g., back-in-stock notifications).

| Column    | Type        | Description                               |
| :-------- | :---------- | :---------------------------------------- |
| `username`| VARCHAR(60) | Primary Key, Foreign Key to `user.email`  |
| `prodid`  | VARCHAR(45) | Primary Key, Foreign Key to `product.pid` |
| `quantity`| INT         | Demanded Quantity                         |

```sql
CREATE TABLE IF NOT EXISTS `shopping-cart`.`user_demand` (
  `username` VARCHAR(60) NOT NULL,
  `prodid` VARCHAR(45) NOT NULL,
  `quantity` INT NULL DEFAULT NULL,
  PRIMARY KEY (`username`, `prodid`),
  INDEX `prodid_idx` (`prodid` ASC) VISIBLE,
  CONSTRAINT `userdemailemail`
    FOREIGN KEY (`username`)
    REFERENCES `shopping-cart`.`user` (`email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `prodid`
    FOREIGN KEY (`prodid`)
    REFERENCES `shopping-cart`.`product` (`pid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
```

### `usercart`

Stores items currently in a user's shopping cart.

| Column    | Type        | Description                               |
| :-------- | :---------- | :---------------------------------------- |
| `username`| VARCHAR(60) | Foreign Key to `user.email`               |
| `prodid`  | VARCHAR(45) | Foreign Key to `product.pid`              |
| `quantity`| INT         | Quantity of the product in the cart       |

```sql
CREATE TABLE IF NOT EXISTS `shopping-cart`.`usercart` (
  `username` VARCHAR(60) NULL DEFAULT NULL,
  `prodid` VARCHAR(45) NULL DEFAULT NULL,
  `quantity` INT NULL DEFAULT NULL,
  INDEX `useremail_idx` (`username` ASC) VISIBLE,
  INDEX `prodidcart_idx` (`prodid` ASC) VISIBLE,
  CONSTRAINT `useremail`
    FOREIGN KEY (`username`)
    REFERENCES `shopping-cart`.`user` (`email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `prodidcart`
    FOREIGN KEY (`prodid`)
    REFERENCES `shopping-cart`.`product` (`pid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
```
