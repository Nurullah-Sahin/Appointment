package com.solidsoft.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.solidsoft.IntegrationTest;
import com.solidsoft.domain.AppointmentDefinition;
import com.solidsoft.repository.AppointmentDefinitionRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link AppointmentDefinitionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppointmentDefinitionResourceIT {

    private static final LocalDate DEFAULT_START_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ALLOW_RESCHEDULE_NO_SOONER_THAN = 1;
    private static final Integer UPDATED_ALLOW_RESCHEDULE_NO_SOONER_THAN = 2;

    private static final Integer DEFAULT_ALLOW_RESCHEDULE_NO_LATER_THAN = 1;
    private static final Integer UPDATED_ALLOW_RESCHEDULE_NO_LATER_THAN = 2;

    private static final Integer DEFAULT_ALLOW_SCHEDULE_NO_SOONER_THAN = 1;
    private static final Integer UPDATED_ALLOW_SCHEDULE_NO_SOONER_THAN = 2;

    private static final Integer DEFAULT_ALLOW_SCHEDULE_NO_LATER_THAN = 1;
    private static final Integer UPDATED_ALLOW_SCHEDULE_NO_LATER_THAN = 2;

    private static final Integer DEFAULT_NUMBER_OF_RESCHEDULE = 1;
    private static final Integer UPDATED_NUMBER_OF_RESCHEDULE = 2;

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

    private static final String ENTITY_API_URL = "/api/appointment-definitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AppointmentDefinitionRepository appointmentDefinitionRepository;

    @Autowired
    private MockMvc restAppointmentDefinitionMockMvc;

    private AppointmentDefinition appointmentDefinition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppointmentDefinition createEntity() {
        AppointmentDefinition appointmentDefinition = new AppointmentDefinition()
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .name(DEFAULT_NAME)
            .allowRescheduleNoSoonerThan(DEFAULT_ALLOW_RESCHEDULE_NO_SOONER_THAN)
            .allowRescheduleNoLaterThan(DEFAULT_ALLOW_RESCHEDULE_NO_LATER_THAN)
            .allowScheduleNoSoonerThan(DEFAULT_ALLOW_SCHEDULE_NO_SOONER_THAN)
            .allowScheduleNoLaterThan(DEFAULT_ALLOW_SCHEDULE_NO_LATER_THAN)
            .numberOfReschedule(DEFAULT_NUMBER_OF_RESCHEDULE)
            .duration(DEFAULT_DURATION);
        return appointmentDefinition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppointmentDefinition createUpdatedEntity() {
        AppointmentDefinition appointmentDefinition = new AppointmentDefinition()
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .name(UPDATED_NAME)
            .allowRescheduleNoSoonerThan(UPDATED_ALLOW_RESCHEDULE_NO_SOONER_THAN)
            .allowRescheduleNoLaterThan(UPDATED_ALLOW_RESCHEDULE_NO_LATER_THAN)
            .allowScheduleNoSoonerThan(UPDATED_ALLOW_SCHEDULE_NO_SOONER_THAN)
            .allowScheduleNoLaterThan(UPDATED_ALLOW_SCHEDULE_NO_LATER_THAN)
            .numberOfReschedule(UPDATED_NUMBER_OF_RESCHEDULE)
            .duration(UPDATED_DURATION);
        return appointmentDefinition;
    }

    @BeforeEach
    public void initTest() {
        appointmentDefinitionRepository.deleteAll();
        appointmentDefinition = createEntity();
    }

    @Test
    void createAppointmentDefinition() throws Exception {
        int databaseSizeBeforeCreate = appointmentDefinitionRepository.findAll().size();
        // Create the AppointmentDefinition
        restAppointmentDefinitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentDefinition))
            )
            .andExpect(status().isCreated());

        // Validate the AppointmentDefinition in the database
        List<AppointmentDefinition> appointmentDefinitionList = appointmentDefinitionRepository.findAll();
        assertThat(appointmentDefinitionList).hasSize(databaseSizeBeforeCreate + 1);
        AppointmentDefinition testAppointmentDefinition = appointmentDefinitionList.get(appointmentDefinitionList.size() - 1);
        assertThat(testAppointmentDefinition.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testAppointmentDefinition.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testAppointmentDefinition.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAppointmentDefinition.getAllowRescheduleNoSoonerThan()).isEqualTo(DEFAULT_ALLOW_RESCHEDULE_NO_SOONER_THAN);
        assertThat(testAppointmentDefinition.getAllowRescheduleNoLaterThan()).isEqualTo(DEFAULT_ALLOW_RESCHEDULE_NO_LATER_THAN);
        assertThat(testAppointmentDefinition.getAllowScheduleNoSoonerThan()).isEqualTo(DEFAULT_ALLOW_SCHEDULE_NO_SOONER_THAN);
        assertThat(testAppointmentDefinition.getAllowScheduleNoLaterThan()).isEqualTo(DEFAULT_ALLOW_SCHEDULE_NO_LATER_THAN);
        assertThat(testAppointmentDefinition.getNumberOfReschedule()).isEqualTo(DEFAULT_NUMBER_OF_RESCHEDULE);
        assertThat(testAppointmentDefinition.getDuration()).isEqualTo(DEFAULT_DURATION);
    }

    @Test
    void createAppointmentDefinitionWithExistingId() throws Exception {
        // Create the AppointmentDefinition with an existing ID
        appointmentDefinition.setId("existing_id");

        int databaseSizeBeforeCreate = appointmentDefinitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppointmentDefinitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentDefinition))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentDefinition in the database
        List<AppointmentDefinition> appointmentDefinitionList = appointmentDefinitionRepository.findAll();
        assertThat(appointmentDefinitionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllAppointmentDefinitions() throws Exception {
        // Initialize the database
        appointmentDefinitionRepository.save(appointmentDefinition);

        // Get all the appointmentDefinitionList
        restAppointmentDefinitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointmentDefinition.getId())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].allowRescheduleNoSoonerThan").value(hasItem(DEFAULT_ALLOW_RESCHEDULE_NO_SOONER_THAN)))
            .andExpect(jsonPath("$.[*].allowRescheduleNoLaterThan").value(hasItem(DEFAULT_ALLOW_RESCHEDULE_NO_LATER_THAN)))
            .andExpect(jsonPath("$.[*].allowScheduleNoSoonerThan").value(hasItem(DEFAULT_ALLOW_SCHEDULE_NO_SOONER_THAN)))
            .andExpect(jsonPath("$.[*].allowScheduleNoLaterThan").value(hasItem(DEFAULT_ALLOW_SCHEDULE_NO_LATER_THAN)))
            .andExpect(jsonPath("$.[*].numberOfReschedule").value(hasItem(DEFAULT_NUMBER_OF_RESCHEDULE)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())));
    }

    @Test
    void getAppointmentDefinition() throws Exception {
        // Initialize the database
        appointmentDefinitionRepository.save(appointmentDefinition);

        // Get the appointmentDefinition
        restAppointmentDefinitionMockMvc
            .perform(get(ENTITY_API_URL_ID, appointmentDefinition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appointmentDefinition.getId()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.allowRescheduleNoSoonerThan").value(DEFAULT_ALLOW_RESCHEDULE_NO_SOONER_THAN))
            .andExpect(jsonPath("$.allowRescheduleNoLaterThan").value(DEFAULT_ALLOW_RESCHEDULE_NO_LATER_THAN))
            .andExpect(jsonPath("$.allowScheduleNoSoonerThan").value(DEFAULT_ALLOW_SCHEDULE_NO_SOONER_THAN))
            .andExpect(jsonPath("$.allowScheduleNoLaterThan").value(DEFAULT_ALLOW_SCHEDULE_NO_LATER_THAN))
            .andExpect(jsonPath("$.numberOfReschedule").value(DEFAULT_NUMBER_OF_RESCHEDULE))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()));
    }

    @Test
    void getNonExistingAppointmentDefinition() throws Exception {
        // Get the appointmentDefinition
        restAppointmentDefinitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewAppointmentDefinition() throws Exception {
        // Initialize the database
        appointmentDefinitionRepository.save(appointmentDefinition);

        int databaseSizeBeforeUpdate = appointmentDefinitionRepository.findAll().size();

        // Update the appointmentDefinition
        AppointmentDefinition updatedAppointmentDefinition = appointmentDefinitionRepository.findById(appointmentDefinition.getId()).get();
        updatedAppointmentDefinition
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .name(UPDATED_NAME)
            .allowRescheduleNoSoonerThan(UPDATED_ALLOW_RESCHEDULE_NO_SOONER_THAN)
            .allowRescheduleNoLaterThan(UPDATED_ALLOW_RESCHEDULE_NO_LATER_THAN)
            .allowScheduleNoSoonerThan(UPDATED_ALLOW_SCHEDULE_NO_SOONER_THAN)
            .allowScheduleNoLaterThan(UPDATED_ALLOW_SCHEDULE_NO_LATER_THAN)
            .numberOfReschedule(UPDATED_NUMBER_OF_RESCHEDULE)
            .duration(UPDATED_DURATION);

        restAppointmentDefinitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAppointmentDefinition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAppointmentDefinition))
            )
            .andExpect(status().isOk());

        // Validate the AppointmentDefinition in the database
        List<AppointmentDefinition> appointmentDefinitionList = appointmentDefinitionRepository.findAll();
        assertThat(appointmentDefinitionList).hasSize(databaseSizeBeforeUpdate);
        AppointmentDefinition testAppointmentDefinition = appointmentDefinitionList.get(appointmentDefinitionList.size() - 1);
        assertThat(testAppointmentDefinition.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testAppointmentDefinition.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testAppointmentDefinition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAppointmentDefinition.getAllowRescheduleNoSoonerThan()).isEqualTo(UPDATED_ALLOW_RESCHEDULE_NO_SOONER_THAN);
        assertThat(testAppointmentDefinition.getAllowRescheduleNoLaterThan()).isEqualTo(UPDATED_ALLOW_RESCHEDULE_NO_LATER_THAN);
        assertThat(testAppointmentDefinition.getAllowScheduleNoSoonerThan()).isEqualTo(UPDATED_ALLOW_SCHEDULE_NO_SOONER_THAN);
        assertThat(testAppointmentDefinition.getAllowScheduleNoLaterThan()).isEqualTo(UPDATED_ALLOW_SCHEDULE_NO_LATER_THAN);
        assertThat(testAppointmentDefinition.getNumberOfReschedule()).isEqualTo(UPDATED_NUMBER_OF_RESCHEDULE);
        assertThat(testAppointmentDefinition.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    void putNonExistingAppointmentDefinition() throws Exception {
        int databaseSizeBeforeUpdate = appointmentDefinitionRepository.findAll().size();
        appointmentDefinition.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentDefinitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appointmentDefinition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentDefinition))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentDefinition in the database
        List<AppointmentDefinition> appointmentDefinitionList = appointmentDefinitionRepository.findAll();
        assertThat(appointmentDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAppointmentDefinition() throws Exception {
        int databaseSizeBeforeUpdate = appointmentDefinitionRepository.findAll().size();
        appointmentDefinition.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentDefinitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentDefinition))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentDefinition in the database
        List<AppointmentDefinition> appointmentDefinitionList = appointmentDefinitionRepository.findAll();
        assertThat(appointmentDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAppointmentDefinition() throws Exception {
        int databaseSizeBeforeUpdate = appointmentDefinitionRepository.findAll().size();
        appointmentDefinition.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentDefinitionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentDefinition))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppointmentDefinition in the database
        List<AppointmentDefinition> appointmentDefinitionList = appointmentDefinitionRepository.findAll();
        assertThat(appointmentDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAppointmentDefinitionWithPatch() throws Exception {
        // Initialize the database
        appointmentDefinitionRepository.save(appointmentDefinition);

        int databaseSizeBeforeUpdate = appointmentDefinitionRepository.findAll().size();

        // Update the appointmentDefinition using partial update
        AppointmentDefinition partialUpdatedAppointmentDefinition = new AppointmentDefinition();
        partialUpdatedAppointmentDefinition.setId(appointmentDefinition.getId());

        partialUpdatedAppointmentDefinition
            .name(UPDATED_NAME)
            .allowRescheduleNoSoonerThan(UPDATED_ALLOW_RESCHEDULE_NO_SOONER_THAN)
            .allowRescheduleNoLaterThan(UPDATED_ALLOW_RESCHEDULE_NO_LATER_THAN)
            .allowScheduleNoLaterThan(UPDATED_ALLOW_SCHEDULE_NO_LATER_THAN)
            .numberOfReschedule(UPDATED_NUMBER_OF_RESCHEDULE)
            .duration(UPDATED_DURATION);

        restAppointmentDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointmentDefinition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppointmentDefinition))
            )
            .andExpect(status().isOk());

        // Validate the AppointmentDefinition in the database
        List<AppointmentDefinition> appointmentDefinitionList = appointmentDefinitionRepository.findAll();
        assertThat(appointmentDefinitionList).hasSize(databaseSizeBeforeUpdate);
        AppointmentDefinition testAppointmentDefinition = appointmentDefinitionList.get(appointmentDefinitionList.size() - 1);
        assertThat(testAppointmentDefinition.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testAppointmentDefinition.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testAppointmentDefinition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAppointmentDefinition.getAllowRescheduleNoSoonerThan()).isEqualTo(UPDATED_ALLOW_RESCHEDULE_NO_SOONER_THAN);
        assertThat(testAppointmentDefinition.getAllowRescheduleNoLaterThan()).isEqualTo(UPDATED_ALLOW_RESCHEDULE_NO_LATER_THAN);
        assertThat(testAppointmentDefinition.getAllowScheduleNoSoonerThan()).isEqualTo(DEFAULT_ALLOW_SCHEDULE_NO_SOONER_THAN);
        assertThat(testAppointmentDefinition.getAllowScheduleNoLaterThan()).isEqualTo(UPDATED_ALLOW_SCHEDULE_NO_LATER_THAN);
        assertThat(testAppointmentDefinition.getNumberOfReschedule()).isEqualTo(UPDATED_NUMBER_OF_RESCHEDULE);
        assertThat(testAppointmentDefinition.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    void fullUpdateAppointmentDefinitionWithPatch() throws Exception {
        // Initialize the database
        appointmentDefinitionRepository.save(appointmentDefinition);

        int databaseSizeBeforeUpdate = appointmentDefinitionRepository.findAll().size();

        // Update the appointmentDefinition using partial update
        AppointmentDefinition partialUpdatedAppointmentDefinition = new AppointmentDefinition();
        partialUpdatedAppointmentDefinition.setId(appointmentDefinition.getId());

        partialUpdatedAppointmentDefinition
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .name(UPDATED_NAME)
            .allowRescheduleNoSoonerThan(UPDATED_ALLOW_RESCHEDULE_NO_SOONER_THAN)
            .allowRescheduleNoLaterThan(UPDATED_ALLOW_RESCHEDULE_NO_LATER_THAN)
            .allowScheduleNoSoonerThan(UPDATED_ALLOW_SCHEDULE_NO_SOONER_THAN)
            .allowScheduleNoLaterThan(UPDATED_ALLOW_SCHEDULE_NO_LATER_THAN)
            .numberOfReschedule(UPDATED_NUMBER_OF_RESCHEDULE)
            .duration(UPDATED_DURATION);

        restAppointmentDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointmentDefinition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppointmentDefinition))
            )
            .andExpect(status().isOk());

        // Validate the AppointmentDefinition in the database
        List<AppointmentDefinition> appointmentDefinitionList = appointmentDefinitionRepository.findAll();
        assertThat(appointmentDefinitionList).hasSize(databaseSizeBeforeUpdate);
        AppointmentDefinition testAppointmentDefinition = appointmentDefinitionList.get(appointmentDefinitionList.size() - 1);
        assertThat(testAppointmentDefinition.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testAppointmentDefinition.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testAppointmentDefinition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAppointmentDefinition.getAllowRescheduleNoSoonerThan()).isEqualTo(UPDATED_ALLOW_RESCHEDULE_NO_SOONER_THAN);
        assertThat(testAppointmentDefinition.getAllowRescheduleNoLaterThan()).isEqualTo(UPDATED_ALLOW_RESCHEDULE_NO_LATER_THAN);
        assertThat(testAppointmentDefinition.getAllowScheduleNoSoonerThan()).isEqualTo(UPDATED_ALLOW_SCHEDULE_NO_SOONER_THAN);
        assertThat(testAppointmentDefinition.getAllowScheduleNoLaterThan()).isEqualTo(UPDATED_ALLOW_SCHEDULE_NO_LATER_THAN);
        assertThat(testAppointmentDefinition.getNumberOfReschedule()).isEqualTo(UPDATED_NUMBER_OF_RESCHEDULE);
        assertThat(testAppointmentDefinition.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    void patchNonExistingAppointmentDefinition() throws Exception {
        int databaseSizeBeforeUpdate = appointmentDefinitionRepository.findAll().size();
        appointmentDefinition.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appointmentDefinition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentDefinition))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentDefinition in the database
        List<AppointmentDefinition> appointmentDefinitionList = appointmentDefinitionRepository.findAll();
        assertThat(appointmentDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAppointmentDefinition() throws Exception {
        int databaseSizeBeforeUpdate = appointmentDefinitionRepository.findAll().size();
        appointmentDefinition.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentDefinition))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentDefinition in the database
        List<AppointmentDefinition> appointmentDefinitionList = appointmentDefinitionRepository.findAll();
        assertThat(appointmentDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAppointmentDefinition() throws Exception {
        int databaseSizeBeforeUpdate = appointmentDefinitionRepository.findAll().size();
        appointmentDefinition.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentDefinition))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppointmentDefinition in the database
        List<AppointmentDefinition> appointmentDefinitionList = appointmentDefinitionRepository.findAll();
        assertThat(appointmentDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAppointmentDefinition() throws Exception {
        // Initialize the database
        appointmentDefinitionRepository.save(appointmentDefinition);

        int databaseSizeBeforeDelete = appointmentDefinitionRepository.findAll().size();

        // Delete the appointmentDefinition
        restAppointmentDefinitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, appointmentDefinition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppointmentDefinition> appointmentDefinitionList = appointmentDefinitionRepository.findAll();
        assertThat(appointmentDefinitionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
