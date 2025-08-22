Feature: Order Tracking and Shipment

  As a user,
  I want to track the shipment status of my orders,
  So that I know when to expect my products.

  Scenario: View Shipped Orders
    Given I have placed orders
    When I navigate to the section for shipped items
    Then I should see a list of all my orders that have been marked as shipped
    And for each shipped order, I should see relevant shipping details (e.g., date shipped, tracking number if applicable).

  Scenario: View Unshipped Orders
    Given I have placed orders
    When I navigate to the section for unshipped items
    Then I should see a list of all my orders that are awaiting shipment
    And for each unshipped order, I should see its current processing status.

  Scenario: Admin Marks Order as Shipped
    Given I am logged in as an administrator
    And there are unshipped orders
    When I select an order and mark it as shipped
    Then the order status should be updated to 'shipped'
    And the order should move from the unshipped list to the shipped list for the user.