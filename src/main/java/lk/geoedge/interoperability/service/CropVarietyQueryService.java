package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CropVariety;
import lk.geoedge.interoperability.repository.CropVarietyRepository;
import lk.geoedge.interoperability.service.criteria.CropVarietyCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CropVariety} entities in the database.
 * The main input is a {@link CropVarietyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CropVariety} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CropVarietyQueryService extends QueryService<CropVariety> {

    private static final Logger LOG = LoggerFactory.getLogger(CropVarietyQueryService.class);

    private final CropVarietyRepository cropVarietyRepository;

    public CropVarietyQueryService(CropVarietyRepository cropVarietyRepository) {
        this.cropVarietyRepository = cropVarietyRepository;
    }

    /**
     * Return a {@link Page} of {@link CropVariety} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CropVariety> findByCriteria(CropVarietyCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CropVariety> specification = createSpecification(criteria);
        return cropVarietyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CropVarietyCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CropVariety> specification = createSpecification(criteria);
        return cropVarietyRepository.count(specification);
    }

    /**
     * Function to convert {@link CropVarietyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CropVariety> createSpecification(CropVarietyCriteria criteria) {
        Specification<CropVariety> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CropVariety_.id),
                buildStringSpecification(criteria.getName(), CropVariety_.name),
                buildRangeSpecification(criteria.getNoOfStages(), CropVariety_.noOfStages),
                buildStringSpecification(criteria.getImage(), CropVariety_.image),
                buildStringSpecification(criteria.getDescription(), CropVariety_.description),
                buildRangeSpecification(criteria.getAddedBy(), CropVariety_.addedBy),
                buildRangeSpecification(criteria.getCreatedAt(), CropVariety_.createdAt),
                buildSpecification(criteria.getCropId(), root -> root.join(CropVariety_.crop, JoinType.LEFT).get(CropVarietyCropType_.id)),
                buildSpecification(criteria.getCropDurationId(), root ->
                    root.join(CropVariety_.cropDuration, JoinType.LEFT).get(CropVarietyCropDuration_.id)
                )
            );
        }
        return specification;
    }
}
