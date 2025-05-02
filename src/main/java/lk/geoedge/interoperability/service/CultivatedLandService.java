package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLand;
import lk.geoedge.interoperability.repository.CultivatedLandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CultivatedLand}.
 */
@Service
@Transactional
public class CultivatedLandService {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandService.class);

    private final CultivatedLandRepository cultivatedLandRepository;

    public CultivatedLandService(CultivatedLandRepository cultivatedLandRepository) {
        this.cultivatedLandRepository = cultivatedLandRepository;
    }

    /**
     * Save a cultivatedLand.
     *
     * @param cultivatedLand the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLand save(CultivatedLand cultivatedLand) {
        LOG.debug("Request to save CultivatedLand : {}", cultivatedLand);
        return cultivatedLandRepository.save(cultivatedLand);
    }

    /**
     * Update a cultivatedLand.
     *
     * @param cultivatedLand the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLand update(CultivatedLand cultivatedLand) {
        LOG.debug("Request to update CultivatedLand : {}", cultivatedLand);
        return cultivatedLandRepository.save(cultivatedLand);
    }

    /**
     * Partially update a cultivatedLand.
     *
     * @param cultivatedLand the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CultivatedLand> partialUpdate(CultivatedLand cultivatedLand) {
        LOG.debug("Request to partially update CultivatedLand : {}", cultivatedLand);

        return cultivatedLandRepository
            .findById(cultivatedLand.getId())
            .map(existingCultivatedLand -> {
                if (cultivatedLand.getLandStatus() != null) {
                    existingCultivatedLand.setLandStatus(cultivatedLand.getLandStatus());
                }
                if (cultivatedLand.getUrea() != null) {
                    existingCultivatedLand.setUrea(cultivatedLand.getUrea());
                }
                if (cultivatedLand.getMop() != null) {
                    existingCultivatedLand.setMop(cultivatedLand.getMop());
                }
                if (cultivatedLand.getTsp() != null) {
                    existingCultivatedLand.setTsp(cultivatedLand.getTsp());
                }
                if (cultivatedLand.getCreatedAt() != null) {
                    existingCultivatedLand.setCreatedAt(cultivatedLand.getCreatedAt());
                }
                if (cultivatedLand.getAddedBy() != null) {
                    existingCultivatedLand.setAddedBy(cultivatedLand.getAddedBy());
                }

                return existingCultivatedLand;
            })
            .map(cultivatedLandRepository::save);
    }

    /**
     * Get one cultivatedLand by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CultivatedLand> findOne(Long id) {
        LOG.debug("Request to get CultivatedLand : {}", id);
        return cultivatedLandRepository.findById(id);
    }

    /**
     * Delete the cultivatedLand by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CultivatedLand : {}", id);
        cultivatedLandRepository.deleteById(id);
    }
}
