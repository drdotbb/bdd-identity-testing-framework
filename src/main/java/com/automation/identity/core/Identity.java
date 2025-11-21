package com.automation.identity.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Identity Object
 */
public class Identity
{
    private String firstName;
    private String lastName;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private Date birthDate;
    private Long userId;

    /**
     * @param firstName First name of Identity
     * @param lastName Last name of Identity
     * @param birthDate Birth Date of Identity in the format of MM/DD/YYYY
     * @param userId User id of Identity
     * @throws Exception ParseException on birth date
     */
    public Identity(String firstName, String lastName, String birthDate, Long userId) throws Exception
    {
        this.firstName = firstName;
        this.lastName = lastName;
        simpleDateFormat.setLenient(false); // Enforce strict date parsing
        this.birthDate = simpleDateFormat.parse(birthDate);
        
        // Check for future date
        if (this.birthDate.after(new Date())) {
            throw new Exception("Birth date cannot be in the future");
        }
        
        this.userId = userId;
    }

    /**
     * @return String First name of Identity
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * @return String Last name of Identity
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * @return Date Birth Date of Identity
     */
    public String getBirthDate()
    {
        // FIX: Use SimpleDateFormat to ensure consistent MM/dd/yyyy format (0-padded)
        return simpleDateFormat.format(birthDate);
    }

    /**
     * @return Long User id of Identity
     */
    public Long getUserId()
    {
        return userId;
    }
}
