package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CultivatedLandEntityCriteriaTest {

    @Test
    void newCultivatedLandEntityCriteriaHasAllFiltersNullTest() {
        var cultivatedLandEntityCriteria = new CultivatedLandEntityCriteria();
        assertThat(cultivatedLandEntityCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cultivatedLandEntityCriteriaFluentMethodsCreatesFiltersTest() {
        var cultivatedLandEntityCriteria = new CultivatedLandEntityCriteria();

        setAllFilters(cultivatedLandEntityCriteria);

        assertThat(cultivatedLandEntityCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cultivatedLandEntityCriteriaCopyCreatesNullFilterTest() {
        var cultivatedLandEntityCriteria = new CultivatedLandEntityCriteria();
        var copy = cultivatedLandEntityCriteria.copy();

        assertThat(cultivatedLandEntityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandEntityCriteria)
        );
    }

    @Test
    void cultivatedLandEntityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cultivatedLandEntityCriteria = new CultivatedLandEntityCriteria();
        setAllFilters(cultivatedLandEntityCriteria);

        var copy = cultivatedLandEntityCriteria.copy();

        assertThat(cultivatedLandEntityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandEntityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cultivatedLandEntityCriteria = new CultivatedLandEntityCriteria();

        assertThat(cultivatedLandEntityCriteria).hasToString("CultivatedLandEntityCriteria{}");
    }

    private static void setAllFilters(CultivatedLandEntityCriteria cultivatedLandEntityCriteria) {
        cultivatedLandEntityCriteria.id();
        cultivatedLandEntityCriteria.landStatus();
        cultivatedLandEntityCriteria.urea();
        cultivatedLandEntityCriteria.mop();
        cultivatedLandEntityCriteria.tsp();
        cultivatedLandEntityCriteria.createdAt();
        cultivatedLandEntityCriteria.addedBy();
        cultivatedLandEntityCriteria.farmFieldId();
        cultivatedLandEntityCriteria.seasonId();
        cultivatedLandEntityCriteria.distinct();
    }

    private static Condition<CultivatedLandEntityCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLandStatus()) &&
                condition.apply(criteria.getUrea()) &&
                condition.apply(criteria.getMop()) &&
                condition.apply(criteria.getTsp()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getFarmFieldId()) &&
                condition.apply(criteria.getSeasonId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CultivatedLandEntityCriteria> copyFiltersAre(
        CultivatedLandEntityCriteria copy,
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
                condition.apply(criteria.getFarmFieldId(), copy.getFarmFieldId()) &&
                condition.apply(criteria.getSeasonId(), copy.getSeasonId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
