package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class IndexPayoutEventListCriteriaTest {

    @Test
    void newIndexPayoutEventListCriteriaHasAllFiltersNullTest() {
        var indexPayoutEventListCriteria = new IndexPayoutEventListCriteria();
        assertThat(indexPayoutEventListCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void indexPayoutEventListCriteriaFluentMethodsCreatesFiltersTest() {
        var indexPayoutEventListCriteria = new IndexPayoutEventListCriteria();

        setAllFilters(indexPayoutEventListCriteria);

        assertThat(indexPayoutEventListCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void indexPayoutEventListCriteriaCopyCreatesNullFilterTest() {
        var indexPayoutEventListCriteria = new IndexPayoutEventListCriteria();
        var copy = indexPayoutEventListCriteria.copy();

        assertThat(indexPayoutEventListCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(indexPayoutEventListCriteria)
        );
    }

    @Test
    void indexPayoutEventListCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var indexPayoutEventListCriteria = new IndexPayoutEventListCriteria();
        setAllFilters(indexPayoutEventListCriteria);

        var copy = indexPayoutEventListCriteria.copy();

        assertThat(indexPayoutEventListCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(indexPayoutEventListCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var indexPayoutEventListCriteria = new IndexPayoutEventListCriteria();

        assertThat(indexPayoutEventListCriteria).hasToString("IndexPayoutEventListCriteria{}");
    }

    private static void setAllFilters(IndexPayoutEventListCriteria indexPayoutEventListCriteria) {
        indexPayoutEventListCriteria.id();
        indexPayoutEventListCriteria.indexPayoutEventId();
        indexPayoutEventListCriteria.ascId();
        indexPayoutEventListCriteria.confirmedBy();
        indexPayoutEventListCriteria.cultivatedExtent();
        indexPayoutEventListCriteria.payout();
        indexPayoutEventListCriteria.confirmedDate();
        indexPayoutEventListCriteria.rejectedBy();
        indexPayoutEventListCriteria.rejectedDate();
        indexPayoutEventListCriteria.reason();
        indexPayoutEventListCriteria.finalPayout();
        indexPayoutEventListCriteria.indexPayoutEventStatus();
        indexPayoutEventListCriteria.isApproved();
        indexPayoutEventListCriteria.monitoringRange();
        indexPayoutEventListCriteria.isInsurance();
        indexPayoutEventListCriteria.insuranceCultivatedLand();
        indexPayoutEventListCriteria.indexChequeId();
        indexPayoutEventListCriteria.indexProductId();
        indexPayoutEventListCriteria.cultivatedFarmerId();
        indexPayoutEventListCriteria.cultivatedLandId();
        indexPayoutEventListCriteria.seasonId();
        indexPayoutEventListCriteria.distinct();
    }

    private static Condition<IndexPayoutEventListCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getIndexPayoutEventId()) &&
                condition.apply(criteria.getAscId()) &&
                condition.apply(criteria.getConfirmedBy()) &&
                condition.apply(criteria.getCultivatedExtent()) &&
                condition.apply(criteria.getPayout()) &&
                condition.apply(criteria.getConfirmedDate()) &&
                condition.apply(criteria.getRejectedBy()) &&
                condition.apply(criteria.getRejectedDate()) &&
                condition.apply(criteria.getReason()) &&
                condition.apply(criteria.getFinalPayout()) &&
                condition.apply(criteria.getIndexPayoutEventStatus()) &&
                condition.apply(criteria.getIsApproved()) &&
                condition.apply(criteria.getMonitoringRange()) &&
                condition.apply(criteria.getIsInsurance()) &&
                condition.apply(criteria.getInsuranceCultivatedLand()) &&
                condition.apply(criteria.getIndexChequeId()) &&
                condition.apply(criteria.getIndexProductId()) &&
                condition.apply(criteria.getCultivatedFarmerId()) &&
                condition.apply(criteria.getCultivatedLandId()) &&
                condition.apply(criteria.getSeasonId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<IndexPayoutEventListCriteria> copyFiltersAre(
        IndexPayoutEventListCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getIndexPayoutEventId(), copy.getIndexPayoutEventId()) &&
                condition.apply(criteria.getAscId(), copy.getAscId()) &&
                condition.apply(criteria.getConfirmedBy(), copy.getConfirmedBy()) &&
                condition.apply(criteria.getCultivatedExtent(), copy.getCultivatedExtent()) &&
                condition.apply(criteria.getPayout(), copy.getPayout()) &&
                condition.apply(criteria.getConfirmedDate(), copy.getConfirmedDate()) &&
                condition.apply(criteria.getRejectedBy(), copy.getRejectedBy()) &&
                condition.apply(criteria.getRejectedDate(), copy.getRejectedDate()) &&
                condition.apply(criteria.getReason(), copy.getReason()) &&
                condition.apply(criteria.getFinalPayout(), copy.getFinalPayout()) &&
                condition.apply(criteria.getIndexPayoutEventStatus(), copy.getIndexPayoutEventStatus()) &&
                condition.apply(criteria.getIsApproved(), copy.getIsApproved()) &&
                condition.apply(criteria.getMonitoringRange(), copy.getMonitoringRange()) &&
                condition.apply(criteria.getIsInsurance(), copy.getIsInsurance()) &&
                condition.apply(criteria.getInsuranceCultivatedLand(), copy.getInsuranceCultivatedLand()) &&
                condition.apply(criteria.getIndexChequeId(), copy.getIndexChequeId()) &&
                condition.apply(criteria.getIndexProductId(), copy.getIndexProductId()) &&
                condition.apply(criteria.getCultivatedFarmerId(), copy.getCultivatedFarmerId()) &&
                condition.apply(criteria.getCultivatedLandId(), copy.getCultivatedLandId()) &&
                condition.apply(criteria.getSeasonId(), copy.getSeasonId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
