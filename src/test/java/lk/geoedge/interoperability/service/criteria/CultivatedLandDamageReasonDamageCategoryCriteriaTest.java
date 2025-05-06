package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CultivatedLandDamageReasonDamageCategoryCriteriaTest {

    @Test
    void newCultivatedLandDamageReasonDamageCategoryCriteriaHasAllFiltersNullTest() {
        var cultivatedLandDamageReasonDamageCategoryCriteria = new CultivatedLandDamageReasonDamageCategoryCriteria();
        assertThat(cultivatedLandDamageReasonDamageCategoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cultivatedLandDamageReasonDamageCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var cultivatedLandDamageReasonDamageCategoryCriteria = new CultivatedLandDamageReasonDamageCategoryCriteria();

        setAllFilters(cultivatedLandDamageReasonDamageCategoryCriteria);

        assertThat(cultivatedLandDamageReasonDamageCategoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cultivatedLandDamageReasonDamageCategoryCriteriaCopyCreatesNullFilterTest() {
        var cultivatedLandDamageReasonDamageCategoryCriteria = new CultivatedLandDamageReasonDamageCategoryCriteria();
        var copy = cultivatedLandDamageReasonDamageCategoryCriteria.copy();

        assertThat(cultivatedLandDamageReasonDamageCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandDamageReasonDamageCategoryCriteria)
        );
    }

    @Test
    void cultivatedLandDamageReasonDamageCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cultivatedLandDamageReasonDamageCategoryCriteria = new CultivatedLandDamageReasonDamageCategoryCriteria();
        setAllFilters(cultivatedLandDamageReasonDamageCategoryCriteria);

        var copy = cultivatedLandDamageReasonDamageCategoryCriteria.copy();

        assertThat(cultivatedLandDamageReasonDamageCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandDamageReasonDamageCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cultivatedLandDamageReasonDamageCategoryCriteria = new CultivatedLandDamageReasonDamageCategoryCriteria();

        assertThat(cultivatedLandDamageReasonDamageCategoryCriteria).hasToString("CultivatedLandDamageReasonDamageCategoryCriteria{}");
    }

    private static void setAllFilters(CultivatedLandDamageReasonDamageCategoryCriteria cultivatedLandDamageReasonDamageCategoryCriteria) {
        cultivatedLandDamageReasonDamageCategoryCriteria.id();
        cultivatedLandDamageReasonDamageCategoryCriteria.categoryName();
        cultivatedLandDamageReasonDamageCategoryCriteria.distinct();
    }

    private static Condition<CultivatedLandDamageReasonDamageCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) && condition.apply(criteria.getCategoryName()) && condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CultivatedLandDamageReasonDamageCategoryCriteria> copyFiltersAre(
        CultivatedLandDamageReasonDamageCategoryCriteria copy,
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
