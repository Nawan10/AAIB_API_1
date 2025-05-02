package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CultivatedLandSeasonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CultivatedLandSeasonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CultivatedLandSeason.class);
        CultivatedLandSeason cultivatedLandSeason1 = getCultivatedLandSeasonSample1();
        CultivatedLandSeason cultivatedLandSeason2 = new CultivatedLandSeason();
        assertThat(cultivatedLandSeason1).isNotEqualTo(cultivatedLandSeason2);

        cultivatedLandSeason2.setId(cultivatedLandSeason1.getId());
        assertThat(cultivatedLandSeason1).isEqualTo(cultivatedLandSeason2);

        cultivatedLandSeason2 = getCultivatedLandSeasonSample2();
        assertThat(cultivatedLandSeason1).isNotEqualTo(cultivatedLandSeason2);
    }
}
