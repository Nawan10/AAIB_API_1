package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.Farmer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Farmer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long>, JpaSpecificationExecutor<Farmer> {}
