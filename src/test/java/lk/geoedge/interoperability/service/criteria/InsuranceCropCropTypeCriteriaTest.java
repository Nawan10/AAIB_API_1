package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InsuranceCropCropTypeCriteriaTest {

    @Test
    void newInsuranceCropCropTypeCriteriaHasAllFiltersNullTest() {
        var insuranceCropCropTypeCriteria = new InsuranceCropCropTypeCriteria();
        assertThat(insuranceCropCropTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void insuranceCropCropTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var insuranceCropCropTypeCriteria = new InsuranceCropCropTypeCriteria();

        setAllFilters(insuranceCropCropTypeCriteria);

        assertThat(insuranceCropCropTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void insuranceCropCropTypeCriteriaCopyCreatesNullFilterTest() {
        var insuranceCropCropTypeCriteria = new InsuranceCropCropTypeCriteria();
        var copy = insuranceCropCropTypeCriteria.copy();

        assertThat(insuranceCropCropTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(insuranceCropCropTypeCriteria)
        );
    }

    @Test
    void insuranceCropCropTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var insuranceCropCropTypeCriteria = new InsuranceCropCropTypeCriteria();
        setAllFilters(insuranceCropCropTypeCriteria);

        var copy = insuranceCropCropTypeCriteria.copy();

        assertThat(insuranceCropCropTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(insuranceCropCropTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var insuranceCropCropTypeCriteria = new InsuranceCropCropTypeCriteria();

        assertThat(insuranceCropCropTypeCriteria).hasToString("InsuranceCropCropTypeCriteria{}");
    }

    private static void setAllFilters(InsuranceCropCropTypeCriteria insuranceCropCropTypeCriteria) {
        insuranceCropCropTypeCriteria.id();
        insuranceCropCropTypeCriteria.crop();
        insuranceCropCropTypeCriteria.image();
        insuranceCropCropTypeCriteria.mainCrop();
        insuranceCropCropTypeCriteria.cropCode();
        insuranceCropCropTypeCriteria.noOfStages();
        insuranceCropCropTypeCriteria.description();
        insuranceCropCropTypeCriteria.cropTypesId();
        insuranceCropCropTypeCriteria.unitsId();
        insuranceCropCropTypeCriteria.area();
        insuranceCropCropTypeCriteria.sumInsured();
        insuranceCropCropTypeCriteria.minSumInsured();
        insuranceCropCropTypeCriteria.maxSumInsured();
        insuranceCropCropTypeCriteria.subsidisedPremiumRate();
        insuranceCropCropTypeCriteria.distinct();
    }

    private static Condition<InsuranceCropCropTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<InsuranceCropCropTypeCriteria> copyFiltersAre(
        InsuranceCropCropTypeCriteria copy,
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
