package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CropDamageCropType;
import lk.geoedge.interoperability.repository.CropDamageCropTypeRepository;
import lk.geoedge.interoperability.service.criteria.CropDamageCropTypeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CropDamageCropType} entities in the database.
 * The main input is a {@link CropDamageCropTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CropDamageCropType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CropDamageCropTypeQueryService extends QueryService<CropDamageCropType> {

    private static final Logger LOG = LoggerFactory.getLogger(CropDamageCropTypeQueryService.class);

    private final CropDamageCropTypeRepository cropDamageCropTypeRepository;

    public CropDamageCropTypeQueryService(CropDamageCropTypeRepository cropDamageCropTypeRepository) {
        this.cropDamageCropTypeRepository = cropDamageCropTypeRepository;
    }

    /**
     * Return a {@link Page} of {@link CropDamageCropType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CropDamageCropType> findByCriteria(CropDamageCropTypeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CropDamageCropType> specification = createSpecification(criteria);
        return cropDamageCropTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CropDamageCropTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CropDamageCropType> specification = createSpecification(criteria);
        return cropDamageCropTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CropDamageCropTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CropDamageCropType> createSpecification(CropDamageCropTypeCriteria criteria) {
        Specification<CropDamageCropType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CropDamageCropType_.id),
                buildStringSpecification(criteria.getCrop(), CropDamageCropType_.crop),
                buildStringSpecification(criteria.getImage(), CropDamageCropType_.image),
                buildRangeSpecification(criteria.getMainCrop(), CropDamageCropType_.mainCrop),
                buildStringSpecification(criteria.getCropCode(), CropDamageCropType_.cropCode),
                buildStringSpecification(criteria.getNoOfStages(), CropDamageCropType_.noOfStages),
                buildStringSpecification(criteria.getDescription(), CropDamageCropType_.description),
                buildRangeSpecification(criteria.getCropTypesId(), CropDamageCropType_.cropTypesId),
                buildRangeSpecification(criteria.getUnitsId(), CropDamageCropType_.unitsId),
                buildRangeSpecification(criteria.getArea(), CropDamageCropType_.area),
                buildRangeSpecification(criteria.getSumInsured(), CropDamageCropType_.sumInsured),
                buildRangeSpecification(criteria.getMinSumInsured(), CropDamageCropType_.minSumInsured),
                buildRangeSpecification(criteria.getMaxSumInsured(), CropDamageCropType_.maxSumInsured),
                buildRangeSpecification(criteria.getSubsidisedPremiumRate(), CropDamageCropType_.subsidisedPremiumRate)
            );
        }
        return specification;
    }
}
