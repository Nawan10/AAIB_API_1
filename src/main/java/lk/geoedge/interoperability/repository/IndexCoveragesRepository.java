package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.IndexCoverages;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IndexCoverages entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndexCoveragesRepository extends JpaRepository<IndexCoverages, Long>, JpaSpecificationExecutor<IndexCoverages> {}
