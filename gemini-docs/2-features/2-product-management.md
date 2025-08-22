Feature: Product Management (Admin)

  Scenario: Add a new product
    Given an admin is on the 'Add Product' page
    When they enter the product name, type, description, price, and quantity
    And they upload a product image
    And they submit the form
    Then a new product should be added to the system

  Scenario: Remove a product
    Given an admin is viewing the list of products
    When they select a product to remove
    And they confirm the removal
    Then the product should be removed from the system

  Scenario: Update a product
    Given an admin is on the 'Update Product' page for a specific product
    When they modify the product's name, type, description, price, or quantity
    And they save the changes
    Then the product's information should be updated

  Scenario: View all products
    Given an admin is on the 'Admin View Product' page
    When they view the list of products
    Then they should see a table with all products and their details

  Scenario: Manage product stock
    Given an admin is on the 'Admin Stock' page
    When they view the product stock levels
    Then they should see a list of all products and their current stock quantities
    When they update the stock quantity for a product
    Then the product's stock level should be updated