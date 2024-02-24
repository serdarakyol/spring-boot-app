package com.example.demo.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ResponseEnum {

    SUCCESS(HttpStatus.OK, 200, "Success");

    private final HttpStatus status;

    private final Integer statusCode;

    private final String statusMessage;

    ResponseEnum(HttpStatus status, Integer statusCode, String statusMessage) {
        this.status = status;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
    
}
