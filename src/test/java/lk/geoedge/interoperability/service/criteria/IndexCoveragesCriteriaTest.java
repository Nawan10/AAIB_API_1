package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class IndexCoveragesCriteriaTest {

    @Test
    void newIndexCoveragesCriteriaHasAllFiltersNullTest() {
        var indexCoveragesCriteria = new IndexCoveragesCriteria();
        assertThat(indexCoveragesCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void indexCoveragesCriteriaFluentMethodsCreatesFiltersTest() {
        var indexCoveragesCriteria = new IndexCoveragesCriteria();

        setAllFilters(indexCoveragesCriteria);

        assertThat(indexCoveragesCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void indexCoveragesCriteriaCopyCreatesNullFilterTest() {
        var indexCoveragesCriteria = new IndexCoveragesCriteria();
        var copy = indexCoveragesCriteria.copy();

        assertThat(indexCoveragesCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(indexCoveragesCriteria)
        );
    }

    @Test
    void indexCoveragesCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var indexCoveragesCriteria = new IndexCoveragesCriteria();
        setAllFilters(indexCoveragesCriteria);

        var copy = indexCoveragesCriteria.copy();

        assertThat(indexCoveragesCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(indexCoveragesCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var indexCoveragesCriteria = new IndexCoveragesCriteria();

        assertThat(indexCoveragesCriteria).hasToString("IndexCoveragesCriteria{}");
    }

    private static void setAllFilters(IndexCoveragesCriteria indexCoveragesCriteria) {
        indexCoveragesCriteria.id();
        indexCoveragesCriteria.indexProductId();
        indexCoveragesCriteria.premiumRate();
        indexCoveragesCriteria.isFree();
        indexCoveragesCriteria.isPaid();
        indexCoveragesCriteria.damageReasonId();
        indexCoveragesCriteria.distinct();
    }

    private static Condition<IndexCoveragesCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getIndexProductId()) &&
                condition.apply(criteria.getPremiumRate()) &&
                condition.apply(criteria.getIsFree()) &&
                condition.apply(criteria.getIsPaid()) &&
                condition.apply(criteria.getDamageReasonId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<IndexCoveragesCriteria> copyFiltersAre(
        IndexCoveragesCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getIndexProductId(), copy.getIndexProductId()) &&
                condition.apply(criteria.getPremiumRate(), copy.getPremiumRate()) &&
                condition.apply(criteria.getIsFree(), copy.getIsFree()) &&
                condition.apply(criteria.getIsPaid(), copy.getIsPaid()) &&
                condition.apply(criteria.getDamageReasonId(), copy.getDamageReasonId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
