package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexPolicyCropType;
import lk.geoedge.interoperability.repository.IndexPolicyCropTypeRepository;
import lk.geoedge.interoperability.service.IndexPolicyCropTypeQueryService;
import lk.geoedge.interoperability.service.IndexPolicyCropTypeService;
import lk.geoedge.interoperability.service.criteria.IndexPolicyCropTypeCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.IndexPolicyCropType}.
 */
@RestController
@RequestMapping("/api/index-policy-crop-types")
public class IndexPolicyCropTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPolicyCropTypeResource.class);

    private static final String ENTITY_NAME = "aaibapi1IndexPolicyCropType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndexPolicyCropTypeService indexPolicyCropTypeService;

    private final IndexPolicyCropTypeRepository indexPolicyCropTypeRepository;

    private final IndexPolicyCropTypeQueryService indexPolicyCropTypeQueryService;

    public IndexPolicyCropTypeResource(
        IndexPolicyCropTypeService indexPolicyCropTypeService,
        IndexPolicyCropTypeRepository indexPolicyCropTypeRepository,
        IndexPolicyCropTypeQueryService indexPolicyCropTypeQueryService
    ) {
        this.indexPolicyCropTypeService = indexPolicyCropTypeService;
        this.indexPolicyCropTypeRepository = indexPolicyCropTypeRepository;
        this.indexPolicyCropTypeQueryService = indexPolicyCropTypeQueryService;
    }

    /**
     * {@code POST  /index-policy-crop-types} : Create a new indexPolicyCropType.
     *
     * @param indexPolicyCropType the indexPolicyCropType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new indexPolicyCropType, or with status {@code 400 (Bad Request)} if the indexPolicyCropType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IndexPolicyCropType> createIndexPolicyCropType(@RequestBody IndexPolicyCropType indexPolicyCropType)
        throws URISyntaxException {
        LOG.debug("REST request to save IndexPolicyCropType : {}", indexPolicyCropType);
        if (indexPolicyCropType.getId() != null) {
            throw new BadRequestAlertException("A new indexPolicyCropType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        indexPolicyCropType = indexPolicyCropTypeService.save(indexPolicyCropType);
        return ResponseEntity.created(new URI("/api/index-policy-crop-types/" + indexPolicyCropType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, indexPolicyCropType.getId().toString()))
            .body(indexPolicyCropType);
    }

    /**
     * {@code PUT  /index-policy-crop-types/:id} : Updates an existing indexPolicyCropType.
     *
     * @param id the id of the indexPolicyCropType to save.
     * @param indexPolicyCropType the indexPolicyCropType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexPolicyCropType,
     * or with status {@code 400 (Bad Request)} if the indexPolicyCropType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the indexPolicyCropType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IndexPolicyCropType> updateIndexPolicyCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexPolicyCropType indexPolicyCropType
    ) throws URISyntaxException {
        LOG.debug("REST request to update IndexPolicyCropType : {}, {}", id, indexPolicyCropType);
        if (indexPolicyCropType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexPolicyCropType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexPolicyCropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        indexPolicyCropType = indexPolicyCropTypeService.update(indexPolicyCropType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexPolicyCropType.getId().toString()))
            .body(indexPolicyCropType);
    }

    /**
     * {@code PATCH  /index-policy-crop-types/:id} : Partial updates given fields of an existing indexPolicyCropType, field will ignore if it is null
     *
     * @param id the id of the indexPolicyCropType to save.
     * @param indexPolicyCropType the indexPolicyCropType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexPolicyCropType,
     * or with status {@code 400 (Bad Request)} if the indexPolicyCropType is not valid,
     * or with status {@code 404 (Not Found)} if the indexPolicyCropType is not found,
     * or with status {@code 500 (Internal Server Error)} if the indexPolicyCropType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IndexPolicyCropType> partialUpdateIndexPolicyCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexPolicyCropType indexPolicyCropType
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IndexPolicyCropType partially : {}, {}", id, indexPolicyCropType);
        if (indexPolicyCropType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexPolicyCropType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexPolicyCropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IndexPolicyCropType> result = indexPolicyCropTypeService.partialUpdate(indexPolicyCropType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexPolicyCropType.getId().toString())
        );
    }

    /**
     * {@code GET  /index-policy-crop-types} : get all the indexPolicyCropTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of indexPolicyCropTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IndexPolicyCropType>> getAllIndexPolicyCropTypes(
        IndexPolicyCropTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get IndexPolicyCropTypes by criteria: {}", criteria);

        Page<IndexPolicyCropType> page = indexPolicyCropTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /index-policy-crop-types/count} : count all the indexPolicyCropTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countIndexPolicyCropTypes(IndexPolicyCropTypeCriteria criteria) {
        LOG.debug("REST request to count IndexPolicyCropTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(indexPolicyCropTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /index-policy-crop-types/:id} : get the "id" indexPolicyCropType.
     *
     * @param id the id of the indexPolicyCropType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the indexPolicyCropType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IndexPolicyCropType> getIndexPolicyCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IndexPolicyCropType : {}", id);
        Optional<IndexPolicyCropType> indexPolicyCropType = indexPolicyCropTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(indexPolicyCropType);
    }

    /**
     * {@code DELETE  /index-policy-crop-types/:id} : delete the "id" indexPolicyCropType.
     *
     * @param id the id of the indexPolicyCropType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndexPolicyCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IndexPolicyCropType : {}", id);
        indexPolicyCropTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
