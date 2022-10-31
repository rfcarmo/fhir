package com.rodrigocarmo.fhir.domain.exception;

public class PatientNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;
	
	public PatientNotFoundException(String patientIdentifier) {
		super(String.format("There is no Patient register with code %s.", patientIdentifier));
	}
	
}
