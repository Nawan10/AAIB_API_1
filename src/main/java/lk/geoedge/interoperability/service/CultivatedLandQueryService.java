package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CultivatedLand;
import lk.geoedge.interoperability.repository.CultivatedLandRepository;
import lk.geoedge.interoperability.service.criteria.CultivatedLandCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CultivatedLand} entities in the database.
 * The main input is a {@link CultivatedLandCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CultivatedLand} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CultivatedLandQueryService extends QueryService<CultivatedLand> {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandQueryService.class);

    private final CultivatedLandRepository cultivatedLandRepository;

    public CultivatedLandQueryService(CultivatedLandRepository cultivatedLandRepository) {
        this.cultivatedLandRepository = cultivatedLandRepository;
    }

    /**
     * Return a {@link Page} of {@link CultivatedLand} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CultivatedLand> findByCriteria(CultivatedLandCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CultivatedLand> specification = createSpecification(criteria);
        return cultivatedLandRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CultivatedLandCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CultivatedLand> specification = createSpecification(criteria);
        return cultivatedLandRepository.count(specification);
    }

    /**
     * Function to convert {@link CultivatedLandCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CultivatedLand> createSpecification(CultivatedLandCriteria criteria) {
        Specification<CultivatedLand> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CultivatedLand_.id),
                buildStringSpecification(criteria.getLandStatus(), CultivatedLand_.landStatus),
                buildRangeSpecification(criteria.getUrea(), CultivatedLand_.urea),
                buildRangeSpecification(criteria.getMop(), CultivatedLand_.mop),
                buildRangeSpecification(criteria.getTsp(), CultivatedLand_.tsp),
                buildRangeSpecification(criteria.getCreatedAt(), CultivatedLand_.createdAt),
                buildStringSpecification(criteria.getAddedBy(), CultivatedLand_.addedBy),
                buildSpecification(criteria.getSeasonId(), root ->
                    root.join(CultivatedLand_.season, JoinType.LEFT).get(CultivatedLandSeason_.id)
                )
            );
        }
        return specification;
    }
}
