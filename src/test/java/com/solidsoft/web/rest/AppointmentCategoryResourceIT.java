package com.solidsoft.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.solidsoft.IntegrationTest;
import com.solidsoft.domain.AppointmentCategory;
import com.solidsoft.repository.AppointmentCategoryRepository;
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
 * Integration tests for the {@link AppointmentCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppointmentCategoryResourceIT {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_HOSTS = "AAAAAAAAAA";
    private static final String UPDATED_HOSTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/appointment-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AppointmentCategoryRepository appointmentCategoryRepository;

    @Autowired
    private MockMvc restAppointmentCategoryMockMvc;

    private AppointmentCategory appointmentCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppointmentCategory createEntity() {
        AppointmentCategory appointmentCategory = new AppointmentCategory().categoryName(DEFAULT_CATEGORY_NAME).hosts(DEFAULT_HOSTS);
        return appointmentCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppointmentCategory createUpdatedEntity() {
        AppointmentCategory appointmentCategory = new AppointmentCategory().categoryName(UPDATED_CATEGORY_NAME).hosts(UPDATED_HOSTS);
        return appointmentCategory;
    }

    @BeforeEach
    public void initTest() {
        appointmentCategoryRepository.deleteAll();
        appointmentCategory = createEntity();
    }

    @Test
    void createAppointmentCategory() throws Exception {
        int databaseSizeBeforeCreate = appointmentCategoryRepository.findAll().size();
        // Create the AppointmentCategory
        restAppointmentCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentCategory))
            )
            .andExpect(status().isCreated());

        // Validate the AppointmentCategory in the database
        List<AppointmentCategory> appointmentCategoryList = appointmentCategoryRepository.findAll();
        assertThat(appointmentCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        AppointmentCategory testAppointmentCategory = appointmentCategoryList.get(appointmentCategoryList.size() - 1);
        assertThat(testAppointmentCategory.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
        assertThat(testAppointmentCategory.getHosts()).isEqualTo(DEFAULT_HOSTS);
    }

    @Test
    void createAppointmentCategoryWithExistingId() throws Exception {
        // Create the AppointmentCategory with an existing ID
        appointmentCategory.setId("existing_id");

        int databaseSizeBeforeCreate = appointmentCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppointmentCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentCategory in the database
        List<AppointmentCategory> appointmentCategoryList = appointmentCategoryRepository.findAll();
        assertThat(appointmentCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllAppointmentCategories() throws Exception {
        // Initialize the database
        appointmentCategoryRepository.save(appointmentCategory);

        // Get all the appointmentCategoryList
        restAppointmentCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointmentCategory.getId())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].hosts").value(hasItem(DEFAULT_HOSTS)));
    }

    @Test
    void getAppointmentCategory() throws Exception {
        // Initialize the database
        appointmentCategoryRepository.save(appointmentCategory);

        // Get the appointmentCategory
        restAppointmentCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, appointmentCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appointmentCategory.getId()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME))
            .andExpect(jsonPath("$.hosts").value(DEFAULT_HOSTS));
    }

    @Test
    void getNonExistingAppointmentCategory() throws Exception {
        // Get the appointmentCategory
        restAppointmentCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewAppointmentCategory() throws Exception {
        // Initialize the database
        appointmentCategoryRepository.save(appointmentCategory);

        int databaseSizeBeforeUpdate = appointmentCategoryRepository.findAll().size();

        // Update the appointmentCategory
        AppointmentCategory updatedAppointmentCategory = appointmentCategoryRepository.findById(appointmentCategory.getId()).get();
        updatedAppointmentCategory.categoryName(UPDATED_CATEGORY_NAME).hosts(UPDATED_HOSTS);

        restAppointmentCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAppointmentCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAppointmentCategory))
            )
            .andExpect(status().isOk());

        // Validate the AppointmentCategory in the database
        List<AppointmentCategory> appointmentCategoryList = appointmentCategoryRepository.findAll();
        assertThat(appointmentCategoryList).hasSize(databaseSizeBeforeUpdate);
        AppointmentCategory testAppointmentCategory = appointmentCategoryList.get(appointmentCategoryList.size() - 1);
        assertThat(testAppointmentCategory.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testAppointmentCategory.getHosts()).isEqualTo(UPDATED_HOSTS);
    }

    @Test
    void putNonExistingAppointmentCategory() throws Exception {
        int databaseSizeBeforeUpdate = appointmentCategoryRepository.findAll().size();
        appointmentCategory.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appointmentCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentCategory in the database
        List<AppointmentCategory> appointmentCategoryList = appointmentCategoryRepository.findAll();
        assertThat(appointmentCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAppointmentCategory() throws Exception {
        int databaseSizeBeforeUpdate = appointmentCategoryRepository.findAll().size();
        appointmentCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentCategory in the database
        List<AppointmentCategory> appointmentCategoryList = appointmentCategoryRepository.findAll();
        assertThat(appointmentCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAppointmentCategory() throws Exception {
        int databaseSizeBeforeUpdate = appointmentCategoryRepository.findAll().size();
        appointmentCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppointmentCategory in the database
        List<AppointmentCategory> appointmentCategoryList = appointmentCategoryRepository.findAll();
        assertThat(appointmentCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAppointmentCategoryWithPatch() throws Exception {
        // Initialize the database
        appointmentCategoryRepository.save(appointmentCategory);

        int databaseSizeBeforeUpdate = appointmentCategoryRepository.findAll().size();

        // Update the appointmentCategory using partial update
        AppointmentCategory partialUpdatedAppointmentCategory = new AppointmentCategory();
        partialUpdatedAppointmentCategory.setId(appointmentCategory.getId());

        partialUpdatedAppointmentCategory.categoryName(UPDATED_CATEGORY_NAME).hosts(UPDATED_HOSTS);

        restAppointmentCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointmentCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppointmentCategory))
            )
            .andExpect(status().isOk());

        // Validate the AppointmentCategory in the database
        List<AppointmentCategory> appointmentCategoryList = appointmentCategoryRepository.findAll();
        assertThat(appointmentCategoryList).hasSize(databaseSizeBeforeUpdate);
        AppointmentCategory testAppointmentCategory = appointmentCategoryList.get(appointmentCategoryList.size() - 1);
        assertThat(testAppointmentCategory.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testAppointmentCategory.getHosts()).isEqualTo(UPDATED_HOSTS);
    }

    @Test
    void fullUpdateAppointmentCategoryWithPatch() throws Exception {
        // Initialize the database
        appointmentCategoryRepository.save(appointmentCategory);

        int databaseSizeBeforeUpdate = appointmentCategoryRepository.findAll().size();

        // Update the appointmentCategory using partial update
        AppointmentCategory partialUpdatedAppointmentCategory = new AppointmentCategory();
        partialUpdatedAppointmentCategory.setId(appointmentCategory.getId());

        partialUpdatedAppointmentCategory.categoryName(UPDATED_CATEGORY_NAME).hosts(UPDATED_HOSTS);

        restAppointmentCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointmentCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppointmentCategory))
            )
            .andExpect(status().isOk());

        // Validate the AppointmentCategory in the database
        List<AppointmentCategory> appointmentCategoryList = appointmentCategoryRepository.findAll();
        assertThat(appointmentCategoryList).hasSize(databaseSizeBeforeUpdate);
        AppointmentCategory testAppointmentCategory = appointmentCategoryList.get(appointmentCategoryList.size() - 1);
        assertThat(testAppointmentCategory.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testAppointmentCategory.getHosts()).isEqualTo(UPDATED_HOSTS);
    }

    @Test
    void patchNonExistingAppointmentCategory() throws Exception {
        int databaseSizeBeforeUpdate = appointmentCategoryRepository.findAll().size();
        appointmentCategory.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appointmentCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentCategory in the database
        List<AppointmentCategory> appointmentCategoryList = appointmentCategoryRepository.findAll();
        assertThat(appointmentCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAppointmentCategory() throws Exception {
        int databaseSizeBeforeUpdate = appointmentCategoryRepository.findAll().size();
        appointmentCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentCategory in the database
        List<AppointmentCategory> appointmentCategoryList = appointmentCategoryRepository.findAll();
        assertThat(appointmentCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAppointmentCategory() throws Exception {
        int databaseSizeBeforeUpdate = appointmentCategoryRepository.findAll().size();
        appointmentCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppointmentCategory in the database
        List<AppointmentCategory> appointmentCategoryList = appointmentCategoryRepository.findAll();
        assertThat(appointmentCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAppointmentCategory() throws Exception {
        // Initialize the database
        appointmentCategoryRepository.save(appointmentCategory);

        int databaseSizeBeforeDelete = appointmentCategoryRepository.findAll().size();

        // Delete the appointmentCategory
        restAppointmentCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, appointmentCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppointmentCategory> appointmentCategoryList = appointmentCategoryRepository.findAll();
        assertThat(appointmentCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
