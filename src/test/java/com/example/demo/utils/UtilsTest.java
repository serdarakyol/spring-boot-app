package com.example.demo.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UtilsTest {
    @Test
    void testIsMailValid() {
        String[] validMails = {
            "serdarakyol55@hotmail.com",
            "serdar@akyol.com",
            "serdar_ak-yol_55@gmail.com",
            "serdarakyol55@gmail.co",
            "1233@hotma.com",
            "serdar.akyol@gmail.com",
        };

        String[] inValidMails = {
            "Serdar.akyol@gmail.com",
            "!serdarakyol55@hotmail.com",
            "@akyol.com",
            "serdar_ak-yol_55@",
            "serdarakyol55@gmail.c",
            "@serdar@hotma.com",
            "serdar@.com",
            "string@asd,com",
            "STRING@ad.co"
        };

        for (int i=0; i<validMails.length; i++){
            // test valid e-mails
            assertEquals(true, Utils.isMailValid(validMails[i]));
            // test invalid e-mails
            assertEquals(false, Utils.isMailValid(inValidMails[i]));
        }
    }
}
