package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandSeason;
import lk.geoedge.interoperability.repository.CultivatedLandSeasonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CultivatedLandSeason}.
 */
@Service
@Transactional
public class CultivatedLandSeasonService {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandSeasonService.class);

    private final CultivatedLandSeasonRepository cultivatedLandSeasonRepository;

    public CultivatedLandSeasonService(CultivatedLandSeasonRepository cultivatedLandSeasonRepository) {
        this.cultivatedLandSeasonRepository = cultivatedLandSeasonRepository;
    }

    /**
     * Save a cultivatedLandSeason.
     *
     * @param cultivatedLandSeason the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandSeason save(CultivatedLandSeason cultivatedLandSeason) {
        LOG.debug("Request to save CultivatedLandSeason : {}", cultivatedLandSeason);
        return cultivatedLandSeasonRepository.save(cultivatedLandSeason);
    }

    /**
     * Update a cultivatedLandSeason.
     *
     * @param cultivatedLandSeason the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandSeason update(CultivatedLandSeason cultivatedLandSeason) {
        LOG.debug("Request to update CultivatedLandSeason : {}", cultivatedLandSeason);
        return cultivatedLandSeasonRepository.save(cultivatedLandSeason);
    }

    /**
     * Partially update a cultivatedLandSeason.
     *
     * @param cultivatedLandSeason the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CultivatedLandSeason> partialUpdate(CultivatedLandSeason cultivatedLandSeason) {
        LOG.debug("Request to partially update CultivatedLandSeason : {}", cultivatedLandSeason);

        return cultivatedLandSeasonRepository
            .findById(cultivatedLandSeason.getId())
            .map(existingCultivatedLandSeason -> {
                if (cultivatedLandSeason.getName() != null) {
                    existingCultivatedLandSeason.setName(cultivatedLandSeason.getName());
                }
                if (cultivatedLandSeason.getPeriod() != null) {
                    existingCultivatedLandSeason.setPeriod(cultivatedLandSeason.getPeriod());
                }

                return existingCultivatedLandSeason;
            })
            .map(cultivatedLandSeasonRepository::save);
    }

    /**
     * Get one cultivatedLandSeason by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CultivatedLandSeason> findOne(Long id) {
        LOG.debug("Request to get CultivatedLandSeason : {}", id);
        return cultivatedLandSeasonRepository.findById(id);
    }

    /**
     * Delete the cultivatedLandSeason by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CultivatedLandSeason : {}", id);
        cultivatedLandSeasonRepository.deleteById(id);
    }
}
