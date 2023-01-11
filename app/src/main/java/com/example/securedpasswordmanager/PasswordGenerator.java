package com.example.securedpasswordmanager;

import java.util.Random;

public class PasswordGenerator {
    public String generatePassword(){
        int length = new Random().ints(10,15).findFirst().getAsInt();
        String randomString = new Random().ints(48, 122).
                filter(i -> (i < 58 || i > 64) && (i < 91 || i > 96)).limit(length).
                collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
        return randomString;
    }
}
