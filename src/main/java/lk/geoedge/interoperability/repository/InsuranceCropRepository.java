package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.InsuranceCrop;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InsuranceCrop entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuranceCropRepository extends JpaRepository<InsuranceCrop, Long>, JpaSpecificationExecutor<InsuranceCrop> {}
