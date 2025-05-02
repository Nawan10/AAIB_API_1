package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandDamageReportDamageType;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReportDamageTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CultivatedLandDamageReportDamageType}.
 */
@Service
@Transactional
public class CultivatedLandDamageReportDamageTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandDamageReportDamageTypeService.class);

    private final CultivatedLandDamageReportDamageTypeRepository cultivatedLandDamageReportDamageTypeRepository;

    public CultivatedLandDamageReportDamageTypeService(
        CultivatedLandDamageReportDamageTypeRepository cultivatedLandDamageReportDamageTypeRepository
    ) {
        this.cultivatedLandDamageReportDamageTypeRepository = cultivatedLandDamageReportDamageTypeRepository;
    }

    /**
     * Save a cultivatedLandDamageReportDamageType.
     *
     * @param cultivatedLandDamageReportDamageType the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandDamageReportDamageType save(CultivatedLandDamageReportDamageType cultivatedLandDamageReportDamageType) {
        LOG.debug("Request to save CultivatedLandDamageReportDamageType : {}", cultivatedLandDamageReportDamageType);
        return cultivatedLandDamageReportDamageTypeRepository.save(cultivatedLandDamageReportDamageType);
    }

    /**
     * Update a cultivatedLandDamageReportDamageType.
     *
     * @param cultivatedLandDamageReportDamageType the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandDamageReportDamageType update(CultivatedLandDamageReportDamageType cultivatedLandDamageReportDamageType) {
        LOG.debug("Request to update CultivatedLandDamageReportDamageType : {}", cultivatedLandDamageReportDamageType);
        return cultivatedLandDamageReportDamageTypeRepository.save(cultivatedLandDamageReportDamageType);
    }

    /**
     * Partially update a cultivatedLandDamageReportDamageType.
     *
     * @param cultivatedLandDamageReportDamageType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CultivatedLandDamageReportDamageType> partialUpdate(
        CultivatedLandDamageReportDamageType cultivatedLandDamageReportDamageType
    ) {
        LOG.debug("Request to partially update CultivatedLandDamageReportDamageType : {}", cultivatedLandDamageReportDamageType);

        return cultivatedLandDamageReportDamageTypeRepository
            .findById(cultivatedLandDamageReportDamageType.getId())
            .map(existingCultivatedLandDamageReportDamageType -> {
                if (cultivatedLandDamageReportDamageType.getTypeName() != null) {
                    existingCultivatedLandDamageReportDamageType.setTypeName(cultivatedLandDamageReportDamageType.getTypeName());
                }

                return existingCultivatedLandDamageReportDamageType;
            })
            .map(cultivatedLandDamageReportDamageTypeRepository::save);
    }

    /**
     * Get one cultivatedLandDamageReportDamageType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CultivatedLandDamageReportDamageType> findOne(Long id) {
        LOG.debug("Request to get CultivatedLandDamageReportDamageType : {}", id);
        return cultivatedLandDamageReportDamageTypeRepository.findById(id);
    }

    /**
     * Delete the cultivatedLandDamageReportDamageType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CultivatedLandDamageReportDamageType : {}", id);
        cultivatedLandDamageReportDamageTypeRepository.deleteById(id);
    }
}
