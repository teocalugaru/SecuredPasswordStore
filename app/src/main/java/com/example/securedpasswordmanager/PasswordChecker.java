package com.example.securedpasswordmanager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PasswordChecker {
    public PasswordChecker(){

    }
    public static int checkComplexity(String password){
        // Checking lower alphabet in string
        int n = password.length();
        boolean hasLower = false, hasUpper = false,
                hasDigit = false, specialChar = false;
        Set<Character> specialCharachters = new HashSet<Character>(
                Arrays.asList('!', '@', '#', '$', '%', '^', '&',
                        '*', '(', ')', '-', '+'));
        for (char i : password.toCharArray())
        {
            if (Character.isLowerCase(i))
                hasLower = true;
            if (Character.isUpperCase(i))
                hasUpper = true;
            if (Character.isDigit(i))
                hasDigit = true;
            if (specialCharachters.contains(i))
                specialChar = true;
        }
        if (hasDigit && hasLower && hasUpper && specialChar
                && (n >= 8)) {
            return 2;
        }
        else if ((hasLower || hasUpper || specialChar)
                && (n >= 6))
            return 1;
            else
                return 0;
    }
}