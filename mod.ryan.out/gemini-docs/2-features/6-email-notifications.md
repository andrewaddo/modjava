Feature: Email Notifications

  Scenario: System sends registration confirmation email
    Given a new user successfully registers
    Then the system should send a registration confirmation email to the user's registered email address

  Scenario: System sends order confirmation email
    Given a user successfully places an order
    Then the system should send an order confirmation email to the user's registered email address with order details

  Scenario: System sends shipping update email
    Given an admin updates an order's shipping status to "shipped"
    Then the system should send a shipping update email to the user with tracking information

  Scenario: System sends back-in-stock notification email
    Given a user has subscribed for back-in-stock notifications for a product
    And the product becomes available in stock
    Then the system should send a back-in-stock notification email to the user
