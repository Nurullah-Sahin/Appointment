package com.solidsoft.service;

import com.solidsoft.domain.AppointmentSlot;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link AppointmentSlot}.
 */
public interface AppointmentSlotService {
    /**
     * Save a appointmentSlot.
     *
     * @param appointmentSlot the entity to save.
     * @return the persisted entity.
     */
    AppointmentSlot save(AppointmentSlot appointmentSlot);

    /**
     * Partially updates a appointmentSlot.
     *
     * @param appointmentSlot the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AppointmentSlot> partialUpdate(AppointmentSlot appointmentSlot);

    /**
     * Get all the appointmentSlots.
     *
     * @return the list of entities.
     */
    List<AppointmentSlot> findAll();

    /**
     * Get the "id" appointmentSlot.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppointmentSlot> findOne(String id);

    /**
     * Delete the "id" appointmentSlot.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
