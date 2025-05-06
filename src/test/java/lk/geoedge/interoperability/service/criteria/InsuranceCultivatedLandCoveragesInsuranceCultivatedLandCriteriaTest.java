package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteriaTest {

    @Test
    void newInsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteriaHasAllFiltersNullTest() {
        var insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria =
            new InsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria();
        assertThat(insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteriaFluentMethodsCreatesFiltersTest() {
        var insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria =
            new InsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria();

        setAllFilters(insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria);

        assertThat(insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteriaCopyCreatesNullFilterTest() {
        var insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria =
            new InsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria();
        var copy = insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria.copy();

        assertThat(insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria)
        );
    }

    @Test
    void insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria =
            new InsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria();
        setAllFilters(insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria);

        var copy = insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria.copy();

        assertThat(insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria =
            new InsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria();

        assertThat(insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria).hasToString(
            "InsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria{}"
        );
    }

    private static void setAllFilters(
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria
    ) {
        insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria.id();
        insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria.cropDurationId();
        insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria.insurancePoliceId();
        insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria.sumInsuredPerAcre();
        insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria.insuranceExtent();
        insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria.sumAmount();
        insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria.insuranceStatus();
        insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria.createdAt();
        insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria.addedBy();
        insuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria.distinct();
    }

    private static Condition<InsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria> criteriaFiltersAre(
        Function<Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCropDurationId()) &&
                condition.apply(criteria.getInsurancePoliceId()) &&
                condition.apply(criteria.getSumInsuredPerAcre()) &&
                condition.apply(criteria.getInsuranceExtent()) &&
                condition.apply(criteria.getSumAmount()) &&
                condition.apply(criteria.getInsuranceStatus()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria> copyFiltersAre(
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCropDurationId(), copy.getCropDurationId()) &&
                condition.apply(criteria.getInsurancePoliceId(), copy.getInsurancePoliceId()) &&
                condition.apply(criteria.getSumInsuredPerAcre(), copy.getSumInsuredPerAcre()) &&
                condition.apply(criteria.getInsuranceExtent(), copy.getInsuranceExtent()) &&
                condition.apply(criteria.getSumAmount(), copy.getSumAmount()) &&
                condition.apply(criteria.getInsuranceStatus(), copy.getInsuranceStatus()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy(), copy.getAddedBy()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
