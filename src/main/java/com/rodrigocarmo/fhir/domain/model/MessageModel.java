package com.rodrigocarmo.fhir.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MessageModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	
	@Column(nullable = false)
	private String bundleId;
	
	@Column(nullable = false)
	private String encounterId;
	
	@Column(nullable = false)
	private String patientId;
	
	@Column(nullable = false)
	private String messageBody;

}
