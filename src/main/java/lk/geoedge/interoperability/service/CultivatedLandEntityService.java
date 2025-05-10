package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandEntity;
import lk.geoedge.interoperability.repository.CultivatedLandEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CultivatedLandEntity}.
 */
@Service
@Transactional
public class CultivatedLandEntityService {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandEntityService.class);

    private final CultivatedLandEntityRepository cultivatedLandEntityRepository;

    public CultivatedLandEntityService(CultivatedLandEntityRepository cultivatedLandEntityRepository) {
        this.cultivatedLandEntityRepository = cultivatedLandEntityRepository;
    }

    /**
     * Save a cultivatedLandEntity.
     *
     * @param cultivatedLandEntity the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandEntity save(CultivatedLandEntity cultivatedLandEntity) {
        LOG.debug("Request to save CultivatedLandEntity : {}", cultivatedLandEntity);
        return cultivatedLandEntityRepository.save(cultivatedLandEntity);
    }

    /**
     * Update a cultivatedLandEntity.
     *
     * @param cultivatedLandEntity the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandEntity update(CultivatedLandEntity cultivatedLandEntity) {
        LOG.debug("Request to update CultivatedLandEntity : {}", cultivatedLandEntity);
        return cultivatedLandEntityRepository.save(cultivatedLandEntity);
    }

    /**
     * Partially update a cultivatedLandEntity.
     *
     * @param cultivatedLandEntity the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CultivatedLandEntity> partialUpdate(CultivatedLandEntity cultivatedLandEntity) {
        LOG.debug("Request to partially update CultivatedLandEntity : {}", cultivatedLandEntity);

        return cultivatedLandEntityRepository
            .findById(cultivatedLandEntity.getId())
            .map(existingCultivatedLandEntity -> {
                if (cultivatedLandEntity.getLandStatus() != null) {
                    existingCultivatedLandEntity.setLandStatus(cultivatedLandEntity.getLandStatus());
                }
                if (cultivatedLandEntity.getUrea() != null) {
                    existingCultivatedLandEntity.setUrea(cultivatedLandEntity.getUrea());
                }
                if (cultivatedLandEntity.getMop() != null) {
                    existingCultivatedLandEntity.setMop(cultivatedLandEntity.getMop());
                }
                if (cultivatedLandEntity.getTsp() != null) {
                    existingCultivatedLandEntity.setTsp(cultivatedLandEntity.getTsp());
                }
                if (cultivatedLandEntity.getCreatedAt() != null) {
                    existingCultivatedLandEntity.setCreatedAt(cultivatedLandEntity.getCreatedAt());
                }
                if (cultivatedLandEntity.getAddedBy() != null) {
                    existingCultivatedLandEntity.setAddedBy(cultivatedLandEntity.getAddedBy());
                }

                return existingCultivatedLandEntity;
            })
            .map(cultivatedLandEntityRepository::save);
    }

    /**
     * Get one cultivatedLandEntity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CultivatedLandEntity> findOne(Long id) {
        LOG.debug("Request to get CultivatedLandEntity : {}", id);
        return cultivatedLandEntityRepository.findById(id);
    }

    /**
     * Delete the cultivatedLandEntity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CultivatedLandEntity : {}", id);
        cultivatedLandEntityRepository.deleteById(id);
    }
}
