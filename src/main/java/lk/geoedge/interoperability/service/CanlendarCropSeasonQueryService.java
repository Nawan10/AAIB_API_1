package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CanlendarCropSeason;
import lk.geoedge.interoperability.repository.CanlendarCropSeasonRepository;
import lk.geoedge.interoperability.service.criteria.CanlendarCropSeasonCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CanlendarCropSeason} entities in the database.
 * The main input is a {@link CanlendarCropSeasonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CanlendarCropSeason} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CanlendarCropSeasonQueryService extends QueryService<CanlendarCropSeason> {

    private static final Logger LOG = LoggerFactory.getLogger(CanlendarCropSeasonQueryService.class);

    private final CanlendarCropSeasonRepository canlendarCropSeasonRepository;

    public CanlendarCropSeasonQueryService(CanlendarCropSeasonRepository canlendarCropSeasonRepository) {
        this.canlendarCropSeasonRepository = canlendarCropSeasonRepository;
    }

    /**
     * Return a {@link Page} of {@link CanlendarCropSeason} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CanlendarCropSeason> findByCriteria(CanlendarCropSeasonCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CanlendarCropSeason> specification = createSpecification(criteria);
        return canlendarCropSeasonRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CanlendarCropSeasonCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CanlendarCropSeason> specification = createSpecification(criteria);
        return canlendarCropSeasonRepository.count(specification);
    }

    /**
     * Function to convert {@link CanlendarCropSeasonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CanlendarCropSeason> createSpecification(CanlendarCropSeasonCriteria criteria) {
        Specification<CanlendarCropSeason> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CanlendarCropSeason_.id),
                buildStringSpecification(criteria.getName(), CanlendarCropSeason_.name),
                buildStringSpecification(criteria.getPeriod(), CanlendarCropSeason_.period)
            );
        }
        return specification;
    }
}
