package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.DamageEntity;
import lk.geoedge.interoperability.repository.DamageEntityRepository;
import lk.geoedge.interoperability.service.DamageEntityQueryService;
import lk.geoedge.interoperability.service.DamageEntityService;
import lk.geoedge.interoperability.service.criteria.DamageEntityCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.DamageEntity}.
 */
@RestController
@RequestMapping("/api/damage-entities")
public class DamageEntityResource {

    private static final Logger LOG = LoggerFactory.getLogger(DamageEntityResource.class);

    private static final String ENTITY_NAME = "aaibapi1DamageEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DamageEntityService damageEntityService;

    private final DamageEntityRepository damageEntityRepository;

    private final DamageEntityQueryService damageEntityQueryService;

    public DamageEntityResource(
        DamageEntityService damageEntityService,
        DamageEntityRepository damageEntityRepository,
        DamageEntityQueryService damageEntityQueryService
    ) {
        this.damageEntityService = damageEntityService;
        this.damageEntityRepository = damageEntityRepository;
        this.damageEntityQueryService = damageEntityQueryService;
    }

    /**
     * {@code POST  /damage-entities} : Create a new damageEntity.
     *
     * @param damageEntity the damageEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new damageEntity, or with status {@code 400 (Bad Request)} if the damageEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DamageEntity> createDamageEntity(@RequestBody DamageEntity damageEntity) throws URISyntaxException {
        LOG.debug("REST request to save DamageEntity : {}", damageEntity);
        if (damageEntity.getId() != null) {
            throw new BadRequestAlertException("A new damageEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        damageEntity = damageEntityService.save(damageEntity);
        return ResponseEntity.created(new URI("/api/damage-entities/" + damageEntity.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, damageEntity.getId().toString()))
            .body(damageEntity);
    }

    /**
     * {@code PUT  /damage-entities/:id} : Updates an existing damageEntity.
     *
     * @param id the id of the damageEntity to save.
     * @param damageEntity the damageEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated damageEntity,
     * or with status {@code 400 (Bad Request)} if the damageEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the damageEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DamageEntity> updateDamageEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DamageEntity damageEntity
    ) throws URISyntaxException {
        LOG.debug("REST request to update DamageEntity : {}, {}", id, damageEntity);
        if (damageEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, damageEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!damageEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        damageEntity = damageEntityService.update(damageEntity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, damageEntity.getId().toString()))
            .body(damageEntity);
    }

    /**
     * {@code PATCH  /damage-entities/:id} : Partial updates given fields of an existing damageEntity, field will ignore if it is null
     *
     * @param id the id of the damageEntity to save.
     * @param damageEntity the damageEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated damageEntity,
     * or with status {@code 400 (Bad Request)} if the damageEntity is not valid,
     * or with status {@code 404 (Not Found)} if the damageEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the damageEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DamageEntity> partialUpdateDamageEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DamageEntity damageEntity
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DamageEntity partially : {}, {}", id, damageEntity);
        if (damageEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, damageEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!damageEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DamageEntity> result = damageEntityService.partialUpdate(damageEntity);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, damageEntity.getId().toString())
        );
    }

    /**
     * {@code GET  /damage-entities} : get all the damageEntities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of damageEntities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DamageEntity>> getAllDamageEntities(
        DamageEntityCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DamageEntities by criteria: {}", criteria);

        Page<DamageEntity> page = damageEntityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /damage-entities/count} : count all the damageEntities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDamageEntities(DamageEntityCriteria criteria) {
        LOG.debug("REST request to count DamageEntities by criteria: {}", criteria);
        return ResponseEntity.ok().body(damageEntityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /damage-entities/:id} : get the "id" damageEntity.
     *
     * @param id the id of the damageEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the damageEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DamageEntity> getDamageEntity(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DamageEntity : {}", id);
        Optional<DamageEntity> damageEntity = damageEntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(damageEntity);
    }

    /**
     * {@code DELETE  /damage-entities/:id} : delete the "id" damageEntity.
     *
     * @param id the id of the damageEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDamageEntity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DamageEntity : {}", id);
        damageEntityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
