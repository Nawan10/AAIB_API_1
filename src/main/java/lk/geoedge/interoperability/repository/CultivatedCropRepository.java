package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CultivatedCrop;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CultivatedCrop entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CultivatedCropRepository extends JpaRepository<CultivatedCrop, Long>, JpaSpecificationExecutor<CultivatedCrop> {}
