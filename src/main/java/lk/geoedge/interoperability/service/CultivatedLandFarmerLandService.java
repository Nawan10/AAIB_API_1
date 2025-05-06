package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandFarmerLand;
import lk.geoedge.interoperability.repository.CultivatedLandFarmerLandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CultivatedLandFarmerLand}.
 */
@Service
@Transactional
public class CultivatedLandFarmerLandService {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandFarmerLandService.class);

    private final CultivatedLandFarmerLandRepository cultivatedLandFarmerLandRepository;

    public CultivatedLandFarmerLandService(CultivatedLandFarmerLandRepository cultivatedLandFarmerLandRepository) {
        this.cultivatedLandFarmerLandRepository = cultivatedLandFarmerLandRepository;
    }

    /**
     * Save a cultivatedLandFarmerLand.
     *
     * @param cultivatedLandFarmerLand the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandFarmerLand save(CultivatedLandFarmerLand cultivatedLandFarmerLand) {
        LOG.debug("Request to save CultivatedLandFarmerLand : {}", cultivatedLandFarmerLand);
        return cultivatedLandFarmerLandRepository.save(cultivatedLandFarmerLand);
    }

    /**
     * Update a cultivatedLandFarmerLand.
     *
     * @param cultivatedLandFarmerLand the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandFarmerLand update(CultivatedLandFarmerLand cultivatedLandFarmerLand) {
        LOG.debug("Request to update CultivatedLandFarmerLand : {}", cultivatedLandFarmerLand);
        return cultivatedLandFarmerLandRepository.save(cultivatedLandFarmerLand);
    }

    /**
     * Partially update a cultivatedLandFarmerLand.
     *
     * @param cultivatedLandFarmerLand the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CultivatedLandFarmerLand> partialUpdate(CultivatedLandFarmerLand cultivatedLandFarmerLand) {
        LOG.debug("Request to partially update CultivatedLandFarmerLand : {}", cultivatedLandFarmerLand);

        return cultivatedLandFarmerLandRepository
            .findById(cultivatedLandFarmerLand.getId())
            .map(existingCultivatedLandFarmerLand -> {
                if (cultivatedLandFarmerLand.getLandStatus() != null) {
                    existingCultivatedLandFarmerLand.setLandStatus(cultivatedLandFarmerLand.getLandStatus());
                }
                if (cultivatedLandFarmerLand.getUrea() != null) {
                    existingCultivatedLandFarmerLand.setUrea(cultivatedLandFarmerLand.getUrea());
                }
                if (cultivatedLandFarmerLand.getMop() != null) {
                    existingCultivatedLandFarmerLand.setMop(cultivatedLandFarmerLand.getMop());
                }
                if (cultivatedLandFarmerLand.getTsp() != null) {
                    existingCultivatedLandFarmerLand.setTsp(cultivatedLandFarmerLand.getTsp());
                }
                if (cultivatedLandFarmerLand.getCreatedAt() != null) {
                    existingCultivatedLandFarmerLand.setCreatedAt(cultivatedLandFarmerLand.getCreatedAt());
                }
                if (cultivatedLandFarmerLand.getAddedBy() != null) {
                    existingCultivatedLandFarmerLand.setAddedBy(cultivatedLandFarmerLand.getAddedBy());
                }

                return existingCultivatedLandFarmerLand;
            })
            .map(cultivatedLandFarmerLandRepository::save);
    }

    /**
     * Get one cultivatedLandFarmerLand by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CultivatedLandFarmerLand> findOne(Long id) {
        LOG.debug("Request to get CultivatedLandFarmerLand : {}", id);
        return cultivatedLandFarmerLandRepository.findById(id);
    }

    /**
     * Delete the cultivatedLandFarmerLand by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CultivatedLandFarmerLand : {}", id);
        cultivatedLandFarmerLandRepository.deleteById(id);
    }
}
