package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InsurancePolicyDamageCriteriaTest {

    @Test
    void newInsurancePolicyDamageCriteriaHasAllFiltersNullTest() {
        var insurancePolicyDamageCriteria = new InsurancePolicyDamageCriteria();
        assertThat(insurancePolicyDamageCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void insurancePolicyDamageCriteriaFluentMethodsCreatesFiltersTest() {
        var insurancePolicyDamageCriteria = new InsurancePolicyDamageCriteria();

        setAllFilters(insurancePolicyDamageCriteria);

        assertThat(insurancePolicyDamageCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void insurancePolicyDamageCriteriaCopyCreatesNullFilterTest() {
        var insurancePolicyDamageCriteria = new InsurancePolicyDamageCriteria();
        var copy = insurancePolicyDamageCriteria.copy();

        assertThat(insurancePolicyDamageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(insurancePolicyDamageCriteria)
        );
    }

    @Test
    void insurancePolicyDamageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var insurancePolicyDamageCriteria = new InsurancePolicyDamageCriteria();
        setAllFilters(insurancePolicyDamageCriteria);

        var copy = insurancePolicyDamageCriteria.copy();

        assertThat(insurancePolicyDamageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(insurancePolicyDamageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var insurancePolicyDamageCriteria = new InsurancePolicyDamageCriteria();

        assertThat(insurancePolicyDamageCriteria).hasToString("InsurancePolicyDamageCriteria{}");
    }

    private static void setAllFilters(InsurancePolicyDamageCriteria insurancePolicyDamageCriteria) {
        insurancePolicyDamageCriteria.id();
        insurancePolicyDamageCriteria.percentage();
        insurancePolicyDamageCriteria.isFree();
        insurancePolicyDamageCriteria.isPaid();
        insurancePolicyDamageCriteria.insurancePolicyId();
        insurancePolicyDamageCriteria.damageReasonId();
        insurancePolicyDamageCriteria.distinct();
    }

    private static Condition<InsurancePolicyDamageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPercentage()) &&
                condition.apply(criteria.getIsFree()) &&
                condition.apply(criteria.getIsPaid()) &&
                condition.apply(criteria.getInsurancePolicyId()) &&
                condition.apply(criteria.getDamageReasonId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InsurancePolicyDamageCriteria> copyFiltersAre(
        InsurancePolicyDamageCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPercentage(), copy.getPercentage()) &&
                condition.apply(criteria.getIsFree(), copy.getIsFree()) &&
                condition.apply(criteria.getIsPaid(), copy.getIsPaid()) &&
                condition.apply(criteria.getInsurancePolicyId(), copy.getInsurancePolicyId()) &&
                condition.apply(criteria.getDamageReasonId(), copy.getDamageReasonId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
