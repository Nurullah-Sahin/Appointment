package com.solidsoft.repository;

import com.solidsoft.domain.AppointmentDefinition;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the AppointmentDefinition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppointmentDefinitionRepository extends MongoRepository<AppointmentDefinition, String> {}
