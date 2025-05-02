package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.FarmerFieldOwnerCropTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FarmerFieldOwnerCropTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FarmerFieldOwnerCropType.class);
        FarmerFieldOwnerCropType farmerFieldOwnerCropType1 = getFarmerFieldOwnerCropTypeSample1();
        FarmerFieldOwnerCropType farmerFieldOwnerCropType2 = new FarmerFieldOwnerCropType();
        assertThat(farmerFieldOwnerCropType1).isNotEqualTo(farmerFieldOwnerCropType2);

        farmerFieldOwnerCropType2.setId(farmerFieldOwnerCropType1.getId());
        assertThat(farmerFieldOwnerCropType1).isEqualTo(farmerFieldOwnerCropType2);

        farmerFieldOwnerCropType2 = getFarmerFieldOwnerCropTypeSample2();
        assertThat(farmerFieldOwnerCropType1).isNotEqualTo(farmerFieldOwnerCropType2);
    }
}
