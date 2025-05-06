package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CropVarietyCropDurationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CropVarietyCropDurationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CropVarietyCropDuration.class);
        CropVarietyCropDuration cropVarietyCropDuration1 = getCropVarietyCropDurationSample1();
        CropVarietyCropDuration cropVarietyCropDuration2 = new CropVarietyCropDuration();
        assertThat(cropVarietyCropDuration1).isNotEqualTo(cropVarietyCropDuration2);

        cropVarietyCropDuration2.setId(cropVarietyCropDuration1.getId());
        assertThat(cropVarietyCropDuration1).isEqualTo(cropVarietyCropDuration2);

        cropVarietyCropDuration2 = getCropVarietyCropDurationSample2();
        assertThat(cropVarietyCropDuration1).isNotEqualTo(cropVarietyCropDuration2);
    }
}
