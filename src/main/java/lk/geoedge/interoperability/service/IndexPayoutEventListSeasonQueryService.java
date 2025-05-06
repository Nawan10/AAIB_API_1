package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.IndexPayoutEventListSeason;
import lk.geoedge.interoperability.repository.IndexPayoutEventListSeasonRepository;
import lk.geoedge.interoperability.service.criteria.IndexPayoutEventListSeasonCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link IndexPayoutEventListSeason} entities in the database.
 * The main input is a {@link IndexPayoutEventListSeasonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link IndexPayoutEventListSeason} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IndexPayoutEventListSeasonQueryService extends QueryService<IndexPayoutEventListSeason> {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPayoutEventListSeasonQueryService.class);

    private final IndexPayoutEventListSeasonRepository indexPayoutEventListSeasonRepository;

    public IndexPayoutEventListSeasonQueryService(IndexPayoutEventListSeasonRepository indexPayoutEventListSeasonRepository) {
        this.indexPayoutEventListSeasonRepository = indexPayoutEventListSeasonRepository;
    }

    /**
     * Return a {@link Page} of {@link IndexPayoutEventListSeason} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IndexPayoutEventListSeason> findByCriteria(IndexPayoutEventListSeasonCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IndexPayoutEventListSeason> specification = createSpecification(criteria);
        return indexPayoutEventListSeasonRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IndexPayoutEventListSeasonCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<IndexPayoutEventListSeason> specification = createSpecification(criteria);
        return indexPayoutEventListSeasonRepository.count(specification);
    }

    /**
     * Function to convert {@link IndexPayoutEventListSeasonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IndexPayoutEventListSeason> createSpecification(IndexPayoutEventListSeasonCriteria criteria) {
        Specification<IndexPayoutEventListSeason> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), IndexPayoutEventListSeason_.id),
                buildStringSpecification(criteria.getName(), IndexPayoutEventListSeason_.name),
                buildStringSpecification(criteria.getPeriod(), IndexPayoutEventListSeason_.period)
            );
        }
        return specification;
    }
}
