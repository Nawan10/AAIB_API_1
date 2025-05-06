package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CropVarietyCriteriaTest {

    @Test
    void newCropVarietyCriteriaHasAllFiltersNullTest() {
        var cropVarietyCriteria = new CropVarietyCriteria();
        assertThat(cropVarietyCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cropVarietyCriteriaFluentMethodsCreatesFiltersTest() {
        var cropVarietyCriteria = new CropVarietyCriteria();

        setAllFilters(cropVarietyCriteria);

        assertThat(cropVarietyCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cropVarietyCriteriaCopyCreatesNullFilterTest() {
        var cropVarietyCriteria = new CropVarietyCriteria();
        var copy = cropVarietyCriteria.copy();

        assertThat(cropVarietyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cropVarietyCriteria)
        );
    }

    @Test
    void cropVarietyCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cropVarietyCriteria = new CropVarietyCriteria();
        setAllFilters(cropVarietyCriteria);

        var copy = cropVarietyCriteria.copy();

        assertThat(cropVarietyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cropVarietyCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cropVarietyCriteria = new CropVarietyCriteria();

        assertThat(cropVarietyCriteria).hasToString("CropVarietyCriteria{}");
    }

    private static void setAllFilters(CropVarietyCriteria cropVarietyCriteria) {
        cropVarietyCriteria.id();
        cropVarietyCriteria.name();
        cropVarietyCriteria.noOfStages();
        cropVarietyCriteria.image();
        cropVarietyCriteria.description();
        cropVarietyCriteria.addedBy();
        cropVarietyCriteria.createdAt();
        cropVarietyCriteria.cropId();
        cropVarietyCriteria.cropDurationId();
        cropVarietyCriteria.distinct();
    }

    private static Condition<CropVarietyCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getNoOfStages()) &&
                condition.apply(criteria.getImage()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getCropId()) &&
                condition.apply(criteria.getCropDurationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CropVarietyCriteria> copyFiltersAre(CropVarietyCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getNoOfStages(), copy.getNoOfStages()) &&
                condition.apply(criteria.getImage(), copy.getImage()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getAddedBy(), copy.getAddedBy()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getCropId(), copy.getCropId()) &&
                condition.apply(criteria.getCropDurationId(), copy.getCropDurationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
