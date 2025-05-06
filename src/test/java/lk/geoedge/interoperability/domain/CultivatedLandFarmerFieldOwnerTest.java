package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CultivatedLandFarmerFieldOwnerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CultivatedLandFarmerFieldOwnerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CultivatedLandFarmerFieldOwner.class);
        CultivatedLandFarmerFieldOwner cultivatedLandFarmerFieldOwner1 = getCultivatedLandFarmerFieldOwnerSample1();
        CultivatedLandFarmerFieldOwner cultivatedLandFarmerFieldOwner2 = new CultivatedLandFarmerFieldOwner();
        assertThat(cultivatedLandFarmerFieldOwner1).isNotEqualTo(cultivatedLandFarmerFieldOwner2);

        cultivatedLandFarmerFieldOwner2.setId(cultivatedLandFarmerFieldOwner1.getId());
        assertThat(cultivatedLandFarmerFieldOwner1).isEqualTo(cultivatedLandFarmerFieldOwner2);

        cultivatedLandFarmerFieldOwner2 = getCultivatedLandFarmerFieldOwnerSample2();
        assertThat(cultivatedLandFarmerFieldOwner1).isNotEqualTo(cultivatedLandFarmerFieldOwner2);
    }
}
