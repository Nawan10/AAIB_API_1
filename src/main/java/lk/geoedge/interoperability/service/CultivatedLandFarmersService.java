package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandFarmers;
import lk.geoedge.interoperability.repository.CultivatedLandFarmersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CultivatedLandFarmers}.
 */
@Service
@Transactional
public class CultivatedLandFarmersService {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandFarmersService.class);

    private final CultivatedLandFarmersRepository cultivatedLandFarmersRepository;

    public CultivatedLandFarmersService(CultivatedLandFarmersRepository cultivatedLandFarmersRepository) {
        this.cultivatedLandFarmersRepository = cultivatedLandFarmersRepository;
    }

    /**
     * Save a cultivatedLandFarmers.
     *
     * @param cultivatedLandFarmers the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandFarmers save(CultivatedLandFarmers cultivatedLandFarmers) {
        LOG.debug("Request to save CultivatedLandFarmers : {}", cultivatedLandFarmers);
        return cultivatedLandFarmersRepository.save(cultivatedLandFarmers);
    }

    /**
     * Update a cultivatedLandFarmers.
     *
     * @param cultivatedLandFarmers the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandFarmers update(CultivatedLandFarmers cultivatedLandFarmers) {
        LOG.debug("Request to update CultivatedLandFarmers : {}", cultivatedLandFarmers);
        return cultivatedLandFarmersRepository.save(cultivatedLandFarmers);
    }

    /**
     * Partially update a cultivatedLandFarmers.
     *
     * @param cultivatedLandFarmers the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CultivatedLandFarmers> partialUpdate(CultivatedLandFarmers cultivatedLandFarmers) {
        LOG.debug("Request to partially update CultivatedLandFarmers : {}", cultivatedLandFarmers);

        return cultivatedLandFarmersRepository
            .findById(cultivatedLandFarmers.getId())
            .map(existingCultivatedLandFarmers -> {
                if (cultivatedLandFarmers.getRelationId() != null) {
                    existingCultivatedLandFarmers.setRelationId(cultivatedLandFarmers.getRelationId());
                }
                if (cultivatedLandFarmers.getCreatedAt() != null) {
                    existingCultivatedLandFarmers.setCreatedAt(cultivatedLandFarmers.getCreatedAt());
                }
                if (cultivatedLandFarmers.getAddedBy() != null) {
                    existingCultivatedLandFarmers.setAddedBy(cultivatedLandFarmers.getAddedBy());
                }

                return existingCultivatedLandFarmers;
            })
            .map(cultivatedLandFarmersRepository::save);
    }

    /**
     * Get one cultivatedLandFarmers by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CultivatedLandFarmers> findOne(Long id) {
        LOG.debug("Request to get CultivatedLandFarmers : {}", id);
        return cultivatedLandFarmersRepository.findById(id);
    }

    /**
     * Delete the cultivatedLandFarmers by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CultivatedLandFarmers : {}", id);
        cultivatedLandFarmersRepository.deleteById(id);
    }
}
