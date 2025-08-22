Feature: User Authentication and Registration

  As a user,
  I want to be able to register for a new account,
  So that I can log in and use the shopping cart application.

  Scenario: Successful User Registration
    Given I am on the registration page
    When I provide valid registration details (email, name, mobile, address, pincode, password)
    And I submit the registration form
    Then my account should be created successfully
    And I should be redirected to the login page or user home page

  Scenario: Successful User Login
    Given I have an existing account
    And I am on the login page
    When I enter my registered email and password
    And I submit the login form
    Then I should be successfully logged in
    And I should be redirected to my user home page

  Scenario: Invalid User Login
    Given I am on the login page
    When I enter incorrect email or password
    And I submit the login form
    Then I should see an error message indicating invalid credentials
    And I should remain on the login page

  Scenario: User Logout
    Given I am logged in
    When I click on the logout option
    Then I should be logged out successfully
    And I should be redirected to the login page or home page
