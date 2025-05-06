package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CultivatedLandFarmersFarmerCriteriaTest {

    @Test
    void newCultivatedLandFarmersFarmerCriteriaHasAllFiltersNullTest() {
        var cultivatedLandFarmersFarmerCriteria = new CultivatedLandFarmersFarmerCriteria();
        assertThat(cultivatedLandFarmersFarmerCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cultivatedLandFarmersFarmerCriteriaFluentMethodsCreatesFiltersTest() {
        var cultivatedLandFarmersFarmerCriteria = new CultivatedLandFarmersFarmerCriteria();

        setAllFilters(cultivatedLandFarmersFarmerCriteria);

        assertThat(cultivatedLandFarmersFarmerCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cultivatedLandFarmersFarmerCriteriaCopyCreatesNullFilterTest() {
        var cultivatedLandFarmersFarmerCriteria = new CultivatedLandFarmersFarmerCriteria();
        var copy = cultivatedLandFarmersFarmerCriteria.copy();

        assertThat(cultivatedLandFarmersFarmerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandFarmersFarmerCriteria)
        );
    }

    @Test
    void cultivatedLandFarmersFarmerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cultivatedLandFarmersFarmerCriteria = new CultivatedLandFarmersFarmerCriteria();
        setAllFilters(cultivatedLandFarmersFarmerCriteria);

        var copy = cultivatedLandFarmersFarmerCriteria.copy();

        assertThat(cultivatedLandFarmersFarmerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandFarmersFarmerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cultivatedLandFarmersFarmerCriteria = new CultivatedLandFarmersFarmerCriteria();

        assertThat(cultivatedLandFarmersFarmerCriteria).hasToString("CultivatedLandFarmersFarmerCriteria{}");
    }

    private static void setAllFilters(CultivatedLandFarmersFarmerCriteria cultivatedLandFarmersFarmerCriteria) {
        cultivatedLandFarmersFarmerCriteria.id();
        cultivatedLandFarmersFarmerCriteria.farmerId();
        cultivatedLandFarmersFarmerCriteria.farmerName();
        cultivatedLandFarmersFarmerCriteria.nicNo();
        cultivatedLandFarmersFarmerCriteria.addressFirstLine();
        cultivatedLandFarmersFarmerCriteria.contactNoEmail();
        cultivatedLandFarmersFarmerCriteria.provinceId();
        cultivatedLandFarmersFarmerCriteria.districtId();
        cultivatedLandFarmersFarmerCriteria.dsId();
        cultivatedLandFarmersFarmerCriteria.gnId();
        cultivatedLandFarmersFarmerCriteria.city();
        cultivatedLandFarmersFarmerCriteria.addedDate();
        cultivatedLandFarmersFarmerCriteria.distinct();
    }

    private static Condition<CultivatedLandFarmersFarmerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CultivatedLandFarmersFarmerCriteria> copyFiltersAre(
        CultivatedLandFarmersFarmerCriteria copy,
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
