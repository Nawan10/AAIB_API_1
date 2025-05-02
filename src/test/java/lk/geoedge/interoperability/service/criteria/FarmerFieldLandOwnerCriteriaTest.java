package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FarmerFieldLandOwnerCriteriaTest {

    @Test
    void newFarmerFieldLandOwnerCriteriaHasAllFiltersNullTest() {
        var farmerFieldLandOwnerCriteria = new FarmerFieldLandOwnerCriteria();
        assertThat(farmerFieldLandOwnerCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void farmerFieldLandOwnerCriteriaFluentMethodsCreatesFiltersTest() {
        var farmerFieldLandOwnerCriteria = new FarmerFieldLandOwnerCriteria();

        setAllFilters(farmerFieldLandOwnerCriteria);

        assertThat(farmerFieldLandOwnerCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void farmerFieldLandOwnerCriteriaCopyCreatesNullFilterTest() {
        var farmerFieldLandOwnerCriteria = new FarmerFieldLandOwnerCriteria();
        var copy = farmerFieldLandOwnerCriteria.copy();

        assertThat(farmerFieldLandOwnerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(farmerFieldLandOwnerCriteria)
        );
    }

    @Test
    void farmerFieldLandOwnerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var farmerFieldLandOwnerCriteria = new FarmerFieldLandOwnerCriteria();
        setAllFilters(farmerFieldLandOwnerCriteria);

        var copy = farmerFieldLandOwnerCriteria.copy();

        assertThat(farmerFieldLandOwnerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(farmerFieldLandOwnerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var farmerFieldLandOwnerCriteria = new FarmerFieldLandOwnerCriteria();

        assertThat(farmerFieldLandOwnerCriteria).hasToString("FarmerFieldLandOwnerCriteria{}");
    }

    private static void setAllFilters(FarmerFieldLandOwnerCriteria farmerFieldLandOwnerCriteria) {
        farmerFieldLandOwnerCriteria.id();
        farmerFieldLandOwnerCriteria.createdAt();
        farmerFieldLandOwnerCriteria.addedBy();
        farmerFieldLandOwnerCriteria.farmerFieldOwnerId();
        farmerFieldLandOwnerCriteria.farmerId();
        farmerFieldLandOwnerCriteria.distinct();
    }

    private static Condition<FarmerFieldLandOwnerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getFarmerFieldOwnerId()) &&
                condition.apply(criteria.getFarmerId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FarmerFieldLandOwnerCriteria> copyFiltersAre(
        FarmerFieldLandOwnerCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy(), copy.getAddedBy()) &&
                condition.apply(criteria.getFarmerFieldOwnerId(), copy.getFarmerFieldOwnerId()) &&
                condition.apply(criteria.getFarmerId(), copy.getFarmerId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
