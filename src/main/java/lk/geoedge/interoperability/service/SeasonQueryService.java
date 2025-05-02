package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.Season;
import lk.geoedge.interoperability.repository.SeasonRepository;
import lk.geoedge.interoperability.service.criteria.SeasonCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Season} entities in the database.
 * The main input is a {@link SeasonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Season} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SeasonQueryService extends QueryService<Season> {

    private static final Logger LOG = LoggerFactory.getLogger(SeasonQueryService.class);

    private final SeasonRepository seasonRepository;

    public SeasonQueryService(SeasonRepository seasonRepository) {
        this.seasonRepository = seasonRepository;
    }

    /**
     * Return a {@link Page} of {@link Season} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Season> findByCriteria(SeasonCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Season> specification = createSpecification(criteria);
        return seasonRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SeasonCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Season> specification = createSpecification(criteria);
        return seasonRepository.count(specification);
    }

    /**
     * Function to convert {@link SeasonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Season> createSpecification(SeasonCriteria criteria) {
        Specification<Season> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Season_.id),
                buildStringSpecification(criteria.getName(), Season_.name),
                buildStringSpecification(criteria.getPeriod(), Season_.period)
            );
        }
        return specification;
    }
}
