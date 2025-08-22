Feature: User Authentication

  Scenario: User Registration
    Given a new user is on the registration page
    When they enter their name, email, password, and other required details
    And they submit the registration form
    Then a new user account should be created
    And the user should be logged in

  Scenario: User Login
    Given a registered user is on the login page
    When they enter their email and password
    And they submit the login form
    Then the user should be successfully logged in
    And they should be redirected to their user dashboard

  Scenario: User Profile
    Given a logged-in user is on their profile page
    When they view their profile information
    Then they should see their name, email, mobile number, and address

  Scenario: Update User Profile
    Given a logged-in user is on their profile page
    When they update their name, mobile number, or address
    And they save the changes
    Then their profile information should be updated