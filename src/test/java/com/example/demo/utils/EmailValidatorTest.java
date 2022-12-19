package com.example.demo.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EmailValidatorTest {
    @Test
    void testIsMailValid() {
        EmailValidator emailValidator = new EmailValidator();
        String[] validMails = {
            "serdarakyol55@hotmail.com",
            "serdar@akyol.com",
            "serdar_ak-yol_55@gmail.com",
            "serdarakyol55@gmail.co",
            "1233@hotma.com",
            "serdar.akyol@gmail.com",
        };

        String[] inValidMails = {
            "!serdarakyol55@hotmail.com",
            "@akyol.com",
            "serdar_ak-yol_55@",
            "serdarakyol55@gmail.c",
            "@serdar@hotma.com",
            "serdar@.com",
        };

        for (int i=0; i<validMails.length; i++){
            assertEquals(true, emailValidator.isMailValid(validMails[i]));
            assertEquals(false, emailValidator.isMailValid(inValidMails[i]));
        }
    }
}
