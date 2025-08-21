Feature: Order Processing

  As a user of the e-commerce platform
  I want to be able to place an order for items in my shopping cart
  So that I can purchase them

  Scenario: Successful Order Placement
    Given I am logged in
    And my shopping cart contains items
    When I proceed to checkout
    And I provide valid payment information
    And I confirm the order
    Then an order should be successfully placed
    And my shopping cart should be emptied
    And I should receive an order confirmation email
    And the order details should be recorded in my order history

  Scenario: Order Placement with Empty Cart
    Given I am logged in
    And my shopping cart is empty
    When I attempt to proceed to checkout
    Then I should be prevented from placing an order
    And I should see a message indicating my cart is empty

  Scenario: Order Placement with Invalid Payment Information
    Given I am logged in
    And my shopping cart contains items
    When I proceed to checkout
    And I provide invalid payment information
    And I confirm the order
    Then the order should not be placed
    And I should see an error message indicating payment failure
    And my shopping cart should retain its items

  As a user of the e-commerce platform
  I want to be able to view my past orders
  So that I can track their status and review my purchase history

  Scenario: View Order History
    Given I am logged in
    And I have placed previous orders
    When I navigate to my order history page
    Then I should see a list of all my past orders
    And for each order, I should see its ID, date, total amount, and current shipping status

  Scenario: View Details of a Specific Order
    Given I am logged in
    And I have placed an order
    When I select a specific order from my order history
    Then I should see the detailed information for that order
    And the details should include all products ordered, their quantities, and individual prices