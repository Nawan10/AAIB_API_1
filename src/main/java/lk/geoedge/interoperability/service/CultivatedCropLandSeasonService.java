package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedCropLandSeason;
import lk.geoedge.interoperability.repository.CultivatedCropLandSeasonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CultivatedCropLandSeason}.
 */
@Service
@Transactional
public class CultivatedCropLandSeasonService {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedCropLandSeasonService.class);

    private final CultivatedCropLandSeasonRepository cultivatedCropLandSeasonRepository;

    public CultivatedCropLandSeasonService(CultivatedCropLandSeasonRepository cultivatedCropLandSeasonRepository) {
        this.cultivatedCropLandSeasonRepository = cultivatedCropLandSeasonRepository;
    }

    /**
     * Save a cultivatedCropLandSeason.
     *
     * @param cultivatedCropLandSeason the entity to save.
     * @return the persisted entity.
     */
    public CultivatedCropLandSeason save(CultivatedCropLandSeason cultivatedCropLandSeason) {
        LOG.debug("Request to save CultivatedCropLandSeason : {}", cultivatedCropLandSeason);
        return cultivatedCropLandSeasonRepository.save(cultivatedCropLandSeason);
    }

    /**
     * Update a cultivatedCropLandSeason.
     *
     * @param cultivatedCropLandSeason the entity to save.
     * @return the persisted entity.
     */
    public CultivatedCropLandSeason update(CultivatedCropLandSeason cultivatedCropLandSeason) {
        LOG.debug("Request to update CultivatedCropLandSeason : {}", cultivatedCropLandSeason);
        return cultivatedCropLandSeasonRepository.save(cultivatedCropLandSeason);
    }

    /**
     * Partially update a cultivatedCropLandSeason.
     *
     * @param cultivatedCropLandSeason the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CultivatedCropLandSeason> partialUpdate(CultivatedCropLandSeason cultivatedCropLandSeason) {
        LOG.debug("Request to partially update CultivatedCropLandSeason : {}", cultivatedCropLandSeason);

        return cultivatedCropLandSeasonRepository
            .findById(cultivatedCropLandSeason.getId())
            .map(existingCultivatedCropLandSeason -> {
                if (cultivatedCropLandSeason.getName() != null) {
                    existingCultivatedCropLandSeason.setName(cultivatedCropLandSeason.getName());
                }
                if (cultivatedCropLandSeason.getPeriod() != null) {
                    existingCultivatedCropLandSeason.setPeriod(cultivatedCropLandSeason.getPeriod());
                }

                return existingCultivatedCropLandSeason;
            })
            .map(cultivatedCropLandSeasonRepository::save);
    }

    /**
     * Get one cultivatedCropLandSeason by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CultivatedCropLandSeason> findOne(Long id) {
        LOG.debug("Request to get CultivatedCropLandSeason : {}", id);
        return cultivatedCropLandSeasonRepository.findById(id);
    }

    /**
     * Delete the cultivatedCropLandSeason by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CultivatedCropLandSeason : {}", id);
        cultivatedCropLandSeasonRepository.deleteById(id);
    }
}
