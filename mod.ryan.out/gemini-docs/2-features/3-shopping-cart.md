Feature: Shopping Cart Operations

  As a user,
  I want to be able to add products to my shopping cart, update quantities, and view cart details,
  So that I can manage items I intend to purchase.

  Scenario: Add Product to Cart
    Given I am logged in
    And I am browsing products
    When I select a product and choose to add it to my cart
    Then the product should be added to my shopping cart
    And the quantity in the cart should reflect my selection
    And I should see a confirmation that the product was added to the cart

  Scenario: Update Product Quantity in Cart
    Given I have products in my shopping cart
    When I change the quantity of a product in my cart
    Then the quantity of that product in my cart should be updated
    And the total amount in the cart should be recalculated

  Scenario: View Shopping Cart Details
    Given I have products in my shopping cart
    When I navigate to my shopping cart details page
    Then I should see a list of all products in my cart
    And I should see the quantity for each product
    And I should see the total price for all items in my cart

  Scenario: Remove Product from Cart
    Given I have products in my shopping cart
    When I choose to remove a product from my cart
    Then the product should be removed from my shopping cart
    And the total amount in the cart should be recalculated

  Scenario: Empty Shopping Cart
    Given I have products in my shopping cart
    When I proceed to checkout or explicitly empty my cart
    Then all products should be removed from my shopping cart
    And the cart total should be zero