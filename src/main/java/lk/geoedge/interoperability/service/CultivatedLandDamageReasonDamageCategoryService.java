package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageCategory;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReasonDamageCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageCategory}.
 */
@Service
@Transactional
public class CultivatedLandDamageReasonDamageCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandDamageReasonDamageCategoryService.class);

    private final CultivatedLandDamageReasonDamageCategoryRepository cultivatedLandDamageReasonDamageCategoryRepository;

    public CultivatedLandDamageReasonDamageCategoryService(
        CultivatedLandDamageReasonDamageCategoryRepository cultivatedLandDamageReasonDamageCategoryRepository
    ) {
        this.cultivatedLandDamageReasonDamageCategoryRepository = cultivatedLandDamageReasonDamageCategoryRepository;
    }

    /**
     * Save a cultivatedLandDamageReasonDamageCategory.
     *
     * @param cultivatedLandDamageReasonDamageCategory the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandDamageReasonDamageCategory save(
        CultivatedLandDamageReasonDamageCategory cultivatedLandDamageReasonDamageCategory
    ) {
        LOG.debug("Request to save CultivatedLandDamageReasonDamageCategory : {}", cultivatedLandDamageReasonDamageCategory);
        return cultivatedLandDamageReasonDamageCategoryRepository.save(cultivatedLandDamageReasonDamageCategory);
    }

    /**
     * Update a cultivatedLandDamageReasonDamageCategory.
     *
     * @param cultivatedLandDamageReasonDamageCategory the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandDamageReasonDamageCategory update(
        CultivatedLandDamageReasonDamageCategory cultivatedLandDamageReasonDamageCategory
    ) {
        LOG.debug("Request to update CultivatedLandDamageReasonDamageCategory : {}", cultivatedLandDamageReasonDamageCategory);
        return cultivatedLandDamageReasonDamageCategoryRepository.save(cultivatedLandDamageReasonDamageCategory);
    }

    /**
     * Partially update a cultivatedLandDamageReasonDamageCategory.
     *
     * @param cultivatedLandDamageReasonDamageCategory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CultivatedLandDamageReasonDamageCategory> partialUpdate(
        CultivatedLandDamageReasonDamageCategory cultivatedLandDamageReasonDamageCategory
    ) {
        LOG.debug("Request to partially update CultivatedLandDamageReasonDamageCategory : {}", cultivatedLandDamageReasonDamageCategory);

        return cultivatedLandDamageReasonDamageCategoryRepository
            .findById(cultivatedLandDamageReasonDamageCategory.getId())
            .map(existingCultivatedLandDamageReasonDamageCategory -> {
                if (cultivatedLandDamageReasonDamageCategory.getCategoryName() != null) {
                    existingCultivatedLandDamageReasonDamageCategory.setCategoryName(
                        cultivatedLandDamageReasonDamageCategory.getCategoryName()
                    );
                }

                return existingCultivatedLandDamageReasonDamageCategory;
            })
            .map(cultivatedLandDamageReasonDamageCategoryRepository::save);
    }

    /**
     * Get one cultivatedLandDamageReasonDamageCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CultivatedLandDamageReasonDamageCategory> findOne(Long id) {
        LOG.debug("Request to get CultivatedLandDamageReasonDamageCategory : {}", id);
        return cultivatedLandDamageReasonDamageCategoryRepository.findById(id);
    }

    /**
     * Delete the cultivatedLandDamageReasonDamageCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CultivatedLandDamageReasonDamageCategory : {}", id);
        cultivatedLandDamageReasonDamageCategoryRepository.deleteById(id);
    }
}
