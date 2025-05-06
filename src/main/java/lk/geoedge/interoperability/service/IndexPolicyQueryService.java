package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.IndexPolicy;
import lk.geoedge.interoperability.repository.IndexPolicyRepository;
import lk.geoedge.interoperability.service.criteria.IndexPolicyCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link IndexPolicy} entities in the database.
 * The main input is a {@link IndexPolicyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link IndexPolicy} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IndexPolicyQueryService extends QueryService<IndexPolicy> {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPolicyQueryService.class);

    private final IndexPolicyRepository indexPolicyRepository;

    public IndexPolicyQueryService(IndexPolicyRepository indexPolicyRepository) {
        this.indexPolicyRepository = indexPolicyRepository;
    }

    /**
     * Return a {@link Page} of {@link IndexPolicy} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IndexPolicy> findByCriteria(IndexPolicyCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IndexPolicy> specification = createSpecification(criteria);
        return indexPolicyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IndexPolicyCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<IndexPolicy> specification = createSpecification(criteria);
        return indexPolicyRepository.count(specification);
    }

    /**
     * Function to convert {@link IndexPolicyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IndexPolicy> createSpecification(IndexPolicyCriteria criteria) {
        Specification<IndexPolicy> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), IndexPolicy_.id),
                buildRangeSpecification(criteria.getStartDate(), IndexPolicy_.startDate),
                buildRangeSpecification(criteria.getEndDate(), IndexPolicy_.endDate),
                buildRangeSpecification(criteria.getStageNo(), IndexPolicy_.stageNo),
                buildRangeSpecification(criteria.getIndexStatus(), IndexPolicy_.indexStatus),
                buildSpecification(criteria.getPolicyId(), root ->
                    root.join(IndexPolicy_.policy, JoinType.LEFT).get(IndexPolicyInsurancePolicy_.id)
                ),
                buildSpecification(criteria.getSeasonId(), root -> root.join(IndexPolicy_.season, JoinType.LEFT).get(IndexPolicySeason_.id)
                ),
                buildSpecification(criteria.getCropVarietyId(), root ->
                    root.join(IndexPolicy_.cropVariety, JoinType.LEFT).get(IndexPolicyCropVariety_.id)
                ),
                buildSpecification(criteria.getCropId(), root -> root.join(IndexPolicy_.crop, JoinType.LEFT).get(IndexPolicyCropType_.id)),
                buildSpecification(criteria.getWeatherStationId(), root ->
                    root.join(IndexPolicy_.weatherStation, JoinType.LEFT).get(IndexPolicyWeatherStation_.id)
                )
            );
        }
        return specification;
    }
}
