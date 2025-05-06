package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CropDamageCropTypeTestSamples.*;
import static lk.geoedge.interoperability.domain.CropDamageDamageTestSamples.*;
import static lk.geoedge.interoperability.domain.CropDamageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CropDamageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CropDamage.class);
        CropDamage cropDamage1 = getCropDamageSample1();
        CropDamage cropDamage2 = new CropDamage();
        assertThat(cropDamage1).isNotEqualTo(cropDamage2);

        cropDamage2.setId(cropDamage1.getId());
        assertThat(cropDamage1).isEqualTo(cropDamage2);

        cropDamage2 = getCropDamageSample2();
        assertThat(cropDamage1).isNotEqualTo(cropDamage2);
    }

    @Test
    void cropTest() {
        CropDamage cropDamage = getCropDamageRandomSampleGenerator();
        CropDamageCropType cropDamageCropTypeBack = getCropDamageCropTypeRandomSampleGenerator();

        cropDamage.setCrop(cropDamageCropTypeBack);
        assertThat(cropDamage.getCrop()).isEqualTo(cropDamageCropTypeBack);

        cropDamage.crop(null);
        assertThat(cropDamage.getCrop()).isNull();
    }

    @Test
    void damageTest() {
        CropDamage cropDamage = getCropDamageRandomSampleGenerator();
        CropDamageDamage cropDamageDamageBack = getCropDamageDamageRandomSampleGenerator();

        cropDamage.setDamage(cropDamageDamageBack);
        assertThat(cropDamage.getDamage()).isEqualTo(cropDamageDamageBack);

        cropDamage.damage(null);
        assertThat(cropDamage.getDamage()).isNull();
    }
}
