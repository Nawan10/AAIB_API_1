package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InsuranceCultivatedLandCriteriaTest {

    @Test
    void newInsuranceCultivatedLandCriteriaHasAllFiltersNullTest() {
        var insuranceCultivatedLandCriteria = new InsuranceCultivatedLandCriteria();
        assertThat(insuranceCultivatedLandCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void insuranceCultivatedLandCriteriaFluentMethodsCreatesFiltersTest() {
        var insuranceCultivatedLandCriteria = new InsuranceCultivatedLandCriteria();

        setAllFilters(insuranceCultivatedLandCriteria);

        assertThat(insuranceCultivatedLandCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void insuranceCultivatedLandCriteriaCopyCreatesNullFilterTest() {
        var insuranceCultivatedLandCriteria = new InsuranceCultivatedLandCriteria();
        var copy = insuranceCultivatedLandCriteria.copy();

        assertThat(insuranceCultivatedLandCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(insuranceCultivatedLandCriteria)
        );
    }

    @Test
    void insuranceCultivatedLandCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var insuranceCultivatedLandCriteria = new InsuranceCultivatedLandCriteria();
        setAllFilters(insuranceCultivatedLandCriteria);

        var copy = insuranceCultivatedLandCriteria.copy();

        assertThat(insuranceCultivatedLandCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(insuranceCultivatedLandCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var insuranceCultivatedLandCriteria = new InsuranceCultivatedLandCriteria();

        assertThat(insuranceCultivatedLandCriteria).hasToString("InsuranceCultivatedLandCriteria{}");
    }

    private static void setAllFilters(InsuranceCultivatedLandCriteria insuranceCultivatedLandCriteria) {
        insuranceCultivatedLandCriteria.id();
        insuranceCultivatedLandCriteria.cropDurationId();
        insuranceCultivatedLandCriteria.insurancePoliceId();
        insuranceCultivatedLandCriteria.sumInsuredPerAcre();
        insuranceCultivatedLandCriteria.insuranceExtent();
        insuranceCultivatedLandCriteria.sumAmount();
        insuranceCultivatedLandCriteria.insuranceStatus();
        insuranceCultivatedLandCriteria.createdAt();
        insuranceCultivatedLandCriteria.addedBy();
        insuranceCultivatedLandCriteria.farmerId();
        insuranceCultivatedLandCriteria.cultivatedLandId();
        insuranceCultivatedLandCriteria.cropId();
        insuranceCultivatedLandCriteria.distinct();
    }

    private static Condition<InsuranceCultivatedLandCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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
                condition.apply(criteria.getFarmerId()) &&
                condition.apply(criteria.getCultivatedLandId()) &&
                condition.apply(criteria.getCropId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InsuranceCultivatedLandCriteria> copyFiltersAre(
        InsuranceCultivatedLandCriteria copy,
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
                condition.apply(criteria.getFarmerId(), copy.getFarmerId()) &&
                condition.apply(criteria.getCultivatedLandId(), copy.getCultivatedLandId()) &&
                condition.apply(criteria.getCropId(), copy.getCropId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
