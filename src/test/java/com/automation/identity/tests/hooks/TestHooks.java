package com.automation.identity.tests.hooks;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import com.automation.identity.core.IdentityManager;
import com.automation.identity.tests.stepdefinitions.IdentityManagerStepDefinitions;
import io.cucumber.java.Scenario;

public class TestHooks {
    
    private final IdentityManager identityManager = IdentityManager.getInstance();
    @Before
    public void setUp(Scenario scenario) {
      
        if (identityManager != null) {
            identityManager.removeAllIdentities();
        }
        
        // Reset the result strings
        IdentityManagerStepDefinitions.removeResult = null;
        IdentityManagerStepDefinitions.updateResult = null;
        System.out.println("Test setup completed.");
    }

    @After
    public void tearDown() {
        
        // Clean up test data
        identityManager.removeAllIdentities();
        
        System.out.println("Test data cleaned up.");
    }
}
