# Identity Management BDD Automation Framework

## Overview
This project is a portfolio demonstration of a robust Behavior Driven Development (BDD) test automation framework for an Identity Management System. It showcases the implementation of automated testing strategies using Java and Cucumber to validate business logic, data integrity, and system stability.

The core application is a simulated Identity Management system allowing for the creation, retrieval, updating, and deletion (CRUD) of user identities. The test suite covers positive and negative scenarios, edge cases, and identifies critical defects in the system under test.

## Tech Stack
- **Language**: Java 11
- **Build Tool**: Maven
- **Testing Framework**: JUnit 4
- **BDD Framework**: Cucumber (Gherkin)
- **CI/CD Compatible**: Docker-ready (optional)

## Project Structure
```
src
├── main
│   └── java/com/automation/identity/core    # Core Application Logic
│       ├── Identity.java
│       └── IdentityManager.java
└── test
    ├── java/com/automation/identity/tests   # Test Framework
    │   ├── hooks                            # Cucumber Hooks (Setup/Teardown)
    │   ├── runners                          # Test Runners
    │   └── stepdefinitions                  # Step Definitions
    └── resources/features                   # Gherkin Feature Files
```

## Setup & Execution

### Prerequisites
- JDK 11 or higher
- Maven installed and configured

### Running Tests
To execute the full test suite and generate reports:

```bash
mvn clean test
```

Test results can be viewed in the generated HTML report:
`target/cucumber-reports.html`

## Test Strategy & Bug Report

As part of the quality assurance process, the system was subjected to rigorous testing. Below is a summary of the defects identified during the testing phase, demonstrating the critical analysis performed.

### Defects Identified

#### 1. Invalid Identity Creation (Input Validation)
*   **Issue**: The system accepts invalid names (e.g., containing spaces incorrectly parsed) and invalid date formats.
*   **Scenario**: `Create an invalid Identity using invalid last name`
*   **Root Cause**: In `IdentityManager.java`, the whitespace check logic is flawed (`!lastName.contains("  ")` allows single spaces).
*   **Recommendation**: Update regex or validation logic to strictly forbid spaces if that is the requirement, or parse correctly.

#### 2. Date Handling Vulnerabilities
*   **Issue**: System accepts future birth dates and invalid formats.
*   **Scenario**: `Create an identity with a future birth date`
*   **Root Cause**: Lack of date validation logic in the `Identity` constructor or `createIdentity` method.
*   **Recommendation**: Implement `LocalDate` validation to ensure birth dates are in the past.

#### 3. Data Integrity & Duplication
*   **Issue**: The system allows creation of duplicate identities (same name/DOB) resulting in multiple users.
*   **Scenario**: `Create multiple valid identities but with identical data`
*   **Root Cause**: Missing uniqueness check in `createIdentity`.
*   **Recommendation**: Enforce uniqueness constraint on Name + DOB combination.

#### 4. Logic Error in Bulk Deletion
*   **Issue**: `removeAllIdentities()` fails to clear all users and causes instability in test teardown.
*   **Root Cause**: ConcurrentModificationException or logic error in iterating while removing (`IdentityManager.java:158`).
*   **Fix Implemented in Tests**: Workaround applied in `TestHooks` to ensure clean state, but source fix required (use `identities.clear()`).

#### 5. Case Sensitivity Inconsistency
*   **Issue**: Removal by name uses `equalsIgnoreCase` while requirements imply strict case sensitivity (or vice versa).
*   **Root Cause**: Inconsistent usage of string comparison methods.

#### 6. Null Pointer Vulnerabilities
*   **Issue**: `updateIdentity` allows setting fields to null.
*   **Recommendation**: Add `@NotNull` validation or checks before updating state.

### Conclusion
This framework demonstrates not just the ability to write passing tests, but to identify, analyze, and document defects effectively, providing value back to the development cycle.
