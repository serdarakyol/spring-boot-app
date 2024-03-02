package com.example.demo.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@Builder @ToString
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {

	@JsonProperty("data")
	private T data;
	
	@JsonProperty("status_code")
	private Integer statusCode;
	
	@JsonProperty("status_message")
	private String statusMessage;
	
	@JsonProperty("timestamp")
	private String timestamp;
	
}
