Feature: Order Processing and Tracking

  As a user,
  I want to be able to place orders, make payments, and track their shipment status,
  So that I can receive my purchased products.

  Scenario: Place New Order
    Given I have products in my shopping cart
    When I proceed to checkout
    And I confirm my order details
    Then a new order should be created
    And my shopping cart should be emptied
    And I should be redirected to the payment page

  Scenario: Make Payment for Order
    Given I have an un-paid order
    When I provide valid payment information
    And I submit the payment
    Then the payment should be processed successfully
    And the order status should be updated to paid
    And a transaction record should be created

  Scenario: View Order Details
    Given I have placed an order
    When I navigate to my order history or order details page
    Then I should see the details of my order, including products, quantities, and total amount
    And I should see the current status of the order (e.g., paid, shipped)

  Scenario: Track Shipped Items
    Given I have placed an order that has been shipped
    When I navigate to the shipped items section
    Then I should see a list of all my orders that have been shipped
    And I should see relevant shipping details if available

  Scenario: View Unshipped Items
    Given I have placed an order that has not yet been shipped
    When I navigate to the unshipped items section
    Then I should see a list of all my orders that are awaiting shipment