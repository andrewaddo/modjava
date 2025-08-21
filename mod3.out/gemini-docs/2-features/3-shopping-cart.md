Feature: Shopping Cart Operations

  As a user of the e-commerce platform
  I want to be able to add products to my shopping cart
  So that I can purchase them later

  Scenario: Add Product to Cart Successfully
    Given I am logged in
    And a product is available in the catalog
    When I view the product details
    And I click the "Add to Cart" button for a specific quantity
    Then the product should be added to my shopping cart
    And my cart should reflect the added product and quantity
    And I should see a confirmation that the item was added to the cart

  Scenario: Add Out-of-Stock Product to Cart
    Given I am logged in
    And a product is out of stock
    When I attempt to add the product to my cart
    Then I should receive a message indicating the product is out of stock
    And the product should not be added to my cart

  As a user of the e-commerce platform
  I want to be able to view the contents of my shopping cart
  So that I can review my selections before checkout

  Scenario: View Cart Contents
    Given I have items in my shopping cart
    When I navigate to the shopping cart page
    Then I should see a list of all products in my cart
    And for each product, I should see its name, price, quantity, and subtotal
    And I should see the total amount for all items in my cart

  As a user of the e-commerce platform
  I want to be able to update the quantity of items in my shopping cart
  So that I can adjust my purchase before checkout

  Scenario: Update Product Quantity in Cart
    Given I have a product in my shopping cart
    When I change the quantity of that product in the cart
    And I confirm the update
    Then the quantity of the product in my cart should be updated
    And the subtotal for that product and the total cart amount should be recalculated

  Scenario: Remove Product from Cart
    Given I have a product in my shopping cart
    When I choose to remove that product from the cart
    Then the product should be removed from my cart
    And the total cart amount should be recalculated

  Scenario: Clear Entire Cart
    Given I have multiple products in my shopping cart
    When I choose to clear my entire cart
    Then all products should be removed from my cart
    And the total cart amount should be zero