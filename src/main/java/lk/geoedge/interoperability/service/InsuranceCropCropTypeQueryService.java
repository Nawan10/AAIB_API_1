package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.InsuranceCropCropType;
import lk.geoedge.interoperability.repository.InsuranceCropCropTypeRepository;
import lk.geoedge.interoperability.service.criteria.InsuranceCropCropTypeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link InsuranceCropCropType} entities in the database.
 * The main input is a {@link InsuranceCropCropTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InsuranceCropCropType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InsuranceCropCropTypeQueryService extends QueryService<InsuranceCropCropType> {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCropCropTypeQueryService.class);

    private final InsuranceCropCropTypeRepository insuranceCropCropTypeRepository;

    public InsuranceCropCropTypeQueryService(InsuranceCropCropTypeRepository insuranceCropCropTypeRepository) {
        this.insuranceCropCropTypeRepository = insuranceCropCropTypeRepository;
    }

    /**
     * Return a {@link Page} of {@link InsuranceCropCropType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InsuranceCropCropType> findByCriteria(InsuranceCropCropTypeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InsuranceCropCropType> specification = createSpecification(criteria);
        return insuranceCropCropTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InsuranceCropCropTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InsuranceCropCropType> specification = createSpecification(criteria);
        return insuranceCropCropTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link InsuranceCropCropTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InsuranceCropCropType> createSpecification(InsuranceCropCropTypeCriteria criteria) {
        Specification<InsuranceCropCropType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), InsuranceCropCropType_.id),
                buildStringSpecification(criteria.getCrop(), InsuranceCropCropType_.crop),
                buildStringSpecification(criteria.getImage(), InsuranceCropCropType_.image),
                buildRangeSpecification(criteria.getMainCrop(), InsuranceCropCropType_.mainCrop),
                buildStringSpecification(criteria.getCropCode(), InsuranceCropCropType_.cropCode),
                buildStringSpecification(criteria.getNoOfStages(), InsuranceCropCropType_.noOfStages),
                buildStringSpecification(criteria.getDescription(), InsuranceCropCropType_.description),
                buildRangeSpecification(criteria.getCropTypesId(), InsuranceCropCropType_.cropTypesId),
                buildRangeSpecification(criteria.getUnitsId(), InsuranceCropCropType_.unitsId),
                buildRangeSpecification(criteria.getArea(), InsuranceCropCropType_.area),
                buildRangeSpecification(criteria.getSumInsured(), InsuranceCropCropType_.sumInsured),
                buildRangeSpecification(criteria.getMinSumInsured(), InsuranceCropCropType_.minSumInsured),
                buildRangeSpecification(criteria.getMaxSumInsured(), InsuranceCropCropType_.maxSumInsured),
                buildRangeSpecification(criteria.getSubsidisedPremiumRate(), InsuranceCropCropType_.subsidisedPremiumRate)
            );
        }
        return specification;
    }
}
