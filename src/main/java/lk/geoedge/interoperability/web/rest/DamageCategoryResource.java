package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.DamageCategory;
import lk.geoedge.interoperability.repository.DamageCategoryRepository;
import lk.geoedge.interoperability.service.DamageCategoryQueryService;
import lk.geoedge.interoperability.service.DamageCategoryService;
import lk.geoedge.interoperability.service.criteria.DamageCategoryCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.DamageCategory}.
 */
@RestController
@RequestMapping("/api/damage-categories")
public class DamageCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(DamageCategoryResource.class);

    private static final String ENTITY_NAME = "aaibapi1DamageCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DamageCategoryService damageCategoryService;

    private final DamageCategoryRepository damageCategoryRepository;

    private final DamageCategoryQueryService damageCategoryQueryService;

    public DamageCategoryResource(
        DamageCategoryService damageCategoryService,
        DamageCategoryRepository damageCategoryRepository,
        DamageCategoryQueryService damageCategoryQueryService
    ) {
        this.damageCategoryService = damageCategoryService;
        this.damageCategoryRepository = damageCategoryRepository;
        this.damageCategoryQueryService = damageCategoryQueryService;
    }

    /**
     * {@code POST  /damage-categories} : Create a new damageCategory.
     *
     * @param damageCategory the damageCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new damageCategory, or with status {@code 400 (Bad Request)} if the damageCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DamageCategory> createDamageCategory(@RequestBody DamageCategory damageCategory) throws URISyntaxException {
        LOG.debug("REST request to save DamageCategory : {}", damageCategory);
        if (damageCategory.getId() != null) {
            throw new BadRequestAlertException("A new damageCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        damageCategory = damageCategoryService.save(damageCategory);
        return ResponseEntity.created(new URI("/api/damage-categories/" + damageCategory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, damageCategory.getId().toString()))
            .body(damageCategory);
    }

    /**
     * {@code PUT  /damage-categories/:id} : Updates an existing damageCategory.
     *
     * @param id the id of the damageCategory to save.
     * @param damageCategory the damageCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated damageCategory,
     * or with status {@code 400 (Bad Request)} if the damageCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the damageCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DamageCategory> updateDamageCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DamageCategory damageCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to update DamageCategory : {}, {}", id, damageCategory);
        if (damageCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, damageCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!damageCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        damageCategory = damageCategoryService.update(damageCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, damageCategory.getId().toString()))
            .body(damageCategory);
    }

    /**
     * {@code PATCH  /damage-categories/:id} : Partial updates given fields of an existing damageCategory, field will ignore if it is null
     *
     * @param id the id of the damageCategory to save.
     * @param damageCategory the damageCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated damageCategory,
     * or with status {@code 400 (Bad Request)} if the damageCategory is not valid,
     * or with status {@code 404 (Not Found)} if the damageCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the damageCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DamageCategory> partialUpdateDamageCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DamageCategory damageCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DamageCategory partially : {}, {}", id, damageCategory);
        if (damageCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, damageCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!damageCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DamageCategory> result = damageCategoryService.partialUpdate(damageCategory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, damageCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /damage-categories} : get all the damageCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of damageCategories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DamageCategory>> getAllDamageCategories(
        DamageCategoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DamageCategories by criteria: {}", criteria);

        Page<DamageCategory> page = damageCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /damage-categories/count} : count all the damageCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDamageCategories(DamageCategoryCriteria criteria) {
        LOG.debug("REST request to count DamageCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(damageCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /damage-categories/:id} : get the "id" damageCategory.
     *
     * @param id the id of the damageCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the damageCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DamageCategory> getDamageCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DamageCategory : {}", id);
        Optional<DamageCategory> damageCategory = damageCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(damageCategory);
    }

    /**
     * {@code DELETE  /damage-categories/:id} : delete the "id" damageCategory.
     *
     * @param id the id of the damageCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDamageCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DamageCategory : {}", id);
        damageCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
