package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageType;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReasonDamageTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageType}.
 */
@Service
@Transactional
public class CultivatedLandDamageReasonDamageTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandDamageReasonDamageTypeService.class);

    private final CultivatedLandDamageReasonDamageTypeRepository cultivatedLandDamageReasonDamageTypeRepository;

    public CultivatedLandDamageReasonDamageTypeService(
        CultivatedLandDamageReasonDamageTypeRepository cultivatedLandDamageReasonDamageTypeRepository
    ) {
        this.cultivatedLandDamageReasonDamageTypeRepository = cultivatedLandDamageReasonDamageTypeRepository;
    }

    /**
     * Save a cultivatedLandDamageReasonDamageType.
     *
     * @param cultivatedLandDamageReasonDamageType the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandDamageReasonDamageType save(CultivatedLandDamageReasonDamageType cultivatedLandDamageReasonDamageType) {
        LOG.debug("Request to save CultivatedLandDamageReasonDamageType : {}", cultivatedLandDamageReasonDamageType);
        return cultivatedLandDamageReasonDamageTypeRepository.save(cultivatedLandDamageReasonDamageType);
    }

    /**
     * Update a cultivatedLandDamageReasonDamageType.
     *
     * @param cultivatedLandDamageReasonDamageType the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandDamageReasonDamageType update(CultivatedLandDamageReasonDamageType cultivatedLandDamageReasonDamageType) {
        LOG.debug("Request to update CultivatedLandDamageReasonDamageType : {}", cultivatedLandDamageReasonDamageType);
        return cultivatedLandDamageReasonDamageTypeRepository.save(cultivatedLandDamageReasonDamageType);
    }

    /**
     * Partially update a cultivatedLandDamageReasonDamageType.
     *
     * @param cultivatedLandDamageReasonDamageType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CultivatedLandDamageReasonDamageType> partialUpdate(
        CultivatedLandDamageReasonDamageType cultivatedLandDamageReasonDamageType
    ) {
        LOG.debug("Request to partially update CultivatedLandDamageReasonDamageType : {}", cultivatedLandDamageReasonDamageType);

        return cultivatedLandDamageReasonDamageTypeRepository
            .findById(cultivatedLandDamageReasonDamageType.getId())
            .map(existingCultivatedLandDamageReasonDamageType -> {
                if (cultivatedLandDamageReasonDamageType.getTypeName() != null) {
                    existingCultivatedLandDamageReasonDamageType.setTypeName(cultivatedLandDamageReasonDamageType.getTypeName());
                }

                return existingCultivatedLandDamageReasonDamageType;
            })
            .map(cultivatedLandDamageReasonDamageTypeRepository::save);
    }

    /**
     * Get one cultivatedLandDamageReasonDamageType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CultivatedLandDamageReasonDamageType> findOne(Long id) {
        LOG.debug("Request to get CultivatedLandDamageReasonDamageType : {}", id);
        return cultivatedLandDamageReasonDamageTypeRepository.findById(id);
    }

    /**
     * Delete the cultivatedLandDamageReasonDamageType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CultivatedLandDamageReasonDamageType : {}", id);
        cultivatedLandDamageReasonDamageTypeRepository.deleteById(id);
    }
}
