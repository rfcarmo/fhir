package com.rodrigocarmo.fhir.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rodrigocarmo.fhir.domain.model.PatientModel;

@Repository
public interface PatientRepository extends JpaRepository<PatientModel, Long> {

	@Query(value = "SELECT * FROM PATIENT_MODEL WHERE IDENTIFIER_VALUE = :patientIdentifier ORDER BY VERSION DESC FETCH FIRST ROW ONLY", nativeQuery = true)
	Optional<PatientModel> findByIdentifier(String patientIdentifier);
	
}
