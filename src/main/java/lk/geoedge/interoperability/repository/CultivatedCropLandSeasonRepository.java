package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CultivatedCropLandSeason;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CultivatedCropLandSeason entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CultivatedCropLandSeasonRepository
    extends JpaRepository<CultivatedCropLandSeason, Long>, JpaSpecificationExecutor<CultivatedCropLandSeason> {}
