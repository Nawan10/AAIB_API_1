package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.DamageTestSamples.*;
import static lk.geoedge.interoperability.domain.DamageTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DamageTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DamageType.class);
        DamageType damageType1 = getDamageTypeSample1();
        DamageType damageType2 = new DamageType();
        assertThat(damageType1).isNotEqualTo(damageType2);

        damageType2.setId(damageType1.getId());
        assertThat(damageType1).isEqualTo(damageType2);

        damageType2 = getDamageTypeSample2();
        assertThat(damageType1).isNotEqualTo(damageType2);
    }

    @Test
    void damageTest() {
        DamageType damageType = getDamageTypeRandomSampleGenerator();
        Damage damageBack = getDamageRandomSampleGenerator();

        damageType.addDamage(damageBack);
        assertThat(damageType.getDamages()).containsOnly(damageBack);
        assertThat(damageBack.getDamageType()).isEqualTo(damageType);

        damageType.removeDamage(damageBack);
        assertThat(damageType.getDamages()).doesNotContain(damageBack);
        assertThat(damageBack.getDamageType()).isNull();

        damageType.damages(new HashSet<>(Set.of(damageBack)));
        assertThat(damageType.getDamages()).containsOnly(damageBack);
        assertThat(damageBack.getDamageType()).isEqualTo(damageType);

        damageType.setDamages(new HashSet<>());
        assertThat(damageType.getDamages()).doesNotContain(damageBack);
        assertThat(damageBack.getDamageType()).isNull();
    }
}
