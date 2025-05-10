package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DamagesTypeCriteriaTest {

    @Test
    void newDamagesTypeCriteriaHasAllFiltersNullTest() {
        var damagesTypeCriteria = new DamagesTypeCriteria();
        assertThat(damagesTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void damagesTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var damagesTypeCriteria = new DamagesTypeCriteria();

        setAllFilters(damagesTypeCriteria);

        assertThat(damagesTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void damagesTypeCriteriaCopyCreatesNullFilterTest() {
        var damagesTypeCriteria = new DamagesTypeCriteria();
        var copy = damagesTypeCriteria.copy();

        assertThat(damagesTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(damagesTypeCriteria)
        );
    }

    @Test
    void damagesTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var damagesTypeCriteria = new DamagesTypeCriteria();
        setAllFilters(damagesTypeCriteria);

        var copy = damagesTypeCriteria.copy();

        assertThat(damagesTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(damagesTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var damagesTypeCriteria = new DamagesTypeCriteria();

        assertThat(damagesTypeCriteria).hasToString("DamagesTypeCriteria{}");
    }

    private static void setAllFilters(DamagesTypeCriteria damagesTypeCriteria) {
        damagesTypeCriteria.id();
        damagesTypeCriteria.typeName();
        damagesTypeCriteria.distinct();
    }

    private static Condition<DamagesTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) && condition.apply(criteria.getTypeName()) && condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DamagesTypeCriteria> copyFiltersAre(DamagesTypeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTypeName(), copy.getTypeName()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
