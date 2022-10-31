package com.rodrigocarmo.fhir.api.controller;

import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigocarmo.fhir.api.controller.openapi.PatientControllerOpenApi;
import com.rodrigocarmo.fhir.domain.model.PatientModel;
import com.rodrigocarmo.fhir.domain.model.TelecomModel;
import com.rodrigocarmo.fhir.domain.service.PatientService;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

@RestController
@RequestMapping("/fhir/patients")
public class PatientController implements PatientControllerOpenApi {

	@Autowired
	private PatientService patientService;
	
	@GetMapping(path = "/{patientIdentifier}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String findByIdentifier(@PathVariable String patientIdentifier) {
		PatientModel patientModel = patientService.findOrFail(patientIdentifier);
		
		Patient patient = new Patient();
		patient.addIdentifier().setSystem(patientModel.getIdentifierSystem()).setValue(patientModel.getIdentifierValue());
		patient.addName().setText(patientModel.getName());
		
		for(TelecomModel telecom : patientModel.getTelecoms()) {
			patient.addTelecom()
				.setUse(ContactPointUse.fromCode(telecom.getTUse().toLowerCase()))
				.setSystem(ContactPointSystem.fromCode(telecom.getTSystem().toLowerCase()))
				.setValue(telecom.getTValue());
		}
		
		patient.setGender(Enumerations.AdministrativeGender.fromCode(patientModel.getGender().toLowerCase()));
		patient.setBirthDateElement(new DateType(patientModel.getBirthDate()));

		FhirContext ctx = FhirContext.forR4();
		IParser parser = ctx.newJsonParser();
		parser.setPrettyPrint(true);
		
		String json = parser.encodeResourceToString(patient);
		
		return json;
	}
	
}
