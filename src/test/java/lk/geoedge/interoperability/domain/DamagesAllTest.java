package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.DamagesAllTestSamples.*;
import static lk.geoedge.interoperability.domain.DamagesCategoryTestSamples.*;
import static lk.geoedge.interoperability.domain.DamagesTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DamagesAllTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DamagesAll.class);
        DamagesAll damagesAll1 = getDamagesAllSample1();
        DamagesAll damagesAll2 = new DamagesAll();
        assertThat(damagesAll1).isNotEqualTo(damagesAll2);

        damagesAll2.setId(damagesAll1.getId());
        assertThat(damagesAll1).isEqualTo(damagesAll2);

        damagesAll2 = getDamagesAllSample2();
        assertThat(damagesAll1).isNotEqualTo(damagesAll2);
    }

    @Test
    void damageCategoryTest() {
        DamagesAll damagesAll = getDamagesAllRandomSampleGenerator();
        DamagesCategory damagesCategoryBack = getDamagesCategoryRandomSampleGenerator();

        damagesAll.setDamageCategory(damagesCategoryBack);
        assertThat(damagesAll.getDamageCategory()).isEqualTo(damagesCategoryBack);

        damagesAll.damageCategory(null);
        assertThat(damagesAll.getDamageCategory()).isNull();
    }

    @Test
    void damageTypeTest() {
        DamagesAll damagesAll = getDamagesAllRandomSampleGenerator();
        DamagesType damagesTypeBack = getDamagesTypeRandomSampleGenerator();

        damagesAll.setDamageType(damagesTypeBack);
        assertThat(damagesAll.getDamageType()).isEqualTo(damagesTypeBack);

        damagesAll.damageType(null);
        assertThat(damagesAll.getDamageType()).isNull();
    }
}
