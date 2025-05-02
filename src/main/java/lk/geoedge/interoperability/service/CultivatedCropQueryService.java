package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CultivatedCrop;
import lk.geoedge.interoperability.repository.CultivatedCropRepository;
import lk.geoedge.interoperability.service.criteria.CultivatedCropCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CultivatedCrop} entities in the database.
 * The main input is a {@link CultivatedCropCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CultivatedCrop} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CultivatedCropQueryService extends QueryService<CultivatedCrop> {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedCropQueryService.class);

    private final CultivatedCropRepository cultivatedCropRepository;

    public CultivatedCropQueryService(CultivatedCropRepository cultivatedCropRepository) {
        this.cultivatedCropRepository = cultivatedCropRepository;
    }

    /**
     * Return a {@link Page} of {@link CultivatedCrop} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CultivatedCrop> findByCriteria(CultivatedCropCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CultivatedCrop> specification = createSpecification(criteria);
        return cultivatedCropRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CultivatedCropCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CultivatedCrop> specification = createSpecification(criteria);
        return cultivatedCropRepository.count(specification);
    }

    /**
     * Function to convert {@link CultivatedCropCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CultivatedCrop> createSpecification(CultivatedCropCriteria criteria) {
        Specification<CultivatedCrop> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CultivatedCrop_.id),
                buildRangeSpecification(criteria.getCultivatedExtend(), CultivatedCrop_.cultivatedExtend),
                buildRangeSpecification(criteria.getStartDate(), CultivatedCrop_.startDate),
                buildRangeSpecification(criteria.getEndDate(), CultivatedCrop_.endDate),
                buildRangeSpecification(criteria.getYield(), CultivatedCrop_.yield),
                buildStringSpecification(criteria.getUnitId(), CultivatedCrop_.unitId),
                buildRangeSpecification(criteria.getCreatedAt(), CultivatedCrop_.createdAt),
                buildStringSpecification(criteria.getAddedBy(), CultivatedCrop_.addedBy),
                buildSpecification(criteria.getCultivatedLandId(), root ->
                    root.join(CultivatedCrop_.cultivatedLand, JoinType.LEFT).get(CultivatedCropCultivatedLand_.id)
                ),
                buildSpecification(criteria.getCropId(), root ->
                    root.join(CultivatedCrop_.crop, JoinType.LEFT).get(CultivatedCropCropType_.id)
                )
            );
        }
        return specification;
    }
}
