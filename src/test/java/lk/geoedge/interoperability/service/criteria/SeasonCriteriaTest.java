package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SeasonCriteriaTest {

    @Test
    void newSeasonCriteriaHasAllFiltersNullTest() {
        var seasonCriteria = new SeasonCriteria();
        assertThat(seasonCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void seasonCriteriaFluentMethodsCreatesFiltersTest() {
        var seasonCriteria = new SeasonCriteria();

        setAllFilters(seasonCriteria);

        assertThat(seasonCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void seasonCriteriaCopyCreatesNullFilterTest() {
        var seasonCriteria = new SeasonCriteria();
        var copy = seasonCriteria.copy();

        assertThat(seasonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(seasonCriteria)
        );
    }

    @Test
    void seasonCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var seasonCriteria = new SeasonCriteria();
        setAllFilters(seasonCriteria);

        var copy = seasonCriteria.copy();

        assertThat(seasonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(seasonCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var seasonCriteria = new SeasonCriteria();

        assertThat(seasonCriteria).hasToString("SeasonCriteria{}");
    }

    private static void setAllFilters(SeasonCriteria seasonCriteria) {
        seasonCriteria.id();
        seasonCriteria.name();
        seasonCriteria.period();
        seasonCriteria.distinct();
    }

    private static Condition<SeasonCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getPeriod()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SeasonCriteria> copyFiltersAre(SeasonCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
