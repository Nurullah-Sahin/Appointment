package com.solidsoft.service.impl;

import com.solidsoft.domain.AppointmentDefinition;
import com.solidsoft.repository.AppointmentDefinitionRepository;
import com.solidsoft.service.AppointmentDefinitionService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link AppointmentDefinition}.
 */
@Service
public class AppointmentDefinitionServiceImpl implements AppointmentDefinitionService {

    private final Logger log = LoggerFactory.getLogger(AppointmentDefinitionServiceImpl.class);

    private final AppointmentDefinitionRepository appointmentDefinitionRepository;

    public AppointmentDefinitionServiceImpl(AppointmentDefinitionRepository appointmentDefinitionRepository) {
        this.appointmentDefinitionRepository = appointmentDefinitionRepository;
    }

    @Override
    public AppointmentDefinition save(AppointmentDefinition appointmentDefinition) {
        log.debug("Request to save AppointmentDefinition : {}", appointmentDefinition);
        return appointmentDefinitionRepository.save(appointmentDefinition);
    }

    @Override
    public Optional<AppointmentDefinition> partialUpdate(AppointmentDefinition appointmentDefinition) {
        log.debug("Request to partially update AppointmentDefinition : {}", appointmentDefinition);

        return appointmentDefinitionRepository
            .findById(appointmentDefinition.getId())
            .map(
                existingAppointmentDefinition -> {
                    if (appointmentDefinition.getStartTime() != null) {
                        existingAppointmentDefinition.setStartTime(appointmentDefinition.getStartTime());
                    }
                    if (appointmentDefinition.getEndTime() != null) {
                        existingAppointmentDefinition.setEndTime(appointmentDefinition.getEndTime());
                    }
                    if (appointmentDefinition.getName() != null) {
                        existingAppointmentDefinition.setName(appointmentDefinition.getName());
                    }
                    if (appointmentDefinition.getAllowRescheduleNoSoonerThan() != null) {
                        existingAppointmentDefinition.setAllowRescheduleNoSoonerThan(
                            appointmentDefinition.getAllowRescheduleNoSoonerThan()
                        );
                    }
                    if (appointmentDefinition.getAllowRescheduleNoLaterThan() != null) {
                        existingAppointmentDefinition.setAllowRescheduleNoLaterThan(appointmentDefinition.getAllowRescheduleNoLaterThan());
                    }
                    if (appointmentDefinition.getAllowScheduleNoSoonerThan() != null) {
                        existingAppointmentDefinition.setAllowScheduleNoSoonerThan(appointmentDefinition.getAllowScheduleNoSoonerThan());
                    }
                    if (appointmentDefinition.getAllowScheduleNoLaterThan() != null) {
                        existingAppointmentDefinition.setAllowScheduleNoLaterThan(appointmentDefinition.getAllowScheduleNoLaterThan());
                    }
                    if (appointmentDefinition.getNumberOfReschedule() != null) {
                        existingAppointmentDefinition.setNumberOfReschedule(appointmentDefinition.getNumberOfReschedule());
                    }
                    if (appointmentDefinition.getDuration() != null) {
                        existingAppointmentDefinition.setDuration(appointmentDefinition.getDuration());
                    }

                    return existingAppointmentDefinition;
                }
            )
            .map(appointmentDefinitionRepository::save);
    }

    @Override
    public List<AppointmentDefinition> findAll() {
        log.debug("Request to get all AppointmentDefinitions");
        return appointmentDefinitionRepository.findAll();
    }

    @Override
    public Optional<AppointmentDefinition> findOne(String id) {
        log.debug("Request to get AppointmentDefinition : {}", id);
        return appointmentDefinitionRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete AppointmentDefinition : {}", id);
        appointmentDefinitionRepository.deleteById(id);
    }
}
