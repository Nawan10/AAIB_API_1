package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DamagesAllCriteriaTest {

    @Test
    void newDamagesAllCriteriaHasAllFiltersNullTest() {
        var damagesAllCriteria = new DamagesAllCriteria();
        assertThat(damagesAllCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void damagesAllCriteriaFluentMethodsCreatesFiltersTest() {
        var damagesAllCriteria = new DamagesAllCriteria();

        setAllFilters(damagesAllCriteria);

        assertThat(damagesAllCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void damagesAllCriteriaCopyCreatesNullFilterTest() {
        var damagesAllCriteria = new DamagesAllCriteria();
        var copy = damagesAllCriteria.copy();

        assertThat(damagesAllCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(damagesAllCriteria)
        );
    }

    @Test
    void damagesAllCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var damagesAllCriteria = new DamagesAllCriteria();
        setAllFilters(damagesAllCriteria);

        var copy = damagesAllCriteria.copy();

        assertThat(damagesAllCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(damagesAllCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var damagesAllCriteria = new DamagesAllCriteria();

        assertThat(damagesAllCriteria).hasToString("DamagesAllCriteria{}");
    }

    private static void setAllFilters(DamagesAllCriteria damagesAllCriteria) {
        damagesAllCriteria.id();
        damagesAllCriteria.damageName();
        damagesAllCriteria.damageCode();
        damagesAllCriteria.damageFamily();
        damagesAllCriteria.damageGenus();
        damagesAllCriteria.damageSpecies();
        damagesAllCriteria.createdAt();
        damagesAllCriteria.addedBy();
        damagesAllCriteria.damageCategoryId();
        damagesAllCriteria.damageTypeId();
        damagesAllCriteria.distinct();
    }

    private static Condition<DamagesAllCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<DamagesAllCriteria> copyFiltersAre(DamagesAllCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
