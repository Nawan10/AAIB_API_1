package lk.geoedge.interoperability.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CultivatedCropCultivatedLandAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCultivatedCropCultivatedLandAllPropertiesEquals(
        CultivatedCropCultivatedLand expected,
        CultivatedCropCultivatedLand actual
    ) {
        assertCultivatedCropCultivatedLandAutoGeneratedPropertiesEquals(expected, actual);
        assertCultivatedCropCultivatedLandAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCultivatedCropCultivatedLandAllUpdatablePropertiesEquals(
        CultivatedCropCultivatedLand expected,
        CultivatedCropCultivatedLand actual
    ) {
        assertCultivatedCropCultivatedLandUpdatableFieldsEquals(expected, actual);
        assertCultivatedCropCultivatedLandUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCultivatedCropCultivatedLandAutoGeneratedPropertiesEquals(
        CultivatedCropCultivatedLand expected,
        CultivatedCropCultivatedLand actual
    ) {
        assertThat(actual)
            .as("Verify CultivatedCropCultivatedLand auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCultivatedCropCultivatedLandUpdatableFieldsEquals(
        CultivatedCropCultivatedLand expected,
        CultivatedCropCultivatedLand actual
    ) {
        assertThat(actual)
            .as("Verify CultivatedCropCultivatedLand relevant properties")
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
    public static void assertCultivatedCropCultivatedLandUpdatableRelationshipsEquals(
        CultivatedCropCultivatedLand expected,
        CultivatedCropCultivatedLand actual
    ) {
        assertThat(actual)
            .as("Verify CultivatedCropCultivatedLand relationships")
            .satisfies(a -> assertThat(a.getSeason()).as("check season").isEqualTo(expected.getSeason()));
    }
}
