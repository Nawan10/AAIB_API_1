package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.IndexPolicyCropType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IndexPolicyCropType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndexPolicyCropTypeRepository
    extends JpaRepository<IndexPolicyCropType, Long>, JpaSpecificationExecutor<IndexPolicyCropType> {}
