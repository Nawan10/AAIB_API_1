package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InsuranceCropCriteriaTest {

    @Test
    void newInsuranceCropCriteriaHasAllFiltersNullTest() {
        var insuranceCropCriteria = new InsuranceCropCriteria();
        assertThat(insuranceCropCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void insuranceCropCriteriaFluentMethodsCreatesFiltersTest() {
        var insuranceCropCriteria = new InsuranceCropCriteria();

        setAllFilters(insuranceCropCriteria);

        assertThat(insuranceCropCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void insuranceCropCriteriaCopyCreatesNullFilterTest() {
        var insuranceCropCriteria = new InsuranceCropCriteria();
        var copy = insuranceCropCriteria.copy();

        assertThat(insuranceCropCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(insuranceCropCriteria)
        );
    }

    @Test
    void insuranceCropCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var insuranceCropCriteria = new InsuranceCropCriteria();
        setAllFilters(insuranceCropCriteria);

        var copy = insuranceCropCriteria.copy();

        assertThat(insuranceCropCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(insuranceCropCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var insuranceCropCriteria = new InsuranceCropCriteria();

        assertThat(insuranceCropCriteria).hasToString("InsuranceCropCriteria{}");
    }

    private static void setAllFilters(InsuranceCropCriteria insuranceCropCriteria) {
        insuranceCropCriteria.id();
        insuranceCropCriteria.policyId();
        insuranceCropCriteria.yield();
        insuranceCropCriteria.createdAt();
        insuranceCropCriteria.addedBy();
        insuranceCropCriteria.cropId();
        insuranceCropCriteria.distinct();
    }

    private static Condition<InsuranceCropCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPolicyId()) &&
                condition.apply(criteria.getYield()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getCropId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InsuranceCropCriteria> copyFiltersAre(
        InsuranceCropCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPolicyId(), copy.getPolicyId()) &&
                condition.apply(criteria.getYield(), copy.getYield()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy(), copy.getAddedBy()) &&
                condition.apply(criteria.getCropId(), copy.getCropId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
