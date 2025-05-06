package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.Mahaweli;
import lk.geoedge.interoperability.repository.MahaweliRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.Mahaweli}.
 */
@Service
@Transactional
public class MahaweliService {

    private static final Logger LOG = LoggerFactory.getLogger(MahaweliService.class);

    private final MahaweliRepository mahaweliRepository;

    public MahaweliService(MahaweliRepository mahaweliRepository) {
        this.mahaweliRepository = mahaweliRepository;
    }

    /**
     * Save a mahaweli.
     *
     * @param mahaweli the entity to save.
     * @return the persisted entity.
     */
    public Mahaweli save(Mahaweli mahaweli) {
        LOG.debug("Request to save Mahaweli : {}", mahaweli);
        return mahaweliRepository.save(mahaweli);
    }

    /**
     * Update a mahaweli.
     *
     * @param mahaweli the entity to save.
     * @return the persisted entity.
     */
    public Mahaweli update(Mahaweli mahaweli) {
        LOG.debug("Request to update Mahaweli : {}", mahaweli);
        return mahaweliRepository.save(mahaweli);
    }

    /**
     * Partially update a mahaweli.
     *
     * @param mahaweli the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Mahaweli> partialUpdate(Mahaweli mahaweli) {
        LOG.debug("Request to partially update Mahaweli : {}", mahaweli);

        return mahaweliRepository
            .findById(mahaweli.getId())
            .map(existingMahaweli -> {
                if (mahaweli.getMahaweli() != null) {
                    existingMahaweli.setMahaweli(mahaweli.getMahaweli());
                }
                if (mahaweli.getCode() != null) {
                    existingMahaweli.setCode(mahaweli.getCode());
                }
                if (mahaweli.getAddedBy() != null) {
                    existingMahaweli.setAddedBy(mahaweli.getAddedBy());
                }
                if (mahaweli.getAddedDate() != null) {
                    existingMahaweli.setAddedDate(mahaweli.getAddedDate());
                }

                return existingMahaweli;
            })
            .map(mahaweliRepository::save);
    }

    /**
     * Get one mahaweli by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Mahaweli> findOne(Long id) {
        LOG.debug("Request to get Mahaweli : {}", id);
        return mahaweliRepository.findById(id);
    }

    /**
     * Delete the mahaweli by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Mahaweli : {}", id);
        mahaweliRepository.deleteById(id);
    }
}
