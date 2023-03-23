package com.elmarangao.trackblaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.elmarangao.trackblaze.IntegrationTest;
import com.elmarangao.trackblaze.domain.Event;
import com.elmarangao.trackblaze.domain.TrackFieldEntry;
import com.elmarangao.trackblaze.domain.TrackFieldEntryAthleteDetails;
import com.elmarangao.trackblaze.domain.enumeration.Category;
import com.elmarangao.trackblaze.repository.TrackFieldEntryRepository;
import com.elmarangao.trackblaze.service.TrackFieldEntryService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TrackFieldEntryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TrackFieldEntryResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Category DEFAULT_CATEGORY = Category.ELEMENTARY_BOYS;
    private static final Category UPDATED_CATEGORY = Category.ELEMENTARY_GIRLS;

    private static final String ENTITY_API_URL = "/api/track-field-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrackFieldEntryRepository trackFieldEntryRepository;

    @Mock
    private TrackFieldEntryRepository trackFieldEntryRepositoryMock;

    @Mock
    private TrackFieldEntryService trackFieldEntryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrackFieldEntryMockMvc;

    private TrackFieldEntry trackFieldEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrackFieldEntry createEntity(EntityManager em) {
        TrackFieldEntry trackFieldEntry = new TrackFieldEntry().description(DEFAULT_DESCRIPTION).category(DEFAULT_CATEGORY);
        // Add required entity
        TrackFieldEntryAthleteDetails trackFieldEntryAthleteDetails;
        if (TestUtil.findAll(em, TrackFieldEntryAthleteDetails.class).isEmpty()) {
            trackFieldEntryAthleteDetails = TrackFieldEntryAthleteDetailsResourceIT.createEntity(em);
            em.persist(trackFieldEntryAthleteDetails);
            em.flush();
        } else {
            trackFieldEntryAthleteDetails = TestUtil.findAll(em, TrackFieldEntryAthleteDetails.class).get(0);
        }
        trackFieldEntry.getDetails().add(trackFieldEntryAthleteDetails);
        // Add required entity
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            event = EventResourceIT.createEntity(em);
            em.persist(event);
            em.flush();
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        trackFieldEntry.setEvent(event);
        return trackFieldEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrackFieldEntry createUpdatedEntity(EntityManager em) {
        TrackFieldEntry trackFieldEntry = new TrackFieldEntry().description(UPDATED_DESCRIPTION).category(UPDATED_CATEGORY);
        // Add required entity
        TrackFieldEntryAthleteDetails trackFieldEntryAthleteDetails;
        if (TestUtil.findAll(em, TrackFieldEntryAthleteDetails.class).isEmpty()) {
            trackFieldEntryAthleteDetails = TrackFieldEntryAthleteDetailsResourceIT.createUpdatedEntity(em);
            em.persist(trackFieldEntryAthleteDetails);
            em.flush();
        } else {
            trackFieldEntryAthleteDetails = TestUtil.findAll(em, TrackFieldEntryAthleteDetails.class).get(0);
        }
        trackFieldEntry.getDetails().add(trackFieldEntryAthleteDetails);
        // Add required entity
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            event = EventResourceIT.createUpdatedEntity(em);
            em.persist(event);
            em.flush();
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        trackFieldEntry.setEvent(event);
        return trackFieldEntry;
    }

    @BeforeEach
    public void initTest() {
        trackFieldEntry = createEntity(em);
    }

    @Test
    @Transactional
    void createTrackFieldEntry() throws Exception {
        int databaseSizeBeforeCreate = trackFieldEntryRepository.findAll().size();
        // Create the TrackFieldEntry
        restTrackFieldEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackFieldEntry))
            )
            .andExpect(status().isCreated());

        // Validate the TrackFieldEntry in the database
        List<TrackFieldEntry> trackFieldEntryList = trackFieldEntryRepository.findAll();
        assertThat(trackFieldEntryList).hasSize(databaseSizeBeforeCreate + 1);
        TrackFieldEntry testTrackFieldEntry = trackFieldEntryList.get(trackFieldEntryList.size() - 1);
        assertThat(testTrackFieldEntry.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTrackFieldEntry.getCategory()).isEqualTo(DEFAULT_CATEGORY);
    }

    @Test
    @Transactional
    void createTrackFieldEntryWithExistingId() throws Exception {
        // Create the TrackFieldEntry with an existing ID
        trackFieldEntry.setId(1L);

        int databaseSizeBeforeCreate = trackFieldEntryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrackFieldEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackFieldEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackFieldEntry in the database
        List<TrackFieldEntry> trackFieldEntryList = trackFieldEntryRepository.findAll();
        assertThat(trackFieldEntryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackFieldEntryRepository.findAll().size();
        // set the field null
        trackFieldEntry.setDescription(null);

        // Create the TrackFieldEntry, which fails.

        restTrackFieldEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackFieldEntry))
            )
            .andExpect(status().isBadRequest());

        List<TrackFieldEntry> trackFieldEntryList = trackFieldEntryRepository.findAll();
        assertThat(trackFieldEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackFieldEntryRepository.findAll().size();
        // set the field null
        trackFieldEntry.setCategory(null);

        // Create the TrackFieldEntry, which fails.

        restTrackFieldEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackFieldEntry))
            )
            .andExpect(status().isBadRequest());

        List<TrackFieldEntry> trackFieldEntryList = trackFieldEntryRepository.findAll();
        assertThat(trackFieldEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTrackFieldEntries() throws Exception {
        // Initialize the database
        trackFieldEntryRepository.saveAndFlush(trackFieldEntry);

        // Get all the trackFieldEntryList
        restTrackFieldEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trackFieldEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTrackFieldEntriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(trackFieldEntryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrackFieldEntryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(trackFieldEntryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTrackFieldEntriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(trackFieldEntryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrackFieldEntryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(trackFieldEntryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTrackFieldEntry() throws Exception {
        // Initialize the database
        trackFieldEntryRepository.saveAndFlush(trackFieldEntry);

        // Get the trackFieldEntry
        restTrackFieldEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, trackFieldEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trackFieldEntry.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTrackFieldEntry() throws Exception {
        // Get the trackFieldEntry
        restTrackFieldEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrackFieldEntry() throws Exception {
        // Initialize the database
        trackFieldEntryRepository.saveAndFlush(trackFieldEntry);

        int databaseSizeBeforeUpdate = trackFieldEntryRepository.findAll().size();

        // Update the trackFieldEntry
        TrackFieldEntry updatedTrackFieldEntry = trackFieldEntryRepository.findById(trackFieldEntry.getId()).get();
        // Disconnect from session so that the updates on updatedTrackFieldEntry are not directly saved in db
        em.detach(updatedTrackFieldEntry);
        updatedTrackFieldEntry.description(UPDATED_DESCRIPTION).category(UPDATED_CATEGORY);

        restTrackFieldEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrackFieldEntry.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTrackFieldEntry))
            )
            .andExpect(status().isOk());

        // Validate the TrackFieldEntry in the database
        List<TrackFieldEntry> trackFieldEntryList = trackFieldEntryRepository.findAll();
        assertThat(trackFieldEntryList).hasSize(databaseSizeBeforeUpdate);
        TrackFieldEntry testTrackFieldEntry = trackFieldEntryList.get(trackFieldEntryList.size() - 1);
        assertThat(testTrackFieldEntry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTrackFieldEntry.getCategory()).isEqualTo(UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void putNonExistingTrackFieldEntry() throws Exception {
        int databaseSizeBeforeUpdate = trackFieldEntryRepository.findAll().size();
        trackFieldEntry.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrackFieldEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trackFieldEntry.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackFieldEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackFieldEntry in the database
        List<TrackFieldEntry> trackFieldEntryList = trackFieldEntryRepository.findAll();
        assertThat(trackFieldEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrackFieldEntry() throws Exception {
        int databaseSizeBeforeUpdate = trackFieldEntryRepository.findAll().size();
        trackFieldEntry.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackFieldEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackFieldEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackFieldEntry in the database
        List<TrackFieldEntry> trackFieldEntryList = trackFieldEntryRepository.findAll();
        assertThat(trackFieldEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrackFieldEntry() throws Exception {
        int databaseSizeBeforeUpdate = trackFieldEntryRepository.findAll().size();
        trackFieldEntry.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackFieldEntryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackFieldEntry))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrackFieldEntry in the database
        List<TrackFieldEntry> trackFieldEntryList = trackFieldEntryRepository.findAll();
        assertThat(trackFieldEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrackFieldEntryWithPatch() throws Exception {
        // Initialize the database
        trackFieldEntryRepository.saveAndFlush(trackFieldEntry);

        int databaseSizeBeforeUpdate = trackFieldEntryRepository.findAll().size();

        // Update the trackFieldEntry using partial update
        TrackFieldEntry partialUpdatedTrackFieldEntry = new TrackFieldEntry();
        partialUpdatedTrackFieldEntry.setId(trackFieldEntry.getId());

        partialUpdatedTrackFieldEntry.description(UPDATED_DESCRIPTION).category(UPDATED_CATEGORY);

        restTrackFieldEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrackFieldEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrackFieldEntry))
            )
            .andExpect(status().isOk());

        // Validate the TrackFieldEntry in the database
        List<TrackFieldEntry> trackFieldEntryList = trackFieldEntryRepository.findAll();
        assertThat(trackFieldEntryList).hasSize(databaseSizeBeforeUpdate);
        TrackFieldEntry testTrackFieldEntry = trackFieldEntryList.get(trackFieldEntryList.size() - 1);
        assertThat(testTrackFieldEntry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTrackFieldEntry.getCategory()).isEqualTo(UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void fullUpdateTrackFieldEntryWithPatch() throws Exception {
        // Initialize the database
        trackFieldEntryRepository.saveAndFlush(trackFieldEntry);

        int databaseSizeBeforeUpdate = trackFieldEntryRepository.findAll().size();

        // Update the trackFieldEntry using partial update
        TrackFieldEntry partialUpdatedTrackFieldEntry = new TrackFieldEntry();
        partialUpdatedTrackFieldEntry.setId(trackFieldEntry.getId());

        partialUpdatedTrackFieldEntry.description(UPDATED_DESCRIPTION).category(UPDATED_CATEGORY);

        restTrackFieldEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrackFieldEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrackFieldEntry))
            )
            .andExpect(status().isOk());

        // Validate the TrackFieldEntry in the database
        List<TrackFieldEntry> trackFieldEntryList = trackFieldEntryRepository.findAll();
        assertThat(trackFieldEntryList).hasSize(databaseSizeBeforeUpdate);
        TrackFieldEntry testTrackFieldEntry = trackFieldEntryList.get(trackFieldEntryList.size() - 1);
        assertThat(testTrackFieldEntry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTrackFieldEntry.getCategory()).isEqualTo(UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void patchNonExistingTrackFieldEntry() throws Exception {
        int databaseSizeBeforeUpdate = trackFieldEntryRepository.findAll().size();
        trackFieldEntry.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrackFieldEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trackFieldEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackFieldEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackFieldEntry in the database
        List<TrackFieldEntry> trackFieldEntryList = trackFieldEntryRepository.findAll();
        assertThat(trackFieldEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrackFieldEntry() throws Exception {
        int databaseSizeBeforeUpdate = trackFieldEntryRepository.findAll().size();
        trackFieldEntry.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackFieldEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackFieldEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackFieldEntry in the database
        List<TrackFieldEntry> trackFieldEntryList = trackFieldEntryRepository.findAll();
        assertThat(trackFieldEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrackFieldEntry() throws Exception {
        int databaseSizeBeforeUpdate = trackFieldEntryRepository.findAll().size();
        trackFieldEntry.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackFieldEntryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackFieldEntry))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrackFieldEntry in the database
        List<TrackFieldEntry> trackFieldEntryList = trackFieldEntryRepository.findAll();
        assertThat(trackFieldEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrackFieldEntry() throws Exception {
        // Initialize the database
        trackFieldEntryRepository.saveAndFlush(trackFieldEntry);

        int databaseSizeBeforeDelete = trackFieldEntryRepository.findAll().size();

        // Delete the trackFieldEntry
        restTrackFieldEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, trackFieldEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TrackFieldEntry> trackFieldEntryList = trackFieldEntryRepository.findAll();
        assertThat(trackFieldEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
