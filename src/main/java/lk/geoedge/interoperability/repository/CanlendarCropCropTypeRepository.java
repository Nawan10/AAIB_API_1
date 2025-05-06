package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CanlendarCropCropType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CanlendarCropCropType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CanlendarCropCropTypeRepository
    extends JpaRepository<CanlendarCropCropType, Long>, JpaSpecificationExecutor<CanlendarCropCropType> {}
