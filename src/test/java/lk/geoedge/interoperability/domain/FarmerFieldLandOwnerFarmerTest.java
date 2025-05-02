package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.FarmerFieldLandOwnerFarmerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FarmerFieldLandOwnerFarmerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FarmerFieldLandOwnerFarmer.class);
        FarmerFieldLandOwnerFarmer farmerFieldLandOwnerFarmer1 = getFarmerFieldLandOwnerFarmerSample1();
        FarmerFieldLandOwnerFarmer farmerFieldLandOwnerFarmer2 = new FarmerFieldLandOwnerFarmer();
        assertThat(farmerFieldLandOwnerFarmer1).isNotEqualTo(farmerFieldLandOwnerFarmer2);

        farmerFieldLandOwnerFarmer2.setId(farmerFieldLandOwnerFarmer1.getId());
        assertThat(farmerFieldLandOwnerFarmer1).isEqualTo(farmerFieldLandOwnerFarmer2);

        farmerFieldLandOwnerFarmer2 = getFarmerFieldLandOwnerFarmerSample2();
        assertThat(farmerFieldLandOwnerFarmer1).isNotEqualTo(farmerFieldLandOwnerFarmer2);
    }
}
