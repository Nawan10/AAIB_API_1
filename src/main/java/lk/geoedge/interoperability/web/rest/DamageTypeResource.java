package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.DamageType;
import lk.geoedge.interoperability.repository.DamageTypeRepository;
import lk.geoedge.interoperability.service.DamageTypeQueryService;
import lk.geoedge.interoperability.service.DamageTypeService;
import lk.geoedge.interoperability.service.criteria.DamageTypeCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.DamageType}.
 */
@RestController
@RequestMapping("/api/damage-types")
public class DamageTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(DamageTypeResource.class);

    private static final String ENTITY_NAME = "aaibapi1DamageType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DamageTypeService damageTypeService;

    private final DamageTypeRepository damageTypeRepository;

    private final DamageTypeQueryService damageTypeQueryService;

    public DamageTypeResource(
        DamageTypeService damageTypeService,
        DamageTypeRepository damageTypeRepository,
        DamageTypeQueryService damageTypeQueryService
    ) {
        this.damageTypeService = damageTypeService;
        this.damageTypeRepository = damageTypeRepository;
        this.damageTypeQueryService = damageTypeQueryService;
    }

    /**
     * {@code POST  /damage-types} : Create a new damageType.
     *
     * @param damageType the damageType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new damageType, or with status {@code 400 (Bad Request)} if the damageType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DamageType> createDamageType(@RequestBody DamageType damageType) throws URISyntaxException {
        LOG.debug("REST request to save DamageType : {}", damageType);
        if (damageType.getId() != null) {
            throw new BadRequestAlertException("A new damageType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        damageType = damageTypeService.save(damageType);
        return ResponseEntity.created(new URI("/api/damage-types/" + damageType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, damageType.getId().toString()))
            .body(damageType);
    }

    /**
     * {@code PUT  /damage-types/:id} : Updates an existing damageType.
     *
     * @param id the id of the damageType to save.
     * @param damageType the damageType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated damageType,
     * or with status {@code 400 (Bad Request)} if the damageType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the damageType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DamageType> updateDamageType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DamageType damageType
    ) throws URISyntaxException {
        LOG.debug("REST request to update DamageType : {}, {}", id, damageType);
        if (damageType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, damageType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!damageTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        damageType = damageTypeService.update(damageType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, damageType.getId().toString()))
            .body(damageType);
    }

    /**
     * {@code PATCH  /damage-types/:id} : Partial updates given fields of an existing damageType, field will ignore if it is null
     *
     * @param id the id of the damageType to save.
     * @param damageType the damageType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated damageType,
     * or with status {@code 400 (Bad Request)} if the damageType is not valid,
     * or with status {@code 404 (Not Found)} if the damageType is not found,
     * or with status {@code 500 (Internal Server Error)} if the damageType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DamageType> partialUpdateDamageType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DamageType damageType
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DamageType partially : {}, {}", id, damageType);
        if (damageType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, damageType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!damageTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DamageType> result = damageTypeService.partialUpdate(damageType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, damageType.getId().toString())
        );
    }

    /**
     * {@code GET  /damage-types} : get all the damageTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of damageTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DamageType>> getAllDamageTypes(
        DamageTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DamageTypes by criteria: {}", criteria);

        Page<DamageType> page = damageTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /damage-types/count} : count all the damageTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDamageTypes(DamageTypeCriteria criteria) {
        LOG.debug("REST request to count DamageTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(damageTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /damage-types/:id} : get the "id" damageType.
     *
     * @param id the id of the damageType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the damageType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DamageType> getDamageType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DamageType : {}", id);
        Optional<DamageType> damageType = damageTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(damageType);
    }

    /**
     * {@code DELETE  /damage-types/:id} : delete the "id" damageType.
     *
     * @param id the id of the damageType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDamageType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DamageType : {}", id);
        damageTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
