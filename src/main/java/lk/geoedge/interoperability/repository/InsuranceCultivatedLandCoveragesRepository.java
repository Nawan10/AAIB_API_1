package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoverages;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InsuranceCultivatedLandCoverages entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuranceCultivatedLandCoveragesRepository
    extends JpaRepository<InsuranceCultivatedLandCoverages, Long>, JpaSpecificationExecutor<InsuranceCultivatedLandCoverages> {}
