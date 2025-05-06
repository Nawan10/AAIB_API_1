package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CropDurationCropType;
import lk.geoedge.interoperability.repository.CropDurationCropTypeRepository;
import lk.geoedge.interoperability.service.criteria.CropDurationCropTypeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CropDurationCropType} entities in the database.
 * The main input is a {@link CropDurationCropTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CropDurationCropType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CropDurationCropTypeQueryService extends QueryService<CropDurationCropType> {

    private static final Logger LOG = LoggerFactory.getLogger(CropDurationCropTypeQueryService.class);

    private final CropDurationCropTypeRepository cropDurationCropTypeRepository;

    public CropDurationCropTypeQueryService(CropDurationCropTypeRepository cropDurationCropTypeRepository) {
        this.cropDurationCropTypeRepository = cropDurationCropTypeRepository;
    }

    /**
     * Return a {@link Page} of {@link CropDurationCropType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CropDurationCropType> findByCriteria(CropDurationCropTypeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CropDurationCropType> specification = createSpecification(criteria);
        return cropDurationCropTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CropDurationCropTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CropDurationCropType> specification = createSpecification(criteria);
        return cropDurationCropTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CropDurationCropTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CropDurationCropType> createSpecification(CropDurationCropTypeCriteria criteria) {
        Specification<CropDurationCropType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CropDurationCropType_.id),
                buildStringSpecification(criteria.getCrop(), CropDurationCropType_.crop),
                buildStringSpecification(criteria.getImage(), CropDurationCropType_.image),
                buildRangeSpecification(criteria.getMainCrop(), CropDurationCropType_.mainCrop),
                buildStringSpecification(criteria.getCropCode(), CropDurationCropType_.cropCode),
                buildStringSpecification(criteria.getNoOfStages(), CropDurationCropType_.noOfStages),
                buildStringSpecification(criteria.getDescription(), CropDurationCropType_.description),
                buildRangeSpecification(criteria.getCropTypesId(), CropDurationCropType_.cropTypesId),
                buildRangeSpecification(criteria.getUnitsId(), CropDurationCropType_.unitsId),
                buildRangeSpecification(criteria.getArea(), CropDurationCropType_.area),
                buildRangeSpecification(criteria.getSumInsured(), CropDurationCropType_.sumInsured),
                buildRangeSpecification(criteria.getMinSumInsured(), CropDurationCropType_.minSumInsured),
                buildRangeSpecification(criteria.getMaxSumInsured(), CropDurationCropType_.maxSumInsured),
                buildRangeSpecification(criteria.getSubsidisedPremiumRate(), CropDurationCropType_.subsidisedPremiumRate)
            );
        }
        return specification;
    }
}
