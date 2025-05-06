package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class IndexPayoutEventListSeasonCriteriaTest {

    @Test
    void newIndexPayoutEventListSeasonCriteriaHasAllFiltersNullTest() {
        var indexPayoutEventListSeasonCriteria = new IndexPayoutEventListSeasonCriteria();
        assertThat(indexPayoutEventListSeasonCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void indexPayoutEventListSeasonCriteriaFluentMethodsCreatesFiltersTest() {
        var indexPayoutEventListSeasonCriteria = new IndexPayoutEventListSeasonCriteria();

        setAllFilters(indexPayoutEventListSeasonCriteria);

        assertThat(indexPayoutEventListSeasonCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void indexPayoutEventListSeasonCriteriaCopyCreatesNullFilterTest() {
        var indexPayoutEventListSeasonCriteria = new IndexPayoutEventListSeasonCriteria();
        var copy = indexPayoutEventListSeasonCriteria.copy();

        assertThat(indexPayoutEventListSeasonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(indexPayoutEventListSeasonCriteria)
        );
    }

    @Test
    void indexPayoutEventListSeasonCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var indexPayoutEventListSeasonCriteria = new IndexPayoutEventListSeasonCriteria();
        setAllFilters(indexPayoutEventListSeasonCriteria);

        var copy = indexPayoutEventListSeasonCriteria.copy();

        assertThat(indexPayoutEventListSeasonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(indexPayoutEventListSeasonCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var indexPayoutEventListSeasonCriteria = new IndexPayoutEventListSeasonCriteria();

        assertThat(indexPayoutEventListSeasonCriteria).hasToString("IndexPayoutEventListSeasonCriteria{}");
    }

    private static void setAllFilters(IndexPayoutEventListSeasonCriteria indexPayoutEventListSeasonCriteria) {
        indexPayoutEventListSeasonCriteria.id();
        indexPayoutEventListSeasonCriteria.name();
        indexPayoutEventListSeasonCriteria.period();
        indexPayoutEventListSeasonCriteria.distinct();
    }

    private static Condition<IndexPayoutEventListSeasonCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getPeriod()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<IndexPayoutEventListSeasonCriteria> copyFiltersAre(
        IndexPayoutEventListSeasonCriteria copy,
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
