package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.InsurancePolicyDamageCultivatedLandDamageReason;
import lk.geoedge.interoperability.repository.InsurancePolicyDamageCultivatedLandDamageReasonRepository;
import lk.geoedge.interoperability.service.criteria.InsurancePolicyDamageCultivatedLandDamageReasonCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link InsurancePolicyDamageCultivatedLandDamageReason} entities in the database.
 * The main input is a {@link InsurancePolicyDamageCultivatedLandDamageReasonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InsurancePolicyDamageCultivatedLandDamageReason} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InsurancePolicyDamageCultivatedLandDamageReasonQueryService
    extends QueryService<InsurancePolicyDamageCultivatedLandDamageReason> {

    private static final Logger LOG = LoggerFactory.getLogger(InsurancePolicyDamageCultivatedLandDamageReasonQueryService.class);

    private final InsurancePolicyDamageCultivatedLandDamageReasonRepository insurancePolicyDamageCultivatedLandDamageReasonRepository;

    public InsurancePolicyDamageCultivatedLandDamageReasonQueryService(
        InsurancePolicyDamageCultivatedLandDamageReasonRepository insurancePolicyDamageCultivatedLandDamageReasonRepository
    ) {
        this.insurancePolicyDamageCultivatedLandDamageReasonRepository = insurancePolicyDamageCultivatedLandDamageReasonRepository;
    }

    /**
     * Return a {@link Page} of {@link InsurancePolicyDamageCultivatedLandDamageReason} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InsurancePolicyDamageCultivatedLandDamageReason> findByCriteria(
        InsurancePolicyDamageCultivatedLandDamageReasonCriteria criteria,
        Pageable page
    ) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InsurancePolicyDamageCultivatedLandDamageReason> specification = createSpecification(criteria);
        return insurancePolicyDamageCultivatedLandDamageReasonRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InsurancePolicyDamageCultivatedLandDamageReasonCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InsurancePolicyDamageCultivatedLandDamageReason> specification = createSpecification(criteria);
        return insurancePolicyDamageCultivatedLandDamageReasonRepository.count(specification);
    }

    /**
     * Function to convert {@link InsurancePolicyDamageCultivatedLandDamageReasonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InsurancePolicyDamageCultivatedLandDamageReason> createSpecification(
        InsurancePolicyDamageCultivatedLandDamageReasonCriteria criteria
    ) {
        Specification<InsurancePolicyDamageCultivatedLandDamageReason> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), InsurancePolicyDamageCultivatedLandDamageReason_.id),
                buildStringSpecification(criteria.getName(), InsurancePolicyDamageCultivatedLandDamageReason_.name),
                buildRangeSpecification(criteria.getDamageCategoryId(), InsurancePolicyDamageCultivatedLandDamageReason_.damageCategoryId),
                buildRangeSpecification(criteria.getDamageTypeId(), InsurancePolicyDamageCultivatedLandDamageReason_.damageTypeId)
            );
        }
        return specification;
    }
}
