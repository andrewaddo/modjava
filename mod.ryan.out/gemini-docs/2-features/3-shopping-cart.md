Feature: Shopping Cart Management

  Scenario: User adds a product to the cart
    Given a user is logged in
    And a product is available in the catalog
    When the user views the product details
    And clicks "Add to Cart"
    Then the product should be added to the user's shopping cart
    And the user should see a confirmation message

  Scenario: User updates product quantity in the cart
    Given a user has products in their shopping cart
    When the user views their cart details
    And updates the quantity of a product in the cart
    And confirms the update
    Then the product quantity in the cart should be updated
    And the cart total should be recalculated

  Scenario: User views cart details
    Given a user has products in their shopping cart
    When the user navigates to the cart details page
    Then the user should see a list of all products in their cart
    And the quantity of each product
    And the total price of the cart
