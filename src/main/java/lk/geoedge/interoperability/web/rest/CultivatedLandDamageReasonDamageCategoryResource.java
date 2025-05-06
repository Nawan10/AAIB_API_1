package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageCategory;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReasonDamageCategoryRepository;
import lk.geoedge.interoperability.service.CultivatedLandDamageReasonDamageCategoryQueryService;
import lk.geoedge.interoperability.service.CultivatedLandDamageReasonDamageCategoryService;
import lk.geoedge.interoperability.service.criteria.CultivatedLandDamageReasonDamageCategoryCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageCategory}.
 */
@RestController
@RequestMapping("/api/cultivated-land-damage-reason-damage-categories")
public class CultivatedLandDamageReasonDamageCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandDamageReasonDamageCategoryResource.class);

    private static final String ENTITY_NAME = "aaibapi1CultivatedLandDamageReasonDamageCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CultivatedLandDamageReasonDamageCategoryService cultivatedLandDamageReasonDamageCategoryService;

    private final CultivatedLandDamageReasonDamageCategoryRepository cultivatedLandDamageReasonDamageCategoryRepository;

    private final CultivatedLandDamageReasonDamageCategoryQueryService cultivatedLandDamageReasonDamageCategoryQueryService;

    public CultivatedLandDamageReasonDamageCategoryResource(
        CultivatedLandDamageReasonDamageCategoryService cultivatedLandDamageReasonDamageCategoryService,
        CultivatedLandDamageReasonDamageCategoryRepository cultivatedLandDamageReasonDamageCategoryRepository,
        CultivatedLandDamageReasonDamageCategoryQueryService cultivatedLandDamageReasonDamageCategoryQueryService
    ) {
        this.cultivatedLandDamageReasonDamageCategoryService = cultivatedLandDamageReasonDamageCategoryService;
        this.cultivatedLandDamageReasonDamageCategoryRepository = cultivatedLandDamageReasonDamageCategoryRepository;
        this.cultivatedLandDamageReasonDamageCategoryQueryService = cultivatedLandDamageReasonDamageCategoryQueryService;
    }

    /**
     * {@code POST  /cultivated-land-damage-reason-damage-categories} : Create a new cultivatedLandDamageReasonDamageCategory.
     *
     * @param cultivatedLandDamageReasonDamageCategory the cultivatedLandDamageReasonDamageCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cultivatedLandDamageReasonDamageCategory, or with status {@code 400 (Bad Request)} if the cultivatedLandDamageReasonDamageCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CultivatedLandDamageReasonDamageCategory> createCultivatedLandDamageReasonDamageCategory(
        @RequestBody CultivatedLandDamageReasonDamageCategory cultivatedLandDamageReasonDamageCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to save CultivatedLandDamageReasonDamageCategory : {}", cultivatedLandDamageReasonDamageCategory);
        if (cultivatedLandDamageReasonDamageCategory.getId() != null) {
            throw new BadRequestAlertException(
                "A new cultivatedLandDamageReasonDamageCategory cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        cultivatedLandDamageReasonDamageCategory = cultivatedLandDamageReasonDamageCategoryService.save(
            cultivatedLandDamageReasonDamageCategory
        );
        return ResponseEntity.created(
            new URI("/api/cultivated-land-damage-reason-damage-categories/" + cultivatedLandDamageReasonDamageCategory.getId())
        )
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    cultivatedLandDamageReasonDamageCategory.getId().toString()
                )
            )
            .body(cultivatedLandDamageReasonDamageCategory);
    }

    /**
     * {@code PUT  /cultivated-land-damage-reason-damage-categories/:id} : Updates an existing cultivatedLandDamageReasonDamageCategory.
     *
     * @param id the id of the cultivatedLandDamageReasonDamageCategory to save.
     * @param cultivatedLandDamageReasonDamageCategory the cultivatedLandDamageReasonDamageCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandDamageReasonDamageCategory,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandDamageReasonDamageCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandDamageReasonDamageCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CultivatedLandDamageReasonDamageCategory> updateCultivatedLandDamageReasonDamageCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandDamageReasonDamageCategory cultivatedLandDamageReasonDamageCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to update CultivatedLandDamageReasonDamageCategory : {}, {}", id, cultivatedLandDamageReasonDamageCategory);
        if (cultivatedLandDamageReasonDamageCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandDamageReasonDamageCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandDamageReasonDamageCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cultivatedLandDamageReasonDamageCategory = cultivatedLandDamageReasonDamageCategoryService.update(
            cultivatedLandDamageReasonDamageCategory
        );
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    cultivatedLandDamageReasonDamageCategory.getId().toString()
                )
            )
            .body(cultivatedLandDamageReasonDamageCategory);
    }

    /**
     * {@code PATCH  /cultivated-land-damage-reason-damage-categories/:id} : Partial updates given fields of an existing cultivatedLandDamageReasonDamageCategory, field will ignore if it is null
     *
     * @param id the id of the cultivatedLandDamageReasonDamageCategory to save.
     * @param cultivatedLandDamageReasonDamageCategory the cultivatedLandDamageReasonDamageCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandDamageReasonDamageCategory,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandDamageReasonDamageCategory is not valid,
     * or with status {@code 404 (Not Found)} if the cultivatedLandDamageReasonDamageCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandDamageReasonDamageCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CultivatedLandDamageReasonDamageCategory> partialUpdateCultivatedLandDamageReasonDamageCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandDamageReasonDamageCategory cultivatedLandDamageReasonDamageCategory
    ) throws URISyntaxException {
        LOG.debug(
            "REST request to partial update CultivatedLandDamageReasonDamageCategory partially : {}, {}",
            id,
            cultivatedLandDamageReasonDamageCategory
        );
        if (cultivatedLandDamageReasonDamageCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandDamageReasonDamageCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandDamageReasonDamageCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CultivatedLandDamageReasonDamageCategory> result = cultivatedLandDamageReasonDamageCategoryService.partialUpdate(
            cultivatedLandDamageReasonDamageCategory
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(
                applicationName,
                false,
                ENTITY_NAME,
                cultivatedLandDamageReasonDamageCategory.getId().toString()
            )
        );
    }

    /**
     * {@code GET  /cultivated-land-damage-reason-damage-categories} : get all the cultivatedLandDamageReasonDamageCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cultivatedLandDamageReasonDamageCategories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CultivatedLandDamageReasonDamageCategory>> getAllCultivatedLandDamageReasonDamageCategories(
        CultivatedLandDamageReasonDamageCategoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CultivatedLandDamageReasonDamageCategories by criteria: {}", criteria);

        Page<CultivatedLandDamageReasonDamageCategory> page = cultivatedLandDamageReasonDamageCategoryQueryService.findByCriteria(
            criteria,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cultivated-land-damage-reason-damage-categories/count} : count all the cultivatedLandDamageReasonDamageCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCultivatedLandDamageReasonDamageCategories(CultivatedLandDamageReasonDamageCategoryCriteria criteria) {
        LOG.debug("REST request to count CultivatedLandDamageReasonDamageCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(cultivatedLandDamageReasonDamageCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cultivated-land-damage-reason-damage-categories/:id} : get the "id" cultivatedLandDamageReasonDamageCategory.
     *
     * @param id the id of the cultivatedLandDamageReasonDamageCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cultivatedLandDamageReasonDamageCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CultivatedLandDamageReasonDamageCategory> getCultivatedLandDamageReasonDamageCategory(
        @PathVariable("id") Long id
    ) {
        LOG.debug("REST request to get CultivatedLandDamageReasonDamageCategory : {}", id);
        Optional<CultivatedLandDamageReasonDamageCategory> cultivatedLandDamageReasonDamageCategory =
            cultivatedLandDamageReasonDamageCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cultivatedLandDamageReasonDamageCategory);
    }

    /**
     * {@code DELETE  /cultivated-land-damage-reason-damage-categories/:id} : delete the "id" cultivatedLandDamageReasonDamageCategory.
     *
     * @param id the id of the cultivatedLandDamageReasonDamageCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultivatedLandDamageReasonDamageCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CultivatedLandDamageReasonDamageCategory : {}", id);
        cultivatedLandDamageReasonDamageCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
