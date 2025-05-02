package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CultivatedCropCropType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CultivatedCropCropType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CultivatedCropCropTypeRepository
    extends JpaRepository<CultivatedCropCropType, Long>, JpaSpecificationExecutor<CultivatedCropCropType> {}
