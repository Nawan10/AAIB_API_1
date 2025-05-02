package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedCropCropType;
import lk.geoedge.interoperability.repository.CultivatedCropCropTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CultivatedCropCropType}.
 */
@Service
@Transactional
public class CultivatedCropCropTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedCropCropTypeService.class);

    private final CultivatedCropCropTypeRepository cultivatedCropCropTypeRepository;

    public CultivatedCropCropTypeService(CultivatedCropCropTypeRepository cultivatedCropCropTypeRepository) {
        this.cultivatedCropCropTypeRepository = cultivatedCropCropTypeRepository;
    }

    /**
     * Save a cultivatedCropCropType.
     *
     * @param cultivatedCropCropType the entity to save.
     * @return the persisted entity.
     */
    public CultivatedCropCropType save(CultivatedCropCropType cultivatedCropCropType) {
        LOG.debug("Request to save CultivatedCropCropType : {}", cultivatedCropCropType);
        return cultivatedCropCropTypeRepository.save(cultivatedCropCropType);
    }

    /**
     * Update a cultivatedCropCropType.
     *
     * @param cultivatedCropCropType the entity to save.
     * @return the persisted entity.
     */
    public CultivatedCropCropType update(CultivatedCropCropType cultivatedCropCropType) {
        LOG.debug("Request to update CultivatedCropCropType : {}", cultivatedCropCropType);
        return cultivatedCropCropTypeRepository.save(cultivatedCropCropType);
    }

    /**
     * Partially update a cultivatedCropCropType.
     *
     * @param cultivatedCropCropType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CultivatedCropCropType> partialUpdate(CultivatedCropCropType cultivatedCropCropType) {
        LOG.debug("Request to partially update CultivatedCropCropType : {}", cultivatedCropCropType);

        return cultivatedCropCropTypeRepository
            .findById(cultivatedCropCropType.getId())
            .map(existingCultivatedCropCropType -> {
                if (cultivatedCropCropType.getCrop() != null) {
                    existingCultivatedCropCropType.setCrop(cultivatedCropCropType.getCrop());
                }
                if (cultivatedCropCropType.getImage() != null) {
                    existingCultivatedCropCropType.setImage(cultivatedCropCropType.getImage());
                }
                if (cultivatedCropCropType.getMainCrop() != null) {
                    existingCultivatedCropCropType.setMainCrop(cultivatedCropCropType.getMainCrop());
                }
                if (cultivatedCropCropType.getCropCode() != null) {
                    existingCultivatedCropCropType.setCropCode(cultivatedCropCropType.getCropCode());
                }
                if (cultivatedCropCropType.getNoOfStages() != null) {
                    existingCultivatedCropCropType.setNoOfStages(cultivatedCropCropType.getNoOfStages());
                }
                if (cultivatedCropCropType.getDescription() != null) {
                    existingCultivatedCropCropType.setDescription(cultivatedCropCropType.getDescription());
                }
                if (cultivatedCropCropType.getCropTypesId() != null) {
                    existingCultivatedCropCropType.setCropTypesId(cultivatedCropCropType.getCropTypesId());
                }
                if (cultivatedCropCropType.getUnitsId() != null) {
                    existingCultivatedCropCropType.setUnitsId(cultivatedCropCropType.getUnitsId());
                }
                if (cultivatedCropCropType.getArea() != null) {
                    existingCultivatedCropCropType.setArea(cultivatedCropCropType.getArea());
                }
                if (cultivatedCropCropType.getSumInsured() != null) {
                    existingCultivatedCropCropType.setSumInsured(cultivatedCropCropType.getSumInsured());
                }
                if (cultivatedCropCropType.getMinSumInsured() != null) {
                    existingCultivatedCropCropType.setMinSumInsured(cultivatedCropCropType.getMinSumInsured());
                }
                if (cultivatedCropCropType.getMaxSumInsured() != null) {
                    existingCultivatedCropCropType.setMaxSumInsured(cultivatedCropCropType.getMaxSumInsured());
                }
                if (cultivatedCropCropType.getSubsidisedPremiumRate() != null) {
                    existingCultivatedCropCropType.setSubsidisedPremiumRate(cultivatedCropCropType.getSubsidisedPremiumRate());
                }

                return existingCultivatedCropCropType;
            })
            .map(cultivatedCropCropTypeRepository::save);
    }

    /**
     * Get one cultivatedCropCropType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CultivatedCropCropType> findOne(Long id) {
        LOG.debug("Request to get CultivatedCropCropType : {}", id);
        return cultivatedCropCropTypeRepository.findById(id);
    }

    /**
     * Delete the cultivatedCropCropType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CultivatedCropCropType : {}", id);
        cultivatedCropCropTypeRepository.deleteById(id);
    }
}
