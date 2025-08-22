Feature: Order Management

  Scenario: Checkout and place an order
    Given a logged-in user has items in their shopping cart
    When they proceed to checkout
    And they provide their shipping and payment information
    And they confirm the order
    Then a new order should be created
    And the items in the cart should be cleared

  Scenario: View order details
    Given a logged-in user has placed an order
    When they navigate to the 'Order Details' page
    Then they should see the details of their order, including the items, quantities, and total price

  Scenario: View shipped items (Admin)
    Given an admin is on the 'Shipped Items' page
    When they view the list of shipped items
    Then they should see a list of all orders that have been shipped

  Scenario: View unshipped items (Admin)
    Given an admin is on the 'Unshipped Items' page
    When they view the list of unshipped items
    Then they should see a list of all orders that have not yet been shipped

  Scenario: Mark an order as shipped (Admin)
    Given an admin is viewing an unshipped order
    When they mark the order as shipped
    Then the order's status should be updated to 'shipped'