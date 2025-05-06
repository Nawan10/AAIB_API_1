package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class IndexPolicySeasonCriteriaTest {

    @Test
    void newIndexPolicySeasonCriteriaHasAllFiltersNullTest() {
        var indexPolicySeasonCriteria = new IndexPolicySeasonCriteria();
        assertThat(indexPolicySeasonCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void indexPolicySeasonCriteriaFluentMethodsCreatesFiltersTest() {
        var indexPolicySeasonCriteria = new IndexPolicySeasonCriteria();

        setAllFilters(indexPolicySeasonCriteria);

        assertThat(indexPolicySeasonCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void indexPolicySeasonCriteriaCopyCreatesNullFilterTest() {
        var indexPolicySeasonCriteria = new IndexPolicySeasonCriteria();
        var copy = indexPolicySeasonCriteria.copy();

        assertThat(indexPolicySeasonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(indexPolicySeasonCriteria)
        );
    }

    @Test
    void indexPolicySeasonCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var indexPolicySeasonCriteria = new IndexPolicySeasonCriteria();
        setAllFilters(indexPolicySeasonCriteria);

        var copy = indexPolicySeasonCriteria.copy();

        assertThat(indexPolicySeasonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(indexPolicySeasonCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var indexPolicySeasonCriteria = new IndexPolicySeasonCriteria();

        assertThat(indexPolicySeasonCriteria).hasToString("IndexPolicySeasonCriteria{}");
    }

    private static void setAllFilters(IndexPolicySeasonCriteria indexPolicySeasonCriteria) {
        indexPolicySeasonCriteria.id();
        indexPolicySeasonCriteria.name();
        indexPolicySeasonCriteria.period();
        indexPolicySeasonCriteria.distinct();
    }

    private static Condition<IndexPolicySeasonCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getPeriod()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<IndexPolicySeasonCriteria> copyFiltersAre(
        IndexPolicySeasonCriteria copy,
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
