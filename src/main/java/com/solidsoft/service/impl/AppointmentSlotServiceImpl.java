package com.solidsoft.service.impl;

import com.solidsoft.domain.AppointmentSlot;
import com.solidsoft.repository.AppointmentSlotRepository;
import com.solidsoft.service.AppointmentSlotService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link AppointmentSlot}.
 */
@Service
public class AppointmentSlotServiceImpl implements AppointmentSlotService {

    private final Logger log = LoggerFactory.getLogger(AppointmentSlotServiceImpl.class);

    private final AppointmentSlotRepository appointmentSlotRepository;

    public AppointmentSlotServiceImpl(AppointmentSlotRepository appointmentSlotRepository) {
        this.appointmentSlotRepository = appointmentSlotRepository;
    }

    @Override
    public AppointmentSlot save(AppointmentSlot appointmentSlot) {
        log.debug("Request to save AppointmentSlot : {}", appointmentSlot);
        return appointmentSlotRepository.save(appointmentSlot);
    }

    @Override
    public Optional<AppointmentSlot> partialUpdate(AppointmentSlot appointmentSlot) {
        log.debug("Request to partially update AppointmentSlot : {}", appointmentSlot);

        return appointmentSlotRepository
            .findById(appointmentSlot.getId())
            .map(
                existingAppointmentSlot -> {
                    if (appointmentSlot.getStartTime() != null) {
                        existingAppointmentSlot.setStartTime(appointmentSlot.getStartTime());
                    }
                    if (appointmentSlot.getEndTime() != null) {
                        existingAppointmentSlot.setEndTime(appointmentSlot.getEndTime());
                    }

                    return existingAppointmentSlot;
                }
            )
            .map(appointmentSlotRepository::save);
    }

    @Override
    public List<AppointmentSlot> findAll() {
        log.debug("Request to get all AppointmentSlots");
        return appointmentSlotRepository.findAll();
    }

    @Override
    public Optional<AppointmentSlot> findOne(String id) {
        log.debug("Request to get AppointmentSlot : {}", id);
        return appointmentSlotRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete AppointmentSlot : {}", id);
        appointmentSlotRepository.deleteById(id);
    }
}
