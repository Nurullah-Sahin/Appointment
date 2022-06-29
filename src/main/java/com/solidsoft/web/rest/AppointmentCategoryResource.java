package com.solidsoft.web.rest;

import com.solidsoft.domain.AppointmentCategory;
import com.solidsoft.repository.AppointmentCategoryRepository;
import com.solidsoft.service.AppointmentCategoryService;
import com.solidsoft.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.solidsoft.domain.AppointmentCategory}.
 */
@RestController
@RequestMapping("/api")
public class AppointmentCategoryResource {

    private final Logger log = LoggerFactory.getLogger(AppointmentCategoryResource.class);

    private static final String ENTITY_NAME = "appointmentCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppointmentCategoryService appointmentCategoryService;

    private final AppointmentCategoryRepository appointmentCategoryRepository;

    public AppointmentCategoryResource(
        AppointmentCategoryService appointmentCategoryService,
        AppointmentCategoryRepository appointmentCategoryRepository
    ) {
        this.appointmentCategoryService = appointmentCategoryService;
        this.appointmentCategoryRepository = appointmentCategoryRepository;
    }

    /**
     * {@code POST  /appointment-categories} : Create a new appointmentCategory.
     *
     * @param appointmentCategory the appointmentCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appointmentCategory, or with status {@code 400 (Bad Request)} if the appointmentCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/appointment-categories")
    public ResponseEntity<AppointmentCategory> createAppointmentCategory(@RequestBody AppointmentCategory appointmentCategory)
        throws URISyntaxException {
        log.debug("REST request to save AppointmentCategory : {}", appointmentCategory);
        if (appointmentCategory.getId() != null) {
            throw new BadRequestAlertException("A new appointmentCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppointmentCategory result = appointmentCategoryService.save(appointmentCategory);
        return ResponseEntity
            .created(new URI("/api/appointment-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /appointment-categories/:id} : Updates an existing appointmentCategory.
     *
     * @param id the id of the appointmentCategory to save.
     * @param appointmentCategory the appointmentCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appointmentCategory,
     * or with status {@code 400 (Bad Request)} if the appointmentCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appointmentCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/appointment-categories/{id}")
    public ResponseEntity<AppointmentCategory> updateAppointmentCategory(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AppointmentCategory appointmentCategory
    ) throws URISyntaxException {
        log.debug("REST request to update AppointmentCategory : {}, {}", id, appointmentCategory);
        if (appointmentCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appointmentCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appointmentCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppointmentCategory result = appointmentCategoryService.save(appointmentCategory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appointmentCategory.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /appointment-categories/:id} : Partial updates given fields of an existing appointmentCategory, field will ignore if it is null
     *
     * @param id the id of the appointmentCategory to save.
     * @param appointmentCategory the appointmentCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appointmentCategory,
     * or with status {@code 400 (Bad Request)} if the appointmentCategory is not valid,
     * or with status {@code 404 (Not Found)} if the appointmentCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the appointmentCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/appointment-categories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AppointmentCategory> partialUpdateAppointmentCategory(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AppointmentCategory appointmentCategory
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppointmentCategory partially : {}, {}", id, appointmentCategory);
        if (appointmentCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appointmentCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appointmentCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppointmentCategory> result = appointmentCategoryService.partialUpdate(appointmentCategory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appointmentCategory.getId())
        );
    }

    /**
     * {@code GET  /appointment-categories} : get all the appointmentCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appointmentCategories in body.
     */
    @GetMapping("/appointment-categories")
    public List<AppointmentCategory> getAllAppointmentCategories() {
        log.debug("REST request to get all AppointmentCategories");
        return appointmentCategoryService.findAll();
    }

    /**
     * {@code GET  /appointment-categories/:id} : get the "id" appointmentCategory.
     *
     * @param id the id of the appointmentCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appointmentCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/appointment-categories/{id}")
    public ResponseEntity<AppointmentCategory> getAppointmentCategory(@PathVariable String id) {
        log.debug("REST request to get AppointmentCategory : {}", id);
        Optional<AppointmentCategory> appointmentCategory = appointmentCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appointmentCategory);
    }

    /**
     * {@code DELETE  /appointment-categories/:id} : delete the "id" appointmentCategory.
     *
     * @param id the id of the appointmentCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/appointment-categories/{id}")
    public ResponseEntity<Void> deleteAppointmentCategory(@PathVariable String id) {
        log.debug("REST request to delete AppointmentCategory : {}", id);
        appointmentCategoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
