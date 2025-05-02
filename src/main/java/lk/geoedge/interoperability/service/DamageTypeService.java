package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.DamageType;
import lk.geoedge.interoperability.repository.DamageTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.DamageType}.
 */
@Service
@Transactional
public class DamageTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(DamageTypeService.class);

    private final DamageTypeRepository damageTypeRepository;

    public DamageTypeService(DamageTypeRepository damageTypeRepository) {
        this.damageTypeRepository = damageTypeRepository;
    }

    /**
     * Save a damageType.
     *
     * @param damageType the entity to save.
     * @return the persisted entity.
     */
    public DamageType save(DamageType damageType) {
        LOG.debug("Request to save DamageType : {}", damageType);
        return damageTypeRepository.save(damageType);
    }

    /**
     * Update a damageType.
     *
     * @param damageType the entity to save.
     * @return the persisted entity.
     */
    public DamageType update(DamageType damageType) {
        LOG.debug("Request to update DamageType : {}", damageType);
        return damageTypeRepository.save(damageType);
    }

    /**
     * Partially update a damageType.
     *
     * @param damageType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DamageType> partialUpdate(DamageType damageType) {
        LOG.debug("Request to partially update DamageType : {}", damageType);

        return damageTypeRepository
            .findById(damageType.getId())
            .map(existingDamageType -> {
                if (damageType.getTypeName() != null) {
                    existingDamageType.setTypeName(damageType.getTypeName());
                }

                return existingDamageType;
            })
            .map(damageTypeRepository::save);
    }

    /**
     * Get one damageType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DamageType> findOne(Long id) {
        LOG.debug("Request to get DamageType : {}", id);
        return damageTypeRepository.findById(id);
    }

    /**
     * Delete the damageType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DamageType : {}", id);
        damageTypeRepository.deleteById(id);
    }
}
