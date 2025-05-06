package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandFarmersFarmer;
import lk.geoedge.interoperability.repository.CultivatedLandFarmersFarmerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CultivatedLandFarmersFarmer}.
 */
@Service
@Transactional
public class CultivatedLandFarmersFarmerService {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandFarmersFarmerService.class);

    private final CultivatedLandFarmersFarmerRepository cultivatedLandFarmersFarmerRepository;

    public CultivatedLandFarmersFarmerService(CultivatedLandFarmersFarmerRepository cultivatedLandFarmersFarmerRepository) {
        this.cultivatedLandFarmersFarmerRepository = cultivatedLandFarmersFarmerRepository;
    }

    /**
     * Save a cultivatedLandFarmersFarmer.
     *
     * @param cultivatedLandFarmersFarmer the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandFarmersFarmer save(CultivatedLandFarmersFarmer cultivatedLandFarmersFarmer) {
        LOG.debug("Request to save CultivatedLandFarmersFarmer : {}", cultivatedLandFarmersFarmer);
        return cultivatedLandFarmersFarmerRepository.save(cultivatedLandFarmersFarmer);
    }

    /**
     * Update a cultivatedLandFarmersFarmer.
     *
     * @param cultivatedLandFarmersFarmer the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandFarmersFarmer update(CultivatedLandFarmersFarmer cultivatedLandFarmersFarmer) {
        LOG.debug("Request to update CultivatedLandFarmersFarmer : {}", cultivatedLandFarmersFarmer);
        return cultivatedLandFarmersFarmerRepository.save(cultivatedLandFarmersFarmer);
    }

    /**
     * Partially update a cultivatedLandFarmersFarmer.
     *
     * @param cultivatedLandFarmersFarmer the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CultivatedLandFarmersFarmer> partialUpdate(CultivatedLandFarmersFarmer cultivatedLandFarmersFarmer) {
        LOG.debug("Request to partially update CultivatedLandFarmersFarmer : {}", cultivatedLandFarmersFarmer);

        return cultivatedLandFarmersFarmerRepository
            .findById(cultivatedLandFarmersFarmer.getId())
            .map(existingCultivatedLandFarmersFarmer -> {
                if (cultivatedLandFarmersFarmer.getFarmerId() != null) {
                    existingCultivatedLandFarmersFarmer.setFarmerId(cultivatedLandFarmersFarmer.getFarmerId());
                }
                if (cultivatedLandFarmersFarmer.getFarmerName() != null) {
                    existingCultivatedLandFarmersFarmer.setFarmerName(cultivatedLandFarmersFarmer.getFarmerName());
                }
                if (cultivatedLandFarmersFarmer.getNicNo() != null) {
                    existingCultivatedLandFarmersFarmer.setNicNo(cultivatedLandFarmersFarmer.getNicNo());
                }
                if (cultivatedLandFarmersFarmer.getAddressFirstLine() != null) {
                    existingCultivatedLandFarmersFarmer.setAddressFirstLine(cultivatedLandFarmersFarmer.getAddressFirstLine());
                }
                if (cultivatedLandFarmersFarmer.getContactNoEmail() != null) {
                    existingCultivatedLandFarmersFarmer.setContactNoEmail(cultivatedLandFarmersFarmer.getContactNoEmail());
                }
                if (cultivatedLandFarmersFarmer.getProvinceId() != null) {
                    existingCultivatedLandFarmersFarmer.setProvinceId(cultivatedLandFarmersFarmer.getProvinceId());
                }
                if (cultivatedLandFarmersFarmer.getDistrictId() != null) {
                    existingCultivatedLandFarmersFarmer.setDistrictId(cultivatedLandFarmersFarmer.getDistrictId());
                }
                if (cultivatedLandFarmersFarmer.getDsId() != null) {
                    existingCultivatedLandFarmersFarmer.setDsId(cultivatedLandFarmersFarmer.getDsId());
                }
                if (cultivatedLandFarmersFarmer.getGnId() != null) {
                    existingCultivatedLandFarmersFarmer.setGnId(cultivatedLandFarmersFarmer.getGnId());
                }
                if (cultivatedLandFarmersFarmer.getCity() != null) {
                    existingCultivatedLandFarmersFarmer.setCity(cultivatedLandFarmersFarmer.getCity());
                }
                if (cultivatedLandFarmersFarmer.getAddedDate() != null) {
                    existingCultivatedLandFarmersFarmer.setAddedDate(cultivatedLandFarmersFarmer.getAddedDate());
                }

                return existingCultivatedLandFarmersFarmer;
            })
            .map(cultivatedLandFarmersFarmerRepository::save);
    }

    /**
     * Get one cultivatedLandFarmersFarmer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CultivatedLandFarmersFarmer> findOne(Long id) {
        LOG.debug("Request to get CultivatedLandFarmersFarmer : {}", id);
        return cultivatedLandFarmersFarmerRepository.findById(id);
    }

    /**
     * Delete the cultivatedLandFarmersFarmer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CultivatedLandFarmersFarmer : {}", id);
        cultivatedLandFarmersFarmerRepository.deleteById(id);
    }
}
