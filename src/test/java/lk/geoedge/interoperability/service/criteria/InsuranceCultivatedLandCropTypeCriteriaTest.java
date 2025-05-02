package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InsuranceCultivatedLandCropTypeCriteriaTest {

    @Test
    void newInsuranceCultivatedLandCropTypeCriteriaHasAllFiltersNullTest() {
        var insuranceCultivatedLandCropTypeCriteria = new InsuranceCultivatedLandCropTypeCriteria();
        assertThat(insuranceCultivatedLandCropTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void insuranceCultivatedLandCropTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var insuranceCultivatedLandCropTypeCriteria = new InsuranceCultivatedLandCropTypeCriteria();

        setAllFilters(insuranceCultivatedLandCropTypeCriteria);

        assertThat(insuranceCultivatedLandCropTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void insuranceCultivatedLandCropTypeCriteriaCopyCreatesNullFilterTest() {
        var insuranceCultivatedLandCropTypeCriteria = new InsuranceCultivatedLandCropTypeCriteria();
        var copy = insuranceCultivatedLandCropTypeCriteria.copy();

        assertThat(insuranceCultivatedLandCropTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(insuranceCultivatedLandCropTypeCriteria)
        );
    }

    @Test
    void insuranceCultivatedLandCropTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var insuranceCultivatedLandCropTypeCriteria = new InsuranceCultivatedLandCropTypeCriteria();
        setAllFilters(insuranceCultivatedLandCropTypeCriteria);

        var copy = insuranceCultivatedLandCropTypeCriteria.copy();

        assertThat(insuranceCultivatedLandCropTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(insuranceCultivatedLandCropTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var insuranceCultivatedLandCropTypeCriteria = new InsuranceCultivatedLandCropTypeCriteria();

        assertThat(insuranceCultivatedLandCropTypeCriteria).hasToString("InsuranceCultivatedLandCropTypeCriteria{}");
    }

    private static void setAllFilters(InsuranceCultivatedLandCropTypeCriteria insuranceCultivatedLandCropTypeCriteria) {
        insuranceCultivatedLandCropTypeCriteria.id();
        insuranceCultivatedLandCropTypeCriteria.crop();
        insuranceCultivatedLandCropTypeCriteria.image();
        insuranceCultivatedLandCropTypeCriteria.mainCrop();
        insuranceCultivatedLandCropTypeCriteria.cropCode();
        insuranceCultivatedLandCropTypeCriteria.noOfStages();
        insuranceCultivatedLandCropTypeCriteria.description();
        insuranceCultivatedLandCropTypeCriteria.cropTypesId();
        insuranceCultivatedLandCropTypeCriteria.unitsId();
        insuranceCultivatedLandCropTypeCriteria.area();
        insuranceCultivatedLandCropTypeCriteria.sumInsured();
        insuranceCultivatedLandCropTypeCriteria.minSumInsured();
        insuranceCultivatedLandCropTypeCriteria.maxSumInsured();
        insuranceCultivatedLandCropTypeCriteria.subsidisedPremiumRate();
        insuranceCultivatedLandCropTypeCriteria.distinct();
    }

    private static Condition<InsuranceCultivatedLandCropTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<InsuranceCultivatedLandCropTypeCriteria> copyFiltersAre(
        InsuranceCultivatedLandCropTypeCriteria copy,
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
