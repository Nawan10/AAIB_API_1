package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CultivatedLandDamageReportDamageTypeCriteriaTest {

    @Test
    void newCultivatedLandDamageReportDamageTypeCriteriaHasAllFiltersNullTest() {
        var cultivatedLandDamageReportDamageTypeCriteria = new CultivatedLandDamageReportDamageTypeCriteria();
        assertThat(cultivatedLandDamageReportDamageTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cultivatedLandDamageReportDamageTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var cultivatedLandDamageReportDamageTypeCriteria = new CultivatedLandDamageReportDamageTypeCriteria();

        setAllFilters(cultivatedLandDamageReportDamageTypeCriteria);

        assertThat(cultivatedLandDamageReportDamageTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cultivatedLandDamageReportDamageTypeCriteriaCopyCreatesNullFilterTest() {
        var cultivatedLandDamageReportDamageTypeCriteria = new CultivatedLandDamageReportDamageTypeCriteria();
        var copy = cultivatedLandDamageReportDamageTypeCriteria.copy();

        assertThat(cultivatedLandDamageReportDamageTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandDamageReportDamageTypeCriteria)
        );
    }

    @Test
    void cultivatedLandDamageReportDamageTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cultivatedLandDamageReportDamageTypeCriteria = new CultivatedLandDamageReportDamageTypeCriteria();
        setAllFilters(cultivatedLandDamageReportDamageTypeCriteria);

        var copy = cultivatedLandDamageReportDamageTypeCriteria.copy();

        assertThat(cultivatedLandDamageReportDamageTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandDamageReportDamageTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cultivatedLandDamageReportDamageTypeCriteria = new CultivatedLandDamageReportDamageTypeCriteria();

        assertThat(cultivatedLandDamageReportDamageTypeCriteria).hasToString("CultivatedLandDamageReportDamageTypeCriteria{}");
    }

    private static void setAllFilters(CultivatedLandDamageReportDamageTypeCriteria cultivatedLandDamageReportDamageTypeCriteria) {
        cultivatedLandDamageReportDamageTypeCriteria.id();
        cultivatedLandDamageReportDamageTypeCriteria.typeName();
        cultivatedLandDamageReportDamageTypeCriteria.distinct();
    }

    private static Condition<CultivatedLandDamageReportDamageTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) && condition.apply(criteria.getTypeName()) && condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CultivatedLandDamageReportDamageTypeCriteria> copyFiltersAre(
        CultivatedLandDamageReportDamageTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTypeName(), copy.getTypeName()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
