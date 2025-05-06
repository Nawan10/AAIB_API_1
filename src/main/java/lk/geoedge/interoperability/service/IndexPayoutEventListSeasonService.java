package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexPayoutEventListSeason;
import lk.geoedge.interoperability.repository.IndexPayoutEventListSeasonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.IndexPayoutEventListSeason}.
 */
@Service
@Transactional
public class IndexPayoutEventListSeasonService {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPayoutEventListSeasonService.class);

    private final IndexPayoutEventListSeasonRepository indexPayoutEventListSeasonRepository;

    public IndexPayoutEventListSeasonService(IndexPayoutEventListSeasonRepository indexPayoutEventListSeasonRepository) {
        this.indexPayoutEventListSeasonRepository = indexPayoutEventListSeasonRepository;
    }

    /**
     * Save a indexPayoutEventListSeason.
     *
     * @param indexPayoutEventListSeason the entity to save.
     * @return the persisted entity.
     */
    public IndexPayoutEventListSeason save(IndexPayoutEventListSeason indexPayoutEventListSeason) {
        LOG.debug("Request to save IndexPayoutEventListSeason : {}", indexPayoutEventListSeason);
        return indexPayoutEventListSeasonRepository.save(indexPayoutEventListSeason);
    }

    /**
     * Update a indexPayoutEventListSeason.
     *
     * @param indexPayoutEventListSeason the entity to save.
     * @return the persisted entity.
     */
    public IndexPayoutEventListSeason update(IndexPayoutEventListSeason indexPayoutEventListSeason) {
        LOG.debug("Request to update IndexPayoutEventListSeason : {}", indexPayoutEventListSeason);
        return indexPayoutEventListSeasonRepository.save(indexPayoutEventListSeason);
    }

    /**
     * Partially update a indexPayoutEventListSeason.
     *
     * @param indexPayoutEventListSeason the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IndexPayoutEventListSeason> partialUpdate(IndexPayoutEventListSeason indexPayoutEventListSeason) {
        LOG.debug("Request to partially update IndexPayoutEventListSeason : {}", indexPayoutEventListSeason);

        return indexPayoutEventListSeasonRepository
            .findById(indexPayoutEventListSeason.getId())
            .map(existingIndexPayoutEventListSeason -> {
                if (indexPayoutEventListSeason.getName() != null) {
                    existingIndexPayoutEventListSeason.setName(indexPayoutEventListSeason.getName());
                }
                if (indexPayoutEventListSeason.getPeriod() != null) {
                    existingIndexPayoutEventListSeason.setPeriod(indexPayoutEventListSeason.getPeriod());
                }

                return existingIndexPayoutEventListSeason;
            })
            .map(indexPayoutEventListSeasonRepository::save);
    }

    /**
     * Get one indexPayoutEventListSeason by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IndexPayoutEventListSeason> findOne(Long id) {
        LOG.debug("Request to get IndexPayoutEventListSeason : {}", id);
        return indexPayoutEventListSeasonRepository.findById(id);
    }

    /**
     * Delete the indexPayoutEventListSeason by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IndexPayoutEventListSeason : {}", id);
        indexPayoutEventListSeasonRepository.deleteById(id);
    }
}
