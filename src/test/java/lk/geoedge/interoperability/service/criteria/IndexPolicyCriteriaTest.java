package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class IndexPolicyCriteriaTest {

    @Test
    void newIndexPolicyCriteriaHasAllFiltersNullTest() {
        var indexPolicyCriteria = new IndexPolicyCriteria();
        assertThat(indexPolicyCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void indexPolicyCriteriaFluentMethodsCreatesFiltersTest() {
        var indexPolicyCriteria = new IndexPolicyCriteria();

        setAllFilters(indexPolicyCriteria);

        assertThat(indexPolicyCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void indexPolicyCriteriaCopyCreatesNullFilterTest() {
        var indexPolicyCriteria = new IndexPolicyCriteria();
        var copy = indexPolicyCriteria.copy();

        assertThat(indexPolicyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(indexPolicyCriteria)
        );
    }

    @Test
    void indexPolicyCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var indexPolicyCriteria = new IndexPolicyCriteria();
        setAllFilters(indexPolicyCriteria);

        var copy = indexPolicyCriteria.copy();

        assertThat(indexPolicyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(indexPolicyCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var indexPolicyCriteria = new IndexPolicyCriteria();

        assertThat(indexPolicyCriteria).hasToString("IndexPolicyCriteria{}");
    }

    private static void setAllFilters(IndexPolicyCriteria indexPolicyCriteria) {
        indexPolicyCriteria.id();
        indexPolicyCriteria.startDate();
        indexPolicyCriteria.endDate();
        indexPolicyCriteria.stageNo();
        indexPolicyCriteria.indexStatus();
        indexPolicyCriteria.policyId();
        indexPolicyCriteria.seasonId();
        indexPolicyCriteria.cropVarietyId();
        indexPolicyCriteria.cropId();
        indexPolicyCriteria.weatherStationId();
        indexPolicyCriteria.distinct();
    }

    private static Condition<IndexPolicyCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getStageNo()) &&
                condition.apply(criteria.getIndexStatus()) &&
                condition.apply(criteria.getPolicyId()) &&
                condition.apply(criteria.getSeasonId()) &&
                condition.apply(criteria.getCropVarietyId()) &&
                condition.apply(criteria.getCropId()) &&
                condition.apply(criteria.getWeatherStationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<IndexPolicyCriteria> copyFiltersAre(IndexPolicyCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getStageNo(), copy.getStageNo()) &&
                condition.apply(criteria.getIndexStatus(), copy.getIndexStatus()) &&
                condition.apply(criteria.getPolicyId(), copy.getPolicyId()) &&
                condition.apply(criteria.getSeasonId(), copy.getSeasonId()) &&
                condition.apply(criteria.getCropVarietyId(), copy.getCropVarietyId()) &&
                condition.apply(criteria.getCropId(), copy.getCropId()) &&
                condition.apply(criteria.getWeatherStationId(), copy.getWeatherStationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
