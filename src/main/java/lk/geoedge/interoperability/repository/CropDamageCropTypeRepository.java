package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CropDamageCropType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CropDamageCropType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CropDamageCropTypeRepository
    extends JpaRepository<CropDamageCropType, Long>, JpaSpecificationExecutor<CropDamageCropType> {}
