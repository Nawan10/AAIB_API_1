package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.Season;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Season entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SeasonRepository extends JpaRepository<Season, Long>, JpaSpecificationExecutor<Season> {}
