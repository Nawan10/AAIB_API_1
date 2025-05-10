package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.DamagesTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DamagesTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DamagesType.class);
        DamagesType damagesType1 = getDamagesTypeSample1();
        DamagesType damagesType2 = new DamagesType();
        assertThat(damagesType1).isNotEqualTo(damagesType2);

        damagesType2.setId(damagesType1.getId());
        assertThat(damagesType1).isEqualTo(damagesType2);

        damagesType2 = getDamagesTypeSample2();
        assertThat(damagesType1).isNotEqualTo(damagesType2);
    }
}
