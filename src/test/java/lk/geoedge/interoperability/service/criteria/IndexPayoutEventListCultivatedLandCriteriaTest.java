package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class IndexPayoutEventListCultivatedLandCriteriaTest {

    @Test
    void newIndexPayoutEventListCultivatedLandCriteriaHasAllFiltersNullTest() {
        var indexPayoutEventListCultivatedLandCriteria = new IndexPayoutEventListCultivatedLandCriteria();
        assertThat(indexPayoutEventListCultivatedLandCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void indexPayoutEventListCultivatedLandCriteriaFluentMethodsCreatesFiltersTest() {
        var indexPayoutEventListCultivatedLandCriteria = new IndexPayoutEventListCultivatedLandCriteria();

        setAllFilters(indexPayoutEventListCultivatedLandCriteria);

        assertThat(indexPayoutEventListCultivatedLandCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void indexPayoutEventListCultivatedLandCriteriaCopyCreatesNullFilterTest() {
        var indexPayoutEventListCultivatedLandCriteria = new IndexPayoutEventListCultivatedLandCriteria();
        var copy = indexPayoutEventListCultivatedLandCriteria.copy();

        assertThat(indexPayoutEventListCultivatedLandCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(indexPayoutEventListCultivatedLandCriteria)
        );
    }

    @Test
    void indexPayoutEventListCultivatedLandCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var indexPayoutEventListCultivatedLandCriteria = new IndexPayoutEventListCultivatedLandCriteria();
        setAllFilters(indexPayoutEventListCultivatedLandCriteria);

        var copy = indexPayoutEventListCultivatedLandCriteria.copy();

        assertThat(indexPayoutEventListCultivatedLandCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(indexPayoutEventListCultivatedLandCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var indexPayoutEventListCultivatedLandCriteria = new IndexPayoutEventListCultivatedLandCriteria();

        assertThat(indexPayoutEventListCultivatedLandCriteria).hasToString("IndexPayoutEventListCultivatedLandCriteria{}");
    }

    private static void setAllFilters(IndexPayoutEventListCultivatedLandCriteria indexPayoutEventListCultivatedLandCriteria) {
        indexPayoutEventListCultivatedLandCriteria.id();
        indexPayoutEventListCultivatedLandCriteria.landStatus();
        indexPayoutEventListCultivatedLandCriteria.urea();
        indexPayoutEventListCultivatedLandCriteria.mop();
        indexPayoutEventListCultivatedLandCriteria.tsp();
        indexPayoutEventListCultivatedLandCriteria.createdAt();
        indexPayoutEventListCultivatedLandCriteria.addedBy();
        indexPayoutEventListCultivatedLandCriteria.distinct();
    }

    private static Condition<IndexPayoutEventListCultivatedLandCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<IndexPayoutEventListCultivatedLandCriteria> copyFiltersAre(
        IndexPayoutEventListCultivatedLandCriteria copy,
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
