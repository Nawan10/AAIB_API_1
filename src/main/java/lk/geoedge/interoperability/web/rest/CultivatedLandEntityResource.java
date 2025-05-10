package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandEntity;
import lk.geoedge.interoperability.repository.CultivatedLandEntityRepository;
import lk.geoedge.interoperability.service.CultivatedLandEntityQueryService;
import lk.geoedge.interoperability.service.CultivatedLandEntityService;
import lk.geoedge.interoperability.service.criteria.CultivatedLandEntityCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CultivatedLandEntity}.
 */
@RestController
@RequestMapping("/api/cultivated-land-entities")
public class CultivatedLandEntityResource {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandEntityResource.class);

    private static final String ENTITY_NAME = "aaibapi1CultivatedLandEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CultivatedLandEntityService cultivatedLandEntityService;

    private final CultivatedLandEntityRepository cultivatedLandEntityRepository;

    private final CultivatedLandEntityQueryService cultivatedLandEntityQueryService;

    public CultivatedLandEntityResource(
        CultivatedLandEntityService cultivatedLandEntityService,
        CultivatedLandEntityRepository cultivatedLandEntityRepository,
        CultivatedLandEntityQueryService cultivatedLandEntityQueryService
    ) {
        this.cultivatedLandEntityService = cultivatedLandEntityService;
        this.cultivatedLandEntityRepository = cultivatedLandEntityRepository;
        this.cultivatedLandEntityQueryService = cultivatedLandEntityQueryService;
    }

    /**
     * {@code POST  /cultivated-land-entities} : Create a new cultivatedLandEntity.
     *
     * @param cultivatedLandEntity the cultivatedLandEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cultivatedLandEntity, or with status {@code 400 (Bad Request)} if the cultivatedLandEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CultivatedLandEntity> createCultivatedLandEntity(@RequestBody CultivatedLandEntity cultivatedLandEntity)
        throws URISyntaxException {
        LOG.debug("REST request to save CultivatedLandEntity : {}", cultivatedLandEntity);
        if (cultivatedLandEntity.getId() != null) {
            throw new BadRequestAlertException("A new cultivatedLandEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cultivatedLandEntity = cultivatedLandEntityService.save(cultivatedLandEntity);
        return ResponseEntity.created(new URI("/api/cultivated-land-entities/" + cultivatedLandEntity.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cultivatedLandEntity.getId().toString()))
            .body(cultivatedLandEntity);
    }

    /**
     * {@code PUT  /cultivated-land-entities/:id} : Updates an existing cultivatedLandEntity.
     *
     * @param id the id of the cultivatedLandEntity to save.
     * @param cultivatedLandEntity the cultivatedLandEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandEntity,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CultivatedLandEntity> updateCultivatedLandEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandEntity cultivatedLandEntity
    ) throws URISyntaxException {
        LOG.debug("REST request to update CultivatedLandEntity : {}, {}", id, cultivatedLandEntity);
        if (cultivatedLandEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cultivatedLandEntity = cultivatedLandEntityService.update(cultivatedLandEntity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedLandEntity.getId().toString()))
            .body(cultivatedLandEntity);
    }

    /**
     * {@code PATCH  /cultivated-land-entities/:id} : Partial updates given fields of an existing cultivatedLandEntity, field will ignore if it is null
     *
     * @param id the id of the cultivatedLandEntity to save.
     * @param cultivatedLandEntity the cultivatedLandEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandEntity,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandEntity is not valid,
     * or with status {@code 404 (Not Found)} if the cultivatedLandEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CultivatedLandEntity> partialUpdateCultivatedLandEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandEntity cultivatedLandEntity
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CultivatedLandEntity partially : {}, {}", id, cultivatedLandEntity);
        if (cultivatedLandEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CultivatedLandEntity> result = cultivatedLandEntityService.partialUpdate(cultivatedLandEntity);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedLandEntity.getId().toString())
        );
    }

    /**
     * {@code GET  /cultivated-land-entities} : get all the cultivatedLandEntities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cultivatedLandEntities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CultivatedLandEntity>> getAllCultivatedLandEntities(
        CultivatedLandEntityCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CultivatedLandEntities by criteria: {}", criteria);

        Page<CultivatedLandEntity> page = cultivatedLandEntityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cultivated-land-entities/count} : count all the cultivatedLandEntities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCultivatedLandEntities(CultivatedLandEntityCriteria criteria) {
        LOG.debug("REST request to count CultivatedLandEntities by criteria: {}", criteria);
        return ResponseEntity.ok().body(cultivatedLandEntityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cultivated-land-entities/:id} : get the "id" cultivatedLandEntity.
     *
     * @param id the id of the cultivatedLandEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cultivatedLandEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CultivatedLandEntity> getCultivatedLandEntity(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CultivatedLandEntity : {}", id);
        Optional<CultivatedLandEntity> cultivatedLandEntity = cultivatedLandEntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cultivatedLandEntity);
    }

    /**
     * {@code DELETE  /cultivated-land-entities/:id} : delete the "id" cultivatedLandEntity.
     *
     * @param id the id of the cultivatedLandEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultivatedLandEntity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CultivatedLandEntity : {}", id);
        cultivatedLandEntityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
