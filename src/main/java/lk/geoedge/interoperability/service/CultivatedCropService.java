package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedCrop;
import lk.geoedge.interoperability.repository.CultivatedCropRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CultivatedCrop}.
 */
@Service
@Transactional
public class CultivatedCropService {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedCropService.class);

    private final CultivatedCropRepository cultivatedCropRepository;

    public CultivatedCropService(CultivatedCropRepository cultivatedCropRepository) {
        this.cultivatedCropRepository = cultivatedCropRepository;
    }

    /**
     * Save a cultivatedCrop.
     *
     * @param cultivatedCrop the entity to save.
     * @return the persisted entity.
     */
    public CultivatedCrop save(CultivatedCrop cultivatedCrop) {
        LOG.debug("Request to save CultivatedCrop : {}", cultivatedCrop);
        return cultivatedCropRepository.save(cultivatedCrop);
    }

    /**
     * Update a cultivatedCrop.
     *
     * @param cultivatedCrop the entity to save.
     * @return the persisted entity.
     */
    public CultivatedCrop update(CultivatedCrop cultivatedCrop) {
        LOG.debug("Request to update CultivatedCrop : {}", cultivatedCrop);
        return cultivatedCropRepository.save(cultivatedCrop);
    }

    /**
     * Partially update a cultivatedCrop.
     *
     * @param cultivatedCrop the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CultivatedCrop> partialUpdate(CultivatedCrop cultivatedCrop) {
        LOG.debug("Request to partially update CultivatedCrop : {}", cultivatedCrop);

        return cultivatedCropRepository
            .findById(cultivatedCrop.getId())
            .map(existingCultivatedCrop -> {
                if (cultivatedCrop.getCultivatedExtend() != null) {
                    existingCultivatedCrop.setCultivatedExtend(cultivatedCrop.getCultivatedExtend());
                }
                if (cultivatedCrop.getStartDate() != null) {
                    existingCultivatedCrop.setStartDate(cultivatedCrop.getStartDate());
                }
                if (cultivatedCrop.getEndDate() != null) {
                    existingCultivatedCrop.setEndDate(cultivatedCrop.getEndDate());
                }
                if (cultivatedCrop.getYield() != null) {
                    existingCultivatedCrop.setYield(cultivatedCrop.getYield());
                }
                if (cultivatedCrop.getUnitId() != null) {
                    existingCultivatedCrop.setUnitId(cultivatedCrop.getUnitId());
                }
                if (cultivatedCrop.getCreatedAt() != null) {
                    existingCultivatedCrop.setCreatedAt(cultivatedCrop.getCreatedAt());
                }
                if (cultivatedCrop.getAddedBy() != null) {
                    existingCultivatedCrop.setAddedBy(cultivatedCrop.getAddedBy());
                }

                return existingCultivatedCrop;
            })
            .map(cultivatedCropRepository::save);
    }

    /**
     * Get one cultivatedCrop by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CultivatedCrop> findOne(Long id) {
        LOG.debug("Request to get CultivatedCrop : {}", id);
        return cultivatedCropRepository.findById(id);
    }

    /**
     * Delete the cultivatedCrop by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CultivatedCrop : {}", id);
        cultivatedCropRepository.deleteById(id);
    }
}
