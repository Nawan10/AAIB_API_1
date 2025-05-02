package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.FarmerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FarmerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Farmer.class);
        Farmer farmer1 = getFarmerSample1();
        Farmer farmer2 = new Farmer();
        assertThat(farmer1).isNotEqualTo(farmer2);

        farmer2.setId(farmer1.getId());
        assertThat(farmer1).isEqualTo(farmer2);

        farmer2 = getFarmerSample2();
        assertThat(farmer1).isNotEqualTo(farmer2);
    }
}
