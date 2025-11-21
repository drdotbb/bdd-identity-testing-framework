package com.automation.identity.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

/**
 * Manager for Identities
 */
public class IdentityManager
{
    private static IdentityManager instance = null;
    private HashMap<Long, Identity> identities = new HashMap();
    private Random rand = new Random();

    private IdentityManager()
    {
        // Disable direct instantiation
    }

    /**
     * Instantiates an instance of IdentityManager
     * @return An instance of IdentityManager
     */
    public static IdentityManager getInstance()
    {
        // Make sure only one instance is created
        if(instance == null)
        {
            instance = new IdentityManager();
        }

        return instance;
    }

    /**
     * Creates a new Identity
     * @param firstName First name of Identity, cannot include a space, " "
     * @param lastName Last name of Identity, cannot include a space, " "
     * @param birthDate Birth Date of Identity in the format of MM/DD/YYYY
     * @return Long User id of Identity or -1 if either firstName or lastName contain a space, " ", or if date is invalid, or if identity already exists.
     * @throws Exception ParseException on birth date
     */
    public Long createIdentity(String firstName, String lastName, String birthDate) throws Exception
    {
        Long returnNumber = new Long(-1);

        // Check if First and Last names don't contain a space
        if(!firstName.contains(" ") && !lastName.contains(" "))
        {
            try {
                 // Check for duplicates
                 // We need to ensure we compare using the formatted date string that the Identity object returns
                 // Since we just updated Identity.getBirthDate() to use SimpleDateFormat, it should match if input is also MM/dd/yyyy
                for (Identity id : identities.values()) {
                    if (id.getFirstName().equals(firstName) && 
                        id.getLastName().equals(lastName) && 
                        id.getBirthDate().equals(birthDate)) { 
                        return -1L;
                    }
                }

                // Create new Identity
                Long userId = Math.abs(rand.nextLong());
                Identity newIdentity = new Identity(firstName, lastName, birthDate, userId);
                identities.put(userId, newIdentity);
                returnNumber = newIdentity.getUserId();
            } catch (Exception e) {
                return -1L;
            }
        }

        return returnNumber;
    }

    /**
     * Returns desired Identity
     * @param userId User id of Identity returned
     * @return Identity with specified userId
     */
    public Identity getIdentity(Long userId)
    {
        // Retrieve Identity
        Identity retrievedIdentity = identities.get(userId);
        return retrievedIdentity;
    }

    /**
     * Returns all Identities
     * @return Collection A collection of all the Identities
     */
    public Collection<Identity> getAllIdentities()
    {
        // Retrieve all Identities
        return identities.values();
    }

    /**
     * Removes desired Identity
     * @param userId User id of Identiy to be removed
     * @return String Message indicating successful or unsuccessful removal
     */
    public String removeIdentity(Long userId)
    {
        String returnMessage = "Could not remove Identity.";

        // Retrieve Identity
        if(identities.containsKey(userId))
        {
            identities.remove(userId);
            returnMessage = "Identity successfully removed.";
        }
        else
        {
            returnMessage = returnMessage + " The identity does not exist.";
        }

        return returnMessage;
    }

    /**
     * Removes all Identities of a given First Name, case sensitive
     * @param firstName First name of Identities to be removed, case sensitive
     * @return String Message "All Identities with First Name of firstName are successfully removed"
     */
    public String removeAllIdentitiesByFirstName(String firstName)
    {
        String returnMessage = "All Identities with First Name of " + firstName + " are successfully removed.";
        Identity identityToCheck;
        String firstNameToCheck;
        Long currentUserId;
        Vector userIdsToRemove = new Vector();

        // Iterate over Identities to find all matching
        Iterator<Map.Entry<Long, Identity>> identityIterator = identities.entrySet().iterator();
        while(identityIterator.hasNext())
        {
            identityToCheck = identityIterator.next().getValue();
            currentUserId = identityToCheck.getUserId();
            firstNameToCheck = identityToCheck.getFirstName();

            if (firstNameToCheck.equals(firstName))
            {
                userIdsToRemove.add(currentUserId);
            }
        }

        // Now remove them
        for(int i=0; i<userIdsToRemove.size(); i++)
        {
            currentUserId = (Long)userIdsToRemove.get(i);
            identities.remove(currentUserId);
        }

        return returnMessage;
    }

    /**
     * Removes desired Identity
     * @return String Message "All Identities successfully removed"
     */
    public String removeAllIdentities()
    {
        identities.clear();
        String returnMessage = "All Identities successfully removed.";
        return returnMessage;
    }


    /**
     * Updates desired Identity
     * @param userId User id of Identity to be updated
     *
     * @param newFirstName New first name for Identity
     * @param newLastName New last name for Identity
     * @param newBirthDate New birthdate for Identity in the format of MM/DD/YYYY
     * @return String Message indicating successful or unsuccessful update
     * @throws Exception ParseException on birth date
     */
    public String updateIdentity(Long userId, String newFirstName, String newLastName, String newBirthDate) throws Exception
    {
        String returnMessage = "Could not update Identity.";

        // FIX: Add null checks (handle both Java null and string "null" from Cucumber)
        if (newFirstName == null || newLastName == null || newBirthDate == null ||
            newFirstName.equals("null") || newLastName.equals("null") || newBirthDate.equals("null")) {
             return returnMessage;
        }

        // Replace Identity
        if(identities.containsKey(userId))
        {
            try {
                Identity identity = new Identity(newFirstName, newLastName, newBirthDate, userId);
                identities.replace(userId, identity);
                returnMessage = "Identity successfully updated.";
            } catch (Exception e) {
                return returnMessage;
            }
        }
        else
        {
            returnMessage = returnMessage + " The identity does not exist.";
        }

        return returnMessage;
    }

}
