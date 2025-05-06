package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.InsurancePolicyDamage;
import lk.geoedge.interoperability.repository.InsurancePolicyDamageRepository;
import lk.geoedge.interoperability.service.criteria.InsurancePolicyDamageCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link InsurancePolicyDamage} entities in the database.
 * The main input is a {@link InsurancePolicyDamageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InsurancePolicyDamage} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InsurancePolicyDamageQueryService extends QueryService<InsurancePolicyDamage> {

    private static final Logger LOG = LoggerFactory.getLogger(InsurancePolicyDamageQueryService.class);

    private final InsurancePolicyDamageRepository insurancePolicyDamageRepository;

    public InsurancePolicyDamageQueryService(InsurancePolicyDamageRepository insurancePolicyDamageRepository) {
        this.insurancePolicyDamageRepository = insurancePolicyDamageRepository;
    }

    /**
     * Return a {@link Page} of {@link InsurancePolicyDamage} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InsurancePolicyDamage> findByCriteria(InsurancePolicyDamageCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InsurancePolicyDamage> specification = createSpecification(criteria);
        return insurancePolicyDamageRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InsurancePolicyDamageCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InsurancePolicyDamage> specification = createSpecification(criteria);
        return insurancePolicyDamageRepository.count(specification);
    }

    /**
     * Function to convert {@link InsurancePolicyDamageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InsurancePolicyDamage> createSpecification(InsurancePolicyDamageCriteria criteria) {
        Specification<InsurancePolicyDamage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), InsurancePolicyDamage_.id),
                buildRangeSpecification(criteria.getPercentage(), InsurancePolicyDamage_.percentage),
                buildRangeSpecification(criteria.getIsFree(), InsurancePolicyDamage_.isFree),
                buildRangeSpecification(criteria.getIsPaid(), InsurancePolicyDamage_.isPaid),
                buildSpecification(criteria.getInsurancePolicyId(), root ->
                    root.join(InsurancePolicyDamage_.insurancePolicy, JoinType.LEFT).get(InsurancePolicy_.id)
                ),
                buildSpecification(criteria.getDamageReasonId(), root ->
                    root.join(InsurancePolicyDamage_.damageReason, JoinType.LEFT).get(InsurancePolicyDamageCultivatedLandDamageReason_.id)
                )
            );
        }
        return specification;
    }
}
