package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DamageEntityCriteriaTest {

    @Test
    void newDamageEntityCriteriaHasAllFiltersNullTest() {
        var damageEntityCriteria = new DamageEntityCriteria();
        assertThat(damageEntityCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void damageEntityCriteriaFluentMethodsCreatesFiltersTest() {
        var damageEntityCriteria = new DamageEntityCriteria();

        setAllFilters(damageEntityCriteria);

        assertThat(damageEntityCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void damageEntityCriteriaCopyCreatesNullFilterTest() {
        var damageEntityCriteria = new DamageEntityCriteria();
        var copy = damageEntityCriteria.copy();

        assertThat(damageEntityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(damageEntityCriteria)
        );
    }

    @Test
    void damageEntityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var damageEntityCriteria = new DamageEntityCriteria();
        setAllFilters(damageEntityCriteria);

        var copy = damageEntityCriteria.copy();

        assertThat(damageEntityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(damageEntityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var damageEntityCriteria = new DamageEntityCriteria();

        assertThat(damageEntityCriteria).hasToString("DamageEntityCriteria{}");
    }

    private static void setAllFilters(DamageEntityCriteria damageEntityCriteria) {
        damageEntityCriteria.id();
        damageEntityCriteria.damageName();
        damageEntityCriteria.damageCode();
        damageEntityCriteria.damageFamily();
        damageEntityCriteria.damageGenus();
        damageEntityCriteria.damageSpecies();
        damageEntityCriteria.createdAt();
        damageEntityCriteria.addedBy();
        damageEntityCriteria.damageCategoryId();
        damageEntityCriteria.damageTypeId();
        damageEntityCriteria.distinct();
    }

    private static Condition<DamageEntityCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<DamageEntityCriteria> copyFiltersAre(
        DamageEntityCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
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
