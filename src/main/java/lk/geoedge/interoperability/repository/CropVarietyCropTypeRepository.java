package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CropVarietyCropType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CropVarietyCropType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CropVarietyCropTypeRepository
    extends JpaRepository<CropVarietyCropType, Long>, JpaSpecificationExecutor<CropVarietyCropType> {}
