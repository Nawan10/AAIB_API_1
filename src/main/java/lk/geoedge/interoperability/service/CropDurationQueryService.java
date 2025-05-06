package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CropDuration;
import lk.geoedge.interoperability.repository.CropDurationRepository;
import lk.geoedge.interoperability.service.criteria.CropDurationCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CropDuration} entities in the database.
 * The main input is a {@link CropDurationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CropDuration} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CropDurationQueryService extends QueryService<CropDuration> {

    private static final Logger LOG = LoggerFactory.getLogger(CropDurationQueryService.class);

    private final CropDurationRepository cropDurationRepository;

    public CropDurationQueryService(CropDurationRepository cropDurationRepository) {
        this.cropDurationRepository = cropDurationRepository;
    }

    /**
     * Return a {@link Page} of {@link CropDuration} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CropDuration> findByCriteria(CropDurationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CropDuration> specification = createSpecification(criteria);
        return cropDurationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CropDurationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CropDuration> specification = createSpecification(criteria);
        return cropDurationRepository.count(specification);
    }

    /**
     * Function to convert {@link CropDurationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CropDuration> createSpecification(CropDurationCriteria criteria) {
        Specification<CropDuration> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CropDuration_.id),
                buildRangeSpecification(criteria.getDuration(), CropDuration_.duration),
                buildStringSpecification(criteria.getName(), CropDuration_.name),
                buildStringSpecification(criteria.getStages(), CropDuration_.stages),
                buildRangeSpecification(criteria.getAddedBy(), CropDuration_.addedBy),
                buildRangeSpecification(criteria.getAddedDate(), CropDuration_.addedDate),
                buildSpecification(criteria.getCropId(), root -> root.join(CropDuration_.crop, JoinType.LEFT).get(CropDurationCropType_.id))
            );
        }
        return specification;
    }
}
