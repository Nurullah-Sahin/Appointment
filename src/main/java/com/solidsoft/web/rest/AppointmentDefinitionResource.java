package com.solidsoft.web.rest;

import com.solidsoft.domain.AppointmentDefinition;
import com.solidsoft.repository.AppointmentDefinitionRepository;
import com.solidsoft.service.AppointmentDefinitionService;
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
 * REST controller for managing {@link com.solidsoft.domain.AppointmentDefinition}.
 */
@RestController
@RequestMapping("/api")
public class AppointmentDefinitionResource {

    private final Logger log = LoggerFactory.getLogger(AppointmentDefinitionResource.class);

    private static final String ENTITY_NAME = "appointmentDefinition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppointmentDefinitionService appointmentDefinitionService;

    private final AppointmentDefinitionRepository appointmentDefinitionRepository;

    public AppointmentDefinitionResource(
        AppointmentDefinitionService appointmentDefinitionService,
        AppointmentDefinitionRepository appointmentDefinitionRepository
    ) {
        this.appointmentDefinitionService = appointmentDefinitionService;
        this.appointmentDefinitionRepository = appointmentDefinitionRepository;
    }

    /**
     * {@code POST  /appointment-definitions} : Create a new appointmentDefinition.
     *
     * @param appointmentDefinition the appointmentDefinition to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appointmentDefinition, or with status {@code 400 (Bad Request)} if the appointmentDefinition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/appointment-definitions")
    public ResponseEntity<AppointmentDefinition> createAppointmentDefinition(@RequestBody AppointmentDefinition appointmentDefinition)
        throws URISyntaxException {
        log.debug("REST request to save AppointmentDefinition : {}", appointmentDefinition);
        if (appointmentDefinition.getId() != null) {
            throw new BadRequestAlertException("A new appointmentDefinition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppointmentDefinition result = appointmentDefinitionService.save(appointmentDefinition);
        return ResponseEntity
            .created(new URI("/api/appointment-definitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /appointment-definitions/:id} : Updates an existing appointmentDefinition.
     *
     * @param id the id of the appointmentDefinition to save.
     * @param appointmentDefinition the appointmentDefinition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appointmentDefinition,
     * or with status {@code 400 (Bad Request)} if the appointmentDefinition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appointmentDefinition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/appointment-definitions/{id}")
    public ResponseEntity<AppointmentDefinition> updateAppointmentDefinition(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AppointmentDefinition appointmentDefinition
    ) throws URISyntaxException {
        log.debug("REST request to update AppointmentDefinition : {}, {}", id, appointmentDefinition);
        if (appointmentDefinition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appointmentDefinition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appointmentDefinitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppointmentDefinition result = appointmentDefinitionService.save(appointmentDefinition);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appointmentDefinition.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /appointment-definitions/:id} : Partial updates given fields of an existing appointmentDefinition, field will ignore if it is null
     *
     * @param id the id of the appointmentDefinition to save.
     * @param appointmentDefinition the appointmentDefinition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appointmentDefinition,
     * or with status {@code 400 (Bad Request)} if the appointmentDefinition is not valid,
     * or with status {@code 404 (Not Found)} if the appointmentDefinition is not found,
     * or with status {@code 500 (Internal Server Error)} if the appointmentDefinition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/appointment-definitions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AppointmentDefinition> partialUpdateAppointmentDefinition(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AppointmentDefinition appointmentDefinition
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppointmentDefinition partially : {}, {}", id, appointmentDefinition);
        if (appointmentDefinition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appointmentDefinition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appointmentDefinitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppointmentDefinition> result = appointmentDefinitionService.partialUpdate(appointmentDefinition);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appointmentDefinition.getId())
        );
    }

    /**
     * {@code GET  /appointment-definitions} : get all the appointmentDefinitions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appointmentDefinitions in body.
     */
    @GetMapping("/appointment-definitions")
    public List<AppointmentDefinition> getAllAppointmentDefinitions() {
        log.debug("REST request to get all AppointmentDefinitions");
        return appointmentDefinitionService.findAll();
    }

    /**
     * {@code GET  /appointment-definitions/:id} : get the "id" appointmentDefinition.
     *
     * @param id the id of the appointmentDefinition to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appointmentDefinition, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/appointment-definitions/{id}")
    public ResponseEntity<AppointmentDefinition> getAppointmentDefinition(@PathVariable String id) {
        log.debug("REST request to get AppointmentDefinition : {}", id);
        Optional<AppointmentDefinition> appointmentDefinition = appointmentDefinitionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appointmentDefinition);
    }

    /**
     * {@code DELETE  /appointment-definitions/:id} : delete the "id" appointmentDefinition.
     *
     * @param id the id of the appointmentDefinition to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/appointment-definitions/{id}")
    public ResponseEntity<Void> deleteAppointmentDefinition(@PathVariable String id) {
        log.debug("REST request to delete AppointmentDefinition : {}", id);
        appointmentDefinitionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
