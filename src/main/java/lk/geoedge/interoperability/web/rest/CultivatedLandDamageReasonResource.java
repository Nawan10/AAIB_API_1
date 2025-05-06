package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandDamageReason;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReasonRepository;
import lk.geoedge.interoperability.service.CultivatedLandDamageReasonQueryService;
import lk.geoedge.interoperability.service.CultivatedLandDamageReasonService;
import lk.geoedge.interoperability.service.criteria.CultivatedLandDamageReasonCriteria;
import lk.geoedge.interoperability.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CultivatedLandDamageReason}.
 */
@RestController
@RequestMapping("/api/cultivated-land-damage-reasons")
public class CultivatedLandDamageReasonResource {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandDamageReasonResource.class);

    private static final String ENTITY_NAME = "aaibapi1CultivatedLandDamageReason";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CultivatedLandDamageReasonService cultivatedLandDamageReasonService;

    private final CultivatedLandDamageReasonRepository cultivatedLandDamageReasonRepository;

    private final CultivatedLandDamageReasonQueryService cultivatedLandDamageReasonQueryService;

    public CultivatedLandDamageReasonResource(
        CultivatedLandDamageReasonService cultivatedLandDamageReasonService,
        CultivatedLandDamageReasonRepository cultivatedLandDamageReasonRepository,
        CultivatedLandDamageReasonQueryService cultivatedLandDamageReasonQueryService
    ) {
        this.cultivatedLandDamageReasonService = cultivatedLandDamageReasonService;
        this.cultivatedLandDamageReasonRepository = cultivatedLandDamageReasonRepository;
        this.cultivatedLandDamageReasonQueryService = cultivatedLandDamageReasonQueryService;
    }

    /**
     * {@code POST  /cultivated-land-damage-reasons} : Create a new cultivatedLandDamageReason.
     *
     * @param cultivatedLandDamageReason the cultivatedLandDamageReason to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cultivatedLandDamageReason, or with status {@code 400 (Bad Request)} if the cultivatedLandDamageReason has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CultivatedLandDamageReason> createCultivatedLandDamageReason(
        @RequestBody CultivatedLandDamageReason cultivatedLandDamageReason
    ) throws URISyntaxException {
        LOG.debug("REST request to save CultivatedLandDamageReason : {}", cultivatedLandDamageReason);
        if (cultivatedLandDamageReason.getId() != null) {
            throw new BadRequestAlertException("A new cultivatedLandDamageReason cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cultivatedLandDamageReason = cultivatedLandDamageReasonService.save(cultivatedLandDamageReason);
        return ResponseEntity.created(new URI("/api/cultivated-land-damage-reasons/" + cultivatedLandDamageReason.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cultivatedLandDamageReason.getId().toString())
            )
            .body(cultivatedLandDamageReason);
    }

    /**
     * {@code PUT  /cultivated-land-damage-reasons/:id} : Updates an existing cultivatedLandDamageReason.
     *
     * @param id the id of the cultivatedLandDamageReason to save.
     * @param cultivatedLandDamageReason the cultivatedLandDamageReason to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandDamageReason,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandDamageReason is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandDamageReason couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CultivatedLandDamageReason> updateCultivatedLandDamageReason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandDamageReason cultivatedLandDamageReason
    ) throws URISyntaxException {
        LOG.debug("REST request to update CultivatedLandDamageReason : {}, {}", id, cultivatedLandDamageReason);
        if (cultivatedLandDamageReason.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandDamageReason.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandDamageReasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cultivatedLandDamageReason = cultivatedLandDamageReasonService.update(cultivatedLandDamageReason);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedLandDamageReason.getId().toString()))
            .body(cultivatedLandDamageReason);
    }

    /**
     * {@code PATCH  /cultivated-land-damage-reasons/:id} : Partial updates given fields of an existing cultivatedLandDamageReason, field will ignore if it is null
     *
     * @param id the id of the cultivatedLandDamageReason to save.
     * @param cultivatedLandDamageReason the cultivatedLandDamageReason to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandDamageReason,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandDamageReason is not valid,
     * or with status {@code 404 (Not Found)} if the cultivatedLandDamageReason is not found,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandDamageReason couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CultivatedLandDamageReason> partialUpdateCultivatedLandDamageReason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandDamageReason cultivatedLandDamageReason
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CultivatedLandDamageReason partially : {}, {}", id, cultivatedLandDamageReason);
        if (cultivatedLandDamageReason.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandDamageReason.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandDamageReasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CultivatedLandDamageReason> result = cultivatedLandDamageReasonService.partialUpdate(cultivatedLandDamageReason);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedLandDamageReason.getId().toString())
        );
    }

    /**
     * {@code GET  /cultivated-land-damage-reasons} : get all the cultivatedLandDamageReasons.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cultivatedLandDamageReasons in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CultivatedLandDamageReason>> getAllCultivatedLandDamageReasons(
        CultivatedLandDamageReasonCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CultivatedLandDamageReasons by criteria: {}", criteria);

        Page<CultivatedLandDamageReason> page = cultivatedLandDamageReasonQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cultivated-land-damage-reasons/count} : count all the cultivatedLandDamageReasons.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCultivatedLandDamageReasons(CultivatedLandDamageReasonCriteria criteria) {
        LOG.debug("REST request to count CultivatedLandDamageReasons by criteria: {}", criteria);
        return ResponseEntity.ok().body(cultivatedLandDamageReasonQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cultivated-land-damage-reasons/:id} : get the "id" cultivatedLandDamageReason.
     *
     * @param id the id of the cultivatedLandDamageReason to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cultivatedLandDamageReason, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CultivatedLandDamageReason> getCultivatedLandDamageReason(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CultivatedLandDamageReason : {}", id);
        Optional<CultivatedLandDamageReason> cultivatedLandDamageReason = cultivatedLandDamageReasonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cultivatedLandDamageReason);
    }

    /**
     * {@code DELETE  /cultivated-land-damage-reasons/:id} : delete the "id" cultivatedLandDamageReason.
     *
     * @param id the id of the cultivatedLandDamageReason to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultivatedLandDamageReason(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CultivatedLandDamageReason : {}", id);
        cultivatedLandDamageReasonService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
