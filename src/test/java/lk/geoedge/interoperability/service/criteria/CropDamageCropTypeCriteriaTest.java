package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CropDamageCropTypeCriteriaTest {

    @Test
    void newCropDamageCropTypeCriteriaHasAllFiltersNullTest() {
        var cropDamageCropTypeCriteria = new CropDamageCropTypeCriteria();
        assertThat(cropDamageCropTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cropDamageCropTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var cropDamageCropTypeCriteria = new CropDamageCropTypeCriteria();

        setAllFilters(cropDamageCropTypeCriteria);

        assertThat(cropDamageCropTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cropDamageCropTypeCriteriaCopyCreatesNullFilterTest() {
        var cropDamageCropTypeCriteria = new CropDamageCropTypeCriteria();
        var copy = cropDamageCropTypeCriteria.copy();

        assertThat(cropDamageCropTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cropDamageCropTypeCriteria)
        );
    }

    @Test
    void cropDamageCropTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cropDamageCropTypeCriteria = new CropDamageCropTypeCriteria();
        setAllFilters(cropDamageCropTypeCriteria);

        var copy = cropDamageCropTypeCriteria.copy();

        assertThat(cropDamageCropTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cropDamageCropTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cropDamageCropTypeCriteria = new CropDamageCropTypeCriteria();

        assertThat(cropDamageCropTypeCriteria).hasToString("CropDamageCropTypeCriteria{}");
    }

    private static void setAllFilters(CropDamageCropTypeCriteria cropDamageCropTypeCriteria) {
        cropDamageCropTypeCriteria.id();
        cropDamageCropTypeCriteria.crop();
        cropDamageCropTypeCriteria.image();
        cropDamageCropTypeCriteria.mainCrop();
        cropDamageCropTypeCriteria.cropCode();
        cropDamageCropTypeCriteria.noOfStages();
        cropDamageCropTypeCriteria.description();
        cropDamageCropTypeCriteria.cropTypesId();
        cropDamageCropTypeCriteria.unitsId();
        cropDamageCropTypeCriteria.area();
        cropDamageCropTypeCriteria.sumInsured();
        cropDamageCropTypeCriteria.minSumInsured();
        cropDamageCropTypeCriteria.maxSumInsured();
        cropDamageCropTypeCriteria.subsidisedPremiumRate();
        cropDamageCropTypeCriteria.distinct();
    }

    private static Condition<CropDamageCropTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CropDamageCropTypeCriteria> copyFiltersAre(
        CropDamageCropTypeCriteria copy,
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
