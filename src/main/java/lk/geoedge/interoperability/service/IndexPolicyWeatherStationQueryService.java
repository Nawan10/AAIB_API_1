package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.IndexPolicyWeatherStation;
import lk.geoedge.interoperability.repository.IndexPolicyWeatherStationRepository;
import lk.geoedge.interoperability.service.criteria.IndexPolicyWeatherStationCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link IndexPolicyWeatherStation} entities in the database.
 * The main input is a {@link IndexPolicyWeatherStationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link IndexPolicyWeatherStation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IndexPolicyWeatherStationQueryService extends QueryService<IndexPolicyWeatherStation> {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPolicyWeatherStationQueryService.class);

    private final IndexPolicyWeatherStationRepository indexPolicyWeatherStationRepository;

    public IndexPolicyWeatherStationQueryService(IndexPolicyWeatherStationRepository indexPolicyWeatherStationRepository) {
        this.indexPolicyWeatherStationRepository = indexPolicyWeatherStationRepository;
    }

    /**
     * Return a {@link Page} of {@link IndexPolicyWeatherStation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IndexPolicyWeatherStation> findByCriteria(IndexPolicyWeatherStationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IndexPolicyWeatherStation> specification = createSpecification(criteria);
        return indexPolicyWeatherStationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IndexPolicyWeatherStationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<IndexPolicyWeatherStation> specification = createSpecification(criteria);
        return indexPolicyWeatherStationRepository.count(specification);
    }

    /**
     * Function to convert {@link IndexPolicyWeatherStationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IndexPolicyWeatherStation> createSpecification(IndexPolicyWeatherStationCriteria criteria) {
        Specification<IndexPolicyWeatherStation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), IndexPolicyWeatherStation_.id),
                buildStringSpecification(criteria.getName(), IndexPolicyWeatherStation_.name),
                buildStringSpecification(criteria.getCode(), IndexPolicyWeatherStation_.code),
                buildRangeSpecification(criteria.getLatitude(), IndexPolicyWeatherStation_.latitude),
                buildRangeSpecification(criteria.getLongitude(), IndexPolicyWeatherStation_.longitude),
                buildRangeSpecification(criteria.getGnId(), IndexPolicyWeatherStation_.gnId),
                buildRangeSpecification(criteria.getDistrictId(), IndexPolicyWeatherStation_.districtId),
                buildRangeSpecification(criteria.getProvinceId(), IndexPolicyWeatherStation_.provinceId),
                buildRangeSpecification(criteria.getDsId(), IndexPolicyWeatherStation_.dsId),
                buildRangeSpecification(criteria.getAddedBy(), IndexPolicyWeatherStation_.addedBy),
                buildRangeSpecification(criteria.getCreatedAt(), IndexPolicyWeatherStation_.createdAt)
            );
        }
        return specification;
    }
}
