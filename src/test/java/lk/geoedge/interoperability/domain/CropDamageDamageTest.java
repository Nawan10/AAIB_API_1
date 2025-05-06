package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CropDamageDamageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CropDamageDamageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CropDamageDamage.class);
        CropDamageDamage cropDamageDamage1 = getCropDamageDamageSample1();
        CropDamageDamage cropDamageDamage2 = new CropDamageDamage();
        assertThat(cropDamageDamage1).isNotEqualTo(cropDamageDamage2);

        cropDamageDamage2.setId(cropDamageDamage1.getId());
        assertThat(cropDamageDamage1).isEqualTo(cropDamageDamage2);

        cropDamageDamage2 = getCropDamageDamageSample2();
        assertThat(cropDamageDamage1).isNotEqualTo(cropDamageDamage2);
    }
}
