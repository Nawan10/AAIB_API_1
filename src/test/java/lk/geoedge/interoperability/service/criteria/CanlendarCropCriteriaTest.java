package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CanlendarCropCriteriaTest {

    @Test
    void newCanlendarCropCriteriaHasAllFiltersNullTest() {
        var canlendarCropCriteria = new CanlendarCropCriteria();
        assertThat(canlendarCropCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void canlendarCropCriteriaFluentMethodsCreatesFiltersTest() {
        var canlendarCropCriteria = new CanlendarCropCriteria();

        setAllFilters(canlendarCropCriteria);

        assertThat(canlendarCropCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void canlendarCropCriteriaCopyCreatesNullFilterTest() {
        var canlendarCropCriteria = new CanlendarCropCriteria();
        var copy = canlendarCropCriteria.copy();

        assertThat(canlendarCropCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(canlendarCropCriteria)
        );
    }

    @Test
    void canlendarCropCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var canlendarCropCriteria = new CanlendarCropCriteria();
        setAllFilters(canlendarCropCriteria);

        var copy = canlendarCropCriteria.copy();

        assertThat(canlendarCropCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(canlendarCropCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var canlendarCropCriteria = new CanlendarCropCriteria();

        assertThat(canlendarCropCriteria).hasToString("CanlendarCropCriteria{}");
    }

    private static void setAllFilters(CanlendarCropCriteria canlendarCropCriteria) {
        canlendarCropCriteria.id();
        canlendarCropCriteria.startDate();
        canlendarCropCriteria.endDate();
        canlendarCropCriteria.percentage();
        canlendarCropCriteria.canlendarCropStatus();
        canlendarCropCriteria.reason();
        canlendarCropCriteria.createdAt();
        canlendarCropCriteria.addedBy();
        canlendarCropCriteria.seasonId();
        canlendarCropCriteria.cropId();
        canlendarCropCriteria.distinct();
    }

    private static Condition<CanlendarCropCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getPercentage()) &&
                condition.apply(criteria.getCanlendarCropStatus()) &&
                condition.apply(criteria.getReason()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getSeasonId()) &&
                condition.apply(criteria.getCropId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CanlendarCropCriteria> copyFiltersAre(
        CanlendarCropCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getPercentage(), copy.getPercentage()) &&
                condition.apply(criteria.getCanlendarCropStatus(), copy.getCanlendarCropStatus()) &&
                condition.apply(criteria.getReason(), copy.getReason()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy(), copy.getAddedBy()) &&
                condition.apply(criteria.getSeasonId(), copy.getSeasonId()) &&
                condition.apply(criteria.getCropId(), copy.getCropId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
