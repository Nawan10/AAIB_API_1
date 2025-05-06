package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CropDamage;
import lk.geoedge.interoperability.repository.CropDamageRepository;
import lk.geoedge.interoperability.service.criteria.CropDamageCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CropDamage} entities in the database.
 * The main input is a {@link CropDamageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CropDamage} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CropDamageQueryService extends QueryService<CropDamage> {

    private static final Logger LOG = LoggerFactory.getLogger(CropDamageQueryService.class);

    private final CropDamageRepository cropDamageRepository;

    public CropDamageQueryService(CropDamageRepository cropDamageRepository) {
        this.cropDamageRepository = cropDamageRepository;
    }

    /**
     * Return a {@link Page} of {@link CropDamage} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CropDamage> findByCriteria(CropDamageCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CropDamage> specification = createSpecification(criteria);
        return cropDamageRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CropDamageCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CropDamage> specification = createSpecification(criteria);
        return cropDamageRepository.count(specification);
    }

    /**
     * Function to convert {@link CropDamageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CropDamage> createSpecification(CropDamageCriteria criteria) {
        Specification<CropDamage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CropDamage_.id),
                buildStringSpecification(criteria.getAddedBy(), CropDamage_.addedBy),
                buildRangeSpecification(criteria.getCreatedAt(), CropDamage_.createdAt),
                buildSpecification(criteria.getCropId(), root -> root.join(CropDamage_.crop, JoinType.LEFT).get(CropDamageCropType_.id)),
                buildSpecification(criteria.getDamageId(), root -> root.join(CropDamage_.damage, JoinType.LEFT).get(CropDamageDamage_.id))
            );
        }
        return specification;
    }
}
