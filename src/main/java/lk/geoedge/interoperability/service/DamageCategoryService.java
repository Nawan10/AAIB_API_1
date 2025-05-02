package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.DamageCategory;
import lk.geoedge.interoperability.repository.DamageCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.DamageCategory}.
 */
@Service
@Transactional
public class DamageCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(DamageCategoryService.class);

    private final DamageCategoryRepository damageCategoryRepository;

    public DamageCategoryService(DamageCategoryRepository damageCategoryRepository) {
        this.damageCategoryRepository = damageCategoryRepository;
    }

    /**
     * Save a damageCategory.
     *
     * @param damageCategory the entity to save.
     * @return the persisted entity.
     */
    public DamageCategory save(DamageCategory damageCategory) {
        LOG.debug("Request to save DamageCategory : {}", damageCategory);
        return damageCategoryRepository.save(damageCategory);
    }

    /**
     * Update a damageCategory.
     *
     * @param damageCategory the entity to save.
     * @return the persisted entity.
     */
    public DamageCategory update(DamageCategory damageCategory) {
        LOG.debug("Request to update DamageCategory : {}", damageCategory);
        return damageCategoryRepository.save(damageCategory);
    }

    /**
     * Partially update a damageCategory.
     *
     * @param damageCategory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DamageCategory> partialUpdate(DamageCategory damageCategory) {
        LOG.debug("Request to partially update DamageCategory : {}", damageCategory);

        return damageCategoryRepository
            .findById(damageCategory.getId())
            .map(existingDamageCategory -> {
                if (damageCategory.getCategoryName() != null) {
                    existingDamageCategory.setCategoryName(damageCategory.getCategoryName());
                }

                return existingDamageCategory;
            })
            .map(damageCategoryRepository::save);
    }

    /**
     * Get one damageCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DamageCategory> findOne(Long id) {
        LOG.debug("Request to get DamageCategory : {}", id);
        return damageCategoryRepository.findById(id);
    }

    /**
     * Delete the damageCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DamageCategory : {}", id);
        damageCategoryRepository.deleteById(id);
    }
}
