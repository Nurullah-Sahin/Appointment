package com.solidsoft.service.impl;

import com.solidsoft.domain.AppointmentCategory;
import com.solidsoft.repository.AppointmentCategoryRepository;
import com.solidsoft.service.AppointmentCategoryService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link AppointmentCategory}.
 */
@Service
public class AppointmentCategoryServiceImpl implements AppointmentCategoryService {

    private final Logger log = LoggerFactory.getLogger(AppointmentCategoryServiceImpl.class);

    private final AppointmentCategoryRepository appointmentCategoryRepository;

    public AppointmentCategoryServiceImpl(AppointmentCategoryRepository appointmentCategoryRepository) {
        this.appointmentCategoryRepository = appointmentCategoryRepository;
    }

    @Override
    public AppointmentCategory save(AppointmentCategory appointmentCategory) {
        log.debug("Request to save AppointmentCategory : {}", appointmentCategory);
        return appointmentCategoryRepository.save(appointmentCategory);
    }

    @Override
    public Optional<AppointmentCategory> partialUpdate(AppointmentCategory appointmentCategory) {
        log.debug("Request to partially update AppointmentCategory : {}", appointmentCategory);

        return appointmentCategoryRepository
            .findById(appointmentCategory.getId())
            .map(
                existingAppointmentCategory -> {
                    if (appointmentCategory.getCategoryName() != null) {
                        existingAppointmentCategory.setCategoryName(appointmentCategory.getCategoryName());
                    }
                    if (appointmentCategory.getHosts() != null) {
                        existingAppointmentCategory.setHosts(appointmentCategory.getHosts());
                    }

                    return existingAppointmentCategory;
                }
            )
            .map(appointmentCategoryRepository::save);
    }

    @Override
    public List<AppointmentCategory> findAll() {
        log.debug("Request to get all AppointmentCategories");
        return appointmentCategoryRepository.findAll();
    }

    @Override
    public Optional<AppointmentCategory> findOne(String id) {
        log.debug("Request to get AppointmentCategory : {}", id);
        return appointmentCategoryRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete AppointmentCategory : {}", id);
        appointmentCategoryRepository.deleteById(id);
    }
}
