package com.elmarangao.trackblaze.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.elmarangao.trackblaze.IntegrationTest;
import com.elmarangao.trackblaze.domain.Athlete;
import com.elmarangao.trackblaze.domain.Event;
import com.elmarangao.trackblaze.domain.School;
import com.elmarangao.trackblaze.domain.enumeration.Gender;
import com.elmarangao.trackblaze.repository.AthleteRepository;
import com.elmarangao.trackblaze.service.AthleteService;
import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link AthleteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AthleteResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/athletes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AthleteRepository athleteRepository;

    @Mock
    private AthleteRepository athleteRepositoryMock;

    @Mock
    private AthleteService athleteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAthleteMockMvc;

    private Athlete athlete;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Athlete createEntity(EntityManager em) {
        Athlete athlete = new Athlete()
            .firstName(DEFAULT_FIRST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .birthDate(DEFAULT_BIRTH_DATE)
            .gender(DEFAULT_GENDER)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .userId(DEFAULT_USER_ID);
        // Add required entity
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            event = EventResourceIT.createEntity(em);
            em.persist(event);
            em.flush();
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        athlete.getEvents().add(event);
        // Add required entity
        School school;
        if (TestUtil.findAll(em, School.class).isEmpty()) {
            school = SchoolResourceIT.createEntity(em);
            em.persist(school);
            em.flush();
        } else {
            school = TestUtil.findAll(em, School.class).get(0);
        }
        athlete.setSchool(school);
        return athlete;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Athlete createUpdatedEntity(EntityManager em) {
        Athlete athlete = new Athlete()
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .userId(UPDATED_USER_ID);
        // Add required entity
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            event = EventResourceIT.createUpdatedEntity(em);
            em.persist(event);
            em.flush();
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        athlete.getEvents().add(event);
        // Add required entity
        School school;
        if (TestUtil.findAll(em, School.class).isEmpty()) {
            school = SchoolResourceIT.createUpdatedEntity(em);
            em.persist(school);
            em.flush();
        } else {
            school = TestUtil.findAll(em, School.class).get(0);
        }
        athlete.setSchool(school);
        return athlete;
    }

    @BeforeEach
    public void initTest() {
        athlete = createEntity(em);
    }

    @Test
    @Transactional
    void createAthlete() throws Exception {
        int databaseSizeBeforeCreate = athleteRepository.findAll().size();
        // Create the Athlete
        restAthleteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(athlete)))
            .andExpect(status().isCreated());

        // Validate the Athlete in the database
        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeCreate + 1);
        Athlete testAthlete = athleteList.get(athleteList.size() - 1);
        assertThat(testAthlete.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAthlete.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testAthlete.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAthlete.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testAthlete.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testAthlete.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testAthlete.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testAthlete.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void createAthleteWithExistingId() throws Exception {
        // Create the Athlete with an existing ID
        athlete.setId(1L);

        int databaseSizeBeforeCreate = athleteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAthleteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(athlete)))
            .andExpect(status().isBadRequest());

        // Validate the Athlete in the database
        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = athleteRepository.findAll().size();
        // set the field null
        athlete.setFirstName(null);

        // Create the Athlete, which fails.

        restAthleteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(athlete)))
            .andExpect(status().isBadRequest());

        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = athleteRepository.findAll().size();
        // set the field null
        athlete.setLastName(null);

        // Create the Athlete, which fails.

        restAthleteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(athlete)))
            .andExpect(status().isBadRequest());

        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBirthDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = athleteRepository.findAll().size();
        // set the field null
        athlete.setBirthDate(null);

        // Create the Athlete, which fails.

        restAthleteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(athlete)))
            .andExpect(status().isBadRequest());

        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = athleteRepository.findAll().size();
        // set the field null
        athlete.setGender(null);

        // Create the Athlete, which fails.

        restAthleteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(athlete)))
            .andExpect(status().isBadRequest());

        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAthletes() throws Exception {
        // Initialize the database
        athleteRepository.saveAndFlush(athlete);

        // Get all the athleteList
        restAthleteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(athlete.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAthletesWithEagerRelationshipsIsEnabled() throws Exception {
        when(athleteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAthleteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(athleteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAthletesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(athleteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAthleteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(athleteRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAthlete() throws Exception {
        // Initialize the database
        athleteRepository.saveAndFlush(athlete);

        // Get the athlete
        restAthleteMockMvc
            .perform(get(ENTITY_API_URL_ID, athlete.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(athlete.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAthlete() throws Exception {
        // Get the athlete
        restAthleteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAthlete() throws Exception {
        // Initialize the database
        athleteRepository.saveAndFlush(athlete);

        int databaseSizeBeforeUpdate = athleteRepository.findAll().size();

        // Update the athlete
        Athlete updatedAthlete = athleteRepository.findById(athlete.getId()).get();
        // Disconnect from session so that the updates on updatedAthlete are not directly saved in db
        em.detach(updatedAthlete);
        updatedAthlete
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .userId(UPDATED_USER_ID);

        restAthleteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAthlete.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAthlete))
            )
            .andExpect(status().isOk());

        // Validate the Athlete in the database
        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeUpdate);
        Athlete testAthlete = athleteList.get(athleteList.size() - 1);
        assertThat(testAthlete.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAthlete.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testAthlete.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAthlete.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testAthlete.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testAthlete.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testAthlete.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testAthlete.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void putNonExistingAthlete() throws Exception {
        int databaseSizeBeforeUpdate = athleteRepository.findAll().size();
        athlete.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAthleteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, athlete.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(athlete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Athlete in the database
        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAthlete() throws Exception {
        int databaseSizeBeforeUpdate = athleteRepository.findAll().size();
        athlete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAthleteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(athlete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Athlete in the database
        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAthlete() throws Exception {
        int databaseSizeBeforeUpdate = athleteRepository.findAll().size();
        athlete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAthleteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(athlete)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Athlete in the database
        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAthleteWithPatch() throws Exception {
        // Initialize the database
        athleteRepository.saveAndFlush(athlete);

        int databaseSizeBeforeUpdate = athleteRepository.findAll().size();

        // Update the athlete using partial update
        Athlete partialUpdatedAthlete = new Athlete();
        partialUpdatedAthlete.setId(athlete.getId());

        partialUpdatedAthlete
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .userId(UPDATED_USER_ID);

        restAthleteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAthlete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAthlete))
            )
            .andExpect(status().isOk());

        // Validate the Athlete in the database
        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeUpdate);
        Athlete testAthlete = athleteList.get(athleteList.size() - 1);
        assertThat(testAthlete.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAthlete.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testAthlete.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAthlete.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testAthlete.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testAthlete.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testAthlete.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testAthlete.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdateAthleteWithPatch() throws Exception {
        // Initialize the database
        athleteRepository.saveAndFlush(athlete);

        int databaseSizeBeforeUpdate = athleteRepository.findAll().size();

        // Update the athlete using partial update
        Athlete partialUpdatedAthlete = new Athlete();
        partialUpdatedAthlete.setId(athlete.getId());

        partialUpdatedAthlete
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .userId(UPDATED_USER_ID);

        restAthleteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAthlete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAthlete))
            )
            .andExpect(status().isOk());

        // Validate the Athlete in the database
        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeUpdate);
        Athlete testAthlete = athleteList.get(athleteList.size() - 1);
        assertThat(testAthlete.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAthlete.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testAthlete.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAthlete.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testAthlete.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testAthlete.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testAthlete.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testAthlete.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingAthlete() throws Exception {
        int databaseSizeBeforeUpdate = athleteRepository.findAll().size();
        athlete.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAthleteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, athlete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(athlete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Athlete in the database
        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAthlete() throws Exception {
        int databaseSizeBeforeUpdate = athleteRepository.findAll().size();
        athlete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAthleteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(athlete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Athlete in the database
        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAthlete() throws Exception {
        int databaseSizeBeforeUpdate = athleteRepository.findAll().size();
        athlete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAthleteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(athlete)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Athlete in the database
        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAthlete() throws Exception {
        // Initialize the database
        athleteRepository.saveAndFlush(athlete);

        int databaseSizeBeforeDelete = athleteRepository.findAll().size();

        // Delete the athlete
        restAthleteMockMvc
            .perform(delete(ENTITY_API_URL_ID, athlete.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
