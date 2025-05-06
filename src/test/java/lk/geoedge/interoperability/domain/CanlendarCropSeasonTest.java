package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CanlendarCropSeasonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CanlendarCropSeasonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CanlendarCropSeason.class);
        CanlendarCropSeason canlendarCropSeason1 = getCanlendarCropSeasonSample1();
        CanlendarCropSeason canlendarCropSeason2 = new CanlendarCropSeason();
        assertThat(canlendarCropSeason1).isNotEqualTo(canlendarCropSeason2);

        canlendarCropSeason2.setId(canlendarCropSeason1.getId());
        assertThat(canlendarCropSeason1).isEqualTo(canlendarCropSeason2);

        canlendarCropSeason2 = getCanlendarCropSeasonSample2();
        assertThat(canlendarCropSeason1).isNotEqualTo(canlendarCropSeason2);
    }
}
