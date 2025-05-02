package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.DamageCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DamageCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DamageCategoryRepository extends JpaRepository<DamageCategory, Long>, JpaSpecificationExecutor<DamageCategory> {}
