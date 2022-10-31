package com.rodrigocarmo.fhir.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rodrigocarmo.fhir.domain.model.TelecomModel;

@Repository
public interface TelecomRepository extends JpaRepository<TelecomModel, Long> {

}
