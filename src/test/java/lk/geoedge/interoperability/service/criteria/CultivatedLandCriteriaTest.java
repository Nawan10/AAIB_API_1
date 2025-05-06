package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CultivatedLandCriteriaTest {

    @Test
    void newCultivatedLandCriteriaHasAllFiltersNullTest() {
        var cultivatedLandCriteria = new CultivatedLandCriteria();
        assertThat(cultivatedLandCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cultivatedLandCriteriaFluentMethodsCreatesFiltersTest() {
        var cultivatedLandCriteria = new CultivatedLandCriteria();

        setAllFilters(cultivatedLandCriteria);

        assertThat(cultivatedLandCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cultivatedLandCriteriaCopyCreatesNullFilterTest() {
        var cultivatedLandCriteria = new CultivatedLandCriteria();
        var copy = cultivatedLandCriteria.copy();

        assertThat(cultivatedLandCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandCriteria)
        );
    }

    @Test
    void cultivatedLandCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cultivatedLandCriteria = new CultivatedLandCriteria();
        setAllFilters(cultivatedLandCriteria);

        var copy = cultivatedLandCriteria.copy();

        assertThat(cultivatedLandCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cultivatedLandCriteria = new CultivatedLandCriteria();

        assertThat(cultivatedLandCriteria).hasToString("CultivatedLandCriteria{}");
    }

    private static void setAllFilters(CultivatedLandCriteria cultivatedLandCriteria) {
        cultivatedLandCriteria.id();
        cultivatedLandCriteria.landStatus();
        cultivatedLandCriteria.urea();
        cultivatedLandCriteria.mop();
        cultivatedLandCriteria.tsp();
        cultivatedLandCriteria.createdAt();
        cultivatedLandCriteria.addedBy();
        cultivatedLandCriteria.farmFieldId();
        cultivatedLandCriteria.seasonId();
        cultivatedLandCriteria.distinct();
    }

    private static Condition<CultivatedLandCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CultivatedLandCriteria> copyFiltersAre(
        CultivatedLandCriteria copy,
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
