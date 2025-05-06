package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CultivatedLandFarmerCriteriaTest {

    @Test
    void newCultivatedLandFarmerCriteriaHasAllFiltersNullTest() {
        var cultivatedLandFarmerCriteria = new CultivatedLandFarmerCriteria();
        assertThat(cultivatedLandFarmerCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cultivatedLandFarmerCriteriaFluentMethodsCreatesFiltersTest() {
        var cultivatedLandFarmerCriteria = new CultivatedLandFarmerCriteria();

        setAllFilters(cultivatedLandFarmerCriteria);

        assertThat(cultivatedLandFarmerCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cultivatedLandFarmerCriteriaCopyCreatesNullFilterTest() {
        var cultivatedLandFarmerCriteria = new CultivatedLandFarmerCriteria();
        var copy = cultivatedLandFarmerCriteria.copy();

        assertThat(cultivatedLandFarmerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandFarmerCriteria)
        );
    }

    @Test
    void cultivatedLandFarmerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cultivatedLandFarmerCriteria = new CultivatedLandFarmerCriteria();
        setAllFilters(cultivatedLandFarmerCriteria);

        var copy = cultivatedLandFarmerCriteria.copy();

        assertThat(cultivatedLandFarmerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandFarmerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cultivatedLandFarmerCriteria = new CultivatedLandFarmerCriteria();

        assertThat(cultivatedLandFarmerCriteria).hasToString("CultivatedLandFarmerCriteria{}");
    }

    private static void setAllFilters(CultivatedLandFarmerCriteria cultivatedLandFarmerCriteria) {
        cultivatedLandFarmerCriteria.id();
        cultivatedLandFarmerCriteria.farmerId();
        cultivatedLandFarmerCriteria.farmerName();
        cultivatedLandFarmerCriteria.nicNo();
        cultivatedLandFarmerCriteria.addressFirstLine();
        cultivatedLandFarmerCriteria.contactNoEmail();
        cultivatedLandFarmerCriteria.provinceId();
        cultivatedLandFarmerCriteria.districtId();
        cultivatedLandFarmerCriteria.dsId();
        cultivatedLandFarmerCriteria.gnId();
        cultivatedLandFarmerCriteria.city();
        cultivatedLandFarmerCriteria.addedDate();
        cultivatedLandFarmerCriteria.distinct();
    }

    private static Condition<CultivatedLandFarmerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CultivatedLandFarmerCriteria> copyFiltersAre(
        CultivatedLandFarmerCriteria copy,
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
