package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.IndexPolicyCropVariety;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IndexPolicyCropVariety entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndexPolicyCropVarietyRepository
    extends JpaRepository<IndexPolicyCropVariety, Long>, JpaSpecificationExecutor<IndexPolicyCropVariety> {}
