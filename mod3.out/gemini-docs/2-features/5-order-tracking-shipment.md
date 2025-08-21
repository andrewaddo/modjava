Feature: Order Tracking and Shipment (Admin)

  As an administrator of the e-commerce platform
  I want to be able to view all unshipped orders
  So that I can manage and process them for delivery

  Scenario: View Unshipped Orders
    Given I am logged in as an administrator
    When I navigate to the unshipped orders section
    Then I should see a list of all orders that have not yet been shipped
    And each order should display relevant details (e.g., order ID, customer, products, quantity, amount)

  As an administrator of the e-commerce platform
  I want to be able to update the shipment status of an order
  So that customers can track their deliveries

  Scenario: Mark Order as Shipped
    Given I am logged in as an administrator
    And there is an unshipped order
    When I select the order
    And I mark it as shipped
    Then the order's status should be updated to "shipped"
    And the order should move from the unshipped list to the shipped list
    And the customer should receive a shipping update email

  As an administrator of the e-commerce platform
  I want to be able to view all shipped orders
  So that I can review past deliveries

  Scenario: View Shipped Orders
    Given I am logged in as an administrator
    When I navigate to the shipped orders section
    Then I should see a list of all orders that have been shipped
    And each order should display relevant details (e.g., order ID, customer, products, quantity, amount, shipment date)