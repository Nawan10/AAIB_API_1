package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.FarmerFieldLandOwnerFarmer;
import lk.geoedge.interoperability.repository.FarmerFieldLandOwnerFarmerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.FarmerFieldLandOwnerFarmer}.
 */
@Service
@Transactional
public class FarmerFieldLandOwnerFarmerService {

    private static final Logger LOG = LoggerFactory.getLogger(FarmerFieldLandOwnerFarmerService.class);

    private final FarmerFieldLandOwnerFarmerRepository farmerFieldLandOwnerFarmerRepository;

    public FarmerFieldLandOwnerFarmerService(FarmerFieldLandOwnerFarmerRepository farmerFieldLandOwnerFarmerRepository) {
        this.farmerFieldLandOwnerFarmerRepository = farmerFieldLandOwnerFarmerRepository;
    }

    /**
     * Save a farmerFieldLandOwnerFarmer.
     *
     * @param farmerFieldLandOwnerFarmer the entity to save.
     * @return the persisted entity.
     */
    public FarmerFieldLandOwnerFarmer save(FarmerFieldLandOwnerFarmer farmerFieldLandOwnerFarmer) {
        LOG.debug("Request to save FarmerFieldLandOwnerFarmer : {}", farmerFieldLandOwnerFarmer);
        return farmerFieldLandOwnerFarmerRepository.save(farmerFieldLandOwnerFarmer);
    }

    /**
     * Update a farmerFieldLandOwnerFarmer.
     *
     * @param farmerFieldLandOwnerFarmer the entity to save.
     * @return the persisted entity.
     */
    public FarmerFieldLandOwnerFarmer update(FarmerFieldLandOwnerFarmer farmerFieldLandOwnerFarmer) {
        LOG.debug("Request to update FarmerFieldLandOwnerFarmer : {}", farmerFieldLandOwnerFarmer);
        return farmerFieldLandOwnerFarmerRepository.save(farmerFieldLandOwnerFarmer);
    }

    /**
     * Partially update a farmerFieldLandOwnerFarmer.
     *
     * @param farmerFieldLandOwnerFarmer the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FarmerFieldLandOwnerFarmer> partialUpdate(FarmerFieldLandOwnerFarmer farmerFieldLandOwnerFarmer) {
        LOG.debug("Request to partially update FarmerFieldLandOwnerFarmer : {}", farmerFieldLandOwnerFarmer);

        return farmerFieldLandOwnerFarmerRepository
            .findById(farmerFieldLandOwnerFarmer.getId())
            .map(existingFarmerFieldLandOwnerFarmer -> {
                if (farmerFieldLandOwnerFarmer.getFarmerId() != null) {
                    existingFarmerFieldLandOwnerFarmer.setFarmerId(farmerFieldLandOwnerFarmer.getFarmerId());
                }
                if (farmerFieldLandOwnerFarmer.getFarmerName() != null) {
                    existingFarmerFieldLandOwnerFarmer.setFarmerName(farmerFieldLandOwnerFarmer.getFarmerName());
                }
                if (farmerFieldLandOwnerFarmer.getNicNo() != null) {
                    existingFarmerFieldLandOwnerFarmer.setNicNo(farmerFieldLandOwnerFarmer.getNicNo());
                }
                if (farmerFieldLandOwnerFarmer.getAddressFirstLine() != null) {
                    existingFarmerFieldLandOwnerFarmer.setAddressFirstLine(farmerFieldLandOwnerFarmer.getAddressFirstLine());
                }
                if (farmerFieldLandOwnerFarmer.getContactNoEmail() != null) {
                    existingFarmerFieldLandOwnerFarmer.setContactNoEmail(farmerFieldLandOwnerFarmer.getContactNoEmail());
                }
                if (farmerFieldLandOwnerFarmer.getProvinceId() != null) {
                    existingFarmerFieldLandOwnerFarmer.setProvinceId(farmerFieldLandOwnerFarmer.getProvinceId());
                }
                if (farmerFieldLandOwnerFarmer.getDistrictId() != null) {
                    existingFarmerFieldLandOwnerFarmer.setDistrictId(farmerFieldLandOwnerFarmer.getDistrictId());
                }
                if (farmerFieldLandOwnerFarmer.getDsId() != null) {
                    existingFarmerFieldLandOwnerFarmer.setDsId(farmerFieldLandOwnerFarmer.getDsId());
                }
                if (farmerFieldLandOwnerFarmer.getGnId() != null) {
                    existingFarmerFieldLandOwnerFarmer.setGnId(farmerFieldLandOwnerFarmer.getGnId());
                }
                if (farmerFieldLandOwnerFarmer.getCity() != null) {
                    existingFarmerFieldLandOwnerFarmer.setCity(farmerFieldLandOwnerFarmer.getCity());
                }
                if (farmerFieldLandOwnerFarmer.getAddedDate() != null) {
                    existingFarmerFieldLandOwnerFarmer.setAddedDate(farmerFieldLandOwnerFarmer.getAddedDate());
                }

                return existingFarmerFieldLandOwnerFarmer;
            })
            .map(farmerFieldLandOwnerFarmerRepository::save);
    }

    /**
     * Get one farmerFieldLandOwnerFarmer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FarmerFieldLandOwnerFarmer> findOne(Long id) {
        LOG.debug("Request to get FarmerFieldLandOwnerFarmer : {}", id);
        return farmerFieldLandOwnerFarmerRepository.findById(id);
    }

    /**
     * Delete the farmerFieldLandOwnerFarmer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FarmerFieldLandOwnerFarmer : {}", id);
        farmerFieldLandOwnerFarmerRepository.deleteById(id);
    }
}
