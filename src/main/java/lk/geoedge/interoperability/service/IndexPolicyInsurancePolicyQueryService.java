package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.IndexPolicyInsurancePolicy;
import lk.geoedge.interoperability.repository.IndexPolicyInsurancePolicyRepository;
import lk.geoedge.interoperability.service.criteria.IndexPolicyInsurancePolicyCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link IndexPolicyInsurancePolicy} entities in the database.
 * The main input is a {@link IndexPolicyInsurancePolicyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link IndexPolicyInsurancePolicy} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IndexPolicyInsurancePolicyQueryService extends QueryService<IndexPolicyInsurancePolicy> {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPolicyInsurancePolicyQueryService.class);

    private final IndexPolicyInsurancePolicyRepository indexPolicyInsurancePolicyRepository;

    public IndexPolicyInsurancePolicyQueryService(IndexPolicyInsurancePolicyRepository indexPolicyInsurancePolicyRepository) {
        this.indexPolicyInsurancePolicyRepository = indexPolicyInsurancePolicyRepository;
    }

    /**
     * Return a {@link Page} of {@link IndexPolicyInsurancePolicy} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IndexPolicyInsurancePolicy> findByCriteria(IndexPolicyInsurancePolicyCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IndexPolicyInsurancePolicy> specification = createSpecification(criteria);
        return indexPolicyInsurancePolicyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IndexPolicyInsurancePolicyCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<IndexPolicyInsurancePolicy> specification = createSpecification(criteria);
        return indexPolicyInsurancePolicyRepository.count(specification);
    }

    /**
     * Function to convert {@link IndexPolicyInsurancePolicyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IndexPolicyInsurancePolicy> createSpecification(IndexPolicyInsurancePolicyCriteria criteria) {
        Specification<IndexPolicyInsurancePolicy> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), IndexPolicyInsurancePolicy_.id),
                buildStringSpecification(criteria.getName(), IndexPolicyInsurancePolicy_.name),
                buildStringSpecification(criteria.getPolicyNo(), IndexPolicyInsurancePolicy_.policyNo),
                buildRangeSpecification(criteria.getIsActivate(), IndexPolicyInsurancePolicy_.isActivate)
            );
        }
        return specification;
    }
}
