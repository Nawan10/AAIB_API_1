package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DamagesCategoryCriteriaTest {

    @Test
    void newDamagesCategoryCriteriaHasAllFiltersNullTest() {
        var damagesCategoryCriteria = new DamagesCategoryCriteria();
        assertThat(damagesCategoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void damagesCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var damagesCategoryCriteria = new DamagesCategoryCriteria();

        setAllFilters(damagesCategoryCriteria);

        assertThat(damagesCategoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void damagesCategoryCriteriaCopyCreatesNullFilterTest() {
        var damagesCategoryCriteria = new DamagesCategoryCriteria();
        var copy = damagesCategoryCriteria.copy();

        assertThat(damagesCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(damagesCategoryCriteria)
        );
    }

    @Test
    void damagesCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var damagesCategoryCriteria = new DamagesCategoryCriteria();
        setAllFilters(damagesCategoryCriteria);

        var copy = damagesCategoryCriteria.copy();

        assertThat(damagesCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(damagesCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var damagesCategoryCriteria = new DamagesCategoryCriteria();

        assertThat(damagesCategoryCriteria).hasToString("DamagesCategoryCriteria{}");
    }

    private static void setAllFilters(DamagesCategoryCriteria damagesCategoryCriteria) {
        damagesCategoryCriteria.id();
        damagesCategoryCriteria.categoryName();
        damagesCategoryCriteria.distinct();
    }

    private static Condition<DamagesCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) && condition.apply(criteria.getCategoryName()) && condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DamagesCategoryCriteria> copyFiltersAre(
        DamagesCategoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCategoryName(), copy.getCategoryName()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
