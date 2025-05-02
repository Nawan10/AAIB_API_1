package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.FarmerFieldLandOwnerFarmerTestSamples.*;
import static lk.geoedge.interoperability.domain.FarmerFieldLandOwnerTestSamples.*;
import static lk.geoedge.interoperability.domain.FarmerFieldOwnerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FarmerFieldLandOwnerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FarmerFieldLandOwner.class);
        FarmerFieldLandOwner farmerFieldLandOwner1 = getFarmerFieldLandOwnerSample1();
        FarmerFieldLandOwner farmerFieldLandOwner2 = new FarmerFieldLandOwner();
        assertThat(farmerFieldLandOwner1).isNotEqualTo(farmerFieldLandOwner2);

        farmerFieldLandOwner2.setId(farmerFieldLandOwner1.getId());
        assertThat(farmerFieldLandOwner1).isEqualTo(farmerFieldLandOwner2);

        farmerFieldLandOwner2 = getFarmerFieldLandOwnerSample2();
        assertThat(farmerFieldLandOwner1).isNotEqualTo(farmerFieldLandOwner2);
    }

    @Test
    void farmerFieldOwnerTest() {
        FarmerFieldLandOwner farmerFieldLandOwner = getFarmerFieldLandOwnerRandomSampleGenerator();
        FarmerFieldOwner farmerFieldOwnerBack = getFarmerFieldOwnerRandomSampleGenerator();

        farmerFieldLandOwner.setFarmerFieldOwner(farmerFieldOwnerBack);
        assertThat(farmerFieldLandOwner.getFarmerFieldOwner()).isEqualTo(farmerFieldOwnerBack);

        farmerFieldLandOwner.farmerFieldOwner(null);
        assertThat(farmerFieldLandOwner.getFarmerFieldOwner()).isNull();
    }

    @Test
    void farmerTest() {
        FarmerFieldLandOwner farmerFieldLandOwner = getFarmerFieldLandOwnerRandomSampleGenerator();
        FarmerFieldLandOwnerFarmer farmerFieldLandOwnerFarmerBack = getFarmerFieldLandOwnerFarmerRandomSampleGenerator();

        farmerFieldLandOwner.setFarmer(farmerFieldLandOwnerFarmerBack);
        assertThat(farmerFieldLandOwner.getFarmer()).isEqualTo(farmerFieldLandOwnerFarmerBack);

        farmerFieldLandOwner.farmer(null);
        assertThat(farmerFieldLandOwner.getFarmer()).isNull();
    }
}
