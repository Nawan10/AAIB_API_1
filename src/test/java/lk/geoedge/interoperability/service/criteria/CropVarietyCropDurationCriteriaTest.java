package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CropVarietyCropDurationCriteriaTest {

    @Test
    void newCropVarietyCropDurationCriteriaHasAllFiltersNullTest() {
        var cropVarietyCropDurationCriteria = new CropVarietyCropDurationCriteria();
        assertThat(cropVarietyCropDurationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cropVarietyCropDurationCriteriaFluentMethodsCreatesFiltersTest() {
        var cropVarietyCropDurationCriteria = new CropVarietyCropDurationCriteria();

        setAllFilters(cropVarietyCropDurationCriteria);

        assertThat(cropVarietyCropDurationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cropVarietyCropDurationCriteriaCopyCreatesNullFilterTest() {
        var cropVarietyCropDurationCriteria = new CropVarietyCropDurationCriteria();
        var copy = cropVarietyCropDurationCriteria.copy();

        assertThat(cropVarietyCropDurationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cropVarietyCropDurationCriteria)
        );
    }

    @Test
    void cropVarietyCropDurationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cropVarietyCropDurationCriteria = new CropVarietyCropDurationCriteria();
        setAllFilters(cropVarietyCropDurationCriteria);

        var copy = cropVarietyCropDurationCriteria.copy();

        assertThat(cropVarietyCropDurationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cropVarietyCropDurationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cropVarietyCropDurationCriteria = new CropVarietyCropDurationCriteria();

        assertThat(cropVarietyCropDurationCriteria).hasToString("CropVarietyCropDurationCriteria{}");
    }

    private static void setAllFilters(CropVarietyCropDurationCriteria cropVarietyCropDurationCriteria) {
        cropVarietyCropDurationCriteria.id();
        cropVarietyCropDurationCriteria.duration();
        cropVarietyCropDurationCriteria.name();
        cropVarietyCropDurationCriteria.stages();
        cropVarietyCropDurationCriteria.addedBy();
        cropVarietyCropDurationCriteria.addedDate();
        cropVarietyCropDurationCriteria.distinct();
    }

    private static Condition<CropVarietyCropDurationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDuration()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getStages()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getAddedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CropVarietyCropDurationCriteria> copyFiltersAre(
        CropVarietyCropDurationCriteria copy,
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
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
