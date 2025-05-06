package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CropVarietyCropDuration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CropVarietyCropDuration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CropVarietyCropDurationRepository
    extends JpaRepository<CropVarietyCropDuration, Long>, JpaSpecificationExecutor<CropVarietyCropDuration> {}
