package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CultivatedLandDamageReasonCriteriaTest {

    @Test
    void newCultivatedLandDamageReasonCriteriaHasAllFiltersNullTest() {
        var cultivatedLandDamageReasonCriteria = new CultivatedLandDamageReasonCriteria();
        assertThat(cultivatedLandDamageReasonCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cultivatedLandDamageReasonCriteriaFluentMethodsCreatesFiltersTest() {
        var cultivatedLandDamageReasonCriteria = new CultivatedLandDamageReasonCriteria();

        setAllFilters(cultivatedLandDamageReasonCriteria);

        assertThat(cultivatedLandDamageReasonCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cultivatedLandDamageReasonCriteriaCopyCreatesNullFilterTest() {
        var cultivatedLandDamageReasonCriteria = new CultivatedLandDamageReasonCriteria();
        var copy = cultivatedLandDamageReasonCriteria.copy();

        assertThat(cultivatedLandDamageReasonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandDamageReasonCriteria)
        );
    }

    @Test
    void cultivatedLandDamageReasonCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cultivatedLandDamageReasonCriteria = new CultivatedLandDamageReasonCriteria();
        setAllFilters(cultivatedLandDamageReasonCriteria);

        var copy = cultivatedLandDamageReasonCriteria.copy();

        assertThat(cultivatedLandDamageReasonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandDamageReasonCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cultivatedLandDamageReasonCriteria = new CultivatedLandDamageReasonCriteria();

        assertThat(cultivatedLandDamageReasonCriteria).hasToString("CultivatedLandDamageReasonCriteria{}");
    }

    private static void setAllFilters(CultivatedLandDamageReasonCriteria cultivatedLandDamageReasonCriteria) {
        cultivatedLandDamageReasonCriteria.id();
        cultivatedLandDamageReasonCriteria.name();
        cultivatedLandDamageReasonCriteria.damageCategoryId();
        cultivatedLandDamageReasonCriteria.damageTypeId();
        cultivatedLandDamageReasonCriteria.distinct();
    }

    private static Condition<CultivatedLandDamageReasonCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDamageCategoryId()) &&
                condition.apply(criteria.getDamageTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CultivatedLandDamageReasonCriteria> copyFiltersAre(
        CultivatedLandDamageReasonCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDamageCategoryId(), copy.getDamageCategoryId()) &&
                condition.apply(criteria.getDamageTypeId(), copy.getDamageTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
