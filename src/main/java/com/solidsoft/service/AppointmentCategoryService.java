package com.solidsoft.service;

import com.solidsoft.domain.AppointmentCategory;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link AppointmentCategory}.
 */
public interface AppointmentCategoryService {
    /**
     * Save a appointmentCategory.
     *
     * @param appointmentCategory the entity to save.
     * @return the persisted entity.
     */
    AppointmentCategory save(AppointmentCategory appointmentCategory);

    /**
     * Partially updates a appointmentCategory.
     *
     * @param appointmentCategory the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AppointmentCategory> partialUpdate(AppointmentCategory appointmentCategory);

    /**
     * Get all the appointmentCategories.
     *
     * @return the list of entities.
     */
    List<AppointmentCategory> findAll();

    /**
     * Get the "id" appointmentCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppointmentCategory> findOne(String id);

    /**
     * Delete the "id" appointmentCategory.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
