package com.automation.identity.tests.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import com.automation.identity.core.IdentityManager;
import com.automation.identity.core.Identity;
import io.cucumber.java.en.Then;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import io.cucumber.datatable.DataTable;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import static org.junit.Assert.assertNotNull;

public class IdentityManagerStepDefinitions {
    //private IdentityManager identityManager = TestHooks.identityManager; // Initialize IdentityManager
    private Long userId; // Store the user ID
    private final IdentityManager identityManager = IdentityManager.getInstance();
    private List<Long> createdUserIds = new ArrayList<>(); // List to track created user IDs
    
    public static String removeResult, updateResult; // Store results of remove and update operations
    
    
    @When("I create an identity with {string} as the first name, {string} as the last name, and {string} as the birth date")
    public void createIdentity(String firstName, String lastName, String birthDate) throws Exception {
        userId = identityManager.createIdentity(firstName, lastName, birthDate); // Create identity
        
        if (userId != -1) { // Check if creation was successful
            createdUserIds.add(userId); // Add to list of created IDs
        }
    }
    
    @Then("I should receive a valid UserId, not equal to -1")
    public void verifyValidUserId() {
        Long failureValue = new Long(-1); // Define failure value
        Long lastCreatedUserId = createdUserIds.get(createdUserIds.size() - 1); // Get last created user ID
        assertNotEquals("UserId should not be -1", failureValue, lastCreatedUserId); // Verify UserId is valid
    }

    @Then("I should receive {int} for UserId")
    public void verifyInvalidUserId(Integer int1) {
        Long failureValue = Long.valueOf(int1); // Convert Integer to Long
        assertEquals("UserId should be -1", failureValue, userId); // Verify UserId matches expected failure value
    }

    @When("I create the following identities:")
    public void createTheFollowingIdentities(DataTable dataTable) throws Exception {
        List<Map<String, String>> identities = dataTable.asMaps(String.class, String.class); // Convert DataTable to list of maps
        
        for (Map<String, String> identity : identities) {
            String expectedResult = identity.get("ExpectedResult"); // Get expected result
            userId = identityManager.createIdentity(
                identity.get("FirstName"),
                identity.get("LastName"),
                identity.get("BirthDate")
            );
            
            // Only store valid IDs
            if (expectedResult == null || expectedResult.equals("valid")) {
                if (userId != -1) { // Check if creation was successful
                    createdUserIds.add(userId); // Add to list of created IDs
                }
            }
        }
    }

    @Then("the system should contain {int} identities")
    public void theSystemShouldContainIdentities(int expectedCount) {
        assertEquals("Number of identities should match", 
            expectedCount, 
            identityManager.getAllIdentities().size()); // Verify the count of identities
    }

    @Then("I should be able to retrieve each identity")
    public void shouldBeAbleToRetrieveEachIdentity() {
        for (Long id : createdUserIds) {
            assertNotNull("Identity should exist for ID: " + identityManager.getIdentity(id), 
                identityManager.getIdentity(id)); // Verify each identity exists
        }
    }

    @When("I remove the identity")
    public void removeIdentity() {
        Long lastCreatedUserId = createdUserIds.get(createdUserIds.size() - 1); // Get last created user ID
        removeResult = identityManager.removeIdentity(lastCreatedUserId); // Remove identity
        assertNotNull(removeResult); // Ensure removal result is not null
        
        createdUserIds.remove(lastCreatedUserId); // Remove ID from list
    }

    @When("I remove the following identities:")
    public void removeIdentities(DataTable dataTable) throws Exception {
        List<Map<String, String>> identitiesToRemove = dataTable.asMaps(String.class, String.class); // Convert DataTable to list of maps
        List<Long> idsToRemove = new ArrayList<>(); // List to track IDs to remove
        
        // For each identity we want to remove
        for (Long id : createdUserIds) {
            Identity storedIdentity = identityManager.getIdentity(id); // Get stored identity
            for (Map<String, String> targetIdentity : identitiesToRemove) {
                // Check if identity matches the target for removal
                if (storedIdentity.getFirstName().equals(targetIdentity.get("FirstName")) &&
                    storedIdentity.getLastName().equals(targetIdentity.get("LastName")) &&
                    storedIdentity.getBirthDate().equals(targetIdentity.get("BirthDate"))) {
                    removeResult = identityManager.removeIdentity(id); // Remove identity
                    verifyRemoveIdentity(); // Verify removal
                    idsToRemove.add(id); // Add to removal list
                }
            }
        }
        createdUserIds.removeAll(idsToRemove); // Remove all IDs that were removed
    }

    @Then("Then we should receive a successfull remove message")
    public void verifyRemoveIdentity(){
        assertEquals("Identity successfully removed.", removeResult); // Verify removal message
    }

    @Then("we should receive a successfull remove message: {string}")
    public void verifyRemoveAllIdentity(String message){
        assertEquals("All Identities successfully removed.", removeResult); // Verify removal message for all
    }

    @When("I remove all identities by first name {string}")
    public void removeIdentityByFirstName(String fName) {
        List<Long> idToRemove = new ArrayList<>(); // List to track IDs to remove
        for(Long id : createdUserIds) {
            if(identityManager.getIdentity(id).getFirstName().equals(fName)) // Check if first name matches
                idToRemove.add(id); // Add to removal list
        }

        removeResult = identityManager.removeAllIdentitiesByFirstName(fName); // Remove by first name
        createdUserIds.removeAll(idToRemove); // Remove IDs from list
    }

    @Then("we should receive a successfull remove message by first name")
    public void verifyRemoveIdentityByFirstName(){
        assertEquals(true, removeResult.contains(" are successfully removed.")); // Verify removal message
    }

    @When("I remove the identity with user id {long}")
    public void removeNonexistentIdentity(Long userid){
        removeResult = identityManager.removeIdentity(userid); // Attempt to remove identity
        createdUserIds.remove(userid); // Remove ID from list
    }
    
    @Then("I should receive an error or {string} message")
    public void verifyInvalidRemoveIdentity(String ErrorMessage){
        assertEquals(ErrorMessage, removeResult); // Verify error message
    }

    @When("I remove all identities")
    public void removeAllIdentities(){
        removeResult = identityManager.removeAllIdentities(); // Remove all identities
        createdUserIds.removeAll(createdUserIds); // Clear the list of created IDs
    }

    @When("I update the identity with new values:")
    public void updateIdentity(DataTable dataTable) throws Exception{
        List<Map<String, String>> identities = dataTable.asMaps(String.class, String.class); // Convert DataTable to list of maps

        for(Map<String,String> identity: identities) {   
            updateResult = identityManager.updateIdentity(userId, 
            identity.get("FirstName"), 
            identity.get("LastName"), 
            identity.get("BirthDate")); // Update identity with new values
        }
    }

    @Then("the identity should be updated successfully")
    public void verifyUpdateIdentity(){
        assertEquals("Identity successfully updated.", updateResult); // Verify update message
    }

    @Given("I store a different userId than the one created")
    public void storeDifferentUserId() {
        userId = userId + 1; // Store a userId that doesn't exist
    }

    @Then("the update should fail")
    public void verifyInvalidUpdateIdentity(){
        assertNotEquals("Identity successfully updated.", updateResult); // Verify update failure
    }
}
