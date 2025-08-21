Feature: Product Management (Admin)
  As an administrator of the e-commerce platform
  I want to be able to add, update, and remove products
  So that I can manage the product catalog effectively

  Scenario: Add New Product
    Given I am logged in as an administrator
    And I am on the add product page
    When I enter valid product details including name, type, info, price, quantity, and image
    And I submit the add product form
    Then the new product should be successfully added to the catalog
    And I should see a confirmation message

  Scenario: Update Existing Product
    Given I am logged in as an administrator
    And I am on the product management page
    And an existing product is available in the catalog
    When I select a product to update
    And I modify its details (e.g., price, quantity, info)
    And I submit the update product form
    Then the product details should be successfully updated in the catalog
    And I should see a confirmation message

  Scenario: Remove Product
    Given I am logged in as an administrator
    And I am on the product management page
    And an existing product is available in the catalog
    When I select a product to remove
    And I confirm the removal
    Then the product should be successfully removed from the catalog
    And I should see a confirmation message
