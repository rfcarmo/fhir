package com.rodrigocarmo.fhir.api.controller.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Encounters", description = "Manages encounters.")
public interface EncounterControllerOpenApi {
	
	@Operation(summary = "Searches for a encounter.", description = "Searches for a encounter by your identifier.", responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "404", description = "Resource not found.", content = @Content(schema = @Schema(ref = "Problem")))
	})
	public String findByIdentifier(@Parameter(description = "Encounter identifier", example = "1", required = true) String encounterIdentifier);

}
