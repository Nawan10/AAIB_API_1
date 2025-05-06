package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class IndexPayoutEventListFarmerCriteriaTest {

    @Test
    void newIndexPayoutEventListFarmerCriteriaHasAllFiltersNullTest() {
        var indexPayoutEventListFarmerCriteria = new IndexPayoutEventListFarmerCriteria();
        assertThat(indexPayoutEventListFarmerCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void indexPayoutEventListFarmerCriteriaFluentMethodsCreatesFiltersTest() {
        var indexPayoutEventListFarmerCriteria = new IndexPayoutEventListFarmerCriteria();

        setAllFilters(indexPayoutEventListFarmerCriteria);

        assertThat(indexPayoutEventListFarmerCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void indexPayoutEventListFarmerCriteriaCopyCreatesNullFilterTest() {
        var indexPayoutEventListFarmerCriteria = new IndexPayoutEventListFarmerCriteria();
        var copy = indexPayoutEventListFarmerCriteria.copy();

        assertThat(indexPayoutEventListFarmerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(indexPayoutEventListFarmerCriteria)
        );
    }

    @Test
    void indexPayoutEventListFarmerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var indexPayoutEventListFarmerCriteria = new IndexPayoutEventListFarmerCriteria();
        setAllFilters(indexPayoutEventListFarmerCriteria);

        var copy = indexPayoutEventListFarmerCriteria.copy();

        assertThat(indexPayoutEventListFarmerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(indexPayoutEventListFarmerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var indexPayoutEventListFarmerCriteria = new IndexPayoutEventListFarmerCriteria();

        assertThat(indexPayoutEventListFarmerCriteria).hasToString("IndexPayoutEventListFarmerCriteria{}");
    }

    private static void setAllFilters(IndexPayoutEventListFarmerCriteria indexPayoutEventListFarmerCriteria) {
        indexPayoutEventListFarmerCriteria.id();
        indexPayoutEventListFarmerCriteria.farmerId();
        indexPayoutEventListFarmerCriteria.farmerName();
        indexPayoutEventListFarmerCriteria.nicNo();
        indexPayoutEventListFarmerCriteria.addressFirstLine();
        indexPayoutEventListFarmerCriteria.contactNoEmail();
        indexPayoutEventListFarmerCriteria.provinceId();
        indexPayoutEventListFarmerCriteria.districtId();
        indexPayoutEventListFarmerCriteria.dsId();
        indexPayoutEventListFarmerCriteria.gnId();
        indexPayoutEventListFarmerCriteria.city();
        indexPayoutEventListFarmerCriteria.addedDate();
        indexPayoutEventListFarmerCriteria.distinct();
    }

    private static Condition<IndexPayoutEventListFarmerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFarmerId()) &&
                condition.apply(criteria.getFarmerName()) &&
                condition.apply(criteria.getNicNo()) &&
                condition.apply(criteria.getAddressFirstLine()) &&
                condition.apply(criteria.getContactNoEmail()) &&
                condition.apply(criteria.getProvinceId()) &&
                condition.apply(criteria.getDistrictId()) &&
                condition.apply(criteria.getDsId()) &&
                condition.apply(criteria.getGnId()) &&
                condition.apply(criteria.getCity()) &&
                condition.apply(criteria.getAddedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<IndexPayoutEventListFarmerCriteria> copyFiltersAre(
        IndexPayoutEventListFarmerCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFarmerId(), copy.getFarmerId()) &&
                condition.apply(criteria.getFarmerName(), copy.getFarmerName()) &&
                condition.apply(criteria.getNicNo(), copy.getNicNo()) &&
                condition.apply(criteria.getAddressFirstLine(), copy.getAddressFirstLine()) &&
                condition.apply(criteria.getContactNoEmail(), copy.getContactNoEmail()) &&
                condition.apply(criteria.getProvinceId(), copy.getProvinceId()) &&
                condition.apply(criteria.getDistrictId(), copy.getDistrictId()) &&
                condition.apply(criteria.getDsId(), copy.getDsId()) &&
                condition.apply(criteria.getGnId(), copy.getGnId()) &&
                condition.apply(criteria.getCity(), copy.getCity()) &&
                condition.apply(criteria.getAddedDate(), copy.getAddedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
