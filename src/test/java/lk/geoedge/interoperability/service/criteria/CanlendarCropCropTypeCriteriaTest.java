package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CanlendarCropCropTypeCriteriaTest {

    @Test
    void newCanlendarCropCropTypeCriteriaHasAllFiltersNullTest() {
        var canlendarCropCropTypeCriteria = new CanlendarCropCropTypeCriteria();
        assertThat(canlendarCropCropTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void canlendarCropCropTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var canlendarCropCropTypeCriteria = new CanlendarCropCropTypeCriteria();

        setAllFilters(canlendarCropCropTypeCriteria);

        assertThat(canlendarCropCropTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void canlendarCropCropTypeCriteriaCopyCreatesNullFilterTest() {
        var canlendarCropCropTypeCriteria = new CanlendarCropCropTypeCriteria();
        var copy = canlendarCropCropTypeCriteria.copy();

        assertThat(canlendarCropCropTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(canlendarCropCropTypeCriteria)
        );
    }

    @Test
    void canlendarCropCropTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var canlendarCropCropTypeCriteria = new CanlendarCropCropTypeCriteria();
        setAllFilters(canlendarCropCropTypeCriteria);

        var copy = canlendarCropCropTypeCriteria.copy();

        assertThat(canlendarCropCropTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(canlendarCropCropTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var canlendarCropCropTypeCriteria = new CanlendarCropCropTypeCriteria();

        assertThat(canlendarCropCropTypeCriteria).hasToString("CanlendarCropCropTypeCriteria{}");
    }

    private static void setAllFilters(CanlendarCropCropTypeCriteria canlendarCropCropTypeCriteria) {
        canlendarCropCropTypeCriteria.id();
        canlendarCropCropTypeCriteria.crop();
        canlendarCropCropTypeCriteria.image();
        canlendarCropCropTypeCriteria.mainCrop();
        canlendarCropCropTypeCriteria.cropCode();
        canlendarCropCropTypeCriteria.noOfStages();
        canlendarCropCropTypeCriteria.description();
        canlendarCropCropTypeCriteria.cropTypesId();
        canlendarCropCropTypeCriteria.unitsId();
        canlendarCropCropTypeCriteria.area();
        canlendarCropCropTypeCriteria.sumInsured();
        canlendarCropCropTypeCriteria.minSumInsured();
        canlendarCropCropTypeCriteria.maxSumInsured();
        canlendarCropCropTypeCriteria.subsidisedPremiumRate();
        canlendarCropCropTypeCriteria.distinct();
    }

    private static Condition<CanlendarCropCropTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CanlendarCropCropTypeCriteria> copyFiltersAre(
        CanlendarCropCropTypeCriteria copy,
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
