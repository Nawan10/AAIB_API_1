package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.CropVariety} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.CropVarietyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /crop-varieties?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CropVarietyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter noOfStages;

    private StringFilter image;

    private StringFilter description;

    private IntegerFilter addedBy;

    private LocalDateFilter createdAt;

    private LongFilter cropId;

    private LongFilter cropDurationId;

    private Boolean distinct;

    public CropVarietyCriteria() {}

    public CropVarietyCriteria(CropVarietyCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.noOfStages = other.optionalNoOfStages().map(IntegerFilter::copy).orElse(null);
        this.image = other.optionalImage().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.addedBy = other.optionalAddedBy().map(IntegerFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(LocalDateFilter::copy).orElse(null);
        this.cropId = other.optionalCropId().map(LongFilter::copy).orElse(null);
        this.cropDurationId = other.optionalCropDurationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CropVarietyCriteria copy() {
        return new CropVarietyCriteria(this);
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

    public IntegerFilter getNoOfStages() {
        return noOfStages;
    }

    public Optional<IntegerFilter> optionalNoOfStages() {
        return Optional.ofNullable(noOfStages);
    }

    public IntegerFilter noOfStages() {
        if (noOfStages == null) {
            setNoOfStages(new IntegerFilter());
        }
        return noOfStages;
    }

    public void setNoOfStages(IntegerFilter noOfStages) {
        this.noOfStages = noOfStages;
    }

    public StringFilter getImage() {
        return image;
    }

    public Optional<StringFilter> optionalImage() {
        return Optional.ofNullable(image);
    }

    public StringFilter image() {
        if (image == null) {
            setImage(new StringFilter());
        }
        return image;
    }

    public void setImage(StringFilter image) {
        this.image = image;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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

    public LocalDateFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<LocalDateFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public LocalDateFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new LocalDateFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(LocalDateFilter createdAt) {
        this.createdAt = createdAt;
    }

    public LongFilter getCropId() {
        return cropId;
    }

    public Optional<LongFilter> optionalCropId() {
        return Optional.ofNullable(cropId);
    }

    public LongFilter cropId() {
        if (cropId == null) {
            setCropId(new LongFilter());
        }
        return cropId;
    }

    public void setCropId(LongFilter cropId) {
        this.cropId = cropId;
    }

    public LongFilter getCropDurationId() {
        return cropDurationId;
    }

    public Optional<LongFilter> optionalCropDurationId() {
        return Optional.ofNullable(cropDurationId);
    }

    public LongFilter cropDurationId() {
        if (cropDurationId == null) {
            setCropDurationId(new LongFilter());
        }
        return cropDurationId;
    }

    public void setCropDurationId(LongFilter cropDurationId) {
        this.cropDurationId = cropDurationId;
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
        final CropVarietyCriteria that = (CropVarietyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(noOfStages, that.noOfStages) &&
            Objects.equals(image, that.image) &&
            Objects.equals(description, that.description) &&
            Objects.equals(addedBy, that.addedBy) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(cropId, that.cropId) &&
            Objects.equals(cropDurationId, that.cropDurationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, noOfStages, image, description, addedBy, createdAt, cropId, cropDurationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CropVarietyCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalNoOfStages().map(f -> "noOfStages=" + f + ", ").orElse("") +
            optionalImage().map(f -> "image=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalAddedBy().map(f -> "addedBy=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalCropId().map(f -> "cropId=" + f + ", ").orElse("") +
            optionalCropDurationId().map(f -> "cropDurationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
