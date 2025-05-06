package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CropDamageCriteriaTest {

    @Test
    void newCropDamageCriteriaHasAllFiltersNullTest() {
        var cropDamageCriteria = new CropDamageCriteria();
        assertThat(cropDamageCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cropDamageCriteriaFluentMethodsCreatesFiltersTest() {
        var cropDamageCriteria = new CropDamageCriteria();

        setAllFilters(cropDamageCriteria);

        assertThat(cropDamageCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cropDamageCriteriaCopyCreatesNullFilterTest() {
        var cropDamageCriteria = new CropDamageCriteria();
        var copy = cropDamageCriteria.copy();

        assertThat(cropDamageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cropDamageCriteria)
        );
    }

    @Test
    void cropDamageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cropDamageCriteria = new CropDamageCriteria();
        setAllFilters(cropDamageCriteria);

        var copy = cropDamageCriteria.copy();

        assertThat(cropDamageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cropDamageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cropDamageCriteria = new CropDamageCriteria();

        assertThat(cropDamageCriteria).hasToString("CropDamageCriteria{}");
    }

    private static void setAllFilters(CropDamageCriteria cropDamageCriteria) {
        cropDamageCriteria.id();
        cropDamageCriteria.addedBy();
        cropDamageCriteria.createdAt();
        cropDamageCriteria.cropId();
        cropDamageCriteria.damageId();
        cropDamageCriteria.distinct();
    }

    private static Condition<CropDamageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getCropId()) &&
                condition.apply(criteria.getDamageId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CropDamageCriteria> copyFiltersAre(CropDamageCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAddedBy(), copy.getAddedBy()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getCropId(), copy.getCropId()) &&
                condition.apply(criteria.getDamageId(), copy.getDamageId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
