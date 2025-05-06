package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.IndexPolicy} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.IndexPolicyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /index-policies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IndexPolicyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private IntegerFilter stageNo;

    private IntegerFilter indexStatus;

    private LongFilter policyId;

    private LongFilter seasonId;

    private LongFilter cropVarietyId;

    private LongFilter cropId;

    private LongFilter weatherStationId;

    private Boolean distinct;

    public IndexPolicyCriteria() {}

    public IndexPolicyCriteria(IndexPolicyCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(LocalDateFilter::copy).orElse(null);
        this.stageNo = other.optionalStageNo().map(IntegerFilter::copy).orElse(null);
        this.indexStatus = other.optionalIndexStatus().map(IntegerFilter::copy).orElse(null);
        this.policyId = other.optionalPolicyId().map(LongFilter::copy).orElse(null);
        this.seasonId = other.optionalSeasonId().map(LongFilter::copy).orElse(null);
        this.cropVarietyId = other.optionalCropVarietyId().map(LongFilter::copy).orElse(null);
        this.cropId = other.optionalCropId().map(LongFilter::copy).orElse(null);
        this.weatherStationId = other.optionalWeatherStationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public IndexPolicyCriteria copy() {
        return new IndexPolicyCriteria(this);
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

    public IntegerFilter getStageNo() {
        return stageNo;
    }

    public Optional<IntegerFilter> optionalStageNo() {
        return Optional.ofNullable(stageNo);
    }

    public IntegerFilter stageNo() {
        if (stageNo == null) {
            setStageNo(new IntegerFilter());
        }
        return stageNo;
    }

    public void setStageNo(IntegerFilter stageNo) {
        this.stageNo = stageNo;
    }

    public IntegerFilter getIndexStatus() {
        return indexStatus;
    }

    public Optional<IntegerFilter> optionalIndexStatus() {
        return Optional.ofNullable(indexStatus);
    }

    public IntegerFilter indexStatus() {
        if (indexStatus == null) {
            setIndexStatus(new IntegerFilter());
        }
        return indexStatus;
    }

    public void setIndexStatus(IntegerFilter indexStatus) {
        this.indexStatus = indexStatus;
    }

    public LongFilter getPolicyId() {
        return policyId;
    }

    public Optional<LongFilter> optionalPolicyId() {
        return Optional.ofNullable(policyId);
    }

    public LongFilter policyId() {
        if (policyId == null) {
            setPolicyId(new LongFilter());
        }
        return policyId;
    }

    public void setPolicyId(LongFilter policyId) {
        this.policyId = policyId;
    }

    public LongFilter getSeasonId() {
        return seasonId;
    }

    public Optional<LongFilter> optionalSeasonId() {
        return Optional.ofNullable(seasonId);
    }

    public LongFilter seasonId() {
        if (seasonId == null) {
            setSeasonId(new LongFilter());
        }
        return seasonId;
    }

    public void setSeasonId(LongFilter seasonId) {
        this.seasonId = seasonId;
    }

    public LongFilter getCropVarietyId() {
        return cropVarietyId;
    }

    public Optional<LongFilter> optionalCropVarietyId() {
        return Optional.ofNullable(cropVarietyId);
    }

    public LongFilter cropVarietyId() {
        if (cropVarietyId == null) {
            setCropVarietyId(new LongFilter());
        }
        return cropVarietyId;
    }

    public void setCropVarietyId(LongFilter cropVarietyId) {
        this.cropVarietyId = cropVarietyId;
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

    public LongFilter getWeatherStationId() {
        return weatherStationId;
    }

    public Optional<LongFilter> optionalWeatherStationId() {
        return Optional.ofNullable(weatherStationId);
    }

    public LongFilter weatherStationId() {
        if (weatherStationId == null) {
            setWeatherStationId(new LongFilter());
        }
        return weatherStationId;
    }

    public void setWeatherStationId(LongFilter weatherStationId) {
        this.weatherStationId = weatherStationId;
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
        final IndexPolicyCriteria that = (IndexPolicyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(stageNo, that.stageNo) &&
            Objects.equals(indexStatus, that.indexStatus) &&
            Objects.equals(policyId, that.policyId) &&
            Objects.equals(seasonId, that.seasonId) &&
            Objects.equals(cropVarietyId, that.cropVarietyId) &&
            Objects.equals(cropId, that.cropId) &&
            Objects.equals(weatherStationId, that.weatherStationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            startDate,
            endDate,
            stageNo,
            indexStatus,
            policyId,
            seasonId,
            cropVarietyId,
            cropId,
            weatherStationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndexPolicyCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalStageNo().map(f -> "stageNo=" + f + ", ").orElse("") +
            optionalIndexStatus().map(f -> "indexStatus=" + f + ", ").orElse("") +
            optionalPolicyId().map(f -> "policyId=" + f + ", ").orElse("") +
            optionalSeasonId().map(f -> "seasonId=" + f + ", ").orElse("") +
            optionalCropVarietyId().map(f -> "cropVarietyId=" + f + ", ").orElse("") +
            optionalCropId().map(f -> "cropId=" + f + ", ").orElse("") +
            optionalWeatherStationId().map(f -> "weatherStationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
