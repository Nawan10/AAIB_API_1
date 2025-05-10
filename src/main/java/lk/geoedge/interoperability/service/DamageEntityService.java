package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.DamageEntity;
import lk.geoedge.interoperability.repository.DamageEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.DamageEntity}.
 */
@Service
@Transactional
public class DamageEntityService {

    private static final Logger LOG = LoggerFactory.getLogger(DamageEntityService.class);

    private final DamageEntityRepository damageEntityRepository;

    public DamageEntityService(DamageEntityRepository damageEntityRepository) {
        this.damageEntityRepository = damageEntityRepository;
    }

    /**
     * Save a damageEntity.
     *
     * @param damageEntity the entity to save.
     * @return the persisted entity.
     */
    public DamageEntity save(DamageEntity damageEntity) {
        LOG.debug("Request to save DamageEntity : {}", damageEntity);
        return damageEntityRepository.save(damageEntity);
    }

    /**
     * Update a damageEntity.
     *
     * @param damageEntity the entity to save.
     * @return the persisted entity.
     */
    public DamageEntity update(DamageEntity damageEntity) {
        LOG.debug("Request to update DamageEntity : {}", damageEntity);
        return damageEntityRepository.save(damageEntity);
    }

    /**
     * Partially update a damageEntity.
     *
     * @param damageEntity the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DamageEntity> partialUpdate(DamageEntity damageEntity) {
        LOG.debug("Request to partially update DamageEntity : {}", damageEntity);

        return damageEntityRepository
            .findById(damageEntity.getId())
            .map(existingDamageEntity -> {
                if (damageEntity.getDamageName() != null) {
                    existingDamageEntity.setDamageName(damageEntity.getDamageName());
                }
                if (damageEntity.getDamageCode() != null) {
                    existingDamageEntity.setDamageCode(damageEntity.getDamageCode());
                }
                if (damageEntity.getDamageFamily() != null) {
                    existingDamageEntity.setDamageFamily(damageEntity.getDamageFamily());
                }
                if (damageEntity.getDamageGenus() != null) {
                    existingDamageEntity.setDamageGenus(damageEntity.getDamageGenus());
                }
                if (damageEntity.getDamageSpecies() != null) {
                    existingDamageEntity.setDamageSpecies(damageEntity.getDamageSpecies());
                }
                if (damageEntity.getCreatedAt() != null) {
                    existingDamageEntity.setCreatedAt(damageEntity.getCreatedAt());
                }
                if (damageEntity.getAddedBy() != null) {
                    existingDamageEntity.setAddedBy(damageEntity.getAddedBy());
                }

                return existingDamageEntity;
            })
            .map(damageEntityRepository::save);
    }

    /**
     * Get one damageEntity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DamageEntity> findOne(Long id) {
        LOG.debug("Request to get DamageEntity : {}", id);
        return damageEntityRepository.findById(id);
    }

    /**
     * Delete the damageEntity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DamageEntity : {}", id);
        damageEntityRepository.deleteById(id);
    }
}
