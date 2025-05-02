package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CultivatedCropLandSeason;
import lk.geoedge.interoperability.repository.CultivatedCropLandSeasonRepository;
import lk.geoedge.interoperability.service.criteria.CultivatedCropLandSeasonCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CultivatedCropLandSeason} entities in the database.
 * The main input is a {@link CultivatedCropLandSeasonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CultivatedCropLandSeason} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CultivatedCropLandSeasonQueryService extends QueryService<CultivatedCropLandSeason> {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedCropLandSeasonQueryService.class);

    private final CultivatedCropLandSeasonRepository cultivatedCropLandSeasonRepository;

    public CultivatedCropLandSeasonQueryService(CultivatedCropLandSeasonRepository cultivatedCropLandSeasonRepository) {
        this.cultivatedCropLandSeasonRepository = cultivatedCropLandSeasonRepository;
    }

    /**
     * Return a {@link Page} of {@link CultivatedCropLandSeason} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CultivatedCropLandSeason> findByCriteria(CultivatedCropLandSeasonCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CultivatedCropLandSeason> specification = createSpecification(criteria);
        return cultivatedCropLandSeasonRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CultivatedCropLandSeasonCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CultivatedCropLandSeason> specification = createSpecification(criteria);
        return cultivatedCropLandSeasonRepository.count(specification);
    }

    /**
     * Function to convert {@link CultivatedCropLandSeasonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CultivatedCropLandSeason> createSpecification(CultivatedCropLandSeasonCriteria criteria) {
        Specification<CultivatedCropLandSeason> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CultivatedCropLandSeason_.id),
                buildStringSpecification(criteria.getName(), CultivatedCropLandSeason_.name),
                buildStringSpecification(criteria.getPeriod(), CultivatedCropLandSeason_.period)
            );
        }
        return specification;
    }
}
