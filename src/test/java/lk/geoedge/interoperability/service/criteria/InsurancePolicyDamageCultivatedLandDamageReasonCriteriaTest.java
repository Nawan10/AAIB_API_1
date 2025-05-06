package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InsurancePolicyDamageCultivatedLandDamageReasonCriteriaTest {

    @Test
    void newInsurancePolicyDamageCultivatedLandDamageReasonCriteriaHasAllFiltersNullTest() {
        var insurancePolicyDamageCultivatedLandDamageReasonCriteria = new InsurancePolicyDamageCultivatedLandDamageReasonCriteria();
        assertThat(insurancePolicyDamageCultivatedLandDamageReasonCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void insurancePolicyDamageCultivatedLandDamageReasonCriteriaFluentMethodsCreatesFiltersTest() {
        var insurancePolicyDamageCultivatedLandDamageReasonCriteria = new InsurancePolicyDamageCultivatedLandDamageReasonCriteria();

        setAllFilters(insurancePolicyDamageCultivatedLandDamageReasonCriteria);

        assertThat(insurancePolicyDamageCultivatedLandDamageReasonCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void insurancePolicyDamageCultivatedLandDamageReasonCriteriaCopyCreatesNullFilterTest() {
        var insurancePolicyDamageCultivatedLandDamageReasonCriteria = new InsurancePolicyDamageCultivatedLandDamageReasonCriteria();
        var copy = insurancePolicyDamageCultivatedLandDamageReasonCriteria.copy();

        assertThat(insurancePolicyDamageCultivatedLandDamageReasonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(insurancePolicyDamageCultivatedLandDamageReasonCriteria)
        );
    }

    @Test
    void insurancePolicyDamageCultivatedLandDamageReasonCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var insurancePolicyDamageCultivatedLandDamageReasonCriteria = new InsurancePolicyDamageCultivatedLandDamageReasonCriteria();
        setAllFilters(insurancePolicyDamageCultivatedLandDamageReasonCriteria);

        var copy = insurancePolicyDamageCultivatedLandDamageReasonCriteria.copy();

        assertThat(insurancePolicyDamageCultivatedLandDamageReasonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(insurancePolicyDamageCultivatedLandDamageReasonCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var insurancePolicyDamageCultivatedLandDamageReasonCriteria = new InsurancePolicyDamageCultivatedLandDamageReasonCriteria();

        assertThat(insurancePolicyDamageCultivatedLandDamageReasonCriteria).hasToString(
            "InsurancePolicyDamageCultivatedLandDamageReasonCriteria{}"
        );
    }

    private static void setAllFilters(
        InsurancePolicyDamageCultivatedLandDamageReasonCriteria insurancePolicyDamageCultivatedLandDamageReasonCriteria
    ) {
        insurancePolicyDamageCultivatedLandDamageReasonCriteria.id();
        insurancePolicyDamageCultivatedLandDamageReasonCriteria.name();
        insurancePolicyDamageCultivatedLandDamageReasonCriteria.damageCategoryId();
        insurancePolicyDamageCultivatedLandDamageReasonCriteria.damageTypeId();
        insurancePolicyDamageCultivatedLandDamageReasonCriteria.distinct();
    }

    private static Condition<InsurancePolicyDamageCultivatedLandDamageReasonCriteria> criteriaFiltersAre(
        Function<Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDamageCategoryId()) &&
                condition.apply(criteria.getDamageTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InsurancePolicyDamageCultivatedLandDamageReasonCriteria> copyFiltersAre(
        InsurancePolicyDamageCultivatedLandDamageReasonCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDamageCategoryId(), copy.getDamageCategoryId()) &&
                condition.apply(criteria.getDamageTypeId(), copy.getDamageTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
