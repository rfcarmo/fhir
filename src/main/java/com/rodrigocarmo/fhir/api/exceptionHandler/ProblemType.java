package com.rodrigocarmo.fhir.api.exceptionHandler;

import lombok.Getter;

@Getter
public enum ProblemType {
	
	RESOURCE_NOT_FOUND("Resource not found."),
	SYSTEM_ERROR("System error.");
	
	private String title;
		
	private ProblemType(String title) {
		this.title = title;
	}

}
