package com.rodrigocarmo.fhir.api.core.springdoc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rodrigocarmo.fhir.api.exceptionHandler.Problem;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

@Configuration
public class SpringDocConfig {
	
	private static final String badRequestResponse = "BadRequestResponse";
    private static final String notFoundResponse = "NotFoundResponse";
    private static final String notAcceptableResponse = "NotAcceptableResponse";
    private static final String internalServerErrorResponse = "InternalServerErrorResponse";
	
	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("HL7 FHIR R4 Rest API")
						.version("v1.0")
						.description("UpHill Chalenge API")
				).components(new Components()
						.schemas(gerarSchemas())
						.responses(gerarResponses())
				);
	}
	
	@Bean
	public OpenApiCustomiser openApiCustomiser() {
		return openAPI -> {
			openAPI.getPaths()
				.values()
				.forEach(pathItem -> pathItem.readOperationsMap()
						.forEach((httpMethod, operation) -> {
							ApiResponses responses = operation.getResponses();
							
							switch (httpMethod) {
								case GET:
									responses.addApiResponse("406", new ApiResponse().$ref(notAcceptableResponse));
									responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
									break;
								case POST:
									responses.addApiResponse("400", new ApiResponse().$ref(badRequestResponse));
									responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
									break;
								default:
									responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
									break;
							}
						}));
		};
	}
	
	private Map<String, Schema> gerarSchemas() {
        final Map<String, Schema> schemaMap = new HashMap<>();

        Map<String, Schema> problemSchema = ModelConverters.getInstance().read(Problem.class);

        schemaMap.putAll(problemSchema);

        return schemaMap;
    }
	
	private Map<String, ApiResponse> gerarResponses() {
        final Map<String, ApiResponse> apiResponseMap = new HashMap<>();

        Content content = new Content()
                .addMediaType(APPLICATION_JSON_VALUE,
                        new MediaType().schema(new Schema<Problem>().$ref("Problem")));

        apiResponseMap.put(badRequestResponse, new ApiResponse()
                .description("Bad request")
                .content(content));

        apiResponseMap.put(notFoundResponse, new ApiResponse()
                .description("Resource not found")
                .content(content));

        apiResponseMap.put(notAcceptableResponse, new ApiResponse()
                .description("Resource has no representation that can be accepted by the consumer")
                .content(content));

        apiResponseMap.put(internalServerErrorResponse, new ApiResponse()
                .description("Internal server error")
                .content(content));

        return apiResponseMap;
    }

}
