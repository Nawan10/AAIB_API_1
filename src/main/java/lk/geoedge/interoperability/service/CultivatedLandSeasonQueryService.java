package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CultivatedLandSeason;
import lk.geoedge.interoperability.repository.CultivatedLandSeasonRepository;
import lk.geoedge.interoperability.service.criteria.CultivatedLandSeasonCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CultivatedLandSeason} entities in the database.
 * The main input is a {@link CultivatedLandSeasonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CultivatedLandSeason} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CultivatedLandSeasonQueryService extends QueryService<CultivatedLandSeason> {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandSeasonQueryService.class);

    private final CultivatedLandSeasonRepository cultivatedLandSeasonRepository;

    public CultivatedLandSeasonQueryService(CultivatedLandSeasonRepository cultivatedLandSeasonRepository) {
        this.cultivatedLandSeasonRepository = cultivatedLandSeasonRepository;
    }

    /**
     * Return a {@link Page} of {@link CultivatedLandSeason} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CultivatedLandSeason> findByCriteria(CultivatedLandSeasonCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CultivatedLandSeason> specification = createSpecification(criteria);
        return cultivatedLandSeasonRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CultivatedLandSeasonCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CultivatedLandSeason> specification = createSpecification(criteria);
        return cultivatedLandSeasonRepository.count(specification);
    }

    /**
     * Function to convert {@link CultivatedLandSeasonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CultivatedLandSeason> createSpecification(CultivatedLandSeasonCriteria criteria) {
        Specification<CultivatedLandSeason> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CultivatedLandSeason_.id),
                buildStringSpecification(criteria.getName(), CultivatedLandSeason_.name),
                buildStringSpecification(criteria.getPeriod(), CultivatedLandSeason_.period)
            );
        }
        return specification;
    }
}
