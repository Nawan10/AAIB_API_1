package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.InsuranceCultivatedLand;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandRepository;
import lk.geoedge.interoperability.service.criteria.InsuranceCultivatedLandCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link InsuranceCultivatedLand} entities in the database.
 * The main input is a {@link InsuranceCultivatedLandCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InsuranceCultivatedLand} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InsuranceCultivatedLandQueryService extends QueryService<InsuranceCultivatedLand> {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCultivatedLandQueryService.class);

    private final InsuranceCultivatedLandRepository insuranceCultivatedLandRepository;

    public InsuranceCultivatedLandQueryService(InsuranceCultivatedLandRepository insuranceCultivatedLandRepository) {
        this.insuranceCultivatedLandRepository = insuranceCultivatedLandRepository;
    }

    /**
     * Return a {@link Page} of {@link InsuranceCultivatedLand} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InsuranceCultivatedLand> findByCriteria(InsuranceCultivatedLandCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InsuranceCultivatedLand> specification = createSpecification(criteria);
        return insuranceCultivatedLandRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InsuranceCultivatedLandCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InsuranceCultivatedLand> specification = createSpecification(criteria);
        return insuranceCultivatedLandRepository.count(specification);
    }

    /**
     * Function to convert {@link InsuranceCultivatedLandCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InsuranceCultivatedLand> createSpecification(InsuranceCultivatedLandCriteria criteria) {
        Specification<InsuranceCultivatedLand> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), InsuranceCultivatedLand_.id),
                buildStringSpecification(criteria.getCropDurationId(), InsuranceCultivatedLand_.cropDurationId),
                buildStringSpecification(criteria.getInsurancePoliceId(), InsuranceCultivatedLand_.insurancePoliceId),
                buildRangeSpecification(criteria.getSumInsuredPerAcre(), InsuranceCultivatedLand_.sumInsuredPerAcre),
                buildRangeSpecification(criteria.getInsuranceExtent(), InsuranceCultivatedLand_.insuranceExtent),
                buildRangeSpecification(criteria.getSumAmount(), InsuranceCultivatedLand_.sumAmount),
                buildStringSpecification(criteria.getInsuranceStatus(), InsuranceCultivatedLand_.insuranceStatus),
                buildRangeSpecification(criteria.getCreatedAt(), InsuranceCultivatedLand_.createdAt),
                buildStringSpecification(criteria.getAddedBy(), InsuranceCultivatedLand_.addedBy),
                buildSpecification(criteria.getFarmerId(), root -> root.join(InsuranceCultivatedLand_.farmer, JoinType.LEFT).get(Farmer_.id)
                ),
                buildSpecification(criteria.getCultivatedLandId(), root ->
                    root.join(InsuranceCultivatedLand_.cultivatedLand, JoinType.LEFT).get(CultivatedLand_.id)
                ),
                buildSpecification(criteria.getCropId(), root ->
                    root.join(InsuranceCultivatedLand_.crop, JoinType.LEFT).get(InsuranceCultivatedLandCropType_.id)
                )
            );
        }
        return specification;
    }
}
