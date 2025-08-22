Feature: Shopping Cart

  Scenario: Add a product to the cart
    Given a logged-in user is viewing a product
    When they click the 'Add to Cart' button
    Then the product should be added to their shopping cart

  Scenario: View the shopping cart
    Given a logged-in user has items in their shopping cart
    When they navigate to the 'Cart Details' page
    Then they should see a list of all items in their cart with quantities and prices

  Scenario: Update the quantity of an item in the cart
    Given a logged-in user is on the 'Cart Details' page
    When they change the quantity of a product
    Then the total price for that item and the cart total should be updated

  Scenario: Remove an item from the cart
    Given a logged-in user is on the 'Cart Details' page
    When they remove a product from their cart
    Then the product should be removed from their shopping cart