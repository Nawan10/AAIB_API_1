package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CultivatedCropLandSeasonCriteriaTest {

    @Test
    void newCultivatedCropLandSeasonCriteriaHasAllFiltersNullTest() {
        var cultivatedCropLandSeasonCriteria = new CultivatedCropLandSeasonCriteria();
        assertThat(cultivatedCropLandSeasonCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cultivatedCropLandSeasonCriteriaFluentMethodsCreatesFiltersTest() {
        var cultivatedCropLandSeasonCriteria = new CultivatedCropLandSeasonCriteria();

        setAllFilters(cultivatedCropLandSeasonCriteria);

        assertThat(cultivatedCropLandSeasonCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cultivatedCropLandSeasonCriteriaCopyCreatesNullFilterTest() {
        var cultivatedCropLandSeasonCriteria = new CultivatedCropLandSeasonCriteria();
        var copy = cultivatedCropLandSeasonCriteria.copy();

        assertThat(cultivatedCropLandSeasonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedCropLandSeasonCriteria)
        );
    }

    @Test
    void cultivatedCropLandSeasonCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cultivatedCropLandSeasonCriteria = new CultivatedCropLandSeasonCriteria();
        setAllFilters(cultivatedCropLandSeasonCriteria);

        var copy = cultivatedCropLandSeasonCriteria.copy();

        assertThat(cultivatedCropLandSeasonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedCropLandSeasonCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cultivatedCropLandSeasonCriteria = new CultivatedCropLandSeasonCriteria();

        assertThat(cultivatedCropLandSeasonCriteria).hasToString("CultivatedCropLandSeasonCriteria{}");
    }

    private static void setAllFilters(CultivatedCropLandSeasonCriteria cultivatedCropLandSeasonCriteria) {
        cultivatedCropLandSeasonCriteria.id();
        cultivatedCropLandSeasonCriteria.name();
        cultivatedCropLandSeasonCriteria.period();
        cultivatedCropLandSeasonCriteria.distinct();
    }

    private static Condition<CultivatedCropLandSeasonCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getPeriod()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CultivatedCropLandSeasonCriteria> copyFiltersAre(
        CultivatedCropLandSeasonCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getPeriod(), copy.getPeriod()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
