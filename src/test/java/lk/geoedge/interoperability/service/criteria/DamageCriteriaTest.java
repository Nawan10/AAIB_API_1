package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DamageCriteriaTest {

    @Test
    void newDamageCriteriaHasAllFiltersNullTest() {
        var damageCriteria = new DamageCriteria();
        assertThat(damageCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void damageCriteriaFluentMethodsCreatesFiltersTest() {
        var damageCriteria = new DamageCriteria();

        setAllFilters(damageCriteria);

        assertThat(damageCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void damageCriteriaCopyCreatesNullFilterTest() {
        var damageCriteria = new DamageCriteria();
        var copy = damageCriteria.copy();

        assertThat(damageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(damageCriteria)
        );
    }

    @Test
    void damageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var damageCriteria = new DamageCriteria();
        setAllFilters(damageCriteria);

        var copy = damageCriteria.copy();

        assertThat(damageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(damageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var damageCriteria = new DamageCriteria();

        assertThat(damageCriteria).hasToString("DamageCriteria{}");
    }

    private static void setAllFilters(DamageCriteria damageCriteria) {
        damageCriteria.id();
        damageCriteria.damageName();
        damageCriteria.damageCode();
        damageCriteria.damageFamily();
        damageCriteria.damageGenus();
        damageCriteria.damageSpecies();
        damageCriteria.createdAt();
        damageCriteria.addedBy();
        damageCriteria.damageCategoryId();
        damageCriteria.damageTypeId();
        damageCriteria.distinct();
    }

    private static Condition<DamageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDamageName()) &&
                condition.apply(criteria.getDamageCode()) &&
                condition.apply(criteria.getDamageFamily()) &&
                condition.apply(criteria.getDamageGenus()) &&
                condition.apply(criteria.getDamageSpecies()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getDamageCategoryId()) &&
                condition.apply(criteria.getDamageTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DamageCriteria> copyFiltersAre(DamageCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDamageName(), copy.getDamageName()) &&
                condition.apply(criteria.getDamageCode(), copy.getDamageCode()) &&
                condition.apply(criteria.getDamageFamily(), copy.getDamageFamily()) &&
                condition.apply(criteria.getDamageGenus(), copy.getDamageGenus()) &&
                condition.apply(criteria.getDamageSpecies(), copy.getDamageSpecies()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy(), copy.getAddedBy()) &&
                condition.apply(criteria.getDamageCategoryId(), copy.getDamageCategoryId()) &&
                condition.apply(criteria.getDamageTypeId(), copy.getDamageTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
