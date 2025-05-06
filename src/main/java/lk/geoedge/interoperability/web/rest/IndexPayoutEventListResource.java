package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexPayoutEventList;
import lk.geoedge.interoperability.repository.IndexPayoutEventListRepository;
import lk.geoedge.interoperability.service.IndexPayoutEventListQueryService;
import lk.geoedge.interoperability.service.IndexPayoutEventListService;
import lk.geoedge.interoperability.service.criteria.IndexPayoutEventListCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.IndexPayoutEventList}.
 */
@RestController
@RequestMapping("/api/index-payout-event-lists")
public class IndexPayoutEventListResource {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPayoutEventListResource.class);

    private static final String ENTITY_NAME = "aaibapi1IndexPayoutEventList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndexPayoutEventListService indexPayoutEventListService;

    private final IndexPayoutEventListRepository indexPayoutEventListRepository;

    private final IndexPayoutEventListQueryService indexPayoutEventListQueryService;

    public IndexPayoutEventListResource(
        IndexPayoutEventListService indexPayoutEventListService,
        IndexPayoutEventListRepository indexPayoutEventListRepository,
        IndexPayoutEventListQueryService indexPayoutEventListQueryService
    ) {
        this.indexPayoutEventListService = indexPayoutEventListService;
        this.indexPayoutEventListRepository = indexPayoutEventListRepository;
        this.indexPayoutEventListQueryService = indexPayoutEventListQueryService;
    }

    /**
     * {@code POST  /index-payout-event-lists} : Create a new indexPayoutEventList.
     *
     * @param indexPayoutEventList the indexPayoutEventList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new indexPayoutEventList, or with status {@code 400 (Bad Request)} if the indexPayoutEventList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IndexPayoutEventList> createIndexPayoutEventList(@RequestBody IndexPayoutEventList indexPayoutEventList)
        throws URISyntaxException {
        LOG.debug("REST request to save IndexPayoutEventList : {}", indexPayoutEventList);
        if (indexPayoutEventList.getId() != null) {
            throw new BadRequestAlertException("A new indexPayoutEventList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        indexPayoutEventList = indexPayoutEventListService.save(indexPayoutEventList);
        return ResponseEntity.created(new URI("/api/index-payout-event-lists/" + indexPayoutEventList.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, indexPayoutEventList.getId().toString()))
            .body(indexPayoutEventList);
    }

    /**
     * {@code PUT  /index-payout-event-lists/:id} : Updates an existing indexPayoutEventList.
     *
     * @param id the id of the indexPayoutEventList to save.
     * @param indexPayoutEventList the indexPayoutEventList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexPayoutEventList,
     * or with status {@code 400 (Bad Request)} if the indexPayoutEventList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the indexPayoutEventList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IndexPayoutEventList> updateIndexPayoutEventList(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexPayoutEventList indexPayoutEventList
    ) throws URISyntaxException {
        LOG.debug("REST request to update IndexPayoutEventList : {}, {}", id, indexPayoutEventList);
        if (indexPayoutEventList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexPayoutEventList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexPayoutEventListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        indexPayoutEventList = indexPayoutEventListService.update(indexPayoutEventList);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexPayoutEventList.getId().toString()))
            .body(indexPayoutEventList);
    }

    /**
     * {@code PATCH  /index-payout-event-lists/:id} : Partial updates given fields of an existing indexPayoutEventList, field will ignore if it is null
     *
     * @param id the id of the indexPayoutEventList to save.
     * @param indexPayoutEventList the indexPayoutEventList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexPayoutEventList,
     * or with status {@code 400 (Bad Request)} if the indexPayoutEventList is not valid,
     * or with status {@code 404 (Not Found)} if the indexPayoutEventList is not found,
     * or with status {@code 500 (Internal Server Error)} if the indexPayoutEventList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IndexPayoutEventList> partialUpdateIndexPayoutEventList(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexPayoutEventList indexPayoutEventList
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IndexPayoutEventList partially : {}, {}", id, indexPayoutEventList);
        if (indexPayoutEventList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexPayoutEventList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexPayoutEventListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IndexPayoutEventList> result = indexPayoutEventListService.partialUpdate(indexPayoutEventList);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexPayoutEventList.getId().toString())
        );
    }

    /**
     * {@code GET  /index-payout-event-lists} : get all the indexPayoutEventLists.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of indexPayoutEventLists in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IndexPayoutEventList>> getAllIndexPayoutEventLists(
        IndexPayoutEventListCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get IndexPayoutEventLists by criteria: {}", criteria);

        Page<IndexPayoutEventList> page = indexPayoutEventListQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /index-payout-event-lists/count} : count all the indexPayoutEventLists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countIndexPayoutEventLists(IndexPayoutEventListCriteria criteria) {
        LOG.debug("REST request to count IndexPayoutEventLists by criteria: {}", criteria);
        return ResponseEntity.ok().body(indexPayoutEventListQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /index-payout-event-lists/:id} : get the "id" indexPayoutEventList.
     *
     * @param id the id of the indexPayoutEventList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the indexPayoutEventList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IndexPayoutEventList> getIndexPayoutEventList(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IndexPayoutEventList : {}", id);
        Optional<IndexPayoutEventList> indexPayoutEventList = indexPayoutEventListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(indexPayoutEventList);
    }

    /**
     * {@code DELETE  /index-payout-event-lists/:id} : delete the "id" indexPayoutEventList.
     *
     * @param id the id of the indexPayoutEventList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndexPayoutEventList(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IndexPayoutEventList : {}", id);
        indexPayoutEventListService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
