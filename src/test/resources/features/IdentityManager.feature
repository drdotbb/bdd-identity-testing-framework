Feature: IdentityManager Create Identity

    As a system admin I would like to create user identities and verify user management.

    @CreateValidIdentity
    Scenario: Create a valid identity
        When I create an identity with "<FirstName>" as the first name, "<LastName>" as the last name, and "<BirthDate>" as the birth date
        Then I should receive a valid UserId, not equal to -1

        Examples:
            |   FirstName |   LastName  |   BirthDate |
            |   John      |   Doe       |   01/01/1998|
            |   Liam      |   Payne     |   01/04/1998|
            |   John123   |   Doe       |   05/01/1998|
            |   John      |   Doe!      |   05/05/1998|

    @CreateInvalidIdentity
    Scenario: Create an invalid Identity using invalid first name
        When I create an identity with "Jason " as the first name, "Smith" as the last name, and "03/04/1997" as the birth date
        Then I should receive -1 for UserId
    
    @CreateInvalidIdentity
    Scenario: Create an invalid Identity using invalid last name
        When I create an identity with "Liam" as the first name, "Pay ne" as the last name, and "01/01/1999" as the birth date
        Then I should receive -1 for UserId

    @CreateInvalidIdentity
    Scenario: Create an invalid Identity using invalid BirthDate format
        When I create an identity with "Brian" as the first name, "Wess" as the last name, and "1999/09/09" as the birth date
        Then I should receive -1 for UserId

    @CreateInvalidIdentity
    Scenario: Create an identity with a future birth date
    When I create an identity with "Adam" as the first name, "Thompson" as the last name, and "01/01/2050" as the birth date
    Then I should receive -1 for UserId
    
    @CreateMultipleIdentities
    Scenario: Create multiple valid identities
        When I create the following identities:
            | FirstName | LastName | BirthDate  |
            | John     | Doe      | 01/01/1998 |
            | Jane     | Smith    | 02/02/1999 |
            | Bob      | Johnson  | 03/03/2000 |
            | Alice    | Brown    | 04/04/2001 |
        Then the system should contain 4 identities
        And I should be able to retrieve each identity
    
    @CreateMultipleIdentities
    Scenario: Create multiple valid identities but with identical data, we should have 1 user created.
        When I create the following identities:
            | FirstName | LastName | BirthDate  |
            | John     | Doe      | 01/01/1998 |
            | John     | Doe      | 01/01/1998 |
            | John     | Doe      | 01/01/1998 |
            | John     | Doe      | 01/01/1998 |
        Then the system should contain 1 identities
        And I should be able to retrieve each identity

    @CreateMultipleIdentitiesWithMixedValidity
    Scenario: Create multiple identities with mixed validity
        When I create the following identities:
            | FirstName| LastName  | BirthDate  | ExpectedResult |
            | John     | Doe       | 01/01/1998 | valid          |
            | Jane     | Smith     | 02/02/1999 | valid          |
            | Bo b     | Johnson   | 03/03/2000 | invalid        |
        Then the system should contain 2 identities
        And I should be able to retrieve each identity
    
    @RemoveIdentity
    Scenario: Remove identity by user id
    Given I create an identity with "Jared" as the first name, "Jackson" as the last name, and "03/04/1997" as the birth date
    When I remove the identity
    Then Then we should receive a successfull remove message
    And the system should contain 0 identities

    @RemoveMultipleIdentities
    Scenario: Create multiple valid identities and attemtp to remove a selection of the users.
        Given I create the following identities:
            | FirstName| LastName | BirthDate  |
            | John     | Doe      | 01/01/1998 |
            | Jane     | Smith    | 02/02/1999 |
            | Bob      | Johnson  | 03/03/2000 |
            | Alice    | Brown    | 04/04/2001 |
    When I remove the following identities:
        | FirstName| LastName | BirthDate  |
        | Bob      | Johnson  | 03/03/2000 |
        | Alice    | Brown    | 04/04/2001 |
        Then Then we should receive a successfull remove message
        And the system should contain 2 identities
        And I should be able to retrieve each identity
    
    @RemoveIdentityByName
    Scenario: Remove identity by exact first name match
    Given I create the following identities:
        | FirstName | LastName | BirthDate  |
        | Alice     | Doe      | 01/01/1998 |
        | Bob       | Smith    | 02/02/1999 |
        | Charlie   | Brown    | 03/03/2000 |
    When I remove all identities by first name "Bob"
    Then we should receive a successfull remove message by first name
    And the system should contain 2 identities
    
    @RemoveIdentityByName
    Scenario: Remove identities by first name with different cases, should enforce case sensitivy.
    Given I create the following identities:
        | FirstName | LastName | BirthDate  |
        | John      | Doe      | 01/01/1998 |
        | JOHN      | Doe      | 01/01/1998 |
        | john      | Smith    | 02/02/2000 |
    When I remove all identities by first name "john"
    Then we should receive a successfull remove message by first name
    And the system should contain 2 identities

    @RemoveNonExistingIdentity
    Scenario: Remove an identity that does not exist
    When I remove the identity with user id 999999999
    Then I should receive an error or "Could not remove Identity. The identity does not exist." message
    And the system should contain 0 identities

    @RemoveAllIdentities
    Scenario: Remove all identities
    Given I create the following identities:
      | FirstName | LastName | BirthDate  |
      | John      | Doe      | 01/01/1998 |
      | Jane      | Smith    | 02/02/1999 |
      | Bob       | Johnson  | 03/03/2000 |
    When I remove all identities
    Then the system should contain 0 identities
    And we should receive a successfull remove message: "All Identities successfully removed."
    
    @UpdateIdentity
    Scenario: Successfully update an existing identity
        Given I create an identity with "John" as the first name, "Doe" as the last name, and "01/01/1998" as the birth date
        When I update the identity with new values:
            | FirstName | LastName | BirthDate  |
            | Jane     | Smith    | 02/02/1999 |
        Then the identity should be updated successfully

    @UpdateIdentity
    Scenario: Attempt updating a non existing identity
        Given I create an identity with "John" as the first name, "Doe" as the last name, and "01/01/1998" as the birth date
        And I store a different userId than the one created
        When I update the identity with new values:
            | FirstName | LastName | BirthDate |
            | Jane     | Smith    | 02/02/1999 |
        Then the update should fail

    @UpdateIdentity
    Scenario: Update identity with null values should fail
        Given I create an identity with "John" as the first name, "Doe" as the last name, and "01/01/1998" as the birth date
        When I update the identity with new values:
            | FirstName | LastName | BirthDate |
            | null      | null     | 02/02/1999 |
        Then the update should fail
    
