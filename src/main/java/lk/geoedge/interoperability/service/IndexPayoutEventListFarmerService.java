package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexPayoutEventListFarmer;
import lk.geoedge.interoperability.repository.IndexPayoutEventListFarmerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.IndexPayoutEventListFarmer}.
 */
@Service
@Transactional
public class IndexPayoutEventListFarmerService {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPayoutEventListFarmerService.class);

    private final IndexPayoutEventListFarmerRepository indexPayoutEventListFarmerRepository;

    public IndexPayoutEventListFarmerService(IndexPayoutEventListFarmerRepository indexPayoutEventListFarmerRepository) {
        this.indexPayoutEventListFarmerRepository = indexPayoutEventListFarmerRepository;
    }

    /**
     * Save a indexPayoutEventListFarmer.
     *
     * @param indexPayoutEventListFarmer the entity to save.
     * @return the persisted entity.
     */
    public IndexPayoutEventListFarmer save(IndexPayoutEventListFarmer indexPayoutEventListFarmer) {
        LOG.debug("Request to save IndexPayoutEventListFarmer : {}", indexPayoutEventListFarmer);
        return indexPayoutEventListFarmerRepository.save(indexPayoutEventListFarmer);
    }

    /**
     * Update a indexPayoutEventListFarmer.
     *
     * @param indexPayoutEventListFarmer the entity to save.
     * @return the persisted entity.
     */
    public IndexPayoutEventListFarmer update(IndexPayoutEventListFarmer indexPayoutEventListFarmer) {
        LOG.debug("Request to update IndexPayoutEventListFarmer : {}", indexPayoutEventListFarmer);
        return indexPayoutEventListFarmerRepository.save(indexPayoutEventListFarmer);
    }

    /**
     * Partially update a indexPayoutEventListFarmer.
     *
     * @param indexPayoutEventListFarmer the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IndexPayoutEventListFarmer> partialUpdate(IndexPayoutEventListFarmer indexPayoutEventListFarmer) {
        LOG.debug("Request to partially update IndexPayoutEventListFarmer : {}", indexPayoutEventListFarmer);

        return indexPayoutEventListFarmerRepository
            .findById(indexPayoutEventListFarmer.getId())
            .map(existingIndexPayoutEventListFarmer -> {
                if (indexPayoutEventListFarmer.getFarmerId() != null) {
                    existingIndexPayoutEventListFarmer.setFarmerId(indexPayoutEventListFarmer.getFarmerId());
                }
                if (indexPayoutEventListFarmer.getFarmerName() != null) {
                    existingIndexPayoutEventListFarmer.setFarmerName(indexPayoutEventListFarmer.getFarmerName());
                }
                if (indexPayoutEventListFarmer.getNicNo() != null) {
                    existingIndexPayoutEventListFarmer.setNicNo(indexPayoutEventListFarmer.getNicNo());
                }
                if (indexPayoutEventListFarmer.getAddressFirstLine() != null) {
                    existingIndexPayoutEventListFarmer.setAddressFirstLine(indexPayoutEventListFarmer.getAddressFirstLine());
                }
                if (indexPayoutEventListFarmer.getContactNoEmail() != null) {
                    existingIndexPayoutEventListFarmer.setContactNoEmail(indexPayoutEventListFarmer.getContactNoEmail());
                }
                if (indexPayoutEventListFarmer.getProvinceId() != null) {
                    existingIndexPayoutEventListFarmer.setProvinceId(indexPayoutEventListFarmer.getProvinceId());
                }
                if (indexPayoutEventListFarmer.getDistrictId() != null) {
                    existingIndexPayoutEventListFarmer.setDistrictId(indexPayoutEventListFarmer.getDistrictId());
                }
                if (indexPayoutEventListFarmer.getDsId() != null) {
                    existingIndexPayoutEventListFarmer.setDsId(indexPayoutEventListFarmer.getDsId());
                }
                if (indexPayoutEventListFarmer.getGnId() != null) {
                    existingIndexPayoutEventListFarmer.setGnId(indexPayoutEventListFarmer.getGnId());
                }
                if (indexPayoutEventListFarmer.getCity() != null) {
                    existingIndexPayoutEventListFarmer.setCity(indexPayoutEventListFarmer.getCity());
                }
                if (indexPayoutEventListFarmer.getAddedDate() != null) {
                    existingIndexPayoutEventListFarmer.setAddedDate(indexPayoutEventListFarmer.getAddedDate());
                }

                return existingIndexPayoutEventListFarmer;
            })
            .map(indexPayoutEventListFarmerRepository::save);
    }

    /**
     * Get one indexPayoutEventListFarmer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IndexPayoutEventListFarmer> findOne(Long id) {
        LOG.debug("Request to get IndexPayoutEventListFarmer : {}", id);
        return indexPayoutEventListFarmerRepository.findById(id);
    }

    /**
     * Delete the indexPayoutEventListFarmer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IndexPayoutEventListFarmer : {}", id);
        indexPayoutEventListFarmerRepository.deleteById(id);
    }
}
