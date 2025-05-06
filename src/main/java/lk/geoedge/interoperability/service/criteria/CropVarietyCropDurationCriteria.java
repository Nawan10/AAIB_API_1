package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.CropVarietyCropDuration} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.CropVarietyCropDurationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /crop-variety-crop-durations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CropVarietyCropDurationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter duration;

    private StringFilter name;

    private StringFilter stages;

    private IntegerFilter addedBy;

    private LocalDateFilter addedDate;

    private Boolean distinct;

    public CropVarietyCropDurationCriteria() {}

    public CropVarietyCropDurationCriteria(CropVarietyCropDurationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.duration = other.optionalDuration().map(IntegerFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.stages = other.optionalStages().map(StringFilter::copy).orElse(null);
        this.addedBy = other.optionalAddedBy().map(IntegerFilter::copy).orElse(null);
        this.addedDate = other.optionalAddedDate().map(LocalDateFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CropVarietyCropDurationCriteria copy() {
        return new CropVarietyCropDurationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getDuration() {
        return duration;
    }

    public Optional<IntegerFilter> optionalDuration() {
        return Optional.ofNullable(duration);
    }

    public IntegerFilter duration() {
        if (duration == null) {
            setDuration(new IntegerFilter());
        }
        return duration;
    }

    public void setDuration(IntegerFilter duration) {
        this.duration = duration;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getStages() {
        return stages;
    }

    public Optional<StringFilter> optionalStages() {
        return Optional.ofNullable(stages);
    }

    public StringFilter stages() {
        if (stages == null) {
            setStages(new StringFilter());
        }
        return stages;
    }

    public void setStages(StringFilter stages) {
        this.stages = stages;
    }

    public IntegerFilter getAddedBy() {
        return addedBy;
    }

    public Optional<IntegerFilter> optionalAddedBy() {
        return Optional.ofNullable(addedBy);
    }

    public IntegerFilter addedBy() {
        if (addedBy == null) {
            setAddedBy(new IntegerFilter());
        }
        return addedBy;
    }

    public void setAddedBy(IntegerFilter addedBy) {
        this.addedBy = addedBy;
    }

    public LocalDateFilter getAddedDate() {
        return addedDate;
    }

    public Optional<LocalDateFilter> optionalAddedDate() {
        return Optional.ofNullable(addedDate);
    }

    public LocalDateFilter addedDate() {
        if (addedDate == null) {
            setAddedDate(new LocalDateFilter());
        }
        return addedDate;
    }

    public void setAddedDate(LocalDateFilter addedDate) {
        this.addedDate = addedDate;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CropVarietyCropDurationCriteria that = (CropVarietyCropDurationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(duration, that.duration) &&
            Objects.equals(name, that.name) &&
            Objects.equals(stages, that.stages) &&
            Objects.equals(addedBy, that.addedBy) &&
            Objects.equals(addedDate, that.addedDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, duration, name, stages, addedBy, addedDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CropVarietyCropDurationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDuration().map(f -> "duration=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalStages().map(f -> "stages=" + f + ", ").orElse("") +
            optionalAddedBy().map(f -> "addedBy=" + f + ", ").orElse("") +
            optionalAddedDate().map(f -> "addedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
