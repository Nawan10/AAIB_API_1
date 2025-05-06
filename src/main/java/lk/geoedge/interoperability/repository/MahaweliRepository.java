package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.Mahaweli;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Mahaweli entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MahaweliRepository extends JpaRepository<Mahaweli, Long>, JpaSpecificationExecutor<Mahaweli> {}
