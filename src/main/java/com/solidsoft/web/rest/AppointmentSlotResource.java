package com.solidsoft.web.rest;

import com.solidsoft.domain.AppointmentSlot;
import com.solidsoft.repository.AppointmentSlotRepository;
import com.solidsoft.service.AppointmentSlotService;
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
 * REST controller for managing {@link com.solidsoft.domain.AppointmentSlot}.
 */
@RestController
@RequestMapping("/api")
public class AppointmentSlotResource {

    private final Logger log = LoggerFactory.getLogger(AppointmentSlotResource.class);

    private static final String ENTITY_NAME = "appointmentSlot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppointmentSlotService appointmentSlotService;

    private final AppointmentSlotRepository appointmentSlotRepository;

    public AppointmentSlotResource(AppointmentSlotService appointmentSlotService, AppointmentSlotRepository appointmentSlotRepository) {
        this.appointmentSlotService = appointmentSlotService;
        this.appointmentSlotRepository = appointmentSlotRepository;
    }

    /**
     * {@code POST  /appointment-slots} : Create a new appointmentSlot.
     *
     * @param appointmentSlot the appointmentSlot to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appointmentSlot, or with status {@code 400 (Bad Request)} if the appointmentSlot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/appointment-slots")
    public ResponseEntity<AppointmentSlot> createAppointmentSlot(@RequestBody AppointmentSlot appointmentSlot) throws URISyntaxException {
        log.debug("REST request to save AppointmentSlot : {}", appointmentSlot);
        if (appointmentSlot.getId() != null) {
            throw new BadRequestAlertException("A new appointmentSlot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppointmentSlot result = appointmentSlotService.save(appointmentSlot);
        return ResponseEntity
            .created(new URI("/api/appointment-slots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /appointment-slots/:id} : Updates an existing appointmentSlot.
     *
     * @param id the id of the appointmentSlot to save.
     * @param appointmentSlot the appointmentSlot to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appointmentSlot,
     * or with status {@code 400 (Bad Request)} if the appointmentSlot is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appointmentSlot couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/appointment-slots/{id}")
    public ResponseEntity<AppointmentSlot> updateAppointmentSlot(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AppointmentSlot appointmentSlot
    ) throws URISyntaxException {
        log.debug("REST request to update AppointmentSlot : {}, {}", id, appointmentSlot);
        if (appointmentSlot.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appointmentSlot.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appointmentSlotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppointmentSlot result = appointmentSlotService.save(appointmentSlot);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appointmentSlot.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /appointment-slots/:id} : Partial updates given fields of an existing appointmentSlot, field will ignore if it is null
     *
     * @param id the id of the appointmentSlot to save.
     * @param appointmentSlot the appointmentSlot to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appointmentSlot,
     * or with status {@code 400 (Bad Request)} if the appointmentSlot is not valid,
     * or with status {@code 404 (Not Found)} if the appointmentSlot is not found,
     * or with status {@code 500 (Internal Server Error)} if the appointmentSlot couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/appointment-slots/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AppointmentSlot> partialUpdateAppointmentSlot(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AppointmentSlot appointmentSlot
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppointmentSlot partially : {}, {}", id, appointmentSlot);
        if (appointmentSlot.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appointmentSlot.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appointmentSlotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppointmentSlot> result = appointmentSlotService.partialUpdate(appointmentSlot);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appointmentSlot.getId())
        );
    }

    /**
     * {@code GET  /appointment-slots} : get all the appointmentSlots.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appointmentSlots in body.
     */
    @GetMapping("/appointment-slots")
    public List<AppointmentSlot> getAllAppointmentSlots() {
        log.debug("REST request to get all AppointmentSlots");
        return appointmentSlotService.findAll();
    }

    /**
     * {@code GET  /appointment-slots/:id} : get the "id" appointmentSlot.
     *
     * @param id the id of the appointmentSlot to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appointmentSlot, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/appointment-slots/{id}")
    public ResponseEntity<AppointmentSlot> getAppointmentSlot(@PathVariable String id) {
        log.debug("REST request to get AppointmentSlot : {}", id);
        Optional<AppointmentSlot> appointmentSlot = appointmentSlotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appointmentSlot);
    }

    /**
     * {@code DELETE  /appointment-slots/:id} : delete the "id" appointmentSlot.
     *
     * @param id the id of the appointmentSlot to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/appointment-slots/{id}")
    public ResponseEntity<Void> deleteAppointmentSlot(@PathVariable String id) {
        log.debug("REST request to delete AppointmentSlot : {}", id);
        appointmentSlotService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
