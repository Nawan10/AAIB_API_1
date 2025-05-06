package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CropDamageDamage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CropDamageDamage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CropDamageDamageRepository extends JpaRepository<CropDamageDamage, Long>, JpaSpecificationExecutor<CropDamageDamage> {}
