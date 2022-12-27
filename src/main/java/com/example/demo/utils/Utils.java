package com.example.demo.utils;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class Utils {
    public boolean isMailValid(String email){
        String regex = "^[\\w-.]+@[a-z]+.[a-z]{2,}$";
        return Pattern.compile(regex).matcher(email).matches();
    }
}
