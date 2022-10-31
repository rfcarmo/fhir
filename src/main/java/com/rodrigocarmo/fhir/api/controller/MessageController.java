package com.rodrigocarmo.fhir.api.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.MessageHeader;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigocarmo.fhir.api.controller.openapi.MessageControllerOpenApi;
import com.rodrigocarmo.fhir.domain.model.EncounterModel;
import com.rodrigocarmo.fhir.domain.model.MessageModel;
import com.rodrigocarmo.fhir.domain.model.PatientModel;
import com.rodrigocarmo.fhir.domain.model.TelecomModel;
import com.rodrigocarmo.fhir.domain.service.EncounterService;
import com.rodrigocarmo.fhir.domain.service.MessageService;
import com.rodrigocarmo.fhir.domain.service.PatientService;
import com.rodrigocarmo.fhir.domain.service.TelecomService;
import com.rodrigocarmo.fhir.utils.BundleCreationService;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ValidationResult;

@RestController
@RequestMapping("/fhir/messages")
public class MessageController implements MessageControllerOpenApi {
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private EncounterService encounterService;
	
	@Autowired
	private PatientService patientService;
	
	@Autowired
	private TelecomService telecomService;

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<String> addMessage(@RequestBody String message) {
		FhirContext ctx = FhirContext.forR4();
		FhirValidator validator = ctx.newValidator();
		IValidatorModule module = new FhirInstanceValidator(ctx);
		validator.registerValidatorModule(module);
		
		MessageHeader messageHeader = new MessageHeader();
		Patient patient = new Patient();
		Encounter encounter = new Encounter();
				
		Bundle bundle = FhirContext.forR4().newJsonParser().parseResource(Bundle.class, message);
		
		List<BundleEntryComponent> bundleComponent = bundle.getEntry();
		
		for (int x = 0; x < bundleComponent.size(); x++) {
			if (bundleComponent.get(x).getResource().fhirType().equals("MessageHeader")) {
				messageHeader = (MessageHeader) bundleComponent.get(x).getResource();
			} else if (bundleComponent.get(x).getResource().fhirType().equals("Encounter")) {
				encounter = (Encounter) bundleComponent.get(x).getResource();
			} else if (bundleComponent.get(x).getResource().fhirType().equals("Patient")) {
				patient = (Patient) bundleComponent.get(x).getResource();
			} 
		}
		
		ValidationResult resultMessageHeader = validator.validateWithResult(messageHeader);
		OperationOutcome ooMessageHeader = (OperationOutcome) resultMessageHeader.toOperationOutcome();
		
		ValidationResult resultEncounter = validator.validateWithResult(encounter);
		OperationOutcome ooEncounter = (OperationOutcome) resultEncounter.toOperationOutcome();
		
		ValidationResult resultPatient = validator.validateWithResult(patient);
		OperationOutcome ooPatient = (OperationOutcome) resultPatient.toOperationOutcome();
		
		Bundle bundleResp = BundleCreationService.createBundle(ooMessageHeader, ooEncounter, ooPatient, resultMessageHeader.isSuccessful(), resultEncounter.isSuccessful(), resultPatient.isSuccessful());
		
		IParser parser = ctx.newJsonParser();
		parser.setPrettyPrint(true);
		
		String json = parser.encodeResourceToString(bundleResp);
		
		if ((resultMessageHeader.isSuccessful()) && (resultPatient.isSuccessful()) && (resultEncounter.isSuccessful())) {
			
			// #Message
			MessageModel messageModel = new MessageModel();
			messageModel.setBundleId(bundle.getId());
			messageModel.setEncounterId(encounter.getId());
			messageModel.setPatientId(patient.getId());
			messageModel.setMessageBody(message);
			messageService.save(messageModel);
			
			// #Encounter
			EncounterModel encounterModel = new EncounterModel();
			encounterModel.setResourceType(encounter.getResourceType().toString());
			encounterModel.setIdentifierSystem(encounter.getIdentifier().get(0).getSystem().toString());
			
			String encounterIdentifierValue = encounter.getIdentifier().get(0).getValue().toString();
			
			encounterModel.setIdentifierValue(encounterIdentifierValue);
			encounterModel.setStatus(encounter.getStatus().toString().toLowerCase());
			encounterModel.setClassSystem(encounter.getClass_().getSystem());
			encounterModel.setClassCode(encounter.getClass_().getCode());
			encounterModel.setClassDisplay(encounter.getClass_().getDisplay());
			encounterModel.setTypeCodingSystem(encounter.getType().get(0).getCoding().get(0).getSystem());
			encounterModel.setTypeCodingCode(encounter.getType().get(0).getCoding().get(0).getCode());
			encounterModel.setTypeCodingDisplay(encounter.getType().get(0).getCoding().get(0).getDisplay());
			encounterModel.setSubjectReference(encounter.getSubject().getReference());
			
			Optional<EncounterModel> encounterCurrent = encounterService.find(encounterIdentifierValue);
			
			if (encounterCurrent.isPresent()) {
				encounterModel.setVersion(encounterCurrent.get().getVersion() + 1);
			} else {
				encounterModel.setVersion(1);
			}
						
			encounterService.save(encounterModel);
			
			// #Patient
			PatientModel patientModel = new PatientModel();
			patientModel.setResourceType(patient.getResourceType().toString());
			patientModel.setIdentifierSystem(patient.getIdentifier().get(0).getSystem());
			
			String patientIdentifierValue = patient.getIdentifier().get(0).getValue();
			
			patientModel.setIdentifierValue(patientIdentifierValue);
			patientModel.setName(patient.getName().get(0).getText());
			patientModel.setGender(patient.getGender().toString().toLowerCase());
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
			String birthDate = dateFormat.format(patient.getBirthDate()); 
			
			patientModel.setBirthDate(birthDate);
			
			Optional<PatientModel> patientCurrent = patientService.find(patientIdentifierValue);
			
			if (patientCurrent.isPresent()) {
				patientModel.setVersion(patientCurrent.get().getVersion() + 1);
			} else {
				patientModel.setVersion(1);
			}
			
			PatientModel patientSaved = patientService.save(patientModel);
			
			// #Patient Telecom
			List<TelecomModel> telecoms = new ArrayList<>();
			
			for (int i = 0; i < patient.getTelecom().size(); i++) {
				TelecomModel telecom = new TelecomModel();
				telecom.setTSystem(patient.getTelecom().get(i).getSystem().toString().toLowerCase());
				telecom.setTValue(patient.getTelecom().get(i).getValue().toString());
				telecom.setTUse(patient.getTelecom().get(i).getUse().toString().toLowerCase());
				telecom.setPatientModel(patientSaved);
				
				telecoms.add(telecom);
			}
			
			telecomService.save(telecoms);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(json);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(json);
		}
	}
	
}
