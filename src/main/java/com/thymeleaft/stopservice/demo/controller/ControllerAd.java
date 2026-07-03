package com.thymeleaft.stopservice.demo.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.Data;

@ControllerAdvice
public class ControllerAd {
	
//	@ExceptionHandler
//	public String printError(Exception ex) {
//		System.out.println("test error");
//		return ex.getMessage();
//	}
	
	@GetMapping(value = "/error")
	public String showError(Exception ex) {
		System.out.println("test error");
		return ex.getMessage();
	}
	
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<ErrorResponse> handleJsonParseException(HttpMessageNotReadableException ex) {
//        // Log the exception with the actual invalid JSON
//        System.out.println("Invalid JSON request body: " + ex.getMessage());
//
//        ErrorResponse errorResponse = new ErrorResponse("Invalid JSON format in request body");
//        return ResponseEntity.badRequest().body(errorResponse);
//    }

}

@Data
class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

}
