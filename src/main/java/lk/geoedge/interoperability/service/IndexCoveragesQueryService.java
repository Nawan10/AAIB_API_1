package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.IndexCoverages;
import lk.geoedge.interoperability.repository.IndexCoveragesRepository;
import lk.geoedge.interoperability.service.criteria.IndexCoveragesCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link IndexCoverages} entities in the database.
 * The main input is a {@link IndexCoveragesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link IndexCoverages} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IndexCoveragesQueryService extends QueryService<IndexCoverages> {

    private static final Logger LOG = LoggerFactory.getLogger(IndexCoveragesQueryService.class);

    private final IndexCoveragesRepository indexCoveragesRepository;

    public IndexCoveragesQueryService(IndexCoveragesRepository indexCoveragesRepository) {
        this.indexCoveragesRepository = indexCoveragesRepository;
    }

    /**
     * Return a {@link Page} of {@link IndexCoverages} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IndexCoverages> findByCriteria(IndexCoveragesCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IndexCoverages> specification = createSpecification(criteria);
        return indexCoveragesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IndexCoveragesCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<IndexCoverages> specification = createSpecification(criteria);
        return indexCoveragesRepository.count(specification);
    }

    /**
     * Function to convert {@link IndexCoveragesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IndexCoverages> createSpecification(IndexCoveragesCriteria criteria) {
        Specification<IndexCoverages> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), IndexCoverages_.id),
                buildRangeSpecification(criteria.getIndexProductId(), IndexCoverages_.indexProductId),
                buildRangeSpecification(criteria.getPremiumRate(), IndexCoverages_.premiumRate),
                buildRangeSpecification(criteria.getIsFree(), IndexCoverages_.isFree),
                buildRangeSpecification(criteria.getIsPaid(), IndexCoverages_.isPaid),
                buildSpecification(criteria.getDamageReasonId(), root ->
                    root.join(IndexCoverages_.damageReason, JoinType.LEFT).get(IndexCoveragesCultivatedLandDamageReason_.id)
                )
            );
        }
        return specification;
    }
}
