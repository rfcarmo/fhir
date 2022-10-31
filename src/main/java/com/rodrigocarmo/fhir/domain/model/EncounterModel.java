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
public class EncounterModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	
	@Column(nullable = false)
	private String resourceType;
	
	@Column(nullable = false)
	private String identifierSystem;
	
	@Column(nullable = false)
	private String identifierValue;
	
	@Column(nullable = false)
	private String status;
	
	@Column(nullable = false)
	private String classSystem;
	
	@Column(nullable = false)
	private String classCode;
	
	@Column(nullable = false)
	private String classDisplay;
	
	@Column(nullable = false)
	private String typeCodingSystem;
	
	@Column(nullable = false)
	private String typeCodingCode;
	
	@Column(nullable = false)
	private String typeCodingDisplay;
	
	@Column(nullable = false)
	private String subjectReference;
	
	@Column(nullable = false)
	private Integer version;
	
}
