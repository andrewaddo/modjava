Feature: Email Notifications
  As a user of the e-commerce platform
  I want to receive email notifications for important events
  So that I stay informed about my account and orders

  Scenario: User Registration Confirmation Email
    Given I have successfully registered an account
    Then I should receive a welcome email confirming my registration

  Scenario: Order Placement Confirmation Email
    Given I have successfully placed an order
    Then I should receive an email confirming my order details

  Scenario: Shipping Update Notification Email
    Given my order's shipping status has been updated to 'shipped'
    Then I should receive an email notifying me of the shipping update

  Scenario: Back-in-Stock Notification Email
    Given I have expressed demand for a product that was out of stock
    And the product is now back in stock
    Then I should receive an email notifying me that the product is available for purchase
