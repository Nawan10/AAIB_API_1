package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CultivatedLandFarmersFarmerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CultivatedLandFarmersFarmerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CultivatedLandFarmersFarmer.class);
        CultivatedLandFarmersFarmer cultivatedLandFarmersFarmer1 = getCultivatedLandFarmersFarmerSample1();
        CultivatedLandFarmersFarmer cultivatedLandFarmersFarmer2 = new CultivatedLandFarmersFarmer();
        assertThat(cultivatedLandFarmersFarmer1).isNotEqualTo(cultivatedLandFarmersFarmer2);

        cultivatedLandFarmersFarmer2.setId(cultivatedLandFarmersFarmer1.getId());
        assertThat(cultivatedLandFarmersFarmer1).isEqualTo(cultivatedLandFarmersFarmer2);

        cultivatedLandFarmersFarmer2 = getCultivatedLandFarmersFarmerSample2();
        assertThat(cultivatedLandFarmersFarmer1).isNotEqualTo(cultivatedLandFarmersFarmer2);
    }
}
