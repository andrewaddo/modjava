Feature: User Authentication
  As a user of the e-commerce platform
  I want to be able to register, log in, and log out
  So that I can access personalized features and secure my account

  Scenario: Successful User Registration
    Given I am on the registration page
    When I enter valid registration details
    And I submit the registration form
    Then my account should be created successfully
    And I should be redirected to the login page

  Scenario: Successful User Login
    Given I have a registered account
    And I am on the login page
    When I enter my correct email and password
    And I submit the login form
    Then I should be logged in successfully
    And I should be redirected to my user dashboard

  Scenario: Unsuccessful User Login with Invalid Credentials
    Given I am on the login page
    When I enter an incorrect email or password
    And I submit the login form
    Then I should see an error message indicating invalid credentials
    And I should remain on the login page

  Scenario: User Logout
    Given I am logged in to my account
    When I click on the logout link/button
    Then I should be logged out successfully
    And I should be redirected to the homepage or login page
