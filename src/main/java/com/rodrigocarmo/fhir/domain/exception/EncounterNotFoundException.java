package com.rodrigocarmo.fhir.domain.exception;

public class EncounterNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;
	
	public EncounterNotFoundException(String encounterIdentifier) {
		super(String.format("There is no Encounter register with code %s.", encounterIdentifier));
	}
	
}
