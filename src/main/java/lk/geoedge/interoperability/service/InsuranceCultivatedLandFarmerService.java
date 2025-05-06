package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandFarmer;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandFarmerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.InsuranceCultivatedLandFarmer}.
 */
@Service
@Transactional
public class InsuranceCultivatedLandFarmerService {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCultivatedLandFarmerService.class);

    private final InsuranceCultivatedLandFarmerRepository insuranceCultivatedLandFarmerRepository;

    public InsuranceCultivatedLandFarmerService(InsuranceCultivatedLandFarmerRepository insuranceCultivatedLandFarmerRepository) {
        this.insuranceCultivatedLandFarmerRepository = insuranceCultivatedLandFarmerRepository;
    }

    /**
     * Save a insuranceCultivatedLandFarmer.
     *
     * @param insuranceCultivatedLandFarmer the entity to save.
     * @return the persisted entity.
     */
    public InsuranceCultivatedLandFarmer save(InsuranceCultivatedLandFarmer insuranceCultivatedLandFarmer) {
        LOG.debug("Request to save InsuranceCultivatedLandFarmer : {}", insuranceCultivatedLandFarmer);
        return insuranceCultivatedLandFarmerRepository.save(insuranceCultivatedLandFarmer);
    }

    /**
     * Update a insuranceCultivatedLandFarmer.
     *
     * @param insuranceCultivatedLandFarmer the entity to save.
     * @return the persisted entity.
     */
    public InsuranceCultivatedLandFarmer update(InsuranceCultivatedLandFarmer insuranceCultivatedLandFarmer) {
        LOG.debug("Request to update InsuranceCultivatedLandFarmer : {}", insuranceCultivatedLandFarmer);
        return insuranceCultivatedLandFarmerRepository.save(insuranceCultivatedLandFarmer);
    }

    /**
     * Partially update a insuranceCultivatedLandFarmer.
     *
     * @param insuranceCultivatedLandFarmer the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InsuranceCultivatedLandFarmer> partialUpdate(InsuranceCultivatedLandFarmer insuranceCultivatedLandFarmer) {
        LOG.debug("Request to partially update InsuranceCultivatedLandFarmer : {}", insuranceCultivatedLandFarmer);

        return insuranceCultivatedLandFarmerRepository
            .findById(insuranceCultivatedLandFarmer.getId())
            .map(existingInsuranceCultivatedLandFarmer -> {
                if (insuranceCultivatedLandFarmer.getFarmerId() != null) {
                    existingInsuranceCultivatedLandFarmer.setFarmerId(insuranceCultivatedLandFarmer.getFarmerId());
                }
                if (insuranceCultivatedLandFarmer.getFarmerName() != null) {
                    existingInsuranceCultivatedLandFarmer.setFarmerName(insuranceCultivatedLandFarmer.getFarmerName());
                }
                if (insuranceCultivatedLandFarmer.getNicNo() != null) {
                    existingInsuranceCultivatedLandFarmer.setNicNo(insuranceCultivatedLandFarmer.getNicNo());
                }
                if (insuranceCultivatedLandFarmer.getAddressFirstLine() != null) {
                    existingInsuranceCultivatedLandFarmer.setAddressFirstLine(insuranceCultivatedLandFarmer.getAddressFirstLine());
                }
                if (insuranceCultivatedLandFarmer.getContactNoEmail() != null) {
                    existingInsuranceCultivatedLandFarmer.setContactNoEmail(insuranceCultivatedLandFarmer.getContactNoEmail());
                }
                if (insuranceCultivatedLandFarmer.getProvinceId() != null) {
                    existingInsuranceCultivatedLandFarmer.setProvinceId(insuranceCultivatedLandFarmer.getProvinceId());
                }
                if (insuranceCultivatedLandFarmer.getDistrictId() != null) {
                    existingInsuranceCultivatedLandFarmer.setDistrictId(insuranceCultivatedLandFarmer.getDistrictId());
                }
                if (insuranceCultivatedLandFarmer.getDsId() != null) {
                    existingInsuranceCultivatedLandFarmer.setDsId(insuranceCultivatedLandFarmer.getDsId());
                }
                if (insuranceCultivatedLandFarmer.getGnId() != null) {
                    existingInsuranceCultivatedLandFarmer.setGnId(insuranceCultivatedLandFarmer.getGnId());
                }
                if (insuranceCultivatedLandFarmer.getCity() != null) {
                    existingInsuranceCultivatedLandFarmer.setCity(insuranceCultivatedLandFarmer.getCity());
                }
                if (insuranceCultivatedLandFarmer.getAddedDate() != null) {
                    existingInsuranceCultivatedLandFarmer.setAddedDate(insuranceCultivatedLandFarmer.getAddedDate());
                }

                return existingInsuranceCultivatedLandFarmer;
            })
            .map(insuranceCultivatedLandFarmerRepository::save);
    }

    /**
     * Get one insuranceCultivatedLandFarmer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InsuranceCultivatedLandFarmer> findOne(Long id) {
        LOG.debug("Request to get InsuranceCultivatedLandFarmer : {}", id);
        return insuranceCultivatedLandFarmerRepository.findById(id);
    }

    /**
     * Delete the insuranceCultivatedLandFarmer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InsuranceCultivatedLandFarmer : {}", id);
        insuranceCultivatedLandFarmerRepository.deleteById(id);
    }
}
