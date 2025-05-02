package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedCropCultivatedLand;
import lk.geoedge.interoperability.repository.CultivatedCropCultivatedLandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CultivatedCropCultivatedLand}.
 */
@Service
@Transactional
public class CultivatedCropCultivatedLandService {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedCropCultivatedLandService.class);

    private final CultivatedCropCultivatedLandRepository cultivatedCropCultivatedLandRepository;

    public CultivatedCropCultivatedLandService(CultivatedCropCultivatedLandRepository cultivatedCropCultivatedLandRepository) {
        this.cultivatedCropCultivatedLandRepository = cultivatedCropCultivatedLandRepository;
    }

    /**
     * Save a cultivatedCropCultivatedLand.
     *
     * @param cultivatedCropCultivatedLand the entity to save.
     * @return the persisted entity.
     */
    public CultivatedCropCultivatedLand save(CultivatedCropCultivatedLand cultivatedCropCultivatedLand) {
        LOG.debug("Request to save CultivatedCropCultivatedLand : {}", cultivatedCropCultivatedLand);
        return cultivatedCropCultivatedLandRepository.save(cultivatedCropCultivatedLand);
    }

    /**
     * Update a cultivatedCropCultivatedLand.
     *
     * @param cultivatedCropCultivatedLand the entity to save.
     * @return the persisted entity.
     */
    public CultivatedCropCultivatedLand update(CultivatedCropCultivatedLand cultivatedCropCultivatedLand) {
        LOG.debug("Request to update CultivatedCropCultivatedLand : {}", cultivatedCropCultivatedLand);
        return cultivatedCropCultivatedLandRepository.save(cultivatedCropCultivatedLand);
    }

    /**
     * Partially update a cultivatedCropCultivatedLand.
     *
     * @param cultivatedCropCultivatedLand the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CultivatedCropCultivatedLand> partialUpdate(CultivatedCropCultivatedLand cultivatedCropCultivatedLand) {
        LOG.debug("Request to partially update CultivatedCropCultivatedLand : {}", cultivatedCropCultivatedLand);

        return cultivatedCropCultivatedLandRepository
            .findById(cultivatedCropCultivatedLand.getId())
            .map(existingCultivatedCropCultivatedLand -> {
                if (cultivatedCropCultivatedLand.getLandStatus() != null) {
                    existingCultivatedCropCultivatedLand.setLandStatus(cultivatedCropCultivatedLand.getLandStatus());
                }
                if (cultivatedCropCultivatedLand.getUrea() != null) {
                    existingCultivatedCropCultivatedLand.setUrea(cultivatedCropCultivatedLand.getUrea());
                }
                if (cultivatedCropCultivatedLand.getMop() != null) {
                    existingCultivatedCropCultivatedLand.setMop(cultivatedCropCultivatedLand.getMop());
                }
                if (cultivatedCropCultivatedLand.getTsp() != null) {
                    existingCultivatedCropCultivatedLand.setTsp(cultivatedCropCultivatedLand.getTsp());
                }
                if (cultivatedCropCultivatedLand.getCreatedAt() != null) {
                    existingCultivatedCropCultivatedLand.setCreatedAt(cultivatedCropCultivatedLand.getCreatedAt());
                }
                if (cultivatedCropCultivatedLand.getAddedBy() != null) {
                    existingCultivatedCropCultivatedLand.setAddedBy(cultivatedCropCultivatedLand.getAddedBy());
                }

                return existingCultivatedCropCultivatedLand;
            })
            .map(cultivatedCropCultivatedLandRepository::save);
    }

    /**
     * Get one cultivatedCropCultivatedLand by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CultivatedCropCultivatedLand> findOne(Long id) {
        LOG.debug("Request to get CultivatedCropCultivatedLand : {}", id);
        return cultivatedCropCultivatedLandRepository.findById(id);
    }

    /**
     * Delete the cultivatedCropCultivatedLand by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CultivatedCropCultivatedLand : {}", id);
        cultivatedCropCultivatedLandRepository.deleteById(id);
    }
}
