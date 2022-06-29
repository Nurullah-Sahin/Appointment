package com.solidsoft.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.solidsoft.IntegrationTest;
import com.solidsoft.domain.AppointmentSlot;
import com.solidsoft.repository.AppointmentSlotRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link AppointmentSlotResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppointmentSlotResourceIT {

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/appointment-slots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AppointmentSlotRepository appointmentSlotRepository;

    @Autowired
    private MockMvc restAppointmentSlotMockMvc;

    private AppointmentSlot appointmentSlot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppointmentSlot createEntity() {
        AppointmentSlot appointmentSlot = new AppointmentSlot().startTime(DEFAULT_START_TIME).endTime(DEFAULT_END_TIME);
        return appointmentSlot;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppointmentSlot createUpdatedEntity() {
        AppointmentSlot appointmentSlot = new AppointmentSlot().startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);
        return appointmentSlot;
    }

    @BeforeEach
    public void initTest() {
        appointmentSlotRepository.deleteAll();
        appointmentSlot = createEntity();
    }

    @Test
    void createAppointmentSlot() throws Exception {
        int databaseSizeBeforeCreate = appointmentSlotRepository.findAll().size();
        // Create the AppointmentSlot
        restAppointmentSlotMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentSlot))
            )
            .andExpect(status().isCreated());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeCreate + 1);
        AppointmentSlot testAppointmentSlot = appointmentSlotList.get(appointmentSlotList.size() - 1);
        assertThat(testAppointmentSlot.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testAppointmentSlot.getEndTime()).isEqualTo(DEFAULT_END_TIME);
    }

    @Test
    void createAppointmentSlotWithExistingId() throws Exception {
        // Create the AppointmentSlot with an existing ID
        appointmentSlot.setId("existing_id");

        int databaseSizeBeforeCreate = appointmentSlotRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppointmentSlotMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentSlot))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllAppointmentSlots() throws Exception {
        // Initialize the database
        appointmentSlotRepository.save(appointmentSlot);

        // Get all the appointmentSlotList
        restAppointmentSlotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointmentSlot.getId())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));
    }

    @Test
    void getAppointmentSlot() throws Exception {
        // Initialize the database
        appointmentSlotRepository.save(appointmentSlot);

        // Get the appointmentSlot
        restAppointmentSlotMockMvc
            .perform(get(ENTITY_API_URL_ID, appointmentSlot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appointmentSlot.getId()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()));
    }

    @Test
    void getNonExistingAppointmentSlot() throws Exception {
        // Get the appointmentSlot
        restAppointmentSlotMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewAppointmentSlot() throws Exception {
        // Initialize the database
        appointmentSlotRepository.save(appointmentSlot);

        int databaseSizeBeforeUpdate = appointmentSlotRepository.findAll().size();

        // Update the appointmentSlot
        AppointmentSlot updatedAppointmentSlot = appointmentSlotRepository.findById(appointmentSlot.getId()).get();
        updatedAppointmentSlot.startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);

        restAppointmentSlotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAppointmentSlot.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAppointmentSlot))
            )
            .andExpect(status().isOk());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeUpdate);
        AppointmentSlot testAppointmentSlot = appointmentSlotList.get(appointmentSlotList.size() - 1);
        assertThat(testAppointmentSlot.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testAppointmentSlot.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    void putNonExistingAppointmentSlot() throws Exception {
        int databaseSizeBeforeUpdate = appointmentSlotRepository.findAll().size();
        appointmentSlot.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentSlotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appointmentSlot.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSlot))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAppointmentSlot() throws Exception {
        int databaseSizeBeforeUpdate = appointmentSlotRepository.findAll().size();
        appointmentSlot.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentSlotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSlot))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAppointmentSlot() throws Exception {
        int databaseSizeBeforeUpdate = appointmentSlotRepository.findAll().size();
        appointmentSlot.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentSlotMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentSlot))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAppointmentSlotWithPatch() throws Exception {
        // Initialize the database
        appointmentSlotRepository.save(appointmentSlot);

        int databaseSizeBeforeUpdate = appointmentSlotRepository.findAll().size();

        // Update the appointmentSlot using partial update
        AppointmentSlot partialUpdatedAppointmentSlot = new AppointmentSlot();
        partialUpdatedAppointmentSlot.setId(appointmentSlot.getId());

        partialUpdatedAppointmentSlot.startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);

        restAppointmentSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointmentSlot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppointmentSlot))
            )
            .andExpect(status().isOk());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeUpdate);
        AppointmentSlot testAppointmentSlot = appointmentSlotList.get(appointmentSlotList.size() - 1);
        assertThat(testAppointmentSlot.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testAppointmentSlot.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    void fullUpdateAppointmentSlotWithPatch() throws Exception {
        // Initialize the database
        appointmentSlotRepository.save(appointmentSlot);

        int databaseSizeBeforeUpdate = appointmentSlotRepository.findAll().size();

        // Update the appointmentSlot using partial update
        AppointmentSlot partialUpdatedAppointmentSlot = new AppointmentSlot();
        partialUpdatedAppointmentSlot.setId(appointmentSlot.getId());

        partialUpdatedAppointmentSlot.startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);

        restAppointmentSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointmentSlot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppointmentSlot))
            )
            .andExpect(status().isOk());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeUpdate);
        AppointmentSlot testAppointmentSlot = appointmentSlotList.get(appointmentSlotList.size() - 1);
        assertThat(testAppointmentSlot.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testAppointmentSlot.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    void patchNonExistingAppointmentSlot() throws Exception {
        int databaseSizeBeforeUpdate = appointmentSlotRepository.findAll().size();
        appointmentSlot.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appointmentSlot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSlot))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAppointmentSlot() throws Exception {
        int databaseSizeBeforeUpdate = appointmentSlotRepository.findAll().size();
        appointmentSlot.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSlot))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAppointmentSlot() throws Exception {
        int databaseSizeBeforeUpdate = appointmentSlotRepository.findAll().size();
        appointmentSlot.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentSlotMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSlot))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAppointmentSlot() throws Exception {
        // Initialize the database
        appointmentSlotRepository.save(appointmentSlot);

        int databaseSizeBeforeDelete = appointmentSlotRepository.findAll().size();

        // Delete the appointmentSlot
        restAppointmentSlotMockMvc
            .perform(delete(ENTITY_API_URL_ID, appointmentSlot.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
