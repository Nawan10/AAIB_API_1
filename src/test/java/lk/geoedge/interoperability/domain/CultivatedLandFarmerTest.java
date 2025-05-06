package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CultivatedLandFarmerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CultivatedLandFarmerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CultivatedLandFarmer.class);
        CultivatedLandFarmer cultivatedLandFarmer1 = getCultivatedLandFarmerSample1();
        CultivatedLandFarmer cultivatedLandFarmer2 = new CultivatedLandFarmer();
        assertThat(cultivatedLandFarmer1).isNotEqualTo(cultivatedLandFarmer2);

        cultivatedLandFarmer2.setId(cultivatedLandFarmer1.getId());
        assertThat(cultivatedLandFarmer1).isEqualTo(cultivatedLandFarmer2);

        cultivatedLandFarmer2 = getCultivatedLandFarmerSample2();
        assertThat(cultivatedLandFarmer1).isNotEqualTo(cultivatedLandFarmer2);
    }
}
