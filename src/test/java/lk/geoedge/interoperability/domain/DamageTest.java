package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.DamageCategoryTestSamples.*;
import static lk.geoedge.interoperability.domain.DamageTestSamples.*;
import static lk.geoedge.interoperability.domain.DamageTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DamageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Damage.class);
        Damage damage1 = getDamageSample1();
        Damage damage2 = new Damage();
        assertThat(damage1).isNotEqualTo(damage2);

        damage2.setId(damage1.getId());
        assertThat(damage1).isEqualTo(damage2);

        damage2 = getDamageSample2();
        assertThat(damage1).isNotEqualTo(damage2);
    }

    @Test
    void damageCategoryTest() {
        Damage damage = getDamageRandomSampleGenerator();
        DamageCategory damageCategoryBack = getDamageCategoryRandomSampleGenerator();

        damage.setDamageCategory(damageCategoryBack);
        assertThat(damage.getDamageCategory()).isEqualTo(damageCategoryBack);

        damage.damageCategory(null);
        assertThat(damage.getDamageCategory()).isNull();
    }

    @Test
    void damageTypeTest() {
        Damage damage = getDamageRandomSampleGenerator();
        DamageType damageTypeBack = getDamageTypeRandomSampleGenerator();

        damage.setDamageType(damageTypeBack);
        assertThat(damage.getDamageType()).isEqualTo(damageTypeBack);

        damage.damageType(null);
        assertThat(damage.getDamageType()).isNull();
    }
}
