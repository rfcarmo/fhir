package com.rodrigocarmo.fhir.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rodrigocarmo.fhir.domain.model.EncounterModel;

@Repository
public interface EncounterRepository extends JpaRepository<EncounterModel, Long> {
	
	@Query(value = "SELECT * FROM ENCOUNTER_MODEL WHERE IDENTIFIER_VALUE = :encounterIdentifier ORDER BY VERSION DESC FETCH FIRST ROW ONLY" , nativeQuery = true)
	Optional<EncounterModel> findByIdentifier(String encounterIdentifier);

}
