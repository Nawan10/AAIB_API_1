package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CropDurationCropType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CropDurationCropType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CropDurationCropTypeRepository
    extends JpaRepository<CropDurationCropType, Long>, JpaSpecificationExecutor<CropDurationCropType> {}
