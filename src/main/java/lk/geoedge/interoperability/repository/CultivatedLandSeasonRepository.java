package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CultivatedLandSeason;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CultivatedLandSeason entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CultivatedLandSeasonRepository
    extends JpaRepository<CultivatedLandSeason, Long>, JpaSpecificationExecutor<CultivatedLandSeason> {}
