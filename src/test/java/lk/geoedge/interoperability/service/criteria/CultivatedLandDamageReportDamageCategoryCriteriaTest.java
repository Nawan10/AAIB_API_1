package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CultivatedLandDamageReportDamageCategoryCriteriaTest {

    @Test
    void newCultivatedLandDamageReportDamageCategoryCriteriaHasAllFiltersNullTest() {
        var cultivatedLandDamageReportDamageCategoryCriteria = new CultivatedLandDamageReportDamageCategoryCriteria();
        assertThat(cultivatedLandDamageReportDamageCategoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cultivatedLandDamageReportDamageCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var cultivatedLandDamageReportDamageCategoryCriteria = new CultivatedLandDamageReportDamageCategoryCriteria();

        setAllFilters(cultivatedLandDamageReportDamageCategoryCriteria);

        assertThat(cultivatedLandDamageReportDamageCategoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cultivatedLandDamageReportDamageCategoryCriteriaCopyCreatesNullFilterTest() {
        var cultivatedLandDamageReportDamageCategoryCriteria = new CultivatedLandDamageReportDamageCategoryCriteria();
        var copy = cultivatedLandDamageReportDamageCategoryCriteria.copy();

        assertThat(cultivatedLandDamageReportDamageCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandDamageReportDamageCategoryCriteria)
        );
    }

    @Test
    void cultivatedLandDamageReportDamageCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cultivatedLandDamageReportDamageCategoryCriteria = new CultivatedLandDamageReportDamageCategoryCriteria();
        setAllFilters(cultivatedLandDamageReportDamageCategoryCriteria);

        var copy = cultivatedLandDamageReportDamageCategoryCriteria.copy();

        assertThat(cultivatedLandDamageReportDamageCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandDamageReportDamageCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cultivatedLandDamageReportDamageCategoryCriteria = new CultivatedLandDamageReportDamageCategoryCriteria();

        assertThat(cultivatedLandDamageReportDamageCategoryCriteria).hasToString("CultivatedLandDamageReportDamageCategoryCriteria{}");
    }

    private static void setAllFilters(CultivatedLandDamageReportDamageCategoryCriteria cultivatedLandDamageReportDamageCategoryCriteria) {
        cultivatedLandDamageReportDamageCategoryCriteria.id();
        cultivatedLandDamageReportDamageCategoryCriteria.categoryName();
        cultivatedLandDamageReportDamageCategoryCriteria.distinct();
    }

    private static Condition<CultivatedLandDamageReportDamageCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) && condition.apply(criteria.getCategoryName()) && condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CultivatedLandDamageReportDamageCategoryCriteria> copyFiltersAre(
        CultivatedLandDamageReportDamageCategoryCriteria copy,
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
