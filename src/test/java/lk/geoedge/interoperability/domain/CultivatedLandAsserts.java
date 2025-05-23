package lk.geoedge.interoperability.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CultivatedLandAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCultivatedLandAllPropertiesEquals(CultivatedLand expected, CultivatedLand actual) {
        assertCultivatedLandAutoGeneratedPropertiesEquals(expected, actual);
        assertCultivatedLandAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCultivatedLandAllUpdatablePropertiesEquals(CultivatedLand expected, CultivatedLand actual) {
        assertCultivatedLandUpdatableFieldsEquals(expected, actual);
        assertCultivatedLandUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCultivatedLandAutoGeneratedPropertiesEquals(CultivatedLand expected, CultivatedLand actual) {
        assertThat(actual)
            .as("Verify CultivatedLand auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCultivatedLandUpdatableFieldsEquals(CultivatedLand expected, CultivatedLand actual) {
        assertThat(actual)
            .as("Verify CultivatedLand relevant properties")
            .satisfies(a -> assertThat(a.getLandStatus()).as("check landStatus").isEqualTo(expected.getLandStatus()))
            .satisfies(a -> assertThat(a.getUrea()).as("check urea").isEqualTo(expected.getUrea()))
            .satisfies(a -> assertThat(a.getMop()).as("check mop").isEqualTo(expected.getMop()))
            .satisfies(a -> assertThat(a.getTsp()).as("check tsp").isEqualTo(expected.getTsp()))
            .satisfies(a -> assertThat(a.getCreatedAt()).as("check createdAt").isEqualTo(expected.getCreatedAt()))
            .satisfies(a -> assertThat(a.getAddedBy()).as("check addedBy").isEqualTo(expected.getAddedBy()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCultivatedLandUpdatableRelationshipsEquals(CultivatedLand expected, CultivatedLand actual) {
        assertThat(actual)
            .as("Verify CultivatedLand relationships")
            .satisfies(a -> assertThat(a.getFarmField()).as("check farmField").isEqualTo(expected.getFarmField()))
            .satisfies(a -> assertThat(a.getSeason()).as("check season").isEqualTo(expected.getSeason()));
    }
}
