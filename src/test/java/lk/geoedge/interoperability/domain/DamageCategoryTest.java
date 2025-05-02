package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.DamageCategoryTestSamples.*;
import static lk.geoedge.interoperability.domain.DamageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DamageCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DamageCategory.class);
        DamageCategory damageCategory1 = getDamageCategorySample1();
        DamageCategory damageCategory2 = new DamageCategory();
        assertThat(damageCategory1).isNotEqualTo(damageCategory2);

        damageCategory2.setId(damageCategory1.getId());
        assertThat(damageCategory1).isEqualTo(damageCategory2);

        damageCategory2 = getDamageCategorySample2();
        assertThat(damageCategory1).isNotEqualTo(damageCategory2);
    }

    @Test
    void damageTest() {
        DamageCategory damageCategory = getDamageCategoryRandomSampleGenerator();
        Damage damageBack = getDamageRandomSampleGenerator();

        damageCategory.addDamage(damageBack);
        assertThat(damageCategory.getDamages()).containsOnly(damageBack);
        assertThat(damageBack.getDamageCategory()).isEqualTo(damageCategory);

        damageCategory.removeDamage(damageBack);
        assertThat(damageCategory.getDamages()).doesNotContain(damageBack);
        assertThat(damageBack.getDamageCategory()).isNull();

        damageCategory.damages(new HashSet<>(Set.of(damageBack)));
        assertThat(damageCategory.getDamages()).containsOnly(damageBack);
        assertThat(damageBack.getDamageCategory()).isEqualTo(damageCategory);

        damageCategory.setDamages(new HashSet<>());
        assertThat(damageCategory.getDamages()).doesNotContain(damageBack);
        assertThat(damageBack.getDamageCategory()).isNull();
    }
}
