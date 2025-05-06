package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.IndexPolicyCropVariety;
import lk.geoedge.interoperability.repository.IndexPolicyCropVarietyRepository;
import lk.geoedge.interoperability.service.criteria.IndexPolicyCropVarietyCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link IndexPolicyCropVariety} entities in the database.
 * The main input is a {@link IndexPolicyCropVarietyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link IndexPolicyCropVariety} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IndexPolicyCropVarietyQueryService extends QueryService<IndexPolicyCropVariety> {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPolicyCropVarietyQueryService.class);

    private final IndexPolicyCropVarietyRepository indexPolicyCropVarietyRepository;

    public IndexPolicyCropVarietyQueryService(IndexPolicyCropVarietyRepository indexPolicyCropVarietyRepository) {
        this.indexPolicyCropVarietyRepository = indexPolicyCropVarietyRepository;
    }

    /**
     * Return a {@link Page} of {@link IndexPolicyCropVariety} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IndexPolicyCropVariety> findByCriteria(IndexPolicyCropVarietyCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IndexPolicyCropVariety> specification = createSpecification(criteria);
        return indexPolicyCropVarietyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IndexPolicyCropVarietyCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<IndexPolicyCropVariety> specification = createSpecification(criteria);
        return indexPolicyCropVarietyRepository.count(specification);
    }

    /**
     * Function to convert {@link IndexPolicyCropVarietyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IndexPolicyCropVariety> createSpecification(IndexPolicyCropVarietyCriteria criteria) {
        Specification<IndexPolicyCropVariety> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), IndexPolicyCropVariety_.id),
                buildStringSpecification(criteria.getName(), IndexPolicyCropVariety_.name),
                buildRangeSpecification(criteria.getNoOfStages(), IndexPolicyCropVariety_.noOfStages),
                buildStringSpecification(criteria.getImage(), IndexPolicyCropVariety_.image),
                buildStringSpecification(criteria.getDescription(), IndexPolicyCropVariety_.description),
                buildRangeSpecification(criteria.getAddedBy(), IndexPolicyCropVariety_.addedBy),
                buildRangeSpecification(criteria.getCreatedAt(), IndexPolicyCropVariety_.createdAt)
            );
        }
        return specification;
    }
}
