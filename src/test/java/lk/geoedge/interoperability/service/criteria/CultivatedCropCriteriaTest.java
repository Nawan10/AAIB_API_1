package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CultivatedCropCriteriaTest {

    @Test
    void newCultivatedCropCriteriaHasAllFiltersNullTest() {
        var cultivatedCropCriteria = new CultivatedCropCriteria();
        assertThat(cultivatedCropCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cultivatedCropCriteriaFluentMethodsCreatesFiltersTest() {
        var cultivatedCropCriteria = new CultivatedCropCriteria();

        setAllFilters(cultivatedCropCriteria);

        assertThat(cultivatedCropCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cultivatedCropCriteriaCopyCreatesNullFilterTest() {
        var cultivatedCropCriteria = new CultivatedCropCriteria();
        var copy = cultivatedCropCriteria.copy();

        assertThat(cultivatedCropCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedCropCriteria)
        );
    }

    @Test
    void cultivatedCropCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cultivatedCropCriteria = new CultivatedCropCriteria();
        setAllFilters(cultivatedCropCriteria);

        var copy = cultivatedCropCriteria.copy();

        assertThat(cultivatedCropCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedCropCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cultivatedCropCriteria = new CultivatedCropCriteria();

        assertThat(cultivatedCropCriteria).hasToString("CultivatedCropCriteria{}");
    }

    private static void setAllFilters(CultivatedCropCriteria cultivatedCropCriteria) {
        cultivatedCropCriteria.id();
        cultivatedCropCriteria.cultivatedExtend();
        cultivatedCropCriteria.startDate();
        cultivatedCropCriteria.endDate();
        cultivatedCropCriteria.yield();
        cultivatedCropCriteria.unitId();
        cultivatedCropCriteria.createdAt();
        cultivatedCropCriteria.addedBy();
        cultivatedCropCriteria.cultivatedLandId();
        cultivatedCropCriteria.cropId();
        cultivatedCropCriteria.distinct();
    }

    private static Condition<CultivatedCropCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCultivatedExtend()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getYield()) &&
                condition.apply(criteria.getUnitId()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getCultivatedLandId()) &&
                condition.apply(criteria.getCropId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CultivatedCropCriteria> copyFiltersAre(
        CultivatedCropCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCultivatedExtend(), copy.getCultivatedExtend()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getYield(), copy.getYield()) &&
                condition.apply(criteria.getUnitId(), copy.getUnitId()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy(), copy.getAddedBy()) &&
                condition.apply(criteria.getCultivatedLandId(), copy.getCultivatedLandId()) &&
                condition.apply(criteria.getCropId(), copy.getCropId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
