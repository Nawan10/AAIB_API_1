package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CropDamage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CropDamage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CropDamageRepository extends JpaRepository<CropDamage, Long>, JpaSpecificationExecutor<CropDamage> {}
