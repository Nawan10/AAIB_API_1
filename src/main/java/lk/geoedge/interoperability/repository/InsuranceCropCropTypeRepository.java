package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.InsuranceCropCropType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InsuranceCropCropType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuranceCropCropTypeRepository
    extends JpaRepository<InsuranceCropCropType, Long>, JpaSpecificationExecutor<InsuranceCropCropType> {}
