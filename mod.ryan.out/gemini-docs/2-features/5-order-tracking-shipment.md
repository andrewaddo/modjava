Feature: Order Tracking and Shipment

  Scenario: User tracks shipping status of an order
    Given a user has placed an order
    When the user navigates to their order history
    And selects an order
    Then the user should see the current shipping status of the order
