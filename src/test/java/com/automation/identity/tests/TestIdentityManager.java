package com.automation.identity.tests;

import com.automation.identity.core.IdentityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

/**
 * Tests for IdentityManager
 */
public class TestIdentityManager
{
    private IdentityManager identityManager;

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        // Executed before any tests are run
    }

    @Before
    public void setUpTest() throws Exception
    {
        // Executed before each test
        identityManager = IdentityManager.getInstance();
    }

    @Test
    public void createValidIdentity() throws Exception
    {
        Long failureValue = new Long(-1);
        Long actualValue = identityManager.createIdentity("Buster", "Dalinski", "12/11/2014");
        assertNotEquals("Valid identity ID expected", failureValue, actualValue);
    }

    @After
    public void cleanUp()
    {
        // Executed after each test
    }
}
