package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CultivatedCropCultivatedLandTestSamples.*;
import static lk.geoedge.interoperability.domain.CultivatedCropLandSeasonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CultivatedCropCultivatedLandTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CultivatedCropCultivatedLand.class);
        CultivatedCropCultivatedLand cultivatedCropCultivatedLand1 = getCultivatedCropCultivatedLandSample1();
        CultivatedCropCultivatedLand cultivatedCropCultivatedLand2 = new CultivatedCropCultivatedLand();
        assertThat(cultivatedCropCultivatedLand1).isNotEqualTo(cultivatedCropCultivatedLand2);

        cultivatedCropCultivatedLand2.setId(cultivatedCropCultivatedLand1.getId());
        assertThat(cultivatedCropCultivatedLand1).isEqualTo(cultivatedCropCultivatedLand2);

        cultivatedCropCultivatedLand2 = getCultivatedCropCultivatedLandSample2();
        assertThat(cultivatedCropCultivatedLand1).isNotEqualTo(cultivatedCropCultivatedLand2);
    }

    @Test
    void seasonTest() {
        CultivatedCropCultivatedLand cultivatedCropCultivatedLand = getCultivatedCropCultivatedLandRandomSampleGenerator();
        CultivatedCropLandSeason cultivatedCropLandSeasonBack = getCultivatedCropLandSeasonRandomSampleGenerator();

        cultivatedCropCultivatedLand.setSeason(cultivatedCropLandSeasonBack);
        assertThat(cultivatedCropCultivatedLand.getSeason()).isEqualTo(cultivatedCropLandSeasonBack);

        cultivatedCropCultivatedLand.season(null);
        assertThat(cultivatedCropCultivatedLand.getSeason()).isNull();
    }
}
