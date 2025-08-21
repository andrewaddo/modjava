Feature: Email Notifications

  As a user of the e-commerce platform
  I want to receive email notifications for important events
  So that I am kept informed about my account and orders

  Scenario: Receive Registration Confirmation Email
    Given I have successfully registered for a new account
    Then I should receive an email confirming my registration
    And the email should contain a welcome message and account details

  Scenario: Receive Order Confirmation Email
    Given I have successfully placed an order
    Then I should receive an email confirming my order
    And the email should contain my order ID, a summary of items, and total amount

  Scenario: Receive Shipping Update Email
    Given my order has been marked as shipped by an administrator
    Then I should receive an email notifying me of the shipment
    And the email should contain my order ID and details about the shipped product

  Scenario: Receive Back-in-Stock Notification Email
    Given I have expressed demand for an out-of-stock product
    And the product has become available again (quantity > 0)
    Then I should receive an email notifying me that the product is back in stock
    And the email should contain the product name and a link to purchase it
    And my demand for that product should be removed from the system