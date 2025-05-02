package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CultivatedLandDamageReportCriteriaTest {

    @Test
    void newCultivatedLandDamageReportCriteriaHasAllFiltersNullTest() {
        var cultivatedLandDamageReportCriteria = new CultivatedLandDamageReportCriteria();
        assertThat(cultivatedLandDamageReportCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cultivatedLandDamageReportCriteriaFluentMethodsCreatesFiltersTest() {
        var cultivatedLandDamageReportCriteria = new CultivatedLandDamageReportCriteria();

        setAllFilters(cultivatedLandDamageReportCriteria);

        assertThat(cultivatedLandDamageReportCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cultivatedLandDamageReportCriteriaCopyCreatesNullFilterTest() {
        var cultivatedLandDamageReportCriteria = new CultivatedLandDamageReportCriteria();
        var copy = cultivatedLandDamageReportCriteria.copy();

        assertThat(cultivatedLandDamageReportCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandDamageReportCriteria)
        );
    }

    @Test
    void cultivatedLandDamageReportCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cultivatedLandDamageReportCriteria = new CultivatedLandDamageReportCriteria();
        setAllFilters(cultivatedLandDamageReportCriteria);

        var copy = cultivatedLandDamageReportCriteria.copy();

        assertThat(cultivatedLandDamageReportCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandDamageReportCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cultivatedLandDamageReportCriteria = new CultivatedLandDamageReportCriteria();

        assertThat(cultivatedLandDamageReportCriteria).hasToString("CultivatedLandDamageReportCriteria{}");
    }

    private static void setAllFilters(CultivatedLandDamageReportCriteria cultivatedLandDamageReportCriteria) {
        cultivatedLandDamageReportCriteria.id();
        cultivatedLandDamageReportCriteria.damageReasonId();
        cultivatedLandDamageReportCriteria.damageServerityId();
        cultivatedLandDamageReportCriteria.damageDateMonitor();
        cultivatedLandDamageReportCriteria.description();
        cultivatedLandDamageReportCriteria.farmerComment();
        cultivatedLandDamageReportCriteria.estimatedYield();
        cultivatedLandDamageReportCriteria.createdAt();
        cultivatedLandDamageReportCriteria.addedBy();
        cultivatedLandDamageReportCriteria.cropId();
        cultivatedLandDamageReportCriteria.damageCategoryId();
        cultivatedLandDamageReportCriteria.damageTypeId();
        cultivatedLandDamageReportCriteria.distinct();
    }

    private static Condition<CultivatedLandDamageReportCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDamageReasonId()) &&
                condition.apply(criteria.getDamageServerityId()) &&
                condition.apply(criteria.getDamageDateMonitor()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getFarmerComment()) &&
                condition.apply(criteria.getEstimatedYield()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getCropId()) &&
                condition.apply(criteria.getDamageCategoryId()) &&
                condition.apply(criteria.getDamageTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CultivatedLandDamageReportCriteria> copyFiltersAre(
        CultivatedLandDamageReportCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDamageReasonId(), copy.getDamageReasonId()) &&
                condition.apply(criteria.getDamageServerityId(), copy.getDamageServerityId()) &&
                condition.apply(criteria.getDamageDateMonitor(), copy.getDamageDateMonitor()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getFarmerComment(), copy.getFarmerComment()) &&
                condition.apply(criteria.getEstimatedYield(), copy.getEstimatedYield()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy(), copy.getAddedBy()) &&
                condition.apply(criteria.getCropId(), copy.getCropId()) &&
                condition.apply(criteria.getDamageCategoryId(), copy.getDamageCategoryId()) &&
                condition.apply(criteria.getDamageTypeId(), copy.getDamageTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
