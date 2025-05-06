package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CropDurationCropTypeCriteriaTest {

    @Test
    void newCropDurationCropTypeCriteriaHasAllFiltersNullTest() {
        var cropDurationCropTypeCriteria = new CropDurationCropTypeCriteria();
        assertThat(cropDurationCropTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cropDurationCropTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var cropDurationCropTypeCriteria = new CropDurationCropTypeCriteria();

        setAllFilters(cropDurationCropTypeCriteria);

        assertThat(cropDurationCropTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cropDurationCropTypeCriteriaCopyCreatesNullFilterTest() {
        var cropDurationCropTypeCriteria = new CropDurationCropTypeCriteria();
        var copy = cropDurationCropTypeCriteria.copy();

        assertThat(cropDurationCropTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cropDurationCropTypeCriteria)
        );
    }

    @Test
    void cropDurationCropTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cropDurationCropTypeCriteria = new CropDurationCropTypeCriteria();
        setAllFilters(cropDurationCropTypeCriteria);

        var copy = cropDurationCropTypeCriteria.copy();

        assertThat(cropDurationCropTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cropDurationCropTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cropDurationCropTypeCriteria = new CropDurationCropTypeCriteria();

        assertThat(cropDurationCropTypeCriteria).hasToString("CropDurationCropTypeCriteria{}");
    }

    private static void setAllFilters(CropDurationCropTypeCriteria cropDurationCropTypeCriteria) {
        cropDurationCropTypeCriteria.id();
        cropDurationCropTypeCriteria.crop();
        cropDurationCropTypeCriteria.image();
        cropDurationCropTypeCriteria.mainCrop();
        cropDurationCropTypeCriteria.cropCode();
        cropDurationCropTypeCriteria.noOfStages();
        cropDurationCropTypeCriteria.description();
        cropDurationCropTypeCriteria.cropTypesId();
        cropDurationCropTypeCriteria.unitsId();
        cropDurationCropTypeCriteria.area();
        cropDurationCropTypeCriteria.sumInsured();
        cropDurationCropTypeCriteria.minSumInsured();
        cropDurationCropTypeCriteria.maxSumInsured();
        cropDurationCropTypeCriteria.subsidisedPremiumRate();
        cropDurationCropTypeCriteria.distinct();
    }

    private static Condition<CropDurationCropTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCrop()) &&
                condition.apply(criteria.getImage()) &&
                condition.apply(criteria.getMainCrop()) &&
                condition.apply(criteria.getCropCode()) &&
                condition.apply(criteria.getNoOfStages()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getCropTypesId()) &&
                condition.apply(criteria.getUnitsId()) &&
                condition.apply(criteria.getArea()) &&
                condition.apply(criteria.getSumInsured()) &&
                condition.apply(criteria.getMinSumInsured()) &&
                condition.apply(criteria.getMaxSumInsured()) &&
                condition.apply(criteria.getSubsidisedPremiumRate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CropDurationCropTypeCriteria> copyFiltersAre(
        CropDurationCropTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCrop(), copy.getCrop()) &&
                condition.apply(criteria.getImage(), copy.getImage()) &&
                condition.apply(criteria.getMainCrop(), copy.getMainCrop()) &&
                condition.apply(criteria.getCropCode(), copy.getCropCode()) &&
                condition.apply(criteria.getNoOfStages(), copy.getNoOfStages()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getCropTypesId(), copy.getCropTypesId()) &&
                condition.apply(criteria.getUnitsId(), copy.getUnitsId()) &&
                condition.apply(criteria.getArea(), copy.getArea()) &&
                condition.apply(criteria.getSumInsured(), copy.getSumInsured()) &&
                condition.apply(criteria.getMinSumInsured(), copy.getMinSumInsured()) &&
                condition.apply(criteria.getMaxSumInsured(), copy.getMaxSumInsured()) &&
                condition.apply(criteria.getSubsidisedPremiumRate(), copy.getSubsidisedPremiumRate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
