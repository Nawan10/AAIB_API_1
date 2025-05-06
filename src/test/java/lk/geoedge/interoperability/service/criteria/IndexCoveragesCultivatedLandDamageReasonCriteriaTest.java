package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class IndexCoveragesCultivatedLandDamageReasonCriteriaTest {

    @Test
    void newIndexCoveragesCultivatedLandDamageReasonCriteriaHasAllFiltersNullTest() {
        var indexCoveragesCultivatedLandDamageReasonCriteria = new IndexCoveragesCultivatedLandDamageReasonCriteria();
        assertThat(indexCoveragesCultivatedLandDamageReasonCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void indexCoveragesCultivatedLandDamageReasonCriteriaFluentMethodsCreatesFiltersTest() {
        var indexCoveragesCultivatedLandDamageReasonCriteria = new IndexCoveragesCultivatedLandDamageReasonCriteria();

        setAllFilters(indexCoveragesCultivatedLandDamageReasonCriteria);

        assertThat(indexCoveragesCultivatedLandDamageReasonCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void indexCoveragesCultivatedLandDamageReasonCriteriaCopyCreatesNullFilterTest() {
        var indexCoveragesCultivatedLandDamageReasonCriteria = new IndexCoveragesCultivatedLandDamageReasonCriteria();
        var copy = indexCoveragesCultivatedLandDamageReasonCriteria.copy();

        assertThat(indexCoveragesCultivatedLandDamageReasonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(indexCoveragesCultivatedLandDamageReasonCriteria)
        );
    }

    @Test
    void indexCoveragesCultivatedLandDamageReasonCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var indexCoveragesCultivatedLandDamageReasonCriteria = new IndexCoveragesCultivatedLandDamageReasonCriteria();
        setAllFilters(indexCoveragesCultivatedLandDamageReasonCriteria);

        var copy = indexCoveragesCultivatedLandDamageReasonCriteria.copy();

        assertThat(indexCoveragesCultivatedLandDamageReasonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(indexCoveragesCultivatedLandDamageReasonCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var indexCoveragesCultivatedLandDamageReasonCriteria = new IndexCoveragesCultivatedLandDamageReasonCriteria();

        assertThat(indexCoveragesCultivatedLandDamageReasonCriteria).hasToString("IndexCoveragesCultivatedLandDamageReasonCriteria{}");
    }

    private static void setAllFilters(IndexCoveragesCultivatedLandDamageReasonCriteria indexCoveragesCultivatedLandDamageReasonCriteria) {
        indexCoveragesCultivatedLandDamageReasonCriteria.id();
        indexCoveragesCultivatedLandDamageReasonCriteria.name();
        indexCoveragesCultivatedLandDamageReasonCriteria.distinct();
    }

    private static Condition<IndexCoveragesCultivatedLandDamageReasonCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria -> condition.apply(criteria.getId()) && condition.apply(criteria.getName()) && condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<IndexCoveragesCultivatedLandDamageReasonCriteria> copyFiltersAre(
        IndexCoveragesCultivatedLandDamageReasonCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
