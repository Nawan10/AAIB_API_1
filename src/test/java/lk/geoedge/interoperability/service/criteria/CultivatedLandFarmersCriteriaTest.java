package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CultivatedLandFarmersCriteriaTest {

    @Test
    void newCultivatedLandFarmersCriteriaHasAllFiltersNullTest() {
        var cultivatedLandFarmersCriteria = new CultivatedLandFarmersCriteria();
        assertThat(cultivatedLandFarmersCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cultivatedLandFarmersCriteriaFluentMethodsCreatesFiltersTest() {
        var cultivatedLandFarmersCriteria = new CultivatedLandFarmersCriteria();

        setAllFilters(cultivatedLandFarmersCriteria);

        assertThat(cultivatedLandFarmersCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cultivatedLandFarmersCriteriaCopyCreatesNullFilterTest() {
        var cultivatedLandFarmersCriteria = new CultivatedLandFarmersCriteria();
        var copy = cultivatedLandFarmersCriteria.copy();

        assertThat(cultivatedLandFarmersCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandFarmersCriteria)
        );
    }

    @Test
    void cultivatedLandFarmersCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cultivatedLandFarmersCriteria = new CultivatedLandFarmersCriteria();
        setAllFilters(cultivatedLandFarmersCriteria);

        var copy = cultivatedLandFarmersCriteria.copy();

        assertThat(cultivatedLandFarmersCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandFarmersCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cultivatedLandFarmersCriteria = new CultivatedLandFarmersCriteria();

        assertThat(cultivatedLandFarmersCriteria).hasToString("CultivatedLandFarmersCriteria{}");
    }

    private static void setAllFilters(CultivatedLandFarmersCriteria cultivatedLandFarmersCriteria) {
        cultivatedLandFarmersCriteria.id();
        cultivatedLandFarmersCriteria.relationId();
        cultivatedLandFarmersCriteria.createdAt();
        cultivatedLandFarmersCriteria.addedBy();
        cultivatedLandFarmersCriteria.farmerId();
        cultivatedLandFarmersCriteria.cultivatedLandId();
        cultivatedLandFarmersCriteria.distinct();
    }

    private static Condition<CultivatedLandFarmersCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getRelationId()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getFarmerId()) &&
                condition.apply(criteria.getCultivatedLandId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CultivatedLandFarmersCriteria> copyFiltersAre(
        CultivatedLandFarmersCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getRelationId(), copy.getRelationId()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy(), copy.getAddedBy()) &&
                condition.apply(criteria.getFarmerId(), copy.getFarmerId()) &&
                condition.apply(criteria.getCultivatedLandId(), copy.getCultivatedLandId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
