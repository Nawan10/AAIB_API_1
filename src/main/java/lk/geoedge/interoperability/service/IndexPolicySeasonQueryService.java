package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.IndexPolicySeason;
import lk.geoedge.interoperability.repository.IndexPolicySeasonRepository;
import lk.geoedge.interoperability.service.criteria.IndexPolicySeasonCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link IndexPolicySeason} entities in the database.
 * The main input is a {@link IndexPolicySeasonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link IndexPolicySeason} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IndexPolicySeasonQueryService extends QueryService<IndexPolicySeason> {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPolicySeasonQueryService.class);

    private final IndexPolicySeasonRepository indexPolicySeasonRepository;

    public IndexPolicySeasonQueryService(IndexPolicySeasonRepository indexPolicySeasonRepository) {
        this.indexPolicySeasonRepository = indexPolicySeasonRepository;
    }

    /**
     * Return a {@link Page} of {@link IndexPolicySeason} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IndexPolicySeason> findByCriteria(IndexPolicySeasonCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IndexPolicySeason> specification = createSpecification(criteria);
        return indexPolicySeasonRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IndexPolicySeasonCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<IndexPolicySeason> specification = createSpecification(criteria);
        return indexPolicySeasonRepository.count(specification);
    }

    /**
     * Function to convert {@link IndexPolicySeasonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IndexPolicySeason> createSpecification(IndexPolicySeasonCriteria criteria) {
        Specification<IndexPolicySeason> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), IndexPolicySeason_.id),
                buildStringSpecification(criteria.getName(), IndexPolicySeason_.name),
                buildStringSpecification(criteria.getPeriod(), IndexPolicySeason_.period)
            );
        }
        return specification;
    }
}
