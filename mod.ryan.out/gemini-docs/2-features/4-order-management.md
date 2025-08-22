Feature: Order Processing and Shipment Tracking

  Scenario: User places an order
    Given a user is logged in
    And the user has items in their shopping cart
    When the user proceeds to checkout
    And provides shipping and payment information
    And confirms the order
    Then an order should be created
    And the user should receive an order confirmation

  Scenario: User makes payment for an order
    Given a user has an outstanding order
    When the user navigates to the payment page for the order
    And provides valid payment details
    And submits the payment
    Then the payment should be processed successfully
    And the order status should be updated to paid

  Scenario: User views order details
    Given a user has placed an order
    When the user navigates to their order history
    And selects a specific order
    Then the user should see the details of the order, including products, quantities, and total amount

  Scenario: Admin views unshipped items
    Given an admin is logged in
    When the admin navigates to the unshipped items page
    Then the admin should see a list of all orders that have not yet been shipped

  Scenario: Admin views shipped items
    Given an admin is logged in
    When the admin navigates to the shipped items page
    Then the admin should see a list of all orders that have been shipped
