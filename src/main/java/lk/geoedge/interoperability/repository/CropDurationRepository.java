package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CropDuration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CropDuration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CropDurationRepository extends JpaRepository<CropDuration, Long>, JpaSpecificationExecutor<CropDuration> {}
