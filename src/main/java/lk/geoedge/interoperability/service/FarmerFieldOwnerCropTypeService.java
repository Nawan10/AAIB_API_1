package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.FarmerFieldOwnerCropType;
import lk.geoedge.interoperability.repository.FarmerFieldOwnerCropTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.FarmerFieldOwnerCropType}.
 */
@Service
@Transactional
public class FarmerFieldOwnerCropTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(FarmerFieldOwnerCropTypeService.class);

    private final FarmerFieldOwnerCropTypeRepository farmerFieldOwnerCropTypeRepository;

    public FarmerFieldOwnerCropTypeService(FarmerFieldOwnerCropTypeRepository farmerFieldOwnerCropTypeRepository) {
        this.farmerFieldOwnerCropTypeRepository = farmerFieldOwnerCropTypeRepository;
    }

    /**
     * Save a farmerFieldOwnerCropType.
     *
     * @param farmerFieldOwnerCropType the entity to save.
     * @return the persisted entity.
     */
    public FarmerFieldOwnerCropType save(FarmerFieldOwnerCropType farmerFieldOwnerCropType) {
        LOG.debug("Request to save FarmerFieldOwnerCropType : {}", farmerFieldOwnerCropType);
        return farmerFieldOwnerCropTypeRepository.save(farmerFieldOwnerCropType);
    }

    /**
     * Update a farmerFieldOwnerCropType.
     *
     * @param farmerFieldOwnerCropType the entity to save.
     * @return the persisted entity.
     */
    public FarmerFieldOwnerCropType update(FarmerFieldOwnerCropType farmerFieldOwnerCropType) {
        LOG.debug("Request to update FarmerFieldOwnerCropType : {}", farmerFieldOwnerCropType);
        return farmerFieldOwnerCropTypeRepository.save(farmerFieldOwnerCropType);
    }

    /**
     * Partially update a farmerFieldOwnerCropType.
     *
     * @param farmerFieldOwnerCropType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FarmerFieldOwnerCropType> partialUpdate(FarmerFieldOwnerCropType farmerFieldOwnerCropType) {
        LOG.debug("Request to partially update FarmerFieldOwnerCropType : {}", farmerFieldOwnerCropType);

        return farmerFieldOwnerCropTypeRepository
            .findById(farmerFieldOwnerCropType.getId())
            .map(existingFarmerFieldOwnerCropType -> {
                if (farmerFieldOwnerCropType.getCrop() != null) {
                    existingFarmerFieldOwnerCropType.setCrop(farmerFieldOwnerCropType.getCrop());
                }
                if (farmerFieldOwnerCropType.getImage() != null) {
                    existingFarmerFieldOwnerCropType.setImage(farmerFieldOwnerCropType.getImage());
                }
                if (farmerFieldOwnerCropType.getMainCrop() != null) {
                    existingFarmerFieldOwnerCropType.setMainCrop(farmerFieldOwnerCropType.getMainCrop());
                }
                if (farmerFieldOwnerCropType.getCropCode() != null) {
                    existingFarmerFieldOwnerCropType.setCropCode(farmerFieldOwnerCropType.getCropCode());
                }
                if (farmerFieldOwnerCropType.getNoOfStages() != null) {
                    existingFarmerFieldOwnerCropType.setNoOfStages(farmerFieldOwnerCropType.getNoOfStages());
                }
                if (farmerFieldOwnerCropType.getDescription() != null) {
                    existingFarmerFieldOwnerCropType.setDescription(farmerFieldOwnerCropType.getDescription());
                }
                if (farmerFieldOwnerCropType.getCropTypesId() != null) {
                    existingFarmerFieldOwnerCropType.setCropTypesId(farmerFieldOwnerCropType.getCropTypesId());
                }
                if (farmerFieldOwnerCropType.getUnitsId() != null) {
                    existingFarmerFieldOwnerCropType.setUnitsId(farmerFieldOwnerCropType.getUnitsId());
                }
                if (farmerFieldOwnerCropType.getArea() != null) {
                    existingFarmerFieldOwnerCropType.setArea(farmerFieldOwnerCropType.getArea());
                }
                if (farmerFieldOwnerCropType.getSumInsured() != null) {
                    existingFarmerFieldOwnerCropType.setSumInsured(farmerFieldOwnerCropType.getSumInsured());
                }
                if (farmerFieldOwnerCropType.getMinSumInsured() != null) {
                    existingFarmerFieldOwnerCropType.setMinSumInsured(farmerFieldOwnerCropType.getMinSumInsured());
                }
                if (farmerFieldOwnerCropType.getMaxSumInsured() != null) {
                    existingFarmerFieldOwnerCropType.setMaxSumInsured(farmerFieldOwnerCropType.getMaxSumInsured());
                }
                if (farmerFieldOwnerCropType.getSubsidisedPremiumRate() != null) {
                    existingFarmerFieldOwnerCropType.setSubsidisedPremiumRate(farmerFieldOwnerCropType.getSubsidisedPremiumRate());
                }

                return existingFarmerFieldOwnerCropType;
            })
            .map(farmerFieldOwnerCropTypeRepository::save);
    }

    /**
     * Get one farmerFieldOwnerCropType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FarmerFieldOwnerCropType> findOne(Long id) {
        LOG.debug("Request to get FarmerFieldOwnerCropType : {}", id);
        return farmerFieldOwnerCropTypeRepository.findById(id);
    }

    /**
     * Delete the farmerFieldOwnerCropType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FarmerFieldOwnerCropType : {}", id);
        farmerFieldOwnerCropTypeRepository.deleteById(id);
    }
}
