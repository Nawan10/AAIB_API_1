package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.DamagesCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DamagesCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DamagesCategory.class);
        DamagesCategory damagesCategory1 = getDamagesCategorySample1();
        DamagesCategory damagesCategory2 = new DamagesCategory();
        assertThat(damagesCategory1).isNotEqualTo(damagesCategory2);

        damagesCategory2.setId(damagesCategory1.getId());
        assertThat(damagesCategory1).isEqualTo(damagesCategory2);

        damagesCategory2 = getDamagesCategorySample2();
        assertThat(damagesCategory1).isNotEqualTo(damagesCategory2);
    }
}
