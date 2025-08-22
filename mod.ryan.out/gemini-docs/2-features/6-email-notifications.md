Feature: Email Notifications

  As a user,
  I want to receive email notifications for important events,
  So that I stay informed about my account and orders.

  Scenario: User Registration Confirmation
    Given I have successfully registered for a new account
    Then I should receive an email confirming my registration
    And the email should contain my registration details and a welcome message.

  Scenario: Order Placement Confirmation
    Given I have successfully placed an order
    Then I should receive an email confirming my order
    And the email should contain my order details (items, total amount, order ID).

  Scenario: Shipping Update Notification
    Given my order has been marked as shipped by an administrator
    Then I should receive an email notification about the shipment
    And the email should contain shipping details (e.g., tracking number, estimated delivery).

  Scenario: Back-in-Stock Notification
    Given I have expressed interest in a product that was out of stock
    And the product is now back in stock
    Then I should receive an email notification that the product is available for purchase.