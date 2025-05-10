package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.DamageCategoryTestSamples.*;
import static lk.geoedge.interoperability.domain.DamageEntityTestSamples.*;
import static lk.geoedge.interoperability.domain.DamageTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DamageEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DamageEntity.class);
        DamageEntity damageEntity1 = getDamageEntitySample1();
        DamageEntity damageEntity2 = new DamageEntity();
        assertThat(damageEntity1).isNotEqualTo(damageEntity2);

        damageEntity2.setId(damageEntity1.getId());
        assertThat(damageEntity1).isEqualTo(damageEntity2);

        damageEntity2 = getDamageEntitySample2();
        assertThat(damageEntity1).isNotEqualTo(damageEntity2);
    }

    @Test
    void damageCategoryTest() {
        DamageEntity damageEntity = getDamageEntityRandomSampleGenerator();
        DamageCategory damageCategoryBack = getDamageCategoryRandomSampleGenerator();

        damageEntity.setDamageCategory(damageCategoryBack);
        assertThat(damageEntity.getDamageCategory()).isEqualTo(damageCategoryBack);

        damageEntity.damageCategory(null);
        assertThat(damageEntity.getDamageCategory()).isNull();
    }

    @Test
    void damageTypeTest() {
        DamageEntity damageEntity = getDamageEntityRandomSampleGenerator();
        DamageType damageTypeBack = getDamageTypeRandomSampleGenerator();

        damageEntity.setDamageType(damageTypeBack);
        assertThat(damageEntity.getDamageType()).isEqualTo(damageTypeBack);

        damageEntity.damageType(null);
        assertThat(damageEntity.getDamageType()).isNull();
    }
}
