package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.DamageCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

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
}
