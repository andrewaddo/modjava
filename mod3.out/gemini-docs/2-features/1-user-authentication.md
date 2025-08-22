Feature: User Authentication and Management

  Scenario: User Registration
    Given a user is on the registration page
    When the user provides valid registration details
    And submits the registration form
    Then the user should be successfully registered
    And redirected to the login page

  Scenario: User Login
    Given a registered user
    And the user is on the login page
    When the user provides valid login credentials
    And submits the login form
    Then the user should be successfully logged in
    And redirected to their home page

  Scenario: User Logout
    Given a logged-in user
    When the user clicks on the logout link
    Then the user should be logged out
    And redirected to the login page or home page

  Scenario: View User Profile
    Given a logged-in user
    When the user navigates to their profile page
    Then the user should see their profile information

  Scenario: Update User Profile
    Given a logged-in user
    And the user is on their profile page
    When the user updates their profile details
    And submits the form
    Then the user's profile information should be updated successfully
