package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CultivatedLandFarmerLandTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CultivatedLandFarmerLandTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CultivatedLandFarmerLand.class);
        CultivatedLandFarmerLand cultivatedLandFarmerLand1 = getCultivatedLandFarmerLandSample1();
        CultivatedLandFarmerLand cultivatedLandFarmerLand2 = new CultivatedLandFarmerLand();
        assertThat(cultivatedLandFarmerLand1).isNotEqualTo(cultivatedLandFarmerLand2);

        cultivatedLandFarmerLand2.setId(cultivatedLandFarmerLand1.getId());
        assertThat(cultivatedLandFarmerLand1).isEqualTo(cultivatedLandFarmerLand2);

        cultivatedLandFarmerLand2 = getCultivatedLandFarmerLandSample2();
        assertThat(cultivatedLandFarmerLand1).isNotEqualTo(cultivatedLandFarmerLand2);
    }
}
