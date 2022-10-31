package com.rodrigocarmo.fhir.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigocarmo.fhir.domain.exception.EncounterNotFoundException;
import com.rodrigocarmo.fhir.domain.model.EncounterModel;
import com.rodrigocarmo.fhir.domain.repository.EncounterRepository;

@Service
public class EncounterService {
	
	@Autowired
	private EncounterRepository encounterRepository;
	
	public void save(EncounterModel encounter) {
		encounterRepository.save(encounter);
	}
	
	public EncounterModel findOrFail(String encounterIdentifier) {
		return encounterRepository.findByIdentifier(encounterIdentifier).orElseThrow(() -> new EncounterNotFoundException(encounterIdentifier));
	}
	
	public Optional<EncounterModel> find(String encounterIdentifier) {
		return encounterRepository.findByIdentifier(encounterIdentifier);
	}

}
