package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CropDamageDamageCriteriaTest {

    @Test
    void newCropDamageDamageCriteriaHasAllFiltersNullTest() {
        var cropDamageDamageCriteria = new CropDamageDamageCriteria();
        assertThat(cropDamageDamageCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cropDamageDamageCriteriaFluentMethodsCreatesFiltersTest() {
        var cropDamageDamageCriteria = new CropDamageDamageCriteria();

        setAllFilters(cropDamageDamageCriteria);

        assertThat(cropDamageDamageCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cropDamageDamageCriteriaCopyCreatesNullFilterTest() {
        var cropDamageDamageCriteria = new CropDamageDamageCriteria();
        var copy = cropDamageDamageCriteria.copy();

        assertThat(cropDamageDamageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cropDamageDamageCriteria)
        );
    }

    @Test
    void cropDamageDamageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cropDamageDamageCriteria = new CropDamageDamageCriteria();
        setAllFilters(cropDamageDamageCriteria);

        var copy = cropDamageDamageCriteria.copy();

        assertThat(cropDamageDamageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cropDamageDamageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cropDamageDamageCriteria = new CropDamageDamageCriteria();

        assertThat(cropDamageDamageCriteria).hasToString("CropDamageDamageCriteria{}");
    }

    private static void setAllFilters(CropDamageDamageCriteria cropDamageDamageCriteria) {
        cropDamageDamageCriteria.id();
        cropDamageDamageCriteria.damageName();
        cropDamageDamageCriteria.damageCode();
        cropDamageDamageCriteria.damageFamily();
        cropDamageDamageCriteria.damageGenus();
        cropDamageDamageCriteria.damageSpecies();
        cropDamageDamageCriteria.createdAt();
        cropDamageDamageCriteria.addedBy();
        cropDamageDamageCriteria.distinct();
    }

    private static Condition<CropDamageDamageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDamageName()) &&
                condition.apply(criteria.getDamageCode()) &&
                condition.apply(criteria.getDamageFamily()) &&
                condition.apply(criteria.getDamageGenus()) &&
                condition.apply(criteria.getDamageSpecies()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CropDamageDamageCriteria> copyFiltersAre(
        CropDamageDamageCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDamageName(), copy.getDamageName()) &&
                condition.apply(criteria.getDamageCode(), copy.getDamageCode()) &&
                condition.apply(criteria.getDamageFamily(), copy.getDamageFamily()) &&
                condition.apply(criteria.getDamageGenus(), copy.getDamageGenus()) &&
                condition.apply(criteria.getDamageSpecies(), copy.getDamageSpecies()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getAddedBy(), copy.getAddedBy()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
