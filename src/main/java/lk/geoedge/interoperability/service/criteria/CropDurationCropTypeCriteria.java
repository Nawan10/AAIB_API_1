package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.CropDurationCropType} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.CropDurationCropTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /crop-duration-crop-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CropDurationCropTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter crop;

    private StringFilter image;

    private IntegerFilter mainCrop;

    private StringFilter cropCode;

    private StringFilter noOfStages;

    private StringFilter description;

    private IntegerFilter cropTypesId;

    private IntegerFilter unitsId;

    private DoubleFilter area;

    private DoubleFilter sumInsured;

    private DoubleFilter minSumInsured;

    private DoubleFilter maxSumInsured;

    private DoubleFilter subsidisedPremiumRate;

    private Boolean distinct;

    public CropDurationCropTypeCriteria() {}

    public CropDurationCropTypeCriteria(CropDurationCropTypeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.crop = other.optionalCrop().map(StringFilter::copy).orElse(null);
        this.image = other.optionalImage().map(StringFilter::copy).orElse(null);
        this.mainCrop = other.optionalMainCrop().map(IntegerFilter::copy).orElse(null);
        this.cropCode = other.optionalCropCode().map(StringFilter::copy).orElse(null);
        this.noOfStages = other.optionalNoOfStages().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.cropTypesId = other.optionalCropTypesId().map(IntegerFilter::copy).orElse(null);
        this.unitsId = other.optionalUnitsId().map(IntegerFilter::copy).orElse(null);
        this.area = other.optionalArea().map(DoubleFilter::copy).orElse(null);
        this.sumInsured = other.optionalSumInsured().map(DoubleFilter::copy).orElse(null);
        this.minSumInsured = other.optionalMinSumInsured().map(DoubleFilter::copy).orElse(null);
        this.maxSumInsured = other.optionalMaxSumInsured().map(DoubleFilter::copy).orElse(null);
        this.subsidisedPremiumRate = other.optionalSubsidisedPremiumRate().map(DoubleFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CropDurationCropTypeCriteria copy() {
        return new CropDurationCropTypeCriteria(this);
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

    public StringFilter getCrop() {
        return crop;
    }

    public Optional<StringFilter> optionalCrop() {
        return Optional.ofNullable(crop);
    }

    public StringFilter crop() {
        if (crop == null) {
            setCrop(new StringFilter());
        }
        return crop;
    }

    public void setCrop(StringFilter crop) {
        this.crop = crop;
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

    public IntegerFilter getMainCrop() {
        return mainCrop;
    }

    public Optional<IntegerFilter> optionalMainCrop() {
        return Optional.ofNullable(mainCrop);
    }

    public IntegerFilter mainCrop() {
        if (mainCrop == null) {
            setMainCrop(new IntegerFilter());
        }
        return mainCrop;
    }

    public void setMainCrop(IntegerFilter mainCrop) {
        this.mainCrop = mainCrop;
    }

    public StringFilter getCropCode() {
        return cropCode;
    }

    public Optional<StringFilter> optionalCropCode() {
        return Optional.ofNullable(cropCode);
    }

    public StringFilter cropCode() {
        if (cropCode == null) {
            setCropCode(new StringFilter());
        }
        return cropCode;
    }

    public void setCropCode(StringFilter cropCode) {
        this.cropCode = cropCode;
    }

    public StringFilter getNoOfStages() {
        return noOfStages;
    }

    public Optional<StringFilter> optionalNoOfStages() {
        return Optional.ofNullable(noOfStages);
    }

    public StringFilter noOfStages() {
        if (noOfStages == null) {
            setNoOfStages(new StringFilter());
        }
        return noOfStages;
    }

    public void setNoOfStages(StringFilter noOfStages) {
        this.noOfStages = noOfStages;
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

    public IntegerFilter getCropTypesId() {
        return cropTypesId;
    }

    public Optional<IntegerFilter> optionalCropTypesId() {
        return Optional.ofNullable(cropTypesId);
    }

    public IntegerFilter cropTypesId() {
        if (cropTypesId == null) {
            setCropTypesId(new IntegerFilter());
        }
        return cropTypesId;
    }

    public void setCropTypesId(IntegerFilter cropTypesId) {
        this.cropTypesId = cropTypesId;
    }

    public IntegerFilter getUnitsId() {
        return unitsId;
    }

    public Optional<IntegerFilter> optionalUnitsId() {
        return Optional.ofNullable(unitsId);
    }

    public IntegerFilter unitsId() {
        if (unitsId == null) {
            setUnitsId(new IntegerFilter());
        }
        return unitsId;
    }

    public void setUnitsId(IntegerFilter unitsId) {
        this.unitsId = unitsId;
    }

    public DoubleFilter getArea() {
        return area;
    }

    public Optional<DoubleFilter> optionalArea() {
        return Optional.ofNullable(area);
    }

    public DoubleFilter area() {
        if (area == null) {
            setArea(new DoubleFilter());
        }
        return area;
    }

    public void setArea(DoubleFilter area) {
        this.area = area;
    }

    public DoubleFilter getSumInsured() {
        return sumInsured;
    }

    public Optional<DoubleFilter> optionalSumInsured() {
        return Optional.ofNullable(sumInsured);
    }

    public DoubleFilter sumInsured() {
        if (sumInsured == null) {
            setSumInsured(new DoubleFilter());
        }
        return sumInsured;
    }

    public void setSumInsured(DoubleFilter sumInsured) {
        this.sumInsured = sumInsured;
    }

    public DoubleFilter getMinSumInsured() {
        return minSumInsured;
    }

    public Optional<DoubleFilter> optionalMinSumInsured() {
        return Optional.ofNullable(minSumInsured);
    }

    public DoubleFilter minSumInsured() {
        if (minSumInsured == null) {
            setMinSumInsured(new DoubleFilter());
        }
        return minSumInsured;
    }

    public void setMinSumInsured(DoubleFilter minSumInsured) {
        this.minSumInsured = minSumInsured;
    }

    public DoubleFilter getMaxSumInsured() {
        return maxSumInsured;
    }

    public Optional<DoubleFilter> optionalMaxSumInsured() {
        return Optional.ofNullable(maxSumInsured);
    }

    public DoubleFilter maxSumInsured() {
        if (maxSumInsured == null) {
            setMaxSumInsured(new DoubleFilter());
        }
        return maxSumInsured;
    }

    public void setMaxSumInsured(DoubleFilter maxSumInsured) {
        this.maxSumInsured = maxSumInsured;
    }

    public DoubleFilter getSubsidisedPremiumRate() {
        return subsidisedPremiumRate;
    }

    public Optional<DoubleFilter> optionalSubsidisedPremiumRate() {
        return Optional.ofNullable(subsidisedPremiumRate);
    }

    public DoubleFilter subsidisedPremiumRate() {
        if (subsidisedPremiumRate == null) {
            setSubsidisedPremiumRate(new DoubleFilter());
        }
        return subsidisedPremiumRate;
    }

    public void setSubsidisedPremiumRate(DoubleFilter subsidisedPremiumRate) {
        this.subsidisedPremiumRate = subsidisedPremiumRate;
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
        final CropDurationCropTypeCriteria that = (CropDurationCropTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(crop, that.crop) &&
            Objects.equals(image, that.image) &&
            Objects.equals(mainCrop, that.mainCrop) &&
            Objects.equals(cropCode, that.cropCode) &&
            Objects.equals(noOfStages, that.noOfStages) &&
            Objects.equals(description, that.description) &&
            Objects.equals(cropTypesId, that.cropTypesId) &&
            Objects.equals(unitsId, that.unitsId) &&
            Objects.equals(area, that.area) &&
            Objects.equals(sumInsured, that.sumInsured) &&
            Objects.equals(minSumInsured, that.minSumInsured) &&
            Objects.equals(maxSumInsured, that.maxSumInsured) &&
            Objects.equals(subsidisedPremiumRate, that.subsidisedPremiumRate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            crop,
            image,
            mainCrop,
            cropCode,
            noOfStages,
            description,
            cropTypesId,
            unitsId,
            area,
            sumInsured,
            minSumInsured,
            maxSumInsured,
            subsidisedPremiumRate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CropDurationCropTypeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCrop().map(f -> "crop=" + f + ", ").orElse("") +
            optionalImage().map(f -> "image=" + f + ", ").orElse("") +
            optionalMainCrop().map(f -> "mainCrop=" + f + ", ").orElse("") +
            optionalCropCode().map(f -> "cropCode=" + f + ", ").orElse("") +
            optionalNoOfStages().map(f -> "noOfStages=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalCropTypesId().map(f -> "cropTypesId=" + f + ", ").orElse("") +
            optionalUnitsId().map(f -> "unitsId=" + f + ", ").orElse("") +
            optionalArea().map(f -> "area=" + f + ", ").orElse("") +
            optionalSumInsured().map(f -> "sumInsured=" + f + ", ").orElse("") +
            optionalMinSumInsured().map(f -> "minSumInsured=" + f + ", ").orElse("") +
            optionalMaxSumInsured().map(f -> "maxSumInsured=" + f + ", ").orElse("") +
            optionalSubsidisedPremiumRate().map(f -> "subsidisedPremiumRate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
