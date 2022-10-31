package com.rodrigocarmo.fhir.api.exceptionHandler;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "Problem")
public class Problem {

	@Schema(example = "400")
	private Integer status;
	
	@Schema(example = "Resource not found.")
	private String title;
	
	@Schema(example = "There is no Patient register with code 999.")
	private String detail;
	
	@Schema(example = "There is no Patient register with code 999.")
	private String userMessage;
	
	@Schema(example = "2022-10-31T14:47:38.8224744")
	private LocalDateTime timestamp;
	
}
