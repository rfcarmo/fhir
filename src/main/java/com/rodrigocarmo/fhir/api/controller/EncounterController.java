package com.rodrigocarmo.fhir.api.controller;

import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigocarmo.fhir.api.controller.openapi.EncounterControllerOpenApi;
import com.rodrigocarmo.fhir.domain.model.EncounterModel;
import com.rodrigocarmo.fhir.domain.service.EncounterService;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;


@RestController
@RequestMapping("/fhir/encounters")
public class EncounterController implements EncounterControllerOpenApi {
	
	@Autowired
	private EncounterService encounterService;
	
	@GetMapping(path = "/{encounterIdentifier}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String findByIdentifier(@PathVariable String encounterIdentifier) {
		EncounterModel encounterModel = encounterService.findOrFail(encounterIdentifier);
		
		Encounter encounter = new Encounter();
		encounter.addIdentifier().setSystem(encounterModel.getIdentifierSystem()).setValue(encounterModel.getIdentifierValue());
		encounter.setStatus(Encounter.EncounterStatus.fromCode(encounterModel.getStatus().toLowerCase()));
		
		Coding classCoding = new Coding().setSystem(encounterModel.getClassSystem()).setCode(encounterModel.getClassCode()).setDisplay(encounterModel.getClassDisplay());
		encounter.setClass_(classCoding);

		encounter.addType().addCoding().setSystem(encounterModel.getTypeCodingSystem()).setCode(encounterModel.getTypeCodingCode()).setDisplay(encounterModel.getTypeCodingDisplay());
		encounter.setSubject(new Reference(encounterModel.getSubjectReference()));
		
		FhirContext ctx = FhirContext.forR4();
		IParser parser = ctx.newJsonParser();
		parser.setPrettyPrint(true);
		
		String json = parser.encodeResourceToString(encounter);
		
		return json;
	}

}
