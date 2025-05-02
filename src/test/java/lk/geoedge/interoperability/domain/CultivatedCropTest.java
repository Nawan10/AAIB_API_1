package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CultivatedCropCropTypeTestSamples.*;
import static lk.geoedge.interoperability.domain.CultivatedCropCultivatedLandTestSamples.*;
import static lk.geoedge.interoperability.domain.CultivatedCropTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CultivatedCropTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CultivatedCrop.class);
        CultivatedCrop cultivatedCrop1 = getCultivatedCropSample1();
        CultivatedCrop cultivatedCrop2 = new CultivatedCrop();
        assertThat(cultivatedCrop1).isNotEqualTo(cultivatedCrop2);

        cultivatedCrop2.setId(cultivatedCrop1.getId());
        assertThat(cultivatedCrop1).isEqualTo(cultivatedCrop2);

        cultivatedCrop2 = getCultivatedCropSample2();
        assertThat(cultivatedCrop1).isNotEqualTo(cultivatedCrop2);
    }

    @Test
    void cultivatedLandTest() {
        CultivatedCrop cultivatedCrop = getCultivatedCropRandomSampleGenerator();
        CultivatedCropCultivatedLand cultivatedCropCultivatedLandBack = getCultivatedCropCultivatedLandRandomSampleGenerator();

        cultivatedCrop.setCultivatedLand(cultivatedCropCultivatedLandBack);
        assertThat(cultivatedCrop.getCultivatedLand()).isEqualTo(cultivatedCropCultivatedLandBack);

        cultivatedCrop.cultivatedLand(null);
        assertThat(cultivatedCrop.getCultivatedLand()).isNull();
    }

    @Test
    void cropTest() {
        CultivatedCrop cultivatedCrop = getCultivatedCropRandomSampleGenerator();
        CultivatedCropCropType cultivatedCropCropTypeBack = getCultivatedCropCropTypeRandomSampleGenerator();

        cultivatedCrop.setCrop(cultivatedCropCropTypeBack);
        assertThat(cultivatedCrop.getCrop()).isEqualTo(cultivatedCropCropTypeBack);

        cultivatedCrop.crop(null);
        assertThat(cultivatedCrop.getCrop()).isNull();
    }
}
