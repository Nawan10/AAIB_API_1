package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CanlendarCropSeason;
import lk.geoedge.interoperability.repository.CanlendarCropSeasonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CanlendarCropSeason}.
 */
@Service
@Transactional
public class CanlendarCropSeasonService {

    private static final Logger LOG = LoggerFactory.getLogger(CanlendarCropSeasonService.class);

    private final CanlendarCropSeasonRepository canlendarCropSeasonRepository;

    public CanlendarCropSeasonService(CanlendarCropSeasonRepository canlendarCropSeasonRepository) {
        this.canlendarCropSeasonRepository = canlendarCropSeasonRepository;
    }

    /**
     * Save a canlendarCropSeason.
     *
     * @param canlendarCropSeason the entity to save.
     * @return the persisted entity.
     */
    public CanlendarCropSeason save(CanlendarCropSeason canlendarCropSeason) {
        LOG.debug("Request to save CanlendarCropSeason : {}", canlendarCropSeason);
        return canlendarCropSeasonRepository.save(canlendarCropSeason);
    }

    /**
     * Update a canlendarCropSeason.
     *
     * @param canlendarCropSeason the entity to save.
     * @return the persisted entity.
     */
    public CanlendarCropSeason update(CanlendarCropSeason canlendarCropSeason) {
        LOG.debug("Request to update CanlendarCropSeason : {}", canlendarCropSeason);
        return canlendarCropSeasonRepository.save(canlendarCropSeason);
    }

    /**
     * Partially update a canlendarCropSeason.
     *
     * @param canlendarCropSeason the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CanlendarCropSeason> partialUpdate(CanlendarCropSeason canlendarCropSeason) {
        LOG.debug("Request to partially update CanlendarCropSeason : {}", canlendarCropSeason);

        return canlendarCropSeasonRepository
            .findById(canlendarCropSeason.getId())
            .map(existingCanlendarCropSeason -> {
                if (canlendarCropSeason.getName() != null) {
                    existingCanlendarCropSeason.setName(canlendarCropSeason.getName());
                }
                if (canlendarCropSeason.getPeriod() != null) {
                    existingCanlendarCropSeason.setPeriod(canlendarCropSeason.getPeriod());
                }

                return existingCanlendarCropSeason;
            })
            .map(canlendarCropSeasonRepository::save);
    }

    /**
     * Get one canlendarCropSeason by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CanlendarCropSeason> findOne(Long id) {
        LOG.debug("Request to get CanlendarCropSeason : {}", id);
        return canlendarCropSeasonRepository.findById(id);
    }

    /**
     * Delete the canlendarCropSeason by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CanlendarCropSeason : {}", id);
        canlendarCropSeasonRepository.deleteById(id);
    }
}
