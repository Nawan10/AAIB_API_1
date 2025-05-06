package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.InsurancePolicyDamageCultivatedLandDamageReasonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsurancePolicyDamageCultivatedLandDamageReasonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsurancePolicyDamageCultivatedLandDamageReason.class);
        InsurancePolicyDamageCultivatedLandDamageReason insurancePolicyDamageCultivatedLandDamageReason1 =
            getInsurancePolicyDamageCultivatedLandDamageReasonSample1();
        InsurancePolicyDamageCultivatedLandDamageReason insurancePolicyDamageCultivatedLandDamageReason2 =
            new InsurancePolicyDamageCultivatedLandDamageReason();
        assertThat(insurancePolicyDamageCultivatedLandDamageReason1).isNotEqualTo(insurancePolicyDamageCultivatedLandDamageReason2);

        insurancePolicyDamageCultivatedLandDamageReason2.setId(insurancePolicyDamageCultivatedLandDamageReason1.getId());
        assertThat(insurancePolicyDamageCultivatedLandDamageReason1).isEqualTo(insurancePolicyDamageCultivatedLandDamageReason2);

        insurancePolicyDamageCultivatedLandDamageReason2 = getInsurancePolicyDamageCultivatedLandDamageReasonSample2();
        assertThat(insurancePolicyDamageCultivatedLandDamageReason1).isNotEqualTo(insurancePolicyDamageCultivatedLandDamageReason2);
    }
}
