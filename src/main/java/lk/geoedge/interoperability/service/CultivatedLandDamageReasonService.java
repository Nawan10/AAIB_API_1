package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandDamageReason;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReasonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CultivatedLandDamageReason}.
 */
@Service
@Transactional
public class CultivatedLandDamageReasonService {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandDamageReasonService.class);

    private final CultivatedLandDamageReasonRepository cultivatedLandDamageReasonRepository;

    public CultivatedLandDamageReasonService(CultivatedLandDamageReasonRepository cultivatedLandDamageReasonRepository) {
        this.cultivatedLandDamageReasonRepository = cultivatedLandDamageReasonRepository;
    }

    /**
     * Save a cultivatedLandDamageReason.
     *
     * @param cultivatedLandDamageReason the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandDamageReason save(CultivatedLandDamageReason cultivatedLandDamageReason) {
        LOG.debug("Request to save CultivatedLandDamageReason : {}", cultivatedLandDamageReason);
        return cultivatedLandDamageReasonRepository.save(cultivatedLandDamageReason);
    }

    /**
     * Update a cultivatedLandDamageReason.
     *
     * @param cultivatedLandDamageReason the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandDamageReason update(CultivatedLandDamageReason cultivatedLandDamageReason) {
        LOG.debug("Request to update CultivatedLandDamageReason : {}", cultivatedLandDamageReason);
        return cultivatedLandDamageReasonRepository.save(cultivatedLandDamageReason);
    }

    /**
     * Partially update a cultivatedLandDamageReason.
     *
     * @param cultivatedLandDamageReason the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CultivatedLandDamageReason> partialUpdate(CultivatedLandDamageReason cultivatedLandDamageReason) {
        LOG.debug("Request to partially update CultivatedLandDamageReason : {}", cultivatedLandDamageReason);

        return cultivatedLandDamageReasonRepository
            .findById(cultivatedLandDamageReason.getId())
            .map(existingCultivatedLandDamageReason -> {
                if (cultivatedLandDamageReason.getName() != null) {
                    existingCultivatedLandDamageReason.setName(cultivatedLandDamageReason.getName());
                }

                return existingCultivatedLandDamageReason;
            })
            .map(cultivatedLandDamageReasonRepository::save);
    }

    /**
     * Get one cultivatedLandDamageReason by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CultivatedLandDamageReason> findOne(Long id) {
        LOG.debug("Request to get CultivatedLandDamageReason : {}", id);
        return cultivatedLandDamageReasonRepository.findById(id);
    }

    /**
     * Delete the cultivatedLandDamageReason by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CultivatedLandDamageReason : {}", id);
        cultivatedLandDamageReasonRepository.deleteById(id);
    }
}
