package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.Season;
import lk.geoedge.interoperability.repository.SeasonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.Season}.
 */
@Service
@Transactional
public class SeasonService {

    private static final Logger LOG = LoggerFactory.getLogger(SeasonService.class);

    private final SeasonRepository seasonRepository;

    public SeasonService(SeasonRepository seasonRepository) {
        this.seasonRepository = seasonRepository;
    }

    /**
     * Save a season.
     *
     * @param season the entity to save.
     * @return the persisted entity.
     */
    public Season save(Season season) {
        LOG.debug("Request to save Season : {}", season);
        return seasonRepository.save(season);
    }

    /**
     * Update a season.
     *
     * @param season the entity to save.
     * @return the persisted entity.
     */
    public Season update(Season season) {
        LOG.debug("Request to update Season : {}", season);
        return seasonRepository.save(season);
    }

    /**
     * Partially update a season.
     *
     * @param season the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Season> partialUpdate(Season season) {
        LOG.debug("Request to partially update Season : {}", season);

        return seasonRepository
            .findById(season.getId())
            .map(existingSeason -> {
                if (season.getName() != null) {
                    existingSeason.setName(season.getName());
                }
                if (season.getPeriod() != null) {
                    existingSeason.setPeriod(season.getPeriod());
                }

                return existingSeason;
            })
            .map(seasonRepository::save);
    }

    /**
     * Get one season by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Season> findOne(Long id) {
        LOG.debug("Request to get Season : {}", id);
        return seasonRepository.findById(id);
    }

    /**
     * Delete the season by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Season : {}", id);
        seasonRepository.deleteById(id);
    }
}
