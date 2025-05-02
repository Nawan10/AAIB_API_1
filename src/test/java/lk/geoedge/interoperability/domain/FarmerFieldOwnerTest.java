package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.FarmerFieldOwnerCropTypeTestSamples.*;
import static lk.geoedge.interoperability.domain.FarmerFieldOwnerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FarmerFieldOwnerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FarmerFieldOwner.class);
        FarmerFieldOwner farmerFieldOwner1 = getFarmerFieldOwnerSample1();
        FarmerFieldOwner farmerFieldOwner2 = new FarmerFieldOwner();
        assertThat(farmerFieldOwner1).isNotEqualTo(farmerFieldOwner2);

        farmerFieldOwner2.setId(farmerFieldOwner1.getId());
        assertThat(farmerFieldOwner1).isEqualTo(farmerFieldOwner2);

        farmerFieldOwner2 = getFarmerFieldOwnerSample2();
        assertThat(farmerFieldOwner1).isNotEqualTo(farmerFieldOwner2);
    }

    @Test
    void cropTest() {
        FarmerFieldOwner farmerFieldOwner = getFarmerFieldOwnerRandomSampleGenerator();
        FarmerFieldOwnerCropType farmerFieldOwnerCropTypeBack = getFarmerFieldOwnerCropTypeRandomSampleGenerator();

        farmerFieldOwner.setCrop(farmerFieldOwnerCropTypeBack);
        assertThat(farmerFieldOwner.getCrop()).isEqualTo(farmerFieldOwnerCropTypeBack);

        farmerFieldOwner.crop(null);
        assertThat(farmerFieldOwner.getCrop()).isNull();
    }
}
