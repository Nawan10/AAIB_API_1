package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CropType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CropType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CropTypeRepository extends JpaRepository<CropType, Long>, JpaSpecificationExecutor<CropType> {}
