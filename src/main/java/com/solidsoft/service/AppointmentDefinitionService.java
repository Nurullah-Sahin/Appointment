package com.solidsoft.service;

import com.solidsoft.domain.AppointmentDefinition;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link AppointmentDefinition}.
 */
public interface AppointmentDefinitionService {
    /**
     * Save a appointmentDefinition.
     *
     * @param appointmentDefinition the entity to save.
     * @return the persisted entity.
     */
    AppointmentDefinition save(AppointmentDefinition appointmentDefinition);

    /**
     * Partially updates a appointmentDefinition.
     *
     * @param appointmentDefinition the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AppointmentDefinition> partialUpdate(AppointmentDefinition appointmentDefinition);

    /**
     * Get all the appointmentDefinitions.
     *
     * @return the list of entities.
     */
    List<AppointmentDefinition> findAll();

    /**
     * Get the "id" appointmentDefinition.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppointmentDefinition> findOne(String id);

    /**
     * Delete the "id" appointmentDefinition.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
