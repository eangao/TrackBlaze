package com.elmarangao.trackblaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.elmarangao.trackblaze.IntegrationTest;
import com.elmarangao.trackblaze.domain.TrackFieldEntryAthleteDetails;
import com.elmarangao.trackblaze.repository.TrackFieldEntryAthleteDetailsRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TrackFieldEntryAthleteDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrackFieldEntryAthleteDetailsResourceIT {

    private static final Integer DEFAULT_RANK = 1;
    private static final Integer UPDATED_RANK = 2;

    private static final String DEFAULT_TIME_HEIGHT_DISTANCE = "AAAAAAAAAA";
    private static final String UPDATED_TIME_HEIGHT_DISTANCE = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/track-field-entry-athlete-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrackFieldEntryAthleteDetailsRepository trackFieldEntryAthleteDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrackFieldEntryAthleteDetailsMockMvc;

    private TrackFieldEntryAthleteDetails trackFieldEntryAthleteDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrackFieldEntryAthleteDetails createEntity(EntityManager em) {
        TrackFieldEntryAthleteDetails trackFieldEntryAthleteDetails = new TrackFieldEntryAthleteDetails()
            .rank(DEFAULT_RANK)
            .timeHeightDistance(DEFAULT_TIME_HEIGHT_DISTANCE)
            .remarks(DEFAULT_REMARKS);
        return trackFieldEntryAthleteDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrackFieldEntryAthleteDetails createUpdatedEntity(EntityManager em) {
        TrackFieldEntryAthleteDetails trackFieldEntryAthleteDetails = new TrackFieldEntryAthleteDetails()
            .rank(UPDATED_RANK)
            .timeHeightDistance(UPDATED_TIME_HEIGHT_DISTANCE)
            .remarks(UPDATED_REMARKS);
        return trackFieldEntryAthleteDetails;
    }

    @BeforeEach
    public void initTest() {
        trackFieldEntryAthleteDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createTrackFieldEntryAthleteDetails() throws Exception {
        int databaseSizeBeforeCreate = trackFieldEntryAthleteDetailsRepository.findAll().size();
        // Create the TrackFieldEntryAthleteDetails
        restTrackFieldEntryAthleteDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackFieldEntryAthleteDetails))
            )
            .andExpect(status().isCreated());

        // Validate the TrackFieldEntryAthleteDetails in the database
        List<TrackFieldEntryAthleteDetails> trackFieldEntryAthleteDetailsList = trackFieldEntryAthleteDetailsRepository.findAll();
        assertThat(trackFieldEntryAthleteDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        TrackFieldEntryAthleteDetails testTrackFieldEntryAthleteDetails = trackFieldEntryAthleteDetailsList.get(
            trackFieldEntryAthleteDetailsList.size() - 1
        );
        assertThat(testTrackFieldEntryAthleteDetails.getRank()).isEqualTo(DEFAULT_RANK);
        assertThat(testTrackFieldEntryAthleteDetails.getTimeHeightDistance()).isEqualTo(DEFAULT_TIME_HEIGHT_DISTANCE);
        assertThat(testTrackFieldEntryAthleteDetails.getRemarks()).isEqualTo(DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void createTrackFieldEntryAthleteDetailsWithExistingId() throws Exception {
        // Create the TrackFieldEntryAthleteDetails with an existing ID
        trackFieldEntryAthleteDetails.setId(1L);

        int databaseSizeBeforeCreate = trackFieldEntryAthleteDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrackFieldEntryAthleteDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackFieldEntryAthleteDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackFieldEntryAthleteDetails in the database
        List<TrackFieldEntryAthleteDetails> trackFieldEntryAthleteDetailsList = trackFieldEntryAthleteDetailsRepository.findAll();
        assertThat(trackFieldEntryAthleteDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrackFieldEntryAthleteDetails() throws Exception {
        // Initialize the database
        trackFieldEntryAthleteDetailsRepository.saveAndFlush(trackFieldEntryAthleteDetails);

        // Get all the trackFieldEntryAthleteDetailsList
        restTrackFieldEntryAthleteDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trackFieldEntryAthleteDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK)))
            .andExpect(jsonPath("$.[*].timeHeightDistance").value(hasItem(DEFAULT_TIME_HEIGHT_DISTANCE)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));
    }

    @Test
    @Transactional
    void getTrackFieldEntryAthleteDetails() throws Exception {
        // Initialize the database
        trackFieldEntryAthleteDetailsRepository.saveAndFlush(trackFieldEntryAthleteDetails);

        // Get the trackFieldEntryAthleteDetails
        restTrackFieldEntryAthleteDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, trackFieldEntryAthleteDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trackFieldEntryAthleteDetails.getId().intValue()))
            .andExpect(jsonPath("$.rank").value(DEFAULT_RANK))
            .andExpect(jsonPath("$.timeHeightDistance").value(DEFAULT_TIME_HEIGHT_DISTANCE))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS));
    }

    @Test
    @Transactional
    void getNonExistingTrackFieldEntryAthleteDetails() throws Exception {
        // Get the trackFieldEntryAthleteDetails
        restTrackFieldEntryAthleteDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrackFieldEntryAthleteDetails() throws Exception {
        // Initialize the database
        trackFieldEntryAthleteDetailsRepository.saveAndFlush(trackFieldEntryAthleteDetails);

        int databaseSizeBeforeUpdate = trackFieldEntryAthleteDetailsRepository.findAll().size();

        // Update the trackFieldEntryAthleteDetails
        TrackFieldEntryAthleteDetails updatedTrackFieldEntryAthleteDetails = trackFieldEntryAthleteDetailsRepository
            .findById(trackFieldEntryAthleteDetails.getId())
            .get();
        // Disconnect from session so that the updates on updatedTrackFieldEntryAthleteDetails are not directly saved in db
        em.detach(updatedTrackFieldEntryAthleteDetails);
        updatedTrackFieldEntryAthleteDetails.rank(UPDATED_RANK).timeHeightDistance(UPDATED_TIME_HEIGHT_DISTANCE).remarks(UPDATED_REMARKS);

        restTrackFieldEntryAthleteDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrackFieldEntryAthleteDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTrackFieldEntryAthleteDetails))
            )
            .andExpect(status().isOk());

        // Validate the TrackFieldEntryAthleteDetails in the database
        List<TrackFieldEntryAthleteDetails> trackFieldEntryAthleteDetailsList = trackFieldEntryAthleteDetailsRepository.findAll();
        assertThat(trackFieldEntryAthleteDetailsList).hasSize(databaseSizeBeforeUpdate);
        TrackFieldEntryAthleteDetails testTrackFieldEntryAthleteDetails = trackFieldEntryAthleteDetailsList.get(
            trackFieldEntryAthleteDetailsList.size() - 1
        );
        assertThat(testTrackFieldEntryAthleteDetails.getRank()).isEqualTo(UPDATED_RANK);
        assertThat(testTrackFieldEntryAthleteDetails.getTimeHeightDistance()).isEqualTo(UPDATED_TIME_HEIGHT_DISTANCE);
        assertThat(testTrackFieldEntryAthleteDetails.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void putNonExistingTrackFieldEntryAthleteDetails() throws Exception {
        int databaseSizeBeforeUpdate = trackFieldEntryAthleteDetailsRepository.findAll().size();
        trackFieldEntryAthleteDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrackFieldEntryAthleteDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trackFieldEntryAthleteDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackFieldEntryAthleteDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackFieldEntryAthleteDetails in the database
        List<TrackFieldEntryAthleteDetails> trackFieldEntryAthleteDetailsList = trackFieldEntryAthleteDetailsRepository.findAll();
        assertThat(trackFieldEntryAthleteDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrackFieldEntryAthleteDetails() throws Exception {
        int databaseSizeBeforeUpdate = trackFieldEntryAthleteDetailsRepository.findAll().size();
        trackFieldEntryAthleteDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackFieldEntryAthleteDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackFieldEntryAthleteDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackFieldEntryAthleteDetails in the database
        List<TrackFieldEntryAthleteDetails> trackFieldEntryAthleteDetailsList = trackFieldEntryAthleteDetailsRepository.findAll();
        assertThat(trackFieldEntryAthleteDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrackFieldEntryAthleteDetails() throws Exception {
        int databaseSizeBeforeUpdate = trackFieldEntryAthleteDetailsRepository.findAll().size();
        trackFieldEntryAthleteDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackFieldEntryAthleteDetailsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackFieldEntryAthleteDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrackFieldEntryAthleteDetails in the database
        List<TrackFieldEntryAthleteDetails> trackFieldEntryAthleteDetailsList = trackFieldEntryAthleteDetailsRepository.findAll();
        assertThat(trackFieldEntryAthleteDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrackFieldEntryAthleteDetailsWithPatch() throws Exception {
        // Initialize the database
        trackFieldEntryAthleteDetailsRepository.saveAndFlush(trackFieldEntryAthleteDetails);

        int databaseSizeBeforeUpdate = trackFieldEntryAthleteDetailsRepository.findAll().size();

        // Update the trackFieldEntryAthleteDetails using partial update
        TrackFieldEntryAthleteDetails partialUpdatedTrackFieldEntryAthleteDetails = new TrackFieldEntryAthleteDetails();
        partialUpdatedTrackFieldEntryAthleteDetails.setId(trackFieldEntryAthleteDetails.getId());

        partialUpdatedTrackFieldEntryAthleteDetails.timeHeightDistance(UPDATED_TIME_HEIGHT_DISTANCE).remarks(UPDATED_REMARKS);

        restTrackFieldEntryAthleteDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrackFieldEntryAthleteDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrackFieldEntryAthleteDetails))
            )
            .andExpect(status().isOk());

        // Validate the TrackFieldEntryAthleteDetails in the database
        List<TrackFieldEntryAthleteDetails> trackFieldEntryAthleteDetailsList = trackFieldEntryAthleteDetailsRepository.findAll();
        assertThat(trackFieldEntryAthleteDetailsList).hasSize(databaseSizeBeforeUpdate);
        TrackFieldEntryAthleteDetails testTrackFieldEntryAthleteDetails = trackFieldEntryAthleteDetailsList.get(
            trackFieldEntryAthleteDetailsList.size() - 1
        );
        assertThat(testTrackFieldEntryAthleteDetails.getRank()).isEqualTo(DEFAULT_RANK);
        assertThat(testTrackFieldEntryAthleteDetails.getTimeHeightDistance()).isEqualTo(UPDATED_TIME_HEIGHT_DISTANCE);
        assertThat(testTrackFieldEntryAthleteDetails.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void fullUpdateTrackFieldEntryAthleteDetailsWithPatch() throws Exception {
        // Initialize the database
        trackFieldEntryAthleteDetailsRepository.saveAndFlush(trackFieldEntryAthleteDetails);

        int databaseSizeBeforeUpdate = trackFieldEntryAthleteDetailsRepository.findAll().size();

        // Update the trackFieldEntryAthleteDetails using partial update
        TrackFieldEntryAthleteDetails partialUpdatedTrackFieldEntryAthleteDetails = new TrackFieldEntryAthleteDetails();
        partialUpdatedTrackFieldEntryAthleteDetails.setId(trackFieldEntryAthleteDetails.getId());

        partialUpdatedTrackFieldEntryAthleteDetails
            .rank(UPDATED_RANK)
            .timeHeightDistance(UPDATED_TIME_HEIGHT_DISTANCE)
            .remarks(UPDATED_REMARKS);

        restTrackFieldEntryAthleteDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrackFieldEntryAthleteDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrackFieldEntryAthleteDetails))
            )
            .andExpect(status().isOk());

        // Validate the TrackFieldEntryAthleteDetails in the database
        List<TrackFieldEntryAthleteDetails> trackFieldEntryAthleteDetailsList = trackFieldEntryAthleteDetailsRepository.findAll();
        assertThat(trackFieldEntryAthleteDetailsList).hasSize(databaseSizeBeforeUpdate);
        TrackFieldEntryAthleteDetails testTrackFieldEntryAthleteDetails = trackFieldEntryAthleteDetailsList.get(
            trackFieldEntryAthleteDetailsList.size() - 1
        );
        assertThat(testTrackFieldEntryAthleteDetails.getRank()).isEqualTo(UPDATED_RANK);
        assertThat(testTrackFieldEntryAthleteDetails.getTimeHeightDistance()).isEqualTo(UPDATED_TIME_HEIGHT_DISTANCE);
        assertThat(testTrackFieldEntryAthleteDetails.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void patchNonExistingTrackFieldEntryAthleteDetails() throws Exception {
        int databaseSizeBeforeUpdate = trackFieldEntryAthleteDetailsRepository.findAll().size();
        trackFieldEntryAthleteDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrackFieldEntryAthleteDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trackFieldEntryAthleteDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackFieldEntryAthleteDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackFieldEntryAthleteDetails in the database
        List<TrackFieldEntryAthleteDetails> trackFieldEntryAthleteDetailsList = trackFieldEntryAthleteDetailsRepository.findAll();
        assertThat(trackFieldEntryAthleteDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrackFieldEntryAthleteDetails() throws Exception {
        int databaseSizeBeforeUpdate = trackFieldEntryAthleteDetailsRepository.findAll().size();
        trackFieldEntryAthleteDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackFieldEntryAthleteDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackFieldEntryAthleteDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackFieldEntryAthleteDetails in the database
        List<TrackFieldEntryAthleteDetails> trackFieldEntryAthleteDetailsList = trackFieldEntryAthleteDetailsRepository.findAll();
        assertThat(trackFieldEntryAthleteDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrackFieldEntryAthleteDetails() throws Exception {
        int databaseSizeBeforeUpdate = trackFieldEntryAthleteDetailsRepository.findAll().size();
        trackFieldEntryAthleteDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackFieldEntryAthleteDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackFieldEntryAthleteDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrackFieldEntryAthleteDetails in the database
        List<TrackFieldEntryAthleteDetails> trackFieldEntryAthleteDetailsList = trackFieldEntryAthleteDetailsRepository.findAll();
        assertThat(trackFieldEntryAthleteDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrackFieldEntryAthleteDetails() throws Exception {
        // Initialize the database
        trackFieldEntryAthleteDetailsRepository.saveAndFlush(trackFieldEntryAthleteDetails);

        int databaseSizeBeforeDelete = trackFieldEntryAthleteDetailsRepository.findAll().size();

        // Delete the trackFieldEntryAthleteDetails
        restTrackFieldEntryAthleteDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, trackFieldEntryAthleteDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TrackFieldEntryAthleteDetails> trackFieldEntryAthleteDetailsList = trackFieldEntryAthleteDetailsRepository.findAll();
        assertThat(trackFieldEntryAthleteDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
