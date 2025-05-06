package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CropDurationCropTypeTestSamples.*;
import static lk.geoedge.interoperability.domain.CropDurationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CropDurationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CropDuration.class);
        CropDuration cropDuration1 = getCropDurationSample1();
        CropDuration cropDuration2 = new CropDuration();
        assertThat(cropDuration1).isNotEqualTo(cropDuration2);

        cropDuration2.setId(cropDuration1.getId());
        assertThat(cropDuration1).isEqualTo(cropDuration2);

        cropDuration2 = getCropDurationSample2();
        assertThat(cropDuration1).isNotEqualTo(cropDuration2);
    }

    @Test
    void cropTest() {
        CropDuration cropDuration = getCropDurationRandomSampleGenerator();
        CropDurationCropType cropDurationCropTypeBack = getCropDurationCropTypeRandomSampleGenerator();

        cropDuration.setCrop(cropDurationCropTypeBack);
        assertThat(cropDuration.getCrop()).isEqualTo(cropDurationCropTypeBack);

        cropDuration.crop(null);
        assertThat(cropDuration.getCrop()).isNull();
    }
}
