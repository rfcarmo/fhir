package com.rodrigocarmo.fhir.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PatientModel {

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
	private String name;
	
	@OneToMany(mappedBy = "patientModel")
	private List<TelecomModel> telecoms = new ArrayList<>();
	
	@Column(nullable = false)
	private String gender;
	
	@Column(nullable = false)
	private String birthDate;
	
	@Column(nullable = false)
	private Integer version;
	
}
