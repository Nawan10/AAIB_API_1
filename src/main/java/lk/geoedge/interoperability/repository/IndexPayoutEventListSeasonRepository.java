package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.IndexPayoutEventListSeason;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IndexPayoutEventListSeason entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndexPayoutEventListSeasonRepository
    extends JpaRepository<IndexPayoutEventListSeason, Long>, JpaSpecificationExecutor<IndexPayoutEventListSeason> {}
