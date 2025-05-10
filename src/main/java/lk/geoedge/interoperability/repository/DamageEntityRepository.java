package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.DamageEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DamageEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DamageEntityRepository extends JpaRepository<DamageEntity, Long>, JpaSpecificationExecutor<DamageEntity> {}
