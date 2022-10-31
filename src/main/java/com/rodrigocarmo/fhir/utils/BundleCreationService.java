package com.rodrigocarmo.fhir.utils;

import java.util.Date;
import java.util.UUID;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.MessageHeader;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.MessageHeader.MessageHeaderResponseComponent;
import org.hl7.fhir.r4.model.MessageHeader.MessageSourceComponent;
import org.hl7.fhir.r4.model.MessageHeader.ResponseType;

public class BundleCreationService {
	
	public static Bundle createBundle(OperationOutcome ooMessageHeader, OperationOutcome ooEncounter, OperationOutcome ooPatient, Boolean resultMessageHeader, Boolean resultEncounter, Boolean resultPatient) {
		String bundleId = UUID.randomUUID().toString();
		String messageHeaderId = UUID.randomUUID().toString();
		String ooMessageHeaderId = UUID.randomUUID().toString();
		String ooEncounterId = UUID.randomUUID().toString();
		String ooPatientId = UUID.randomUUID().toString();
		String responseId = UUID.randomUUID().toString();
		
		Bundle bundleResp = new Bundle();
		bundleResp.getMeta().setLastUpdated(new Date());
		bundleResp.setType(BundleType.MESSAGE);
		bundleResp.setId(bundleId);
		
		MessageHeader msh = new MessageHeader();
		msh.setId(messageHeaderId);
		
		Coding codingMessageHeader = new Coding();
		codingMessageHeader.setSystem("http://teste.org/fhir/message-events");
		codingMessageHeader.setCode("admin-notify");
		
		msh.setEvent(codingMessageHeader);
		
		MessageSourceComponent source = new MessageSourceComponent();
		source.setName("api");
		source.setEndpoint("rodrigo/api");
		
		msh.setSource(source);
		
		MessageHeaderResponseComponent response = new MessageHeaderResponseComponent();
		response.setIdentifier(responseId);
		
		if (resultMessageHeader && resultPatient && resultEncounter) {
			response.setCode(ResponseType.OK);
		} else {
			response.setCode(ResponseType.FATALERROR);
			
			if (!resultMessageHeader) {
				response.setDetails(new Reference("OperationOutcome/" + ooMessageHeaderId));
			} else if (!resultEncounter) {
				response.setDetails(new Reference("OperationOutcome/" + ooEncounterId));
			} else {
				response.setDetails(new Reference("OperationOutcome/" + ooPatientId));
			}
		}
		
		msh.setResponse(response);
		
		Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
		entry.setFullUrl("http://teste.pt/fhir/MessageHeader/" + msh.getIdElement().getIdPart());
		entry.setResource(msh);
		bundleResp.addEntry(entry);
		
		Bundle.BundleEntryComponent operationOutMessageHeader = new Bundle.BundleEntryComponent();
		operationOutMessageHeader.setFullUrl("http://teste.pt/fhir/OperationOutcome/" + ooMessageHeaderId);
		operationOutMessageHeader.setResource(ooMessageHeader);
		bundleResp.addEntry(operationOutMessageHeader);
				
		Bundle.BundleEntryComponent operationOutEncounter = new Bundle.BundleEntryComponent();
		operationOutEncounter.setFullUrl("http://teste.pt/fhir/OperationOutcome/" + ooEncounterId);
		operationOutEncounter.setResource(ooEncounter);
		bundleResp.addEntry(operationOutEncounter);
		
		Bundle.BundleEntryComponent operationOutPatient = new Bundle.BundleEntryComponent();
		operationOutPatient.setFullUrl("http://teste.pt/fhir/OperationOutcome/" + ooPatientId);
		operationOutPatient.setResource(ooPatient);
		bundleResp.addEntry(operationOutPatient);
		
		return bundleResp;
	}

}
