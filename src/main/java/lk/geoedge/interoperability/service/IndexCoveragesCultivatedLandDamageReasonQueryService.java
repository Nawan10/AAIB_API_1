package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.IndexCoveragesCultivatedLandDamageReason;
import lk.geoedge.interoperability.repository.IndexCoveragesCultivatedLandDamageReasonRepository;
import lk.geoedge.interoperability.service.criteria.IndexCoveragesCultivatedLandDamageReasonCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link IndexCoveragesCultivatedLandDamageReason} entities in the database.
 * The main input is a {@link IndexCoveragesCultivatedLandDamageReasonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link IndexCoveragesCultivatedLandDamageReason} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IndexCoveragesCultivatedLandDamageReasonQueryService extends QueryService<IndexCoveragesCultivatedLandDamageReason> {

    private static final Logger LOG = LoggerFactory.getLogger(IndexCoveragesCultivatedLandDamageReasonQueryService.class);

    private final IndexCoveragesCultivatedLandDamageReasonRepository indexCoveragesCultivatedLandDamageReasonRepository;

    public IndexCoveragesCultivatedLandDamageReasonQueryService(
        IndexCoveragesCultivatedLandDamageReasonRepository indexCoveragesCultivatedLandDamageReasonRepository
    ) {
        this.indexCoveragesCultivatedLandDamageReasonRepository = indexCoveragesCultivatedLandDamageReasonRepository;
    }

    /**
     * Return a {@link Page} of {@link IndexCoveragesCultivatedLandDamageReason} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IndexCoveragesCultivatedLandDamageReason> findByCriteria(
        IndexCoveragesCultivatedLandDamageReasonCriteria criteria,
        Pageable page
    ) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IndexCoveragesCultivatedLandDamageReason> specification = createSpecification(criteria);
        return indexCoveragesCultivatedLandDamageReasonRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IndexCoveragesCultivatedLandDamageReasonCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<IndexCoveragesCultivatedLandDamageReason> specification = createSpecification(criteria);
        return indexCoveragesCultivatedLandDamageReasonRepository.count(specification);
    }

    /**
     * Function to convert {@link IndexCoveragesCultivatedLandDamageReasonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IndexCoveragesCultivatedLandDamageReason> createSpecification(
        IndexCoveragesCultivatedLandDamageReasonCriteria criteria
    ) {
        Specification<IndexCoveragesCultivatedLandDamageReason> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), IndexCoveragesCultivatedLandDamageReason_.id),
                buildStringSpecification(criteria.getName(), IndexCoveragesCultivatedLandDamageReason_.name)
            );
        }
        return specification;
    }
}
