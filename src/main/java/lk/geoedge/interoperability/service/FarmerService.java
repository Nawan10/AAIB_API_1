package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.Farmer;
import lk.geoedge.interoperability.repository.FarmerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.Farmer}.
 */
@Service
@Transactional
public class FarmerService {

    private static final Logger LOG = LoggerFactory.getLogger(FarmerService.class);

    private final FarmerRepository farmerRepository;

    public FarmerService(FarmerRepository farmerRepository) {
        this.farmerRepository = farmerRepository;
    }

    /**
     * Save a farmer.
     *
     * @param farmer the entity to save.
     * @return the persisted entity.
     */
    public Farmer save(Farmer farmer) {
        LOG.debug("Request to save Farmer : {}", farmer);
        return farmerRepository.save(farmer);
    }

    /**
     * Update a farmer.
     *
     * @param farmer the entity to save.
     * @return the persisted entity.
     */
    public Farmer update(Farmer farmer) {
        LOG.debug("Request to update Farmer : {}", farmer);
        return farmerRepository.save(farmer);
    }

    /**
     * Partially update a farmer.
     *
     * @param farmer the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Farmer> partialUpdate(Farmer farmer) {
        LOG.debug("Request to partially update Farmer : {}", farmer);

        return farmerRepository
            .findById(farmer.getId())
            .map(existingFarmer -> {
                if (farmer.getFarmerId() != null) {
                    existingFarmer.setFarmerId(farmer.getFarmerId());
                }
                if (farmer.getFarmerName() != null) {
                    existingFarmer.setFarmerName(farmer.getFarmerName());
                }
                if (farmer.getNicNo() != null) {
                    existingFarmer.setNicNo(farmer.getNicNo());
                }
                if (farmer.getAddressFirstLine() != null) {
                    existingFarmer.setAddressFirstLine(farmer.getAddressFirstLine());
                }
                if (farmer.getContactNoEmail() != null) {
                    existingFarmer.setContactNoEmail(farmer.getContactNoEmail());
                }
                if (farmer.getProvinceId() != null) {
                    existingFarmer.setProvinceId(farmer.getProvinceId());
                }
                if (farmer.getDistrictId() != null) {
                    existingFarmer.setDistrictId(farmer.getDistrictId());
                }
                if (farmer.getDsId() != null) {
                    existingFarmer.setDsId(farmer.getDsId());
                }
                if (farmer.getGnId() != null) {
                    existingFarmer.setGnId(farmer.getGnId());
                }
                if (farmer.getCity() != null) {
                    existingFarmer.setCity(farmer.getCity());
                }
                if (farmer.getAddedDate() != null) {
                    existingFarmer.setAddedDate(farmer.getAddedDate());
                }

                return existingFarmer;
            })
            .map(farmerRepository::save);
    }

    /**
     * Get one farmer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Farmer> findOne(Long id) {
        LOG.debug("Request to get Farmer : {}", id);
        return farmerRepository.findById(id);
    }

    /**
     * Delete the farmer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Farmer : {}", id);
        farmerRepository.deleteById(id);
    }
}
