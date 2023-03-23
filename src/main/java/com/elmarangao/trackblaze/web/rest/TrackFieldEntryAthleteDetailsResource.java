package com.elmarangao.trackblaze.web.rest;

import com.elmarangao.trackblaze.domain.TrackFieldEntryAthleteDetails;
import com.elmarangao.trackblaze.repository.TrackFieldEntryAthleteDetailsRepository;
import com.elmarangao.trackblaze.service.TrackFieldEntryAthleteDetailsService;
import com.elmarangao.trackblaze.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.elmarangao.trackblaze.domain.TrackFieldEntryAthleteDetails}.
 */
@RestController
@RequestMapping("/api")
public class TrackFieldEntryAthleteDetailsResource {

    private final Logger log = LoggerFactory.getLogger(TrackFieldEntryAthleteDetailsResource.class);

    private static final String ENTITY_NAME = "trackFieldEntryAthleteDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrackFieldEntryAthleteDetailsService trackFieldEntryAthleteDetailsService;

    private final TrackFieldEntryAthleteDetailsRepository trackFieldEntryAthleteDetailsRepository;

    public TrackFieldEntryAthleteDetailsResource(
        TrackFieldEntryAthleteDetailsService trackFieldEntryAthleteDetailsService,
        TrackFieldEntryAthleteDetailsRepository trackFieldEntryAthleteDetailsRepository
    ) {
        this.trackFieldEntryAthleteDetailsService = trackFieldEntryAthleteDetailsService;
        this.trackFieldEntryAthleteDetailsRepository = trackFieldEntryAthleteDetailsRepository;
    }

    /**
     * {@code POST  /track-field-entry-athlete-details} : Create a new trackFieldEntryAthleteDetails.
     *
     * @param trackFieldEntryAthleteDetails the trackFieldEntryAthleteDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trackFieldEntryAthleteDetails, or with status {@code 400 (Bad Request)} if the trackFieldEntryAthleteDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/track-field-entry-athlete-details")
    public ResponseEntity<TrackFieldEntryAthleteDetails> createTrackFieldEntryAthleteDetails(
        @RequestBody TrackFieldEntryAthleteDetails trackFieldEntryAthleteDetails
    ) throws URISyntaxException {
        log.debug("REST request to save TrackFieldEntryAthleteDetails : {}", trackFieldEntryAthleteDetails);
        if (trackFieldEntryAthleteDetails.getId() != null) {
            throw new BadRequestAlertException("A new trackFieldEntryAthleteDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrackFieldEntryAthleteDetails result = trackFieldEntryAthleteDetailsService.save(trackFieldEntryAthleteDetails);
        return ResponseEntity
            .created(new URI("/api/track-field-entry-athlete-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /track-field-entry-athlete-details/:id} : Updates an existing trackFieldEntryAthleteDetails.
     *
     * @param id the id of the trackFieldEntryAthleteDetails to save.
     * @param trackFieldEntryAthleteDetails the trackFieldEntryAthleteDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trackFieldEntryAthleteDetails,
     * or with status {@code 400 (Bad Request)} if the trackFieldEntryAthleteDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trackFieldEntryAthleteDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/track-field-entry-athlete-details/{id}")
    public ResponseEntity<TrackFieldEntryAthleteDetails> updateTrackFieldEntryAthleteDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrackFieldEntryAthleteDetails trackFieldEntryAthleteDetails
    ) throws URISyntaxException {
        log.debug("REST request to update TrackFieldEntryAthleteDetails : {}, {}", id, trackFieldEntryAthleteDetails);
        if (trackFieldEntryAthleteDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trackFieldEntryAthleteDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trackFieldEntryAthleteDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TrackFieldEntryAthleteDetails result = trackFieldEntryAthleteDetailsService.update(trackFieldEntryAthleteDetails);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trackFieldEntryAthleteDetails.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /track-field-entry-athlete-details/:id} : Partial updates given fields of an existing trackFieldEntryAthleteDetails, field will ignore if it is null
     *
     * @param id the id of the trackFieldEntryAthleteDetails to save.
     * @param trackFieldEntryAthleteDetails the trackFieldEntryAthleteDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trackFieldEntryAthleteDetails,
     * or with status {@code 400 (Bad Request)} if the trackFieldEntryAthleteDetails is not valid,
     * or with status {@code 404 (Not Found)} if the trackFieldEntryAthleteDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the trackFieldEntryAthleteDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/track-field-entry-athlete-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrackFieldEntryAthleteDetails> partialUpdateTrackFieldEntryAthleteDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrackFieldEntryAthleteDetails trackFieldEntryAthleteDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update TrackFieldEntryAthleteDetails partially : {}, {}", id, trackFieldEntryAthleteDetails);
        if (trackFieldEntryAthleteDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trackFieldEntryAthleteDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trackFieldEntryAthleteDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrackFieldEntryAthleteDetails> result = trackFieldEntryAthleteDetailsService.partialUpdate(trackFieldEntryAthleteDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trackFieldEntryAthleteDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /track-field-entry-athlete-details} : get all the trackFieldEntryAthleteDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trackFieldEntryAthleteDetails in body.
     */
    @GetMapping("/track-field-entry-athlete-details")
    public ResponseEntity<List<TrackFieldEntryAthleteDetails>> getAllTrackFieldEntryAthleteDetails(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of TrackFieldEntryAthleteDetails");
        Page<TrackFieldEntryAthleteDetails> page = trackFieldEntryAthleteDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /track-field-entry-athlete-details/:id} : get the "id" trackFieldEntryAthleteDetails.
     *
     * @param id the id of the trackFieldEntryAthleteDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trackFieldEntryAthleteDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/track-field-entry-athlete-details/{id}")
    public ResponseEntity<TrackFieldEntryAthleteDetails> getTrackFieldEntryAthleteDetails(@PathVariable Long id) {
        log.debug("REST request to get TrackFieldEntryAthleteDetails : {}", id);
        Optional<TrackFieldEntryAthleteDetails> trackFieldEntryAthleteDetails = trackFieldEntryAthleteDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trackFieldEntryAthleteDetails);
    }

    /**
     * {@code DELETE  /track-field-entry-athlete-details/:id} : delete the "id" trackFieldEntryAthleteDetails.
     *
     * @param id the id of the trackFieldEntryAthleteDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/track-field-entry-athlete-details/{id}")
    public ResponseEntity<Void> deleteTrackFieldEntryAthleteDetails(@PathVariable Long id) {
        log.debug("REST request to delete TrackFieldEntryAthleteDetails : {}", id);
        trackFieldEntryAthleteDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
