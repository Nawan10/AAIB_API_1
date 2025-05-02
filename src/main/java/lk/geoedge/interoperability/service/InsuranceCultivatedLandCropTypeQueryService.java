package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCropType;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandCropTypeRepository;
import lk.geoedge.interoperability.service.criteria.InsuranceCultivatedLandCropTypeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link InsuranceCultivatedLandCropType} entities in the database.
 * The main input is a {@link InsuranceCultivatedLandCropTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InsuranceCultivatedLandCropType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InsuranceCultivatedLandCropTypeQueryService extends QueryService<InsuranceCultivatedLandCropType> {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCultivatedLandCropTypeQueryService.class);

    private final InsuranceCultivatedLandCropTypeRepository insuranceCultivatedLandCropTypeRepository;

    public InsuranceCultivatedLandCropTypeQueryService(
        InsuranceCultivatedLandCropTypeRepository insuranceCultivatedLandCropTypeRepository
    ) {
        this.insuranceCultivatedLandCropTypeRepository = insuranceCultivatedLandCropTypeRepository;
    }

    /**
     * Return a {@link Page} of {@link InsuranceCultivatedLandCropType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InsuranceCultivatedLandCropType> findByCriteria(InsuranceCultivatedLandCropTypeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InsuranceCultivatedLandCropType> specification = createSpecification(criteria);
        return insuranceCultivatedLandCropTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InsuranceCultivatedLandCropTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InsuranceCultivatedLandCropType> specification = createSpecification(criteria);
        return insuranceCultivatedLandCropTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link InsuranceCultivatedLandCropTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InsuranceCultivatedLandCropType> createSpecification(InsuranceCultivatedLandCropTypeCriteria criteria) {
        Specification<InsuranceCultivatedLandCropType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), InsuranceCultivatedLandCropType_.id),
                buildStringSpecification(criteria.getCrop(), InsuranceCultivatedLandCropType_.crop),
                buildStringSpecification(criteria.getImage(), InsuranceCultivatedLandCropType_.image),
                buildRangeSpecification(criteria.getMainCrop(), InsuranceCultivatedLandCropType_.mainCrop),
                buildStringSpecification(criteria.getCropCode(), InsuranceCultivatedLandCropType_.cropCode),
                buildStringSpecification(criteria.getNoOfStages(), InsuranceCultivatedLandCropType_.noOfStages),
                buildStringSpecification(criteria.getDescription(), InsuranceCultivatedLandCropType_.description),
                buildRangeSpecification(criteria.getCropTypesId(), InsuranceCultivatedLandCropType_.cropTypesId),
                buildRangeSpecification(criteria.getUnitsId(), InsuranceCultivatedLandCropType_.unitsId),
                buildRangeSpecification(criteria.getArea(), InsuranceCultivatedLandCropType_.area),
                buildRangeSpecification(criteria.getSumInsured(), InsuranceCultivatedLandCropType_.sumInsured),
                buildRangeSpecification(criteria.getMinSumInsured(), InsuranceCultivatedLandCropType_.minSumInsured),
                buildRangeSpecification(criteria.getMaxSumInsured(), InsuranceCultivatedLandCropType_.maxSumInsured),
                buildRangeSpecification(criteria.getSubsidisedPremiumRate(), InsuranceCultivatedLandCropType_.subsidisedPremiumRate)
            );
        }
        return specification;
    }
}
