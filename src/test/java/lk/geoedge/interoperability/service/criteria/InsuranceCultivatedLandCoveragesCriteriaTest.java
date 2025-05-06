package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InsuranceCultivatedLandCoveragesCriteriaTest {

    @Test
    void newInsuranceCultivatedLandCoveragesCriteriaHasAllFiltersNullTest() {
        var insuranceCultivatedLandCoveragesCriteria = new InsuranceCultivatedLandCoveragesCriteria();
        assertThat(insuranceCultivatedLandCoveragesCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void insuranceCultivatedLandCoveragesCriteriaFluentMethodsCreatesFiltersTest() {
        var insuranceCultivatedLandCoveragesCriteria = new InsuranceCultivatedLandCoveragesCriteria();

        setAllFilters(insuranceCultivatedLandCoveragesCriteria);

        assertThat(insuranceCultivatedLandCoveragesCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void insuranceCultivatedLandCoveragesCriteriaCopyCreatesNullFilterTest() {
        var insuranceCultivatedLandCoveragesCriteria = new InsuranceCultivatedLandCoveragesCriteria();
        var copy = insuranceCultivatedLandCoveragesCriteria.copy();

        assertThat(insuranceCultivatedLandCoveragesCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(insuranceCultivatedLandCoveragesCriteria)
        );
    }

    @Test
    void insuranceCultivatedLandCoveragesCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var insuranceCultivatedLandCoveragesCriteria = new InsuranceCultivatedLandCoveragesCriteria();
        setAllFilters(insuranceCultivatedLandCoveragesCriteria);

        var copy = insuranceCultivatedLandCoveragesCriteria.copy();

        assertThat(insuranceCultivatedLandCoveragesCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(insuranceCultivatedLandCoveragesCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var insuranceCultivatedLandCoveragesCriteria = new InsuranceCultivatedLandCoveragesCriteria();

        assertThat(insuranceCultivatedLandCoveragesCriteria).hasToString("InsuranceCultivatedLandCoveragesCriteria{}");
    }

    private static void setAllFilters(InsuranceCultivatedLandCoveragesCriteria insuranceCultivatedLandCoveragesCriteria) {
        insuranceCultivatedLandCoveragesCriteria.id();
        insuranceCultivatedLandCoveragesCriteria.converageAmount();
        insuranceCultivatedLandCoveragesCriteria.isSelect();
        insuranceCultivatedLandCoveragesCriteria.createdAt();
        insuranceCultivatedLandCoveragesCriteria.addedBy();
        insuranceCultivatedLandCoveragesCriteria.insuranceCultivatedLandId();
        insuranceCultivatedLandCoveragesCriteria.indexCoverageId();
        insuranceCultivatedLandCoveragesCriteria.distinct();
    }

    private static Condition<InsuranceCultivatedLandCoveragesCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getConverageAmount()) &&
                condition.apply(criteria.getIsSelect()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getInsuranceCultivatedLandId()) &&
                condition.apply(criteria.getIndexCoverageId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InsuranceCultivatedLandCoveragesCriteria> copyFiltersAre(
        InsuranceCultivatedLandCoveragesCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getConverageAmount(), copy.getConverageAmount()) &&
                condition.apply(criteria.getIsSelect(), copy.getIsSelect()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy(), copy.getAddedBy()) &&
                condition.apply(criteria.getInsuranceCultivatedLandId(), copy.getInsuranceCultivatedLandId()) &&
                condition.apply(criteria.getIndexCoverageId(), copy.getIndexCoverageId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
