package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CropDamage;
import lk.geoedge.interoperability.repository.CropDamageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CropDamage}.
 */
@Service
@Transactional
public class CropDamageService {

    private static final Logger LOG = LoggerFactory.getLogger(CropDamageService.class);

    private final CropDamageRepository cropDamageRepository;

    public CropDamageService(CropDamageRepository cropDamageRepository) {
        this.cropDamageRepository = cropDamageRepository;
    }

    /**
     * Save a cropDamage.
     *
     * @param cropDamage the entity to save.
     * @return the persisted entity.
     */
    public CropDamage save(CropDamage cropDamage) {
        LOG.debug("Request to save CropDamage : {}", cropDamage);
        return cropDamageRepository.save(cropDamage);
    }

    /**
     * Update a cropDamage.
     *
     * @param cropDamage the entity to save.
     * @return the persisted entity.
     */
    public CropDamage update(CropDamage cropDamage) {
        LOG.debug("Request to update CropDamage : {}", cropDamage);
        return cropDamageRepository.save(cropDamage);
    }

    /**
     * Partially update a cropDamage.
     *
     * @param cropDamage the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CropDamage> partialUpdate(CropDamage cropDamage) {
        LOG.debug("Request to partially update CropDamage : {}", cropDamage);

        return cropDamageRepository
            .findById(cropDamage.getId())
            .map(existingCropDamage -> {
                if (cropDamage.getAddedBy() != null) {
                    existingCropDamage.setAddedBy(cropDamage.getAddedBy());
                }
                if (cropDamage.getCreatedAt() != null) {
                    existingCropDamage.setCreatedAt(cropDamage.getCreatedAt());
                }

                return existingCropDamage;
            })
            .map(cropDamageRepository::save);
    }

    /**
     * Get one cropDamage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CropDamage> findOne(Long id) {
        LOG.debug("Request to get CropDamage : {}", id);
        return cropDamageRepository.findById(id);
    }

    /**
     * Delete the cropDamage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CropDamage : {}", id);
        cropDamageRepository.deleteById(id);
    }
}
