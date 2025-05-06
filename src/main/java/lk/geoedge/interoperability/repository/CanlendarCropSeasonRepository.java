package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CanlendarCropSeason;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CanlendarCropSeason entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CanlendarCropSeasonRepository
    extends JpaRepository<CanlendarCropSeason, Long>, JpaSpecificationExecutor<CanlendarCropSeason> {}
