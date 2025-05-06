package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoveragesInsuranceCultivatedLand;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository;
import lk.geoedge.interoperability.service.criteria.InsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link InsuranceCultivatedLandCoveragesInsuranceCultivatedLand} entities in the database.
 * The main input is a {@link InsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InsuranceCultivatedLandCoveragesInsuranceCultivatedLand} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InsuranceCultivatedLandCoveragesInsuranceCultivatedLandQueryService
    extends QueryService<InsuranceCultivatedLandCoveragesInsuranceCultivatedLand> {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCultivatedLandCoveragesInsuranceCultivatedLandQueryService.class);

    private final InsuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository;

    public InsuranceCultivatedLandCoveragesInsuranceCultivatedLandQueryService(
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository
    ) {
        this.insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository;
    }

    /**
     * Return a {@link Page} of {@link InsuranceCultivatedLandCoveragesInsuranceCultivatedLand} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InsuranceCultivatedLandCoveragesInsuranceCultivatedLand> findByCriteria(
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria criteria,
        Pageable page
    ) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InsuranceCultivatedLandCoveragesInsuranceCultivatedLand> specification = createSpecification(criteria);
        return insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InsuranceCultivatedLandCoveragesInsuranceCultivatedLand> specification = createSpecification(criteria);
        return insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.count(specification);
    }

    /**
     * Function to convert {@link InsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InsuranceCultivatedLandCoveragesInsuranceCultivatedLand> createSpecification(
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria criteria
    ) {
        Specification<InsuranceCultivatedLandCoveragesInsuranceCultivatedLand> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), InsuranceCultivatedLandCoveragesInsuranceCultivatedLand_.id),
                buildStringSpecification(
                    criteria.getCropDurationId(),
                    InsuranceCultivatedLandCoveragesInsuranceCultivatedLand_.cropDurationId
                ),
                buildStringSpecification(
                    criteria.getInsurancePoliceId(),
                    InsuranceCultivatedLandCoveragesInsuranceCultivatedLand_.insurancePoliceId
                ),
                buildRangeSpecification(
                    criteria.getSumInsuredPerAcre(),
                    InsuranceCultivatedLandCoveragesInsuranceCultivatedLand_.sumInsuredPerAcre
                ),
                buildRangeSpecification(
                    criteria.getInsuranceExtent(),
                    InsuranceCultivatedLandCoveragesInsuranceCultivatedLand_.insuranceExtent
                ),
                buildRangeSpecification(criteria.getSumAmount(), InsuranceCultivatedLandCoveragesInsuranceCultivatedLand_.sumAmount),
                buildStringSpecification(
                    criteria.getInsuranceStatus(),
                    InsuranceCultivatedLandCoveragesInsuranceCultivatedLand_.insuranceStatus
                ),
                buildRangeSpecification(criteria.getCreatedAt(), InsuranceCultivatedLandCoveragesInsuranceCultivatedLand_.createdAt),
                buildStringSpecification(criteria.getAddedBy(), InsuranceCultivatedLandCoveragesInsuranceCultivatedLand_.addedBy)
            );
        }
        return specification;
    }
}
