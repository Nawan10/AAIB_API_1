package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.CultivatedCrop} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.CultivatedCropResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cultivated-crops?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CultivatedCropCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter cultivatedExtend;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private DoubleFilter yield;

    private StringFilter unitId;

    private LocalDateFilter createdAt;

    private StringFilter addedBy;

    private LongFilter cultivatedLandId;

    private LongFilter cropId;

    private Boolean distinct;

    public CultivatedCropCriteria() {}

    public CultivatedCropCriteria(CultivatedCropCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.cultivatedExtend = other.optionalCultivatedExtend().map(DoubleFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(LocalDateFilter::copy).orElse(null);
        this.yield = other.optionalYield().map(DoubleFilter::copy).orElse(null);
        this.unitId = other.optionalUnitId().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(LocalDateFilter::copy).orElse(null);
        this.addedBy = other.optionalAddedBy().map(StringFilter::copy).orElse(null);
        this.cultivatedLandId = other.optionalCultivatedLandId().map(LongFilter::copy).orElse(null);
        this.cropId = other.optionalCropId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CultivatedCropCriteria copy() {
        return new CultivatedCropCriteria(this);
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

    public DoubleFilter getCultivatedExtend() {
        return cultivatedExtend;
    }

    public Optional<DoubleFilter> optionalCultivatedExtend() {
        return Optional.ofNullable(cultivatedExtend);
    }

    public DoubleFilter cultivatedExtend() {
        if (cultivatedExtend == null) {
            setCultivatedExtend(new DoubleFilter());
        }
        return cultivatedExtend;
    }

    public void setCultivatedExtend(DoubleFilter cultivatedExtend) {
        this.cultivatedExtend = cultivatedExtend;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public Optional<LocalDateFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            setStartDate(new LocalDateFilter());
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public Optional<LocalDateFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            setEndDate(new LocalDateFilter());
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public DoubleFilter getYield() {
        return yield;
    }

    public Optional<DoubleFilter> optionalYield() {
        return Optional.ofNullable(yield);
    }

    public DoubleFilter yield() {
        if (yield == null) {
            setYield(new DoubleFilter());
        }
        return yield;
    }

    public void setYield(DoubleFilter yield) {
        this.yield = yield;
    }

    public StringFilter getUnitId() {
        return unitId;
    }

    public Optional<StringFilter> optionalUnitId() {
        return Optional.ofNullable(unitId);
    }

    public StringFilter unitId() {
        if (unitId == null) {
            setUnitId(new StringFilter());
        }
        return unitId;
    }

    public void setUnitId(StringFilter unitId) {
        this.unitId = unitId;
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

    public StringFilter getAddedBy() {
        return addedBy;
    }

    public Optional<StringFilter> optionalAddedBy() {
        return Optional.ofNullable(addedBy);
    }

    public StringFilter addedBy() {
        if (addedBy == null) {
            setAddedBy(new StringFilter());
        }
        return addedBy;
    }

    public void setAddedBy(StringFilter addedBy) {
        this.addedBy = addedBy;
    }

    public LongFilter getCultivatedLandId() {
        return cultivatedLandId;
    }

    public Optional<LongFilter> optionalCultivatedLandId() {
        return Optional.ofNullable(cultivatedLandId);
    }

    public LongFilter cultivatedLandId() {
        if (cultivatedLandId == null) {
            setCultivatedLandId(new LongFilter());
        }
        return cultivatedLandId;
    }

    public void setCultivatedLandId(LongFilter cultivatedLandId) {
        this.cultivatedLandId = cultivatedLandId;
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
        final CultivatedCropCriteria that = (CultivatedCropCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cultivatedExtend, that.cultivatedExtend) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(yield, that.yield) &&
            Objects.equals(unitId, that.unitId) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(addedBy, that.addedBy) &&
            Objects.equals(cultivatedLandId, that.cultivatedLandId) &&
            Objects.equals(cropId, that.cropId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            cultivatedExtend,
            startDate,
            endDate,
            yield,
            unitId,
            createdAt,
            addedBy,
            cultivatedLandId,
            cropId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CultivatedCropCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCultivatedExtend().map(f -> "cultivatedExtend=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalYield().map(f -> "yield=" + f + ", ").orElse("") +
            optionalUnitId().map(f -> "unitId=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalAddedBy().map(f -> "addedBy=" + f + ", ").orElse("") +
            optionalCultivatedLandId().map(f -> "cultivatedLandId=" + f + ", ").orElse("") +
            optionalCropId().map(f -> "cropId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
