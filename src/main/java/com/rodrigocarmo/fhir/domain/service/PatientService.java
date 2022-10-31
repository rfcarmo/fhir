package com.rodrigocarmo.fhir.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigocarmo.fhir.domain.exception.PatientNotFoundException;
import com.rodrigocarmo.fhir.domain.model.PatientModel;
import com.rodrigocarmo.fhir.domain.repository.PatientRepository;

@Service
public class PatientService {
	
	@Autowired
	private PatientRepository patientRepository;
	
	public PatientModel save(PatientModel patient) {
		return patientRepository.save(patient);
	}
	
	public PatientModel findOrFail(String patientIdentifier) {
		return patientRepository.findByIdentifier(patientIdentifier).orElseThrow(() -> new PatientNotFoundException(patientIdentifier));
	}
	
	public Optional<PatientModel> find(String patientIdentifier) {
		return patientRepository.findByIdentifier(patientIdentifier);
	}

}
