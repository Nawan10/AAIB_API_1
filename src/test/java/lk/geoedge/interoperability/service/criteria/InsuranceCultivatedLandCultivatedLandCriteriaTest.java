package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InsuranceCultivatedLandCultivatedLandCriteriaTest {

    @Test
    void newInsuranceCultivatedLandCultivatedLandCriteriaHasAllFiltersNullTest() {
        var insuranceCultivatedLandCultivatedLandCriteria = new InsuranceCultivatedLandCultivatedLandCriteria();
        assertThat(insuranceCultivatedLandCultivatedLandCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void insuranceCultivatedLandCultivatedLandCriteriaFluentMethodsCreatesFiltersTest() {
        var insuranceCultivatedLandCultivatedLandCriteria = new InsuranceCultivatedLandCultivatedLandCriteria();

        setAllFilters(insuranceCultivatedLandCultivatedLandCriteria);

        assertThat(insuranceCultivatedLandCultivatedLandCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void insuranceCultivatedLandCultivatedLandCriteriaCopyCreatesNullFilterTest() {
        var insuranceCultivatedLandCultivatedLandCriteria = new InsuranceCultivatedLandCultivatedLandCriteria();
        var copy = insuranceCultivatedLandCultivatedLandCriteria.copy();

        assertThat(insuranceCultivatedLandCultivatedLandCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(insuranceCultivatedLandCultivatedLandCriteria)
        );
    }

    @Test
    void insuranceCultivatedLandCultivatedLandCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var insuranceCultivatedLandCultivatedLandCriteria = new InsuranceCultivatedLandCultivatedLandCriteria();
        setAllFilters(insuranceCultivatedLandCultivatedLandCriteria);

        var copy = insuranceCultivatedLandCultivatedLandCriteria.copy();

        assertThat(insuranceCultivatedLandCultivatedLandCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(insuranceCultivatedLandCultivatedLandCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var insuranceCultivatedLandCultivatedLandCriteria = new InsuranceCultivatedLandCultivatedLandCriteria();

        assertThat(insuranceCultivatedLandCultivatedLandCriteria).hasToString("InsuranceCultivatedLandCultivatedLandCriteria{}");
    }

    private static void setAllFilters(InsuranceCultivatedLandCultivatedLandCriteria insuranceCultivatedLandCultivatedLandCriteria) {
        insuranceCultivatedLandCultivatedLandCriteria.id();
        insuranceCultivatedLandCultivatedLandCriteria.landStatus();
        insuranceCultivatedLandCultivatedLandCriteria.urea();
        insuranceCultivatedLandCultivatedLandCriteria.mop();
        insuranceCultivatedLandCultivatedLandCriteria.tsp();
        insuranceCultivatedLandCultivatedLandCriteria.createdAt();
        insuranceCultivatedLandCultivatedLandCriteria.addedBy();
        insuranceCultivatedLandCultivatedLandCriteria.distinct();
    }

    private static Condition<InsuranceCultivatedLandCultivatedLandCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLandStatus()) &&
                condition.apply(criteria.getUrea()) &&
                condition.apply(criteria.getMop()) &&
                condition.apply(criteria.getTsp()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InsuranceCultivatedLandCultivatedLandCriteria> copyFiltersAre(
        InsuranceCultivatedLandCultivatedLandCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLandStatus(), copy.getLandStatus()) &&
                condition.apply(criteria.getUrea(), copy.getUrea()) &&
                condition.apply(criteria.getMop(), copy.getMop()) &&
                condition.apply(criteria.getTsp(), copy.getTsp()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy(), copy.getAddedBy()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
