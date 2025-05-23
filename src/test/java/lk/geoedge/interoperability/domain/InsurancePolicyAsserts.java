package lk.geoedge.interoperability.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class InsurancePolicyAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInsurancePolicyAllPropertiesEquals(InsurancePolicy expected, InsurancePolicy actual) {
        assertInsurancePolicyAutoGeneratedPropertiesEquals(expected, actual);
        assertInsurancePolicyAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInsurancePolicyAllUpdatablePropertiesEquals(InsurancePolicy expected, InsurancePolicy actual) {
        assertInsurancePolicyUpdatableFieldsEquals(expected, actual);
        assertInsurancePolicyUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInsurancePolicyAutoGeneratedPropertiesEquals(InsurancePolicy expected, InsurancePolicy actual) {
        assertThat(actual)
            .as("Verify InsurancePolicy auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInsurancePolicyUpdatableFieldsEquals(InsurancePolicy expected, InsurancePolicy actual) {
        assertThat(actual)
            .as("Verify InsurancePolicy relevant properties")
            .satisfies(a -> assertThat(a.getName()).as("check name").isEqualTo(expected.getName()))
            .satisfies(a -> assertThat(a.getPolicyNo()).as("check policyNo").isEqualTo(expected.getPolicyNo()))
            .satisfies(a -> assertThat(a.getIsActivate()).as("check isActivate").isEqualTo(expected.getIsActivate()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInsurancePolicyUpdatableRelationshipsEquals(InsurancePolicy expected, InsurancePolicy actual) {
        // empty method
    }
}
