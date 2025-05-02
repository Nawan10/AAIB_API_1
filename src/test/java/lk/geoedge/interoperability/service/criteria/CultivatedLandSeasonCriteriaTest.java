package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CultivatedLandSeasonCriteriaTest {

    @Test
    void newCultivatedLandSeasonCriteriaHasAllFiltersNullTest() {
        var cultivatedLandSeasonCriteria = new CultivatedLandSeasonCriteria();
        assertThat(cultivatedLandSeasonCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cultivatedLandSeasonCriteriaFluentMethodsCreatesFiltersTest() {
        var cultivatedLandSeasonCriteria = new CultivatedLandSeasonCriteria();

        setAllFilters(cultivatedLandSeasonCriteria);

        assertThat(cultivatedLandSeasonCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cultivatedLandSeasonCriteriaCopyCreatesNullFilterTest() {
        var cultivatedLandSeasonCriteria = new CultivatedLandSeasonCriteria();
        var copy = cultivatedLandSeasonCriteria.copy();

        assertThat(cultivatedLandSeasonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandSeasonCriteria)
        );
    }

    @Test
    void cultivatedLandSeasonCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cultivatedLandSeasonCriteria = new CultivatedLandSeasonCriteria();
        setAllFilters(cultivatedLandSeasonCriteria);

        var copy = cultivatedLandSeasonCriteria.copy();

        assertThat(cultivatedLandSeasonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandSeasonCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cultivatedLandSeasonCriteria = new CultivatedLandSeasonCriteria();

        assertThat(cultivatedLandSeasonCriteria).hasToString("CultivatedLandSeasonCriteria{}");
    }

    private static void setAllFilters(CultivatedLandSeasonCriteria cultivatedLandSeasonCriteria) {
        cultivatedLandSeasonCriteria.id();
        cultivatedLandSeasonCriteria.name();
        cultivatedLandSeasonCriteria.period();
        cultivatedLandSeasonCriteria.distinct();
    }

    private static Condition<CultivatedLandSeasonCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getPeriod()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CultivatedLandSeasonCriteria> copyFiltersAre(
        CultivatedLandSeasonCriteria copy,
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
