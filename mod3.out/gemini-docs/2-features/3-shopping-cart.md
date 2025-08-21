Feature: Shopping Cart Operations
  As a user of the e-commerce platform
  I want to be able to add products to my cart, update quantities, and view my cart
  So that I can prepare my order before checkout

  Scenario: Add Product to Cart
    Given I am logged in
    And I am browsing products
    When I select a product
    And I click 'Add to Cart'
    Then the product should be added to my shopping cart
    And the cart total should be updated

  Scenario: Update Product Quantity in Cart
    Given I have products in my shopping cart
    And I am viewing my shopping cart
    When I change the quantity of a product in the cart
    And I update the cart
    Then the product quantity should be updated
    And the cart total should reflect the change

  Scenario: Remove Product from Cart
    Given I have products in my shopping cart
    And I am viewing my shopping cart
    When I choose to remove a product from the cart
    Then the product should be removed from my shopping cart
    And the cart total should be updated accordingly

  Scenario: View Shopping Cart Details
    Given I have products in my shopping cart
    When I navigate to the shopping cart page
    Then I should see a list of all products in my cart
    And I should see the quantity for each product
    And I should see the total price of all items in my cart
