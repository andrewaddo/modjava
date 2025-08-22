Feature: Product Management (Admin)

  Scenario: Admin adds a new product
    Given an admin is logged in
    And the admin is on the add product page
    When the admin provides valid product details
    And uploads a product image
    And submits the form
    Then the new product should be added to the catalog
    And the admin should see a success message

  Scenario: Admin views all products
    Given an admin is logged in
    When the admin navigates to the view products page
    Then the admin should see a list of all products with their details

  Scenario: Admin updates a product
    Given an admin is logged in
    And an existing product in the catalog
    When the admin navigates to the update product page for that product
    And updates the product details
    And submits the form
    Then the product details should be updated successfully
    And the admin should see a success message

  Scenario: Admin removes a product
    Given an admin is logged in
    And an existing product in the catalog
    When the admin navigates to the remove product page
    And selects the product to remove
    And confirms the removal
    Then the product should be removed from the catalog
    And the admin should see a success message

  Scenario: Admin manages product stock
    Given an admin is logged in
    And an existing product in the catalog
    When the admin navigates to the stock management page
    And updates the quantity of a product
    And submits the form
    Then the product stock should be updated
    And the admin should see a success message
