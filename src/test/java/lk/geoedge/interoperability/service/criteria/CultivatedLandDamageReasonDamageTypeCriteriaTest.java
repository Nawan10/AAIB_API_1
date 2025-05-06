package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CultivatedLandDamageReasonDamageTypeCriteriaTest {

    @Test
    void newCultivatedLandDamageReasonDamageTypeCriteriaHasAllFiltersNullTest() {
        var cultivatedLandDamageReasonDamageTypeCriteria = new CultivatedLandDamageReasonDamageTypeCriteria();
        assertThat(cultivatedLandDamageReasonDamageTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cultivatedLandDamageReasonDamageTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var cultivatedLandDamageReasonDamageTypeCriteria = new CultivatedLandDamageReasonDamageTypeCriteria();

        setAllFilters(cultivatedLandDamageReasonDamageTypeCriteria);

        assertThat(cultivatedLandDamageReasonDamageTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cultivatedLandDamageReasonDamageTypeCriteriaCopyCreatesNullFilterTest() {
        var cultivatedLandDamageReasonDamageTypeCriteria = new CultivatedLandDamageReasonDamageTypeCriteria();
        var copy = cultivatedLandDamageReasonDamageTypeCriteria.copy();

        assertThat(cultivatedLandDamageReasonDamageTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandDamageReasonDamageTypeCriteria)
        );
    }

    @Test
    void cultivatedLandDamageReasonDamageTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cultivatedLandDamageReasonDamageTypeCriteria = new CultivatedLandDamageReasonDamageTypeCriteria();
        setAllFilters(cultivatedLandDamageReasonDamageTypeCriteria);

        var copy = cultivatedLandDamageReasonDamageTypeCriteria.copy();

        assertThat(cultivatedLandDamageReasonDamageTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandDamageReasonDamageTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cultivatedLandDamageReasonDamageTypeCriteria = new CultivatedLandDamageReasonDamageTypeCriteria();

        assertThat(cultivatedLandDamageReasonDamageTypeCriteria).hasToString("CultivatedLandDamageReasonDamageTypeCriteria{}");
    }

    private static void setAllFilters(CultivatedLandDamageReasonDamageTypeCriteria cultivatedLandDamageReasonDamageTypeCriteria) {
        cultivatedLandDamageReasonDamageTypeCriteria.id();
        cultivatedLandDamageReasonDamageTypeCriteria.typeName();
        cultivatedLandDamageReasonDamageTypeCriteria.distinct();
    }

    private static Condition<CultivatedLandDamageReasonDamageTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) && condition.apply(criteria.getTypeName()) && condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CultivatedLandDamageReasonDamageTypeCriteria> copyFiltersAre(
        CultivatedLandDamageReasonDamageTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTypeName(), copy.getTypeName()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
