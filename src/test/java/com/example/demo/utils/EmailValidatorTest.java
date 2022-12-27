package com.example.demo.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EmailValidatorTest {
    @Test
    void testIsMailValid() {
        Utils utils = new Utils();
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
            // test valid e-mails
            assertEquals(true, utils.isMailValid(validMails[i]));
            // test invalid e-mails
            assertEquals(false, utils.isMailValid(inValidMails[i]));
        }
    }
}
