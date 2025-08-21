Feature: Product Management (Admin)

  As an administrator of the e-commerce platform
  I want to be able to add new products to the catalog
  So that users can browse and purchase them

  Scenario: Add New Product Successfully
    Given I am logged in as an administrator
    And I am on the add product page
    When I fill in all required product details (name, type, info, price, quantity, image)
    And I submit the add product form
    Then the new product should be successfully added to the catalog
    And I should see a confirmation message
    And the product should be visible in the product listings

  Scenario: Add New Product with Missing Information
    Given I am logged in as an administrator
    And I am on the add product page
    When I leave a required product detail empty
    And I submit the add product form
    Then I should see an error message indicating missing information
    And the product should not be added to the catalog

  As an administrator of the e-commerce platform
  I want to be able to view existing products
  So that I can monitor the catalog and manage inventory

  Scenario: View All Products
    Given I am logged in as an administrator
    When I navigate to the product viewing page
    Then I should see a list of all products in the catalog
    And each product should display its details (name, type, price, quantity)

  As an administrator of the e-commerce platform
  I want to be able to update details of existing products
  So that I can correct information or adjust inventory

  Scenario: Update Product Details Successfully
    Given I am logged in as an administrator
    And an existing product is in the catalog
    When I navigate to the update product page for that product
    And I modify one or more product details (e.g., price, quantity, info)
    And I submit the update form
    Then the product details should be successfully updated in the catalog
    And I should see a confirmation message
    And the updated details should be reflected in product listings
    And if the quantity changes from 0 to >0, users who demanded it should be notified

  Scenario: Update Product with Invalid Information
    Given I am logged in as an administrator
    And an existing product is in the catalog
    When I modify product details with invalid data (e.g., negative price, non-numeric quantity)
    And I submit the update form
    Then I should see an error message indicating invalid input
    And the product details should not be updated

  As an administrator of the e-commerce platform
  I want to be able to remove products from the catalog
  So that I can manage discontinued or unavailable items

  Scenario: Remove Product Successfully
    Given I am logged in as an administrator
    And an existing product is in the catalog
    When I select to remove that product
    And I confirm the removal
    Then the product should be successfully removed from the catalog
    And I should see a confirmation message
    And the product should no longer be visible in product listings

  Scenario: Attempt to Remove Non-Existent Product
    Given I am logged in as an administrator
    When I attempt to remove a product that does not exist
    Then I should see an error message indicating the product was not found
    And the catalog should remain unchanged