Feature: Order Management
  As a user of the e-commerce platform
  I want to be able to place orders and track their shipping status
  So that I can receive my purchased items

  Scenario: Place a New Order
    Given I have items in my shopping cart
    And I am logged in
    When I proceed to checkout
    And I provide valid payment and shipping information
    And I confirm the order
    Then a new order should be created successfully
    And I should receive an order confirmation
    And my shopping cart should be emptied

  Scenario: View Order History
    Given I am logged in
    And I have previously placed orders
    When I navigate to my order history
    Then I should see a list of all my past orders
    And for each order, I should see details like order ID, date, total amount, and status

  Scenario: Track Order Shipping Status
    Given I have a placed order
    And I am logged in
    When I view the details of a specific order
    Then I should see the current shipping status of that order (e.g., 'shipped', 'pending')

  Scenario: Admin Views All Orders
    Given I am logged in as an administrator
    When I navigate to the admin order management section
    Then I should see a list of all customer orders
    And I should be able to filter or sort orders by status (e.g., shipped, unshipped)

  Scenario: Admin Updates Order Shipping Status
    Given I am logged in as an administrator
    And there is an unshipped order
    When I select an unshipped order
    And I update its status to 'shipped'
    Then the order's shipping status should be updated
    And the user should be able to see the updated status
