package com.automation.identity.tests.hooks;

import com.automation.identity.core.IdentityManager;


public class ScenarioContext {
    private IdentityManager identityManager;

    public IdentityManager getIdentityManager() {
        return identityManager;
    }

    public void setIdentityManager(IdentityManager identityManager) {
        this.identityManager = identityManager;
    }
}
