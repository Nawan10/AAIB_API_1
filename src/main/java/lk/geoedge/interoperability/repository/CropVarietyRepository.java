package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CropVariety;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CropVariety entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CropVarietyRepository extends JpaRepository<CropVariety, Long>, JpaSpecificationExecutor<CropVariety> {}
