package com.thymeleaft.stopservice.demo.model.enumeration;

public enum EAsin {
	
	AsinNotFound404("asin non trovato"),
	AsinCharMatch("dp/");
	
	private String value;
	
	EAsin(String value){
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

}
