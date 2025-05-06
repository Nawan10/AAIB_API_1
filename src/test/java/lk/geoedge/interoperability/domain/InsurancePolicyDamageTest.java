package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.InsurancePolicyDamageCultivatedLandDamageReasonTestSamples.*;
import static lk.geoedge.interoperability.domain.InsurancePolicyDamageTestSamples.*;
import static lk.geoedge.interoperability.domain.InsurancePolicyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsurancePolicyDamageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsurancePolicyDamage.class);
        InsurancePolicyDamage insurancePolicyDamage1 = getInsurancePolicyDamageSample1();
        InsurancePolicyDamage insurancePolicyDamage2 = new InsurancePolicyDamage();
        assertThat(insurancePolicyDamage1).isNotEqualTo(insurancePolicyDamage2);

        insurancePolicyDamage2.setId(insurancePolicyDamage1.getId());
        assertThat(insurancePolicyDamage1).isEqualTo(insurancePolicyDamage2);

        insurancePolicyDamage2 = getInsurancePolicyDamageSample2();
        assertThat(insurancePolicyDamage1).isNotEqualTo(insurancePolicyDamage2);
    }

    @Test
    void insurancePolicyTest() {
        InsurancePolicyDamage insurancePolicyDamage = getInsurancePolicyDamageRandomSampleGenerator();
        InsurancePolicy insurancePolicyBack = getInsurancePolicyRandomSampleGenerator();

        insurancePolicyDamage.setInsurancePolicy(insurancePolicyBack);
        assertThat(insurancePolicyDamage.getInsurancePolicy()).isEqualTo(insurancePolicyBack);

        insurancePolicyDamage.insurancePolicy(null);
        assertThat(insurancePolicyDamage.getInsurancePolicy()).isNull();
    }

    @Test
    void damageReasonTest() {
        InsurancePolicyDamage insurancePolicyDamage = getInsurancePolicyDamageRandomSampleGenerator();
        InsurancePolicyDamageCultivatedLandDamageReason insurancePolicyDamageCultivatedLandDamageReasonBack =
            getInsurancePolicyDamageCultivatedLandDamageReasonRandomSampleGenerator();

        insurancePolicyDamage.setDamageReason(insurancePolicyDamageCultivatedLandDamageReasonBack);
        assertThat(insurancePolicyDamage.getDamageReason()).isEqualTo(insurancePolicyDamageCultivatedLandDamageReasonBack);

        insurancePolicyDamage.damageReason(null);
        assertThat(insurancePolicyDamage.getDamageReason()).isNull();
    }
}
