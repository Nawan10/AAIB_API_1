package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CultivatedCropLandSeasonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CultivatedCropLandSeasonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CultivatedCropLandSeason.class);
        CultivatedCropLandSeason cultivatedCropLandSeason1 = getCultivatedCropLandSeasonSample1();
        CultivatedCropLandSeason cultivatedCropLandSeason2 = new CultivatedCropLandSeason();
        assertThat(cultivatedCropLandSeason1).isNotEqualTo(cultivatedCropLandSeason2);

        cultivatedCropLandSeason2.setId(cultivatedCropLandSeason1.getId());
        assertThat(cultivatedCropLandSeason1).isEqualTo(cultivatedCropLandSeason2);

        cultivatedCropLandSeason2 = getCultivatedCropLandSeasonSample2();
        assertThat(cultivatedCropLandSeason1).isNotEqualTo(cultivatedCropLandSeason2);
    }
}
