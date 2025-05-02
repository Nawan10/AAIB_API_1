package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandDamageReportDamageCategory;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReportDamageCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CultivatedLandDamageReportDamageCategory}.
 */
@Service
@Transactional
public class CultivatedLandDamageReportDamageCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandDamageReportDamageCategoryService.class);

    private final CultivatedLandDamageReportDamageCategoryRepository cultivatedLandDamageReportDamageCategoryRepository;

    public CultivatedLandDamageReportDamageCategoryService(
        CultivatedLandDamageReportDamageCategoryRepository cultivatedLandDamageReportDamageCategoryRepository
    ) {
        this.cultivatedLandDamageReportDamageCategoryRepository = cultivatedLandDamageReportDamageCategoryRepository;
    }

    /**
     * Save a cultivatedLandDamageReportDamageCategory.
     *
     * @param cultivatedLandDamageReportDamageCategory the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandDamageReportDamageCategory save(
        CultivatedLandDamageReportDamageCategory cultivatedLandDamageReportDamageCategory
    ) {
        LOG.debug("Request to save CultivatedLandDamageReportDamageCategory : {}", cultivatedLandDamageReportDamageCategory);
        return cultivatedLandDamageReportDamageCategoryRepository.save(cultivatedLandDamageReportDamageCategory);
    }

    /**
     * Update a cultivatedLandDamageReportDamageCategory.
     *
     * @param cultivatedLandDamageReportDamageCategory the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandDamageReportDamageCategory update(
        CultivatedLandDamageReportDamageCategory cultivatedLandDamageReportDamageCategory
    ) {
        LOG.debug("Request to update CultivatedLandDamageReportDamageCategory : {}", cultivatedLandDamageReportDamageCategory);
        return cultivatedLandDamageReportDamageCategoryRepository.save(cultivatedLandDamageReportDamageCategory);
    }

    /**
     * Partially update a cultivatedLandDamageReportDamageCategory.
     *
     * @param cultivatedLandDamageReportDamageCategory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CultivatedLandDamageReportDamageCategory> partialUpdate(
        CultivatedLandDamageReportDamageCategory cultivatedLandDamageReportDamageCategory
    ) {
        LOG.debug("Request to partially update CultivatedLandDamageReportDamageCategory : {}", cultivatedLandDamageReportDamageCategory);

        return cultivatedLandDamageReportDamageCategoryRepository
            .findById(cultivatedLandDamageReportDamageCategory.getId())
            .map(existingCultivatedLandDamageReportDamageCategory -> {
                if (cultivatedLandDamageReportDamageCategory.getCategoryName() != null) {
                    existingCultivatedLandDamageReportDamageCategory.setCategoryName(
                        cultivatedLandDamageReportDamageCategory.getCategoryName()
                    );
                }

                return existingCultivatedLandDamageReportDamageCategory;
            })
            .map(cultivatedLandDamageReportDamageCategoryRepository::save);
    }

    /**
     * Get one cultivatedLandDamageReportDamageCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CultivatedLandDamageReportDamageCategory> findOne(Long id) {
        LOG.debug("Request to get CultivatedLandDamageReportDamageCategory : {}", id);
        return cultivatedLandDamageReportDamageCategoryRepository.findById(id);
    }

    /**
     * Delete the cultivatedLandDamageReportDamageCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CultivatedLandDamageReportDamageCategory : {}", id);
        cultivatedLandDamageReportDamageCategoryRepository.deleteById(id);
    }
}
