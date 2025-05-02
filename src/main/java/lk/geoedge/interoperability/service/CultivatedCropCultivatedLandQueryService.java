package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CultivatedCropCultivatedLand;
import lk.geoedge.interoperability.repository.CultivatedCropCultivatedLandRepository;
import lk.geoedge.interoperability.service.criteria.CultivatedCropCultivatedLandCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CultivatedCropCultivatedLand} entities in the database.
 * The main input is a {@link CultivatedCropCultivatedLandCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CultivatedCropCultivatedLand} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CultivatedCropCultivatedLandQueryService extends QueryService<CultivatedCropCultivatedLand> {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedCropCultivatedLandQueryService.class);

    private final CultivatedCropCultivatedLandRepository cultivatedCropCultivatedLandRepository;

    public CultivatedCropCultivatedLandQueryService(CultivatedCropCultivatedLandRepository cultivatedCropCultivatedLandRepository) {
        this.cultivatedCropCultivatedLandRepository = cultivatedCropCultivatedLandRepository;
    }

    /**
     * Return a {@link Page} of {@link CultivatedCropCultivatedLand} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CultivatedCropCultivatedLand> findByCriteria(CultivatedCropCultivatedLandCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CultivatedCropCultivatedLand> specification = createSpecification(criteria);
        return cultivatedCropCultivatedLandRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CultivatedCropCultivatedLandCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CultivatedCropCultivatedLand> specification = createSpecification(criteria);
        return cultivatedCropCultivatedLandRepository.count(specification);
    }

    /**
     * Function to convert {@link CultivatedCropCultivatedLandCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CultivatedCropCultivatedLand> createSpecification(CultivatedCropCultivatedLandCriteria criteria) {
        Specification<CultivatedCropCultivatedLand> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CultivatedCropCultivatedLand_.id),
                buildStringSpecification(criteria.getLandStatus(), CultivatedCropCultivatedLand_.landStatus),
                buildRangeSpecification(criteria.getUrea(), CultivatedCropCultivatedLand_.urea),
                buildRangeSpecification(criteria.getMop(), CultivatedCropCultivatedLand_.mop),
                buildRangeSpecification(criteria.getTsp(), CultivatedCropCultivatedLand_.tsp),
                buildRangeSpecification(criteria.getCreatedAt(), CultivatedCropCultivatedLand_.createdAt),
                buildStringSpecification(criteria.getAddedBy(), CultivatedCropCultivatedLand_.addedBy),
                buildSpecification(criteria.getSeasonId(), root ->
                    root.join(CultivatedCropCultivatedLand_.season, JoinType.LEFT).get(CultivatedCropLandSeason_.id)
                )
            );
        }
        return specification;
    }
}
