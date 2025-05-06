package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CultivatedLandFarmerLandTestSamples.*;
import static lk.geoedge.interoperability.domain.CultivatedLandFarmersFarmerTestSamples.*;
import static lk.geoedge.interoperability.domain.CultivatedLandFarmersTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CultivatedLandFarmersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CultivatedLandFarmers.class);
        CultivatedLandFarmers cultivatedLandFarmers1 = getCultivatedLandFarmersSample1();
        CultivatedLandFarmers cultivatedLandFarmers2 = new CultivatedLandFarmers();
        assertThat(cultivatedLandFarmers1).isNotEqualTo(cultivatedLandFarmers2);

        cultivatedLandFarmers2.setId(cultivatedLandFarmers1.getId());
        assertThat(cultivatedLandFarmers1).isEqualTo(cultivatedLandFarmers2);

        cultivatedLandFarmers2 = getCultivatedLandFarmersSample2();
        assertThat(cultivatedLandFarmers1).isNotEqualTo(cultivatedLandFarmers2);
    }

    @Test
    void farmerTest() {
        CultivatedLandFarmers cultivatedLandFarmers = getCultivatedLandFarmersRandomSampleGenerator();
        CultivatedLandFarmersFarmer cultivatedLandFarmersFarmerBack = getCultivatedLandFarmersFarmerRandomSampleGenerator();

        cultivatedLandFarmers.setFarmer(cultivatedLandFarmersFarmerBack);
        assertThat(cultivatedLandFarmers.getFarmer()).isEqualTo(cultivatedLandFarmersFarmerBack);

        cultivatedLandFarmers.farmer(null);
        assertThat(cultivatedLandFarmers.getFarmer()).isNull();
    }

    @Test
    void cultivatedLandTest() {
        CultivatedLandFarmers cultivatedLandFarmers = getCultivatedLandFarmersRandomSampleGenerator();
        CultivatedLandFarmerLand cultivatedLandFarmerLandBack = getCultivatedLandFarmerLandRandomSampleGenerator();

        cultivatedLandFarmers.setCultivatedLand(cultivatedLandFarmerLandBack);
        assertThat(cultivatedLandFarmers.getCultivatedLand()).isEqualTo(cultivatedLandFarmerLandBack);

        cultivatedLandFarmers.cultivatedLand(null);
        assertThat(cultivatedLandFarmers.getCultivatedLand()).isNull();
    }
}
