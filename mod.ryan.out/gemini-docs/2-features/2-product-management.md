Feature: Product Management (Admin)

  As an administrator,
  I want to manage products in the catalog,
  So that I can add, update, and remove products, and manage their stock.

  Scenario: Add New Product
    Given I am logged in as an administrator
    And I am on the add product page
    When I provide valid product details (ID, name, type, info, price, quantity, image)
    And I submit the add product form
    Then the new product should be added to the catalog
    And I should see a confirmation message

  Scenario: View All Products
    Given I am logged in as an administrator
    When I navigate to the view products page
    Then I should see a list of all products with their details

  Scenario: Update Existing Product
    Given I am logged in as an administrator
    And an existing product is available in the catalog
    When I navigate to the update product page for a specific product
    And I modify its details (e.g., price, quantity, info)
    And I submit the update product form
    Then the product details should be updated in the catalog
    And I should see a confirmation message

  Scenario: Remove Product
    Given I am logged in as an administrator
    And an existing product is available in the catalog
    When I navigate to the remove product page or section
    And I select a product to remove
    And I confirm the removal
    Then the product should be removed from the catalog
    And I should see a confirmation message

  Scenario: Manage Product Stock
    Given I am logged in as an administrator
    When I navigate to the stock management page
    Then I should see the current stock levels for all products
    And I should be able to adjust stock quantities for individual products