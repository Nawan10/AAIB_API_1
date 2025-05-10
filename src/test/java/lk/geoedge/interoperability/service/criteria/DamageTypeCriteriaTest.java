package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DamageTypeCriteriaTest {

    @Test
    void newDamageTypeCriteriaHasAllFiltersNullTest() {
        var damageTypeCriteria = new DamageTypeCriteria();
        assertThat(damageTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void damageTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var damageTypeCriteria = new DamageTypeCriteria();

        setAllFilters(damageTypeCriteria);

        assertThat(damageTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void damageTypeCriteriaCopyCreatesNullFilterTest() {
        var damageTypeCriteria = new DamageTypeCriteria();
        var copy = damageTypeCriteria.copy();

        assertThat(damageTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(damageTypeCriteria)
        );
    }

    @Test
    void damageTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var damageTypeCriteria = new DamageTypeCriteria();
        setAllFilters(damageTypeCriteria);

        var copy = damageTypeCriteria.copy();

        assertThat(damageTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(damageTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var damageTypeCriteria = new DamageTypeCriteria();

        assertThat(damageTypeCriteria).hasToString("DamageTypeCriteria{}");
    }

    private static void setAllFilters(DamageTypeCriteria damageTypeCriteria) {
        damageTypeCriteria.id();
        damageTypeCriteria.typeName();
        damageTypeCriteria.distinct();
    }

    private static Condition<DamageTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) && condition.apply(criteria.getTypeName()) && condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DamageTypeCriteria> copyFiltersAre(DamageTypeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTypeName(), copy.getTypeName()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
