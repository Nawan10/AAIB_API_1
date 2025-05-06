package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CropDamageDamage;
import lk.geoedge.interoperability.repository.CropDamageDamageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CropDamageDamage}.
 */
@Service
@Transactional
public class CropDamageDamageService {

    private static final Logger LOG = LoggerFactory.getLogger(CropDamageDamageService.class);

    private final CropDamageDamageRepository cropDamageDamageRepository;

    public CropDamageDamageService(CropDamageDamageRepository cropDamageDamageRepository) {
        this.cropDamageDamageRepository = cropDamageDamageRepository;
    }

    /**
     * Save a cropDamageDamage.
     *
     * @param cropDamageDamage the entity to save.
     * @return the persisted entity.
     */
    public CropDamageDamage save(CropDamageDamage cropDamageDamage) {
        LOG.debug("Request to save CropDamageDamage : {}", cropDamageDamage);
        return cropDamageDamageRepository.save(cropDamageDamage);
    }

    /**
     * Update a cropDamageDamage.
     *
     * @param cropDamageDamage the entity to save.
     * @return the persisted entity.
     */
    public CropDamageDamage update(CropDamageDamage cropDamageDamage) {
        LOG.debug("Request to update CropDamageDamage : {}", cropDamageDamage);
        return cropDamageDamageRepository.save(cropDamageDamage);
    }

    /**
     * Partially update a cropDamageDamage.
     *
     * @param cropDamageDamage the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CropDamageDamage> partialUpdate(CropDamageDamage cropDamageDamage) {
        LOG.debug("Request to partially update CropDamageDamage : {}", cropDamageDamage);

        return cropDamageDamageRepository
            .findById(cropDamageDamage.getId())
            .map(existingCropDamageDamage -> {
                if (cropDamageDamage.getDamageName() != null) {
                    existingCropDamageDamage.setDamageName(cropDamageDamage.getDamageName());
                }
                if (cropDamageDamage.getDamageCode() != null) {
                    existingCropDamageDamage.setDamageCode(cropDamageDamage.getDamageCode());
                }
                if (cropDamageDamage.getDamageFamily() != null) {
                    existingCropDamageDamage.setDamageFamily(cropDamageDamage.getDamageFamily());
                }
                if (cropDamageDamage.getDamageGenus() != null) {
                    existingCropDamageDamage.setDamageGenus(cropDamageDamage.getDamageGenus());
                }
                if (cropDamageDamage.getDamageSpecies() != null) {
                    existingCropDamageDamage.setDamageSpecies(cropDamageDamage.getDamageSpecies());
                }
                if (cropDamageDamage.getCreatedAt() != null) {
                    existingCropDamageDamage.setCreatedAt(cropDamageDamage.getCreatedAt());
                }
                if (cropDamageDamage.getAddedBy() != null) {
                    existingCropDamageDamage.setAddedBy(cropDamageDamage.getAddedBy());
                }

                return existingCropDamageDamage;
            })
            .map(cropDamageDamageRepository::save);
    }

    /**
     * Get one cropDamageDamage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CropDamageDamage> findOne(Long id) {
        LOG.debug("Request to get CropDamageDamage : {}", id);
        return cropDamageDamageRepository.findById(id);
    }

    /**
     * Delete the cropDamageDamage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CropDamageDamage : {}", id);
        cropDamageDamageRepository.deleteById(id);
    }
}
