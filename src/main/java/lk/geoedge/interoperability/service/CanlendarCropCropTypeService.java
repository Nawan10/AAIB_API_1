package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CanlendarCropCropType;
import lk.geoedge.interoperability.repository.CanlendarCropCropTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CanlendarCropCropType}.
 */
@Service
@Transactional
public class CanlendarCropCropTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(CanlendarCropCropTypeService.class);

    private final CanlendarCropCropTypeRepository canlendarCropCropTypeRepository;

    public CanlendarCropCropTypeService(CanlendarCropCropTypeRepository canlendarCropCropTypeRepository) {
        this.canlendarCropCropTypeRepository = canlendarCropCropTypeRepository;
    }

    /**
     * Save a canlendarCropCropType.
     *
     * @param canlendarCropCropType the entity to save.
     * @return the persisted entity.
     */
    public CanlendarCropCropType save(CanlendarCropCropType canlendarCropCropType) {
        LOG.debug("Request to save CanlendarCropCropType : {}", canlendarCropCropType);
        return canlendarCropCropTypeRepository.save(canlendarCropCropType);
    }

    /**
     * Update a canlendarCropCropType.
     *
     * @param canlendarCropCropType the entity to save.
     * @return the persisted entity.
     */
    public CanlendarCropCropType update(CanlendarCropCropType canlendarCropCropType) {
        LOG.debug("Request to update CanlendarCropCropType : {}", canlendarCropCropType);
        return canlendarCropCropTypeRepository.save(canlendarCropCropType);
    }

    /**
     * Partially update a canlendarCropCropType.
     *
     * @param canlendarCropCropType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CanlendarCropCropType> partialUpdate(CanlendarCropCropType canlendarCropCropType) {
        LOG.debug("Request to partially update CanlendarCropCropType : {}", canlendarCropCropType);

        return canlendarCropCropTypeRepository
            .findById(canlendarCropCropType.getId())
            .map(existingCanlendarCropCropType -> {
                if (canlendarCropCropType.getCrop() != null) {
                    existingCanlendarCropCropType.setCrop(canlendarCropCropType.getCrop());
                }
                if (canlendarCropCropType.getImage() != null) {
                    existingCanlendarCropCropType.setImage(canlendarCropCropType.getImage());
                }
                if (canlendarCropCropType.getMainCrop() != null) {
                    existingCanlendarCropCropType.setMainCrop(canlendarCropCropType.getMainCrop());
                }
                if (canlendarCropCropType.getCropCode() != null) {
                    existingCanlendarCropCropType.setCropCode(canlendarCropCropType.getCropCode());
                }
                if (canlendarCropCropType.getNoOfStages() != null) {
                    existingCanlendarCropCropType.setNoOfStages(canlendarCropCropType.getNoOfStages());
                }
                if (canlendarCropCropType.getDescription() != null) {
                    existingCanlendarCropCropType.setDescription(canlendarCropCropType.getDescription());
                }
                if (canlendarCropCropType.getCropTypesId() != null) {
                    existingCanlendarCropCropType.setCropTypesId(canlendarCropCropType.getCropTypesId());
                }
                if (canlendarCropCropType.getUnitsId() != null) {
                    existingCanlendarCropCropType.setUnitsId(canlendarCropCropType.getUnitsId());
                }
                if (canlendarCropCropType.getArea() != null) {
                    existingCanlendarCropCropType.setArea(canlendarCropCropType.getArea());
                }
                if (canlendarCropCropType.getSumInsured() != null) {
                    existingCanlendarCropCropType.setSumInsured(canlendarCropCropType.getSumInsured());
                }
                if (canlendarCropCropType.getMinSumInsured() != null) {
                    existingCanlendarCropCropType.setMinSumInsured(canlendarCropCropType.getMinSumInsured());
                }
                if (canlendarCropCropType.getMaxSumInsured() != null) {
                    existingCanlendarCropCropType.setMaxSumInsured(canlendarCropCropType.getMaxSumInsured());
                }
                if (canlendarCropCropType.getSubsidisedPremiumRate() != null) {
                    existingCanlendarCropCropType.setSubsidisedPremiumRate(canlendarCropCropType.getSubsidisedPremiumRate());
                }

                return existingCanlendarCropCropType;
            })
            .map(canlendarCropCropTypeRepository::save);
    }

    /**
     * Get one canlendarCropCropType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CanlendarCropCropType> findOne(Long id) {
        LOG.debug("Request to get CanlendarCropCropType : {}", id);
        return canlendarCropCropTypeRepository.findById(id);
    }

    /**
     * Delete the canlendarCropCropType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CanlendarCropCropType : {}", id);
        canlendarCropCropTypeRepository.deleteById(id);
    }
}
