package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FarmerFieldLandOwnerFarmerCriteriaTest {

    @Test
    void newFarmerFieldLandOwnerFarmerCriteriaHasAllFiltersNullTest() {
        var farmerFieldLandOwnerFarmerCriteria = new FarmerFieldLandOwnerFarmerCriteria();
        assertThat(farmerFieldLandOwnerFarmerCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void farmerFieldLandOwnerFarmerCriteriaFluentMethodsCreatesFiltersTest() {
        var farmerFieldLandOwnerFarmerCriteria = new FarmerFieldLandOwnerFarmerCriteria();

        setAllFilters(farmerFieldLandOwnerFarmerCriteria);

        assertThat(farmerFieldLandOwnerFarmerCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void farmerFieldLandOwnerFarmerCriteriaCopyCreatesNullFilterTest() {
        var farmerFieldLandOwnerFarmerCriteria = new FarmerFieldLandOwnerFarmerCriteria();
        var copy = farmerFieldLandOwnerFarmerCriteria.copy();

        assertThat(farmerFieldLandOwnerFarmerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(farmerFieldLandOwnerFarmerCriteria)
        );
    }

    @Test
    void farmerFieldLandOwnerFarmerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var farmerFieldLandOwnerFarmerCriteria = new FarmerFieldLandOwnerFarmerCriteria();
        setAllFilters(farmerFieldLandOwnerFarmerCriteria);

        var copy = farmerFieldLandOwnerFarmerCriteria.copy();

        assertThat(farmerFieldLandOwnerFarmerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(farmerFieldLandOwnerFarmerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var farmerFieldLandOwnerFarmerCriteria = new FarmerFieldLandOwnerFarmerCriteria();

        assertThat(farmerFieldLandOwnerFarmerCriteria).hasToString("FarmerFieldLandOwnerFarmerCriteria{}");
    }

    private static void setAllFilters(FarmerFieldLandOwnerFarmerCriteria farmerFieldLandOwnerFarmerCriteria) {
        farmerFieldLandOwnerFarmerCriteria.id();
        farmerFieldLandOwnerFarmerCriteria.farmerId();
        farmerFieldLandOwnerFarmerCriteria.farmerName();
        farmerFieldLandOwnerFarmerCriteria.nicNo();
        farmerFieldLandOwnerFarmerCriteria.addressFirstLine();
        farmerFieldLandOwnerFarmerCriteria.contactNoEmail();
        farmerFieldLandOwnerFarmerCriteria.provinceId();
        farmerFieldLandOwnerFarmerCriteria.districtId();
        farmerFieldLandOwnerFarmerCriteria.dsId();
        farmerFieldLandOwnerFarmerCriteria.gnId();
        farmerFieldLandOwnerFarmerCriteria.city();
        farmerFieldLandOwnerFarmerCriteria.addedDate();
        farmerFieldLandOwnerFarmerCriteria.distinct();
    }

    private static Condition<FarmerFieldLandOwnerFarmerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<FarmerFieldLandOwnerFarmerCriteria> copyFiltersAre(
        FarmerFieldLandOwnerFarmerCriteria copy,
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
