package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CultivatedLandFarmerFieldOwnerTestSamples.*;
import static lk.geoedge.interoperability.domain.CultivatedLandSeasonTestSamples.*;
import static lk.geoedge.interoperability.domain.CultivatedLandTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CultivatedLandTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CultivatedLand.class);
        CultivatedLand cultivatedLand1 = getCultivatedLandSample1();
        CultivatedLand cultivatedLand2 = new CultivatedLand();
        assertThat(cultivatedLand1).isNotEqualTo(cultivatedLand2);

        cultivatedLand2.setId(cultivatedLand1.getId());
        assertThat(cultivatedLand1).isEqualTo(cultivatedLand2);

        cultivatedLand2 = getCultivatedLandSample2();
        assertThat(cultivatedLand1).isNotEqualTo(cultivatedLand2);
    }

    @Test
    void farmFieldTest() {
        CultivatedLand cultivatedLand = getCultivatedLandRandomSampleGenerator();
        CultivatedLandFarmerFieldOwner cultivatedLandFarmerFieldOwnerBack = getCultivatedLandFarmerFieldOwnerRandomSampleGenerator();

        cultivatedLand.setFarmField(cultivatedLandFarmerFieldOwnerBack);
        assertThat(cultivatedLand.getFarmField()).isEqualTo(cultivatedLandFarmerFieldOwnerBack);

        cultivatedLand.farmField(null);
        assertThat(cultivatedLand.getFarmField()).isNull();
    }

    @Test
    void seasonTest() {
        CultivatedLand cultivatedLand = getCultivatedLandRandomSampleGenerator();
        CultivatedLandSeason cultivatedLandSeasonBack = getCultivatedLandSeasonRandomSampleGenerator();

        cultivatedLand.setSeason(cultivatedLandSeasonBack);
        assertThat(cultivatedLand.getSeason()).isEqualTo(cultivatedLandSeasonBack);

        cultivatedLand.season(null);
        assertThat(cultivatedLand.getSeason()).isNull();
    }
}
