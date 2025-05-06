package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandFarmerFieldOwner;
import lk.geoedge.interoperability.repository.CultivatedLandFarmerFieldOwnerRepository;
import lk.geoedge.interoperability.service.CultivatedLandFarmerFieldOwnerQueryService;
import lk.geoedge.interoperability.service.CultivatedLandFarmerFieldOwnerService;
import lk.geoedge.interoperability.service.criteria.CultivatedLandFarmerFieldOwnerCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CultivatedLandFarmerFieldOwner}.
 */
@RestController
@RequestMapping("/api/cultivated-land-farmer-field-owners")
public class CultivatedLandFarmerFieldOwnerResource {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandFarmerFieldOwnerResource.class);

    private static final String ENTITY_NAME = "aaibapi1CultivatedLandFarmerFieldOwner";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CultivatedLandFarmerFieldOwnerService cultivatedLandFarmerFieldOwnerService;

    private final CultivatedLandFarmerFieldOwnerRepository cultivatedLandFarmerFieldOwnerRepository;

    private final CultivatedLandFarmerFieldOwnerQueryService cultivatedLandFarmerFieldOwnerQueryService;

    public CultivatedLandFarmerFieldOwnerResource(
        CultivatedLandFarmerFieldOwnerService cultivatedLandFarmerFieldOwnerService,
        CultivatedLandFarmerFieldOwnerRepository cultivatedLandFarmerFieldOwnerRepository,
        CultivatedLandFarmerFieldOwnerQueryService cultivatedLandFarmerFieldOwnerQueryService
    ) {
        this.cultivatedLandFarmerFieldOwnerService = cultivatedLandFarmerFieldOwnerService;
        this.cultivatedLandFarmerFieldOwnerRepository = cultivatedLandFarmerFieldOwnerRepository;
        this.cultivatedLandFarmerFieldOwnerQueryService = cultivatedLandFarmerFieldOwnerQueryService;
    }

    /**
     * {@code POST  /cultivated-land-farmer-field-owners} : Create a new cultivatedLandFarmerFieldOwner.
     *
     * @param cultivatedLandFarmerFieldOwner the cultivatedLandFarmerFieldOwner to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cultivatedLandFarmerFieldOwner, or with status {@code 400 (Bad Request)} if the cultivatedLandFarmerFieldOwner has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CultivatedLandFarmerFieldOwner> createCultivatedLandFarmerFieldOwner(
        @RequestBody CultivatedLandFarmerFieldOwner cultivatedLandFarmerFieldOwner
    ) throws URISyntaxException {
        LOG.debug("REST request to save CultivatedLandFarmerFieldOwner : {}", cultivatedLandFarmerFieldOwner);
        if (cultivatedLandFarmerFieldOwner.getId() != null) {
            throw new BadRequestAlertException("A new cultivatedLandFarmerFieldOwner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerService.save(cultivatedLandFarmerFieldOwner);
        return ResponseEntity.created(new URI("/api/cultivated-land-farmer-field-owners/" + cultivatedLandFarmerFieldOwner.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cultivatedLandFarmerFieldOwner.getId().toString())
            )
            .body(cultivatedLandFarmerFieldOwner);
    }

    /**
     * {@code PUT  /cultivated-land-farmer-field-owners/:id} : Updates an existing cultivatedLandFarmerFieldOwner.
     *
     * @param id the id of the cultivatedLandFarmerFieldOwner to save.
     * @param cultivatedLandFarmerFieldOwner the cultivatedLandFarmerFieldOwner to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandFarmerFieldOwner,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandFarmerFieldOwner is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandFarmerFieldOwner couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CultivatedLandFarmerFieldOwner> updateCultivatedLandFarmerFieldOwner(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandFarmerFieldOwner cultivatedLandFarmerFieldOwner
    ) throws URISyntaxException {
        LOG.debug("REST request to update CultivatedLandFarmerFieldOwner : {}, {}", id, cultivatedLandFarmerFieldOwner);
        if (cultivatedLandFarmerFieldOwner.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandFarmerFieldOwner.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandFarmerFieldOwnerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerService.update(cultivatedLandFarmerFieldOwner);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedLandFarmerFieldOwner.getId().toString())
            )
            .body(cultivatedLandFarmerFieldOwner);
    }

    /**
     * {@code PATCH  /cultivated-land-farmer-field-owners/:id} : Partial updates given fields of an existing cultivatedLandFarmerFieldOwner, field will ignore if it is null
     *
     * @param id the id of the cultivatedLandFarmerFieldOwner to save.
     * @param cultivatedLandFarmerFieldOwner the cultivatedLandFarmerFieldOwner to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandFarmerFieldOwner,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandFarmerFieldOwner is not valid,
     * or with status {@code 404 (Not Found)} if the cultivatedLandFarmerFieldOwner is not found,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandFarmerFieldOwner couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CultivatedLandFarmerFieldOwner> partialUpdateCultivatedLandFarmerFieldOwner(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandFarmerFieldOwner cultivatedLandFarmerFieldOwner
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CultivatedLandFarmerFieldOwner partially : {}, {}", id, cultivatedLandFarmerFieldOwner);
        if (cultivatedLandFarmerFieldOwner.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandFarmerFieldOwner.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandFarmerFieldOwnerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CultivatedLandFarmerFieldOwner> result = cultivatedLandFarmerFieldOwnerService.partialUpdate(
            cultivatedLandFarmerFieldOwner
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedLandFarmerFieldOwner.getId().toString())
        );
    }

    /**
     * {@code GET  /cultivated-land-farmer-field-owners} : get all the cultivatedLandFarmerFieldOwners.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cultivatedLandFarmerFieldOwners in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CultivatedLandFarmerFieldOwner>> getAllCultivatedLandFarmerFieldOwners(
        CultivatedLandFarmerFieldOwnerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CultivatedLandFarmerFieldOwners by criteria: {}", criteria);

        Page<CultivatedLandFarmerFieldOwner> page = cultivatedLandFarmerFieldOwnerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cultivated-land-farmer-field-owners/count} : count all the cultivatedLandFarmerFieldOwners.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCultivatedLandFarmerFieldOwners(CultivatedLandFarmerFieldOwnerCriteria criteria) {
        LOG.debug("REST request to count CultivatedLandFarmerFieldOwners by criteria: {}", criteria);
        return ResponseEntity.ok().body(cultivatedLandFarmerFieldOwnerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cultivated-land-farmer-field-owners/:id} : get the "id" cultivatedLandFarmerFieldOwner.
     *
     * @param id the id of the cultivatedLandFarmerFieldOwner to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cultivatedLandFarmerFieldOwner, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CultivatedLandFarmerFieldOwner> getCultivatedLandFarmerFieldOwner(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CultivatedLandFarmerFieldOwner : {}", id);
        Optional<CultivatedLandFarmerFieldOwner> cultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cultivatedLandFarmerFieldOwner);
    }

    /**
     * {@code DELETE  /cultivated-land-farmer-field-owners/:id} : delete the "id" cultivatedLandFarmerFieldOwner.
     *
     * @param id the id of the cultivatedLandFarmerFieldOwner to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultivatedLandFarmerFieldOwner(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CultivatedLandFarmerFieldOwner : {}", id);
        cultivatedLandFarmerFieldOwnerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
