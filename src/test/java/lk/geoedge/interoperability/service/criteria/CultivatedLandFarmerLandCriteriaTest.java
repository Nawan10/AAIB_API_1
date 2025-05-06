package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CultivatedLandFarmerLandCriteriaTest {

    @Test
    void newCultivatedLandFarmerLandCriteriaHasAllFiltersNullTest() {
        var cultivatedLandFarmerLandCriteria = new CultivatedLandFarmerLandCriteria();
        assertThat(cultivatedLandFarmerLandCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cultivatedLandFarmerLandCriteriaFluentMethodsCreatesFiltersTest() {
        var cultivatedLandFarmerLandCriteria = new CultivatedLandFarmerLandCriteria();

        setAllFilters(cultivatedLandFarmerLandCriteria);

        assertThat(cultivatedLandFarmerLandCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cultivatedLandFarmerLandCriteriaCopyCreatesNullFilterTest() {
        var cultivatedLandFarmerLandCriteria = new CultivatedLandFarmerLandCriteria();
        var copy = cultivatedLandFarmerLandCriteria.copy();

        assertThat(cultivatedLandFarmerLandCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandFarmerLandCriteria)
        );
    }

    @Test
    void cultivatedLandFarmerLandCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cultivatedLandFarmerLandCriteria = new CultivatedLandFarmerLandCriteria();
        setAllFilters(cultivatedLandFarmerLandCriteria);

        var copy = cultivatedLandFarmerLandCriteria.copy();

        assertThat(cultivatedLandFarmerLandCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandFarmerLandCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cultivatedLandFarmerLandCriteria = new CultivatedLandFarmerLandCriteria();

        assertThat(cultivatedLandFarmerLandCriteria).hasToString("CultivatedLandFarmerLandCriteria{}");
    }

    private static void setAllFilters(CultivatedLandFarmerLandCriteria cultivatedLandFarmerLandCriteria) {
        cultivatedLandFarmerLandCriteria.id();
        cultivatedLandFarmerLandCriteria.landStatus();
        cultivatedLandFarmerLandCriteria.urea();
        cultivatedLandFarmerLandCriteria.mop();
        cultivatedLandFarmerLandCriteria.tsp();
        cultivatedLandFarmerLandCriteria.createdAt();
        cultivatedLandFarmerLandCriteria.addedBy();
        cultivatedLandFarmerLandCriteria.distinct();
    }

    private static Condition<CultivatedLandFarmerLandCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CultivatedLandFarmerLandCriteria> copyFiltersAre(
        CultivatedLandFarmerLandCriteria copy,
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
