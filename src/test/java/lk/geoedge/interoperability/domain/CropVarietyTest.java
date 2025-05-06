package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CropVarietyCropDurationTestSamples.*;
import static lk.geoedge.interoperability.domain.CropVarietyCropTypeTestSamples.*;
import static lk.geoedge.interoperability.domain.CropVarietyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CropVarietyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CropVariety.class);
        CropVariety cropVariety1 = getCropVarietySample1();
        CropVariety cropVariety2 = new CropVariety();
        assertThat(cropVariety1).isNotEqualTo(cropVariety2);

        cropVariety2.setId(cropVariety1.getId());
        assertThat(cropVariety1).isEqualTo(cropVariety2);

        cropVariety2 = getCropVarietySample2();
        assertThat(cropVariety1).isNotEqualTo(cropVariety2);
    }

    @Test
    void cropTest() {
        CropVariety cropVariety = getCropVarietyRandomSampleGenerator();
        CropVarietyCropType cropVarietyCropTypeBack = getCropVarietyCropTypeRandomSampleGenerator();

        cropVariety.setCrop(cropVarietyCropTypeBack);
        assertThat(cropVariety.getCrop()).isEqualTo(cropVarietyCropTypeBack);

        cropVariety.crop(null);
        assertThat(cropVariety.getCrop()).isNull();
    }

    @Test
    void cropDurationTest() {
        CropVariety cropVariety = getCropVarietyRandomSampleGenerator();
        CropVarietyCropDuration cropVarietyCropDurationBack = getCropVarietyCropDurationRandomSampleGenerator();

        cropVariety.setCropDuration(cropVarietyCropDurationBack);
        assertThat(cropVariety.getCropDuration()).isEqualTo(cropVarietyCropDurationBack);

        cropVariety.cropDuration(null);
        assertThat(cropVariety.getCropDuration()).isNull();
    }
}
