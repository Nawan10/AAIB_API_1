package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CanlendarCropCropTypeTestSamples.*;
import static lk.geoedge.interoperability.domain.CanlendarCropSeasonTestSamples.*;
import static lk.geoedge.interoperability.domain.CanlendarCropTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CanlendarCropTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CanlendarCrop.class);
        CanlendarCrop canlendarCrop1 = getCanlendarCropSample1();
        CanlendarCrop canlendarCrop2 = new CanlendarCrop();
        assertThat(canlendarCrop1).isNotEqualTo(canlendarCrop2);

        canlendarCrop2.setId(canlendarCrop1.getId());
        assertThat(canlendarCrop1).isEqualTo(canlendarCrop2);

        canlendarCrop2 = getCanlendarCropSample2();
        assertThat(canlendarCrop1).isNotEqualTo(canlendarCrop2);
    }

    @Test
    void seasonTest() {
        CanlendarCrop canlendarCrop = getCanlendarCropRandomSampleGenerator();
        CanlendarCropSeason canlendarCropSeasonBack = getCanlendarCropSeasonRandomSampleGenerator();

        canlendarCrop.setSeason(canlendarCropSeasonBack);
        assertThat(canlendarCrop.getSeason()).isEqualTo(canlendarCropSeasonBack);

        canlendarCrop.season(null);
        assertThat(canlendarCrop.getSeason()).isNull();
    }

    @Test
    void cropTest() {
        CanlendarCrop canlendarCrop = getCanlendarCropRandomSampleGenerator();
        CanlendarCropCropType canlendarCropCropTypeBack = getCanlendarCropCropTypeRandomSampleGenerator();

        canlendarCrop.setCrop(canlendarCropCropTypeBack);
        assertThat(canlendarCrop.getCrop()).isEqualTo(canlendarCropCropTypeBack);

        canlendarCrop.crop(null);
        assertThat(canlendarCrop.getCrop()).isNull();
    }
}
