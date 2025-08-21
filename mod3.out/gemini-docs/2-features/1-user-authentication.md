Feature: User Authentication and Registration

  As a user of the e-commerce platform
  I want to be able to register for a new account
  So that I can log in and use the platform's features

  Scenario: Successful User Registration
    Given I am on the registration page
    When I enter valid registration details
    And I submit the registration form
    Then my account should be created successfully
    And I should receive a registration confirmation email
    And I should be redirected to the login page or user home page

  Scenario: User Registration with Existing Email
    Given I am on the registration page
    And an account with my email already exists
    When I enter my existing email and other valid registration details
    And I submit the registration form
    Then I should see an error message indicating the email is already registered
    And my account should not be re-created

  As a user of the e-commerce platform
  I want to be able to log in to my account
  So that I can access my personalized features and make purchases

  Scenario: Successful User Login
    Given I have a registered account
    And I am on the login page
    When I enter my correct email and password
    And I submit the login form
    Then I should be successfully logged in
    And I should be redirected to my user home page

  Scenario: User Login with Invalid Credentials
    Given I am on the login page
    When I enter an incorrect email or password
    And I submit the login form
    Then I should see an error message indicating invalid credentials
    And I should remain on the login page

  As a logged-in user
  I want to be able to log out of my account
  So that I can secure my session and prevent unauthorized access

  Scenario: Successful User Logout
    Given I am logged in to my account
    When I click on the logout option
    Then I should be logged out successfully
    And I should be redirected to the login page or home page