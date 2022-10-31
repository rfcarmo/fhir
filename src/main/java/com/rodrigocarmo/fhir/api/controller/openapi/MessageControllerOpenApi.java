package com.rodrigocarmo.fhir.api.controller.openapi;

import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Messages", description = "Manages messages.")
public interface MessageControllerOpenApi {

	@Operation(summary = "Add messages.", description = "Adds the message and both patient and encounter resources contained in it.", responses = {
			@ApiResponse(responseCode = "200")
	})
	public ResponseEntity<String> addMessage(@RequestBody(description = "FHIR message .json", required = true) String message);

}
