package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCropType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InsuranceCultivatedLandCropType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuranceCultivatedLandCropTypeRepository
    extends JpaRepository<InsuranceCultivatedLandCropType, Long>, JpaSpecificationExecutor<InsuranceCultivatedLandCropType> {}
