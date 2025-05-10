package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.DamageTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

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
}
