package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CropDurationCriteriaTest {

    @Test
    void newCropDurationCriteriaHasAllFiltersNullTest() {
        var cropDurationCriteria = new CropDurationCriteria();
        assertThat(cropDurationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cropDurationCriteriaFluentMethodsCreatesFiltersTest() {
        var cropDurationCriteria = new CropDurationCriteria();

        setAllFilters(cropDurationCriteria);

        assertThat(cropDurationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cropDurationCriteriaCopyCreatesNullFilterTest() {
        var cropDurationCriteria = new CropDurationCriteria();
        var copy = cropDurationCriteria.copy();

        assertThat(cropDurationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cropDurationCriteria)
        );
    }

    @Test
    void cropDurationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cropDurationCriteria = new CropDurationCriteria();
        setAllFilters(cropDurationCriteria);

        var copy = cropDurationCriteria.copy();

        assertThat(cropDurationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cropDurationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cropDurationCriteria = new CropDurationCriteria();

        assertThat(cropDurationCriteria).hasToString("CropDurationCriteria{}");
    }

    private static void setAllFilters(CropDurationCriteria cropDurationCriteria) {
        cropDurationCriteria.id();
        cropDurationCriteria.duration();
        cropDurationCriteria.name();
        cropDurationCriteria.stages();
        cropDurationCriteria.addedBy();
        cropDurationCriteria.addedDate();
        cropDurationCriteria.cropId();
        cropDurationCriteria.distinct();
    }

    private static Condition<CropDurationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDuration()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getStages()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getAddedDate()) &&
                condition.apply(criteria.getCropId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CropDurationCriteria> copyFiltersAre(
        CropDurationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDuration(), copy.getDuration()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getStages(), copy.getStages()) &&
                condition.apply(criteria.getAddedBy(), copy.getAddedBy()) &&
                condition.apply(criteria.getAddedDate(), copy.getAddedDate()) &&
                condition.apply(criteria.getCropId(), copy.getCropId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
