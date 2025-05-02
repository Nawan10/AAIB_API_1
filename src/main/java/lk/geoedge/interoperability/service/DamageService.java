package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.Damage;
import lk.geoedge.interoperability.repository.DamageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.Damage}.
 */
@Service
@Transactional
public class DamageService {

    private static final Logger LOG = LoggerFactory.getLogger(DamageService.class);

    private final DamageRepository damageRepository;

    public DamageService(DamageRepository damageRepository) {
        this.damageRepository = damageRepository;
    }

    /**
     * Save a damage.
     *
     * @param damage the entity to save.
     * @return the persisted entity.
     */
    public Damage save(Damage damage) {
        LOG.debug("Request to save Damage : {}", damage);
        return damageRepository.save(damage);
    }

    /**
     * Update a damage.
     *
     * @param damage the entity to save.
     * @return the persisted entity.
     */
    public Damage update(Damage damage) {
        LOG.debug("Request to update Damage : {}", damage);
        return damageRepository.save(damage);
    }

    /**
     * Partially update a damage.
     *
     * @param damage the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Damage> partialUpdate(Damage damage) {
        LOG.debug("Request to partially update Damage : {}", damage);

        return damageRepository
            .findById(damage.getId())
            .map(existingDamage -> {
                if (damage.getDamageName() != null) {
                    existingDamage.setDamageName(damage.getDamageName());
                }
                if (damage.getDamageCode() != null) {
                    existingDamage.setDamageCode(damage.getDamageCode());
                }
                if (damage.getDamageFamily() != null) {
                    existingDamage.setDamageFamily(damage.getDamageFamily());
                }
                if (damage.getDamageGenus() != null) {
                    existingDamage.setDamageGenus(damage.getDamageGenus());
                }
                if (damage.getDamageSpecies() != null) {
                    existingDamage.setDamageSpecies(damage.getDamageSpecies());
                }
                if (damage.getCreatedAt() != null) {
                    existingDamage.setCreatedAt(damage.getCreatedAt());
                }
                if (damage.getAddedBy() != null) {
                    existingDamage.setAddedBy(damage.getAddedBy());
                }

                return existingDamage;
            })
            .map(damageRepository::save);
    }

    /**
     * Get one damage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Damage> findOne(Long id) {
        LOG.debug("Request to get Damage : {}", id);
        return damageRepository.findById(id);
    }

    /**
     * Delete the damage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Damage : {}", id);
        damageRepository.deleteById(id);
    }
}
