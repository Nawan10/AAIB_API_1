package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DamageCategoryCriteriaTest {

    @Test
    void newDamageCategoryCriteriaHasAllFiltersNullTest() {
        var damageCategoryCriteria = new DamageCategoryCriteria();
        assertThat(damageCategoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void damageCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var damageCategoryCriteria = new DamageCategoryCriteria();

        setAllFilters(damageCategoryCriteria);

        assertThat(damageCategoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void damageCategoryCriteriaCopyCreatesNullFilterTest() {
        var damageCategoryCriteria = new DamageCategoryCriteria();
        var copy = damageCategoryCriteria.copy();

        assertThat(damageCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(damageCategoryCriteria)
        );
    }

    @Test
    void damageCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var damageCategoryCriteria = new DamageCategoryCriteria();
        setAllFilters(damageCategoryCriteria);

        var copy = damageCategoryCriteria.copy();

        assertThat(damageCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(damageCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var damageCategoryCriteria = new DamageCategoryCriteria();

        assertThat(damageCategoryCriteria).hasToString("DamageCategoryCriteria{}");
    }

    private static void setAllFilters(DamageCategoryCriteria damageCategoryCriteria) {
        damageCategoryCriteria.id();
        damageCategoryCriteria.categoryName();
        damageCategoryCriteria.damageId();
        damageCategoryCriteria.distinct();
    }

    private static Condition<DamageCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCategoryName()) &&
                condition.apply(criteria.getDamageId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DamageCategoryCriteria> copyFiltersAre(
        DamageCategoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCategoryName(), copy.getCategoryName()) &&
                condition.apply(criteria.getDamageId(), copy.getDamageId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
