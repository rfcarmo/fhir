package com.rodrigocarmo.fhir.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigocarmo.fhir.domain.model.TelecomModel;
import com.rodrigocarmo.fhir.domain.repository.TelecomRepository;

@Service
public class TelecomService {
	
	@Autowired
	private TelecomRepository telecomRepository;
	
	public void save(List<TelecomModel> telecomModels) {
		for (TelecomModel telecom : telecomModels) {
			telecomRepository.save(telecom);
		}
	}

}
