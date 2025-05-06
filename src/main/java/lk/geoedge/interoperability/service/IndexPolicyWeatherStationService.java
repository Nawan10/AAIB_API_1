package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexPolicyWeatherStation;
import lk.geoedge.interoperability.repository.IndexPolicyWeatherStationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.IndexPolicyWeatherStation}.
 */
@Service
@Transactional
public class IndexPolicyWeatherStationService {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPolicyWeatherStationService.class);

    private final IndexPolicyWeatherStationRepository indexPolicyWeatherStationRepository;

    public IndexPolicyWeatherStationService(IndexPolicyWeatherStationRepository indexPolicyWeatherStationRepository) {
        this.indexPolicyWeatherStationRepository = indexPolicyWeatherStationRepository;
    }

    /**
     * Save a indexPolicyWeatherStation.
     *
     * @param indexPolicyWeatherStation the entity to save.
     * @return the persisted entity.
     */
    public IndexPolicyWeatherStation save(IndexPolicyWeatherStation indexPolicyWeatherStation) {
        LOG.debug("Request to save IndexPolicyWeatherStation : {}", indexPolicyWeatherStation);
        return indexPolicyWeatherStationRepository.save(indexPolicyWeatherStation);
    }

    /**
     * Update a indexPolicyWeatherStation.
     *
     * @param indexPolicyWeatherStation the entity to save.
     * @return the persisted entity.
     */
    public IndexPolicyWeatherStation update(IndexPolicyWeatherStation indexPolicyWeatherStation) {
        LOG.debug("Request to update IndexPolicyWeatherStation : {}", indexPolicyWeatherStation);
        return indexPolicyWeatherStationRepository.save(indexPolicyWeatherStation);
    }

    /**
     * Partially update a indexPolicyWeatherStation.
     *
     * @param indexPolicyWeatherStation the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IndexPolicyWeatherStation> partialUpdate(IndexPolicyWeatherStation indexPolicyWeatherStation) {
        LOG.debug("Request to partially update IndexPolicyWeatherStation : {}", indexPolicyWeatherStation);

        return indexPolicyWeatherStationRepository
            .findById(indexPolicyWeatherStation.getId())
            .map(existingIndexPolicyWeatherStation -> {
                if (indexPolicyWeatherStation.getName() != null) {
                    existingIndexPolicyWeatherStation.setName(indexPolicyWeatherStation.getName());
                }
                if (indexPolicyWeatherStation.getCode() != null) {
                    existingIndexPolicyWeatherStation.setCode(indexPolicyWeatherStation.getCode());
                }
                if (indexPolicyWeatherStation.getLatitude() != null) {
                    existingIndexPolicyWeatherStation.setLatitude(indexPolicyWeatherStation.getLatitude());
                }
                if (indexPolicyWeatherStation.getLongitude() != null) {
                    existingIndexPolicyWeatherStation.setLongitude(indexPolicyWeatherStation.getLongitude());
                }
                if (indexPolicyWeatherStation.getGnId() != null) {
                    existingIndexPolicyWeatherStation.setGnId(indexPolicyWeatherStation.getGnId());
                }
                if (indexPolicyWeatherStation.getDistrictId() != null) {
                    existingIndexPolicyWeatherStation.setDistrictId(indexPolicyWeatherStation.getDistrictId());
                }
                if (indexPolicyWeatherStation.getProvinceId() != null) {
                    existingIndexPolicyWeatherStation.setProvinceId(indexPolicyWeatherStation.getProvinceId());
                }
                if (indexPolicyWeatherStation.getDsId() != null) {
                    existingIndexPolicyWeatherStation.setDsId(indexPolicyWeatherStation.getDsId());
                }
                if (indexPolicyWeatherStation.getAddedBy() != null) {
                    existingIndexPolicyWeatherStation.setAddedBy(indexPolicyWeatherStation.getAddedBy());
                }
                if (indexPolicyWeatherStation.getCreatedAt() != null) {
                    existingIndexPolicyWeatherStation.setCreatedAt(indexPolicyWeatherStation.getCreatedAt());
                }

                return existingIndexPolicyWeatherStation;
            })
            .map(indexPolicyWeatherStationRepository::save);
    }

    /**
     * Get one indexPolicyWeatherStation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IndexPolicyWeatherStation> findOne(Long id) {
        LOG.debug("Request to get IndexPolicyWeatherStation : {}", id);
        return indexPolicyWeatherStationRepository.findById(id);
    }

    /**
     * Delete the indexPolicyWeatherStation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IndexPolicyWeatherStation : {}", id);
        indexPolicyWeatherStationRepository.deleteById(id);
    }
}
