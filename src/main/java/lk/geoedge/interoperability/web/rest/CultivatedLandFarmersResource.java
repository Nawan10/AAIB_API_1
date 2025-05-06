package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandFarmers;
import lk.geoedge.interoperability.repository.CultivatedLandFarmersRepository;
import lk.geoedge.interoperability.service.CultivatedLandFarmersQueryService;
import lk.geoedge.interoperability.service.CultivatedLandFarmersService;
import lk.geoedge.interoperability.service.criteria.CultivatedLandFarmersCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CultivatedLandFarmers}.
 */
@RestController
@RequestMapping("/api/cultivated-land-farmers")
public class CultivatedLandFarmersResource {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandFarmersResource.class);

    private static final String ENTITY_NAME = "aaibapi1CultivatedLandFarmers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CultivatedLandFarmersService cultivatedLandFarmersService;

    private final CultivatedLandFarmersRepository cultivatedLandFarmersRepository;

    private final CultivatedLandFarmersQueryService cultivatedLandFarmersQueryService;

    public CultivatedLandFarmersResource(
        CultivatedLandFarmersService cultivatedLandFarmersService,
        CultivatedLandFarmersRepository cultivatedLandFarmersRepository,
        CultivatedLandFarmersQueryService cultivatedLandFarmersQueryService
    ) {
        this.cultivatedLandFarmersService = cultivatedLandFarmersService;
        this.cultivatedLandFarmersRepository = cultivatedLandFarmersRepository;
        this.cultivatedLandFarmersQueryService = cultivatedLandFarmersQueryService;
    }

    /**
     * {@code POST  /cultivated-land-farmers} : Create a new cultivatedLandFarmers.
     *
     * @param cultivatedLandFarmers the cultivatedLandFarmers to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cultivatedLandFarmers, or with status {@code 400 (Bad Request)} if the cultivatedLandFarmers has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CultivatedLandFarmers> createCultivatedLandFarmers(@RequestBody CultivatedLandFarmers cultivatedLandFarmers)
        throws URISyntaxException {
        LOG.debug("REST request to save CultivatedLandFarmers : {}", cultivatedLandFarmers);
        if (cultivatedLandFarmers.getId() != null) {
            throw new BadRequestAlertException("A new cultivatedLandFarmers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cultivatedLandFarmers = cultivatedLandFarmersService.save(cultivatedLandFarmers);
        return ResponseEntity.created(new URI("/api/cultivated-land-farmers/" + cultivatedLandFarmers.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cultivatedLandFarmers.getId().toString()))
            .body(cultivatedLandFarmers);
    }

    /**
     * {@code PUT  /cultivated-land-farmers/:id} : Updates an existing cultivatedLandFarmers.
     *
     * @param id the id of the cultivatedLandFarmers to save.
     * @param cultivatedLandFarmers the cultivatedLandFarmers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandFarmers,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandFarmers is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandFarmers couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CultivatedLandFarmers> updateCultivatedLandFarmers(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandFarmers cultivatedLandFarmers
    ) throws URISyntaxException {
        LOG.debug("REST request to update CultivatedLandFarmers : {}, {}", id, cultivatedLandFarmers);
        if (cultivatedLandFarmers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandFarmers.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandFarmersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cultivatedLandFarmers = cultivatedLandFarmersService.update(cultivatedLandFarmers);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedLandFarmers.getId().toString()))
            .body(cultivatedLandFarmers);
    }

    /**
     * {@code PATCH  /cultivated-land-farmers/:id} : Partial updates given fields of an existing cultivatedLandFarmers, field will ignore if it is null
     *
     * @param id the id of the cultivatedLandFarmers to save.
     * @param cultivatedLandFarmers the cultivatedLandFarmers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandFarmers,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandFarmers is not valid,
     * or with status {@code 404 (Not Found)} if the cultivatedLandFarmers is not found,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandFarmers couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CultivatedLandFarmers> partialUpdateCultivatedLandFarmers(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandFarmers cultivatedLandFarmers
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CultivatedLandFarmers partially : {}, {}", id, cultivatedLandFarmers);
        if (cultivatedLandFarmers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandFarmers.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandFarmersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CultivatedLandFarmers> result = cultivatedLandFarmersService.partialUpdate(cultivatedLandFarmers);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedLandFarmers.getId().toString())
        );
    }

    /**
     * {@code GET  /cultivated-land-farmers} : get all the cultivatedLandFarmers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cultivatedLandFarmers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CultivatedLandFarmers>> getAllCultivatedLandFarmers(
        CultivatedLandFarmersCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CultivatedLandFarmers by criteria: {}", criteria);

        Page<CultivatedLandFarmers> page = cultivatedLandFarmersQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cultivated-land-farmers/count} : count all the cultivatedLandFarmers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCultivatedLandFarmers(CultivatedLandFarmersCriteria criteria) {
        LOG.debug("REST request to count CultivatedLandFarmers by criteria: {}", criteria);
        return ResponseEntity.ok().body(cultivatedLandFarmersQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cultivated-land-farmers/:id} : get the "id" cultivatedLandFarmers.
     *
     * @param id the id of the cultivatedLandFarmers to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cultivatedLandFarmers, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CultivatedLandFarmers> getCultivatedLandFarmers(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CultivatedLandFarmers : {}", id);
        Optional<CultivatedLandFarmers> cultivatedLandFarmers = cultivatedLandFarmersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cultivatedLandFarmers);
    }

    /**
     * {@code DELETE  /cultivated-land-farmers/:id} : delete the "id" cultivatedLandFarmers.
     *
     * @param id the id of the cultivatedLandFarmers to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultivatedLandFarmers(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CultivatedLandFarmers : {}", id);
        cultivatedLandFarmersService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
