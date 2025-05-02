package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCropType;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandCropTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.InsuranceCultivatedLandCropType}.
 */
@Service
@Transactional
public class InsuranceCultivatedLandCropTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCultivatedLandCropTypeService.class);

    private final InsuranceCultivatedLandCropTypeRepository insuranceCultivatedLandCropTypeRepository;

    public InsuranceCultivatedLandCropTypeService(InsuranceCultivatedLandCropTypeRepository insuranceCultivatedLandCropTypeRepository) {
        this.insuranceCultivatedLandCropTypeRepository = insuranceCultivatedLandCropTypeRepository;
    }

    /**
     * Save a insuranceCultivatedLandCropType.
     *
     * @param insuranceCultivatedLandCropType the entity to save.
     * @return the persisted entity.
     */
    public InsuranceCultivatedLandCropType save(InsuranceCultivatedLandCropType insuranceCultivatedLandCropType) {
        LOG.debug("Request to save InsuranceCultivatedLandCropType : {}", insuranceCultivatedLandCropType);
        return insuranceCultivatedLandCropTypeRepository.save(insuranceCultivatedLandCropType);
    }

    /**
     * Update a insuranceCultivatedLandCropType.
     *
     * @param insuranceCultivatedLandCropType the entity to save.
     * @return the persisted entity.
     */
    public InsuranceCultivatedLandCropType update(InsuranceCultivatedLandCropType insuranceCultivatedLandCropType) {
        LOG.debug("Request to update InsuranceCultivatedLandCropType : {}", insuranceCultivatedLandCropType);
        return insuranceCultivatedLandCropTypeRepository.save(insuranceCultivatedLandCropType);
    }

    /**
     * Partially update a insuranceCultivatedLandCropType.
     *
     * @param insuranceCultivatedLandCropType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InsuranceCultivatedLandCropType> partialUpdate(InsuranceCultivatedLandCropType insuranceCultivatedLandCropType) {
        LOG.debug("Request to partially update InsuranceCultivatedLandCropType : {}", insuranceCultivatedLandCropType);

        return insuranceCultivatedLandCropTypeRepository
            .findById(insuranceCultivatedLandCropType.getId())
            .map(existingInsuranceCultivatedLandCropType -> {
                if (insuranceCultivatedLandCropType.getCrop() != null) {
                    existingInsuranceCultivatedLandCropType.setCrop(insuranceCultivatedLandCropType.getCrop());
                }
                if (insuranceCultivatedLandCropType.getImage() != null) {
                    existingInsuranceCultivatedLandCropType.setImage(insuranceCultivatedLandCropType.getImage());
                }
                if (insuranceCultivatedLandCropType.getMainCrop() != null) {
                    existingInsuranceCultivatedLandCropType.setMainCrop(insuranceCultivatedLandCropType.getMainCrop());
                }
                if (insuranceCultivatedLandCropType.getCropCode() != null) {
                    existingInsuranceCultivatedLandCropType.setCropCode(insuranceCultivatedLandCropType.getCropCode());
                }
                if (insuranceCultivatedLandCropType.getNoOfStages() != null) {
                    existingInsuranceCultivatedLandCropType.setNoOfStages(insuranceCultivatedLandCropType.getNoOfStages());
                }
                if (insuranceCultivatedLandCropType.getDescription() != null) {
                    existingInsuranceCultivatedLandCropType.setDescription(insuranceCultivatedLandCropType.getDescription());
                }
                if (insuranceCultivatedLandCropType.getCropTypesId() != null) {
                    existingInsuranceCultivatedLandCropType.setCropTypesId(insuranceCultivatedLandCropType.getCropTypesId());
                }
                if (insuranceCultivatedLandCropType.getUnitsId() != null) {
                    existingInsuranceCultivatedLandCropType.setUnitsId(insuranceCultivatedLandCropType.getUnitsId());
                }
                if (insuranceCultivatedLandCropType.getArea() != null) {
                    existingInsuranceCultivatedLandCropType.setArea(insuranceCultivatedLandCropType.getArea());
                }
                if (insuranceCultivatedLandCropType.getSumInsured() != null) {
                    existingInsuranceCultivatedLandCropType.setSumInsured(insuranceCultivatedLandCropType.getSumInsured());
                }
                if (insuranceCultivatedLandCropType.getMinSumInsured() != null) {
                    existingInsuranceCultivatedLandCropType.setMinSumInsured(insuranceCultivatedLandCropType.getMinSumInsured());
                }
                if (insuranceCultivatedLandCropType.getMaxSumInsured() != null) {
                    existingInsuranceCultivatedLandCropType.setMaxSumInsured(insuranceCultivatedLandCropType.getMaxSumInsured());
                }
                if (insuranceCultivatedLandCropType.getSubsidisedPremiumRate() != null) {
                    existingInsuranceCultivatedLandCropType.setSubsidisedPremiumRate(
                        insuranceCultivatedLandCropType.getSubsidisedPremiumRate()
                    );
                }

                return existingInsuranceCultivatedLandCropType;
            })
            .map(insuranceCultivatedLandCropTypeRepository::save);
    }

    /**
     * Get one insuranceCultivatedLandCropType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InsuranceCultivatedLandCropType> findOne(Long id) {
        LOG.debug("Request to get InsuranceCultivatedLandCropType : {}", id);
        return insuranceCultivatedLandCropTypeRepository.findById(id);
    }

    /**
     * Delete the insuranceCultivatedLandCropType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InsuranceCultivatedLandCropType : {}", id);
        insuranceCultivatedLandCropTypeRepository.deleteById(id);
    }
}
