package com.rodrigocarmo.fhir.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TelecomModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	
	@Column(nullable = false)
	private String tSystem;
	
	@Column(nullable = false)
	private String tValue;
	
	@Column(nullable = false)
	private String tUse;
		
	@ManyToOne
	@JoinColumn(name="patient_id", nullable = false)
	@JsonIgnore
	private PatientModel patientModel;
	
}
