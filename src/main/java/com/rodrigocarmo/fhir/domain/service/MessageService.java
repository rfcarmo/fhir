package com.rodrigocarmo.fhir.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigocarmo.fhir.domain.model.MessageModel;
import com.rodrigocarmo.fhir.domain.repository.MessageRepository;

@Service
public class MessageService {
	
	@Autowired
	private MessageRepository messageRepository;
	
	public MessageModel save(MessageModel message) {
		return messageRepository.save(message);
	}

}
