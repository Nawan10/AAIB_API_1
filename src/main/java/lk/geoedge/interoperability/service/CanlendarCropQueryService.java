package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CanlendarCrop;
import lk.geoedge.interoperability.repository.CanlendarCropRepository;
import lk.geoedge.interoperability.service.criteria.CanlendarCropCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CanlendarCrop} entities in the database.
 * The main input is a {@link CanlendarCropCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CanlendarCrop} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CanlendarCropQueryService extends QueryService<CanlendarCrop> {

    private static final Logger LOG = LoggerFactory.getLogger(CanlendarCropQueryService.class);

    private final CanlendarCropRepository canlendarCropRepository;

    public CanlendarCropQueryService(CanlendarCropRepository canlendarCropRepository) {
        this.canlendarCropRepository = canlendarCropRepository;
    }

    /**
     * Return a {@link Page} of {@link CanlendarCrop} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CanlendarCrop> findByCriteria(CanlendarCropCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CanlendarCrop> specification = createSpecification(criteria);
        return canlendarCropRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CanlendarCropCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CanlendarCrop> specification = createSpecification(criteria);
        return canlendarCropRepository.count(specification);
    }

    /**
     * Function to convert {@link CanlendarCropCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CanlendarCrop> createSpecification(CanlendarCropCriteria criteria) {
        Specification<CanlendarCrop> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CanlendarCrop_.id),
                buildRangeSpecification(criteria.getStartDate(), CanlendarCrop_.startDate),
                buildRangeSpecification(criteria.getEndDate(), CanlendarCrop_.endDate),
                buildRangeSpecification(criteria.getPercentage(), CanlendarCrop_.percentage),
                buildRangeSpecification(criteria.getCanlendarCropStatus(), CanlendarCrop_.canlendarCropStatus),
                buildStringSpecification(criteria.getReason(), CanlendarCrop_.reason),
                buildRangeSpecification(criteria.getCreatedAt(), CanlendarCrop_.createdAt),
                buildStringSpecification(criteria.getAddedBy(), CanlendarCrop_.addedBy),
                buildSpecification(criteria.getSeasonId(), root ->
                    root.join(CanlendarCrop_.season, JoinType.LEFT).get(CanlendarCropSeason_.id)
                ),
                buildSpecification(criteria.getCropId(), root ->
                    root.join(CanlendarCrop_.crop, JoinType.LEFT).get(CanlendarCropCropType_.id)
                )
            );
        }
        return specification;
    }
}
