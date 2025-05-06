package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class IndexPolicyCropTypeCriteriaTest {

    @Test
    void newIndexPolicyCropTypeCriteriaHasAllFiltersNullTest() {
        var indexPolicyCropTypeCriteria = new IndexPolicyCropTypeCriteria();
        assertThat(indexPolicyCropTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void indexPolicyCropTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var indexPolicyCropTypeCriteria = new IndexPolicyCropTypeCriteria();

        setAllFilters(indexPolicyCropTypeCriteria);

        assertThat(indexPolicyCropTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void indexPolicyCropTypeCriteriaCopyCreatesNullFilterTest() {
        var indexPolicyCropTypeCriteria = new IndexPolicyCropTypeCriteria();
        var copy = indexPolicyCropTypeCriteria.copy();

        assertThat(indexPolicyCropTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(indexPolicyCropTypeCriteria)
        );
    }

    @Test
    void indexPolicyCropTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var indexPolicyCropTypeCriteria = new IndexPolicyCropTypeCriteria();
        setAllFilters(indexPolicyCropTypeCriteria);

        var copy = indexPolicyCropTypeCriteria.copy();

        assertThat(indexPolicyCropTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(indexPolicyCropTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var indexPolicyCropTypeCriteria = new IndexPolicyCropTypeCriteria();

        assertThat(indexPolicyCropTypeCriteria).hasToString("IndexPolicyCropTypeCriteria{}");
    }

    private static void setAllFilters(IndexPolicyCropTypeCriteria indexPolicyCropTypeCriteria) {
        indexPolicyCropTypeCriteria.id();
        indexPolicyCropTypeCriteria.crop();
        indexPolicyCropTypeCriteria.image();
        indexPolicyCropTypeCriteria.mainCrop();
        indexPolicyCropTypeCriteria.cropCode();
        indexPolicyCropTypeCriteria.noOfStages();
        indexPolicyCropTypeCriteria.description();
        indexPolicyCropTypeCriteria.cropTypesId();
        indexPolicyCropTypeCriteria.unitsId();
        indexPolicyCropTypeCriteria.area();
        indexPolicyCropTypeCriteria.sumInsured();
        indexPolicyCropTypeCriteria.minSumInsured();
        indexPolicyCropTypeCriteria.maxSumInsured();
        indexPolicyCropTypeCriteria.subsidisedPremiumRate();
        indexPolicyCropTypeCriteria.distinct();
    }

    private static Condition<IndexPolicyCropTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<IndexPolicyCropTypeCriteria> copyFiltersAre(
        IndexPolicyCropTypeCriteria copy,
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
