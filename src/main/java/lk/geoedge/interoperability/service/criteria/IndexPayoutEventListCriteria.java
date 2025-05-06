package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.IndexPayoutEventList} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.IndexPayoutEventListResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /index-payout-event-lists?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IndexPayoutEventListCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter indexPayoutEventId;

    private IntegerFilter ascId;

    private IntegerFilter confirmedBy;

    private DoubleFilter cultivatedExtent;

    private DoubleFilter payout;

    private StringFilter confirmedDate;

    private IntegerFilter rejectedBy;

    private StringFilter rejectedDate;

    private StringFilter reason;

    private DoubleFilter finalPayout;

    private IntegerFilter indexPayoutEventStatus;

    private IntegerFilter isApproved;

    private DoubleFilter monitoringRange;

    private IntegerFilter isInsurance;

    private IntegerFilter insuranceCultivatedLand;

    private IntegerFilter indexChequeId;

    private IntegerFilter indexProductId;

    private LongFilter cultivatedFarmerId;

    private LongFilter cultivatedLandId;

    private LongFilter seasonId;

    private Boolean distinct;

    public IndexPayoutEventListCriteria() {}

    public IndexPayoutEventListCriteria(IndexPayoutEventListCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.indexPayoutEventId = other.optionalIndexPayoutEventId().map(IntegerFilter::copy).orElse(null);
        this.ascId = other.optionalAscId().map(IntegerFilter::copy).orElse(null);
        this.confirmedBy = other.optionalConfirmedBy().map(IntegerFilter::copy).orElse(null);
        this.cultivatedExtent = other.optionalCultivatedExtent().map(DoubleFilter::copy).orElse(null);
        this.payout = other.optionalPayout().map(DoubleFilter::copy).orElse(null);
        this.confirmedDate = other.optionalConfirmedDate().map(StringFilter::copy).orElse(null);
        this.rejectedBy = other.optionalRejectedBy().map(IntegerFilter::copy).orElse(null);
        this.rejectedDate = other.optionalRejectedDate().map(StringFilter::copy).orElse(null);
        this.reason = other.optionalReason().map(StringFilter::copy).orElse(null);
        this.finalPayout = other.optionalFinalPayout().map(DoubleFilter::copy).orElse(null);
        this.indexPayoutEventStatus = other.optionalIndexPayoutEventStatus().map(IntegerFilter::copy).orElse(null);
        this.isApproved = other.optionalIsApproved().map(IntegerFilter::copy).orElse(null);
        this.monitoringRange = other.optionalMonitoringRange().map(DoubleFilter::copy).orElse(null);
        this.isInsurance = other.optionalIsInsurance().map(IntegerFilter::copy).orElse(null);
        this.insuranceCultivatedLand = other.optionalInsuranceCultivatedLand().map(IntegerFilter::copy).orElse(null);
        this.indexChequeId = other.optionalIndexChequeId().map(IntegerFilter::copy).orElse(null);
        this.indexProductId = other.optionalIndexProductId().map(IntegerFilter::copy).orElse(null);
        this.cultivatedFarmerId = other.optionalCultivatedFarmerId().map(LongFilter::copy).orElse(null);
        this.cultivatedLandId = other.optionalCultivatedLandId().map(LongFilter::copy).orElse(null);
        this.seasonId = other.optionalSeasonId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public IndexPayoutEventListCriteria copy() {
        return new IndexPayoutEventListCriteria(this);
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

    public IntegerFilter getIndexPayoutEventId() {
        return indexPayoutEventId;
    }

    public Optional<IntegerFilter> optionalIndexPayoutEventId() {
        return Optional.ofNullable(indexPayoutEventId);
    }

    public IntegerFilter indexPayoutEventId() {
        if (indexPayoutEventId == null) {
            setIndexPayoutEventId(new IntegerFilter());
        }
        return indexPayoutEventId;
    }

    public void setIndexPayoutEventId(IntegerFilter indexPayoutEventId) {
        this.indexPayoutEventId = indexPayoutEventId;
    }

    public IntegerFilter getAscId() {
        return ascId;
    }

    public Optional<IntegerFilter> optionalAscId() {
        return Optional.ofNullable(ascId);
    }

    public IntegerFilter ascId() {
        if (ascId == null) {
            setAscId(new IntegerFilter());
        }
        return ascId;
    }

    public void setAscId(IntegerFilter ascId) {
        this.ascId = ascId;
    }

    public IntegerFilter getConfirmedBy() {
        return confirmedBy;
    }

    public Optional<IntegerFilter> optionalConfirmedBy() {
        return Optional.ofNullable(confirmedBy);
    }

    public IntegerFilter confirmedBy() {
        if (confirmedBy == null) {
            setConfirmedBy(new IntegerFilter());
        }
        return confirmedBy;
    }

    public void setConfirmedBy(IntegerFilter confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    public DoubleFilter getCultivatedExtent() {
        return cultivatedExtent;
    }

    public Optional<DoubleFilter> optionalCultivatedExtent() {
        return Optional.ofNullable(cultivatedExtent);
    }

    public DoubleFilter cultivatedExtent() {
        if (cultivatedExtent == null) {
            setCultivatedExtent(new DoubleFilter());
        }
        return cultivatedExtent;
    }

    public void setCultivatedExtent(DoubleFilter cultivatedExtent) {
        this.cultivatedExtent = cultivatedExtent;
    }

    public DoubleFilter getPayout() {
        return payout;
    }

    public Optional<DoubleFilter> optionalPayout() {
        return Optional.ofNullable(payout);
    }

    public DoubleFilter payout() {
        if (payout == null) {
            setPayout(new DoubleFilter());
        }
        return payout;
    }

    public void setPayout(DoubleFilter payout) {
        this.payout = payout;
    }

    public StringFilter getConfirmedDate() {
        return confirmedDate;
    }

    public Optional<StringFilter> optionalConfirmedDate() {
        return Optional.ofNullable(confirmedDate);
    }

    public StringFilter confirmedDate() {
        if (confirmedDate == null) {
            setConfirmedDate(new StringFilter());
        }
        return confirmedDate;
    }

    public void setConfirmedDate(StringFilter confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    public IntegerFilter getRejectedBy() {
        return rejectedBy;
    }

    public Optional<IntegerFilter> optionalRejectedBy() {
        return Optional.ofNullable(rejectedBy);
    }

    public IntegerFilter rejectedBy() {
        if (rejectedBy == null) {
            setRejectedBy(new IntegerFilter());
        }
        return rejectedBy;
    }

    public void setRejectedBy(IntegerFilter rejectedBy) {
        this.rejectedBy = rejectedBy;
    }

    public StringFilter getRejectedDate() {
        return rejectedDate;
    }

    public Optional<StringFilter> optionalRejectedDate() {
        return Optional.ofNullable(rejectedDate);
    }

    public StringFilter rejectedDate() {
        if (rejectedDate == null) {
            setRejectedDate(new StringFilter());
        }
        return rejectedDate;
    }

    public void setRejectedDate(StringFilter rejectedDate) {
        this.rejectedDate = rejectedDate;
    }

    public StringFilter getReason() {
        return reason;
    }

    public Optional<StringFilter> optionalReason() {
        return Optional.ofNullable(reason);
    }

    public StringFilter reason() {
        if (reason == null) {
            setReason(new StringFilter());
        }
        return reason;
    }

    public void setReason(StringFilter reason) {
        this.reason = reason;
    }

    public DoubleFilter getFinalPayout() {
        return finalPayout;
    }

    public Optional<DoubleFilter> optionalFinalPayout() {
        return Optional.ofNullable(finalPayout);
    }

    public DoubleFilter finalPayout() {
        if (finalPayout == null) {
            setFinalPayout(new DoubleFilter());
        }
        return finalPayout;
    }

    public void setFinalPayout(DoubleFilter finalPayout) {
        this.finalPayout = finalPayout;
    }

    public IntegerFilter getIndexPayoutEventStatus() {
        return indexPayoutEventStatus;
    }

    public Optional<IntegerFilter> optionalIndexPayoutEventStatus() {
        return Optional.ofNullable(indexPayoutEventStatus);
    }

    public IntegerFilter indexPayoutEventStatus() {
        if (indexPayoutEventStatus == null) {
            setIndexPayoutEventStatus(new IntegerFilter());
        }
        return indexPayoutEventStatus;
    }

    public void setIndexPayoutEventStatus(IntegerFilter indexPayoutEventStatus) {
        this.indexPayoutEventStatus = indexPayoutEventStatus;
    }

    public IntegerFilter getIsApproved() {
        return isApproved;
    }

    public Optional<IntegerFilter> optionalIsApproved() {
        return Optional.ofNullable(isApproved);
    }

    public IntegerFilter isApproved() {
        if (isApproved == null) {
            setIsApproved(new IntegerFilter());
        }
        return isApproved;
    }

    public void setIsApproved(IntegerFilter isApproved) {
        this.isApproved = isApproved;
    }

    public DoubleFilter getMonitoringRange() {
        return monitoringRange;
    }

    public Optional<DoubleFilter> optionalMonitoringRange() {
        return Optional.ofNullable(monitoringRange);
    }

    public DoubleFilter monitoringRange() {
        if (monitoringRange == null) {
            setMonitoringRange(new DoubleFilter());
        }
        return monitoringRange;
    }

    public void setMonitoringRange(DoubleFilter monitoringRange) {
        this.monitoringRange = monitoringRange;
    }

    public IntegerFilter getIsInsurance() {
        return isInsurance;
    }

    public Optional<IntegerFilter> optionalIsInsurance() {
        return Optional.ofNullable(isInsurance);
    }

    public IntegerFilter isInsurance() {
        if (isInsurance == null) {
            setIsInsurance(new IntegerFilter());
        }
        return isInsurance;
    }

    public void setIsInsurance(IntegerFilter isInsurance) {
        this.isInsurance = isInsurance;
    }

    public IntegerFilter getInsuranceCultivatedLand() {
        return insuranceCultivatedLand;
    }

    public Optional<IntegerFilter> optionalInsuranceCultivatedLand() {
        return Optional.ofNullable(insuranceCultivatedLand);
    }

    public IntegerFilter insuranceCultivatedLand() {
        if (insuranceCultivatedLand == null) {
            setInsuranceCultivatedLand(new IntegerFilter());
        }
        return insuranceCultivatedLand;
    }

    public void setInsuranceCultivatedLand(IntegerFilter insuranceCultivatedLand) {
        this.insuranceCultivatedLand = insuranceCultivatedLand;
    }

    public IntegerFilter getIndexChequeId() {
        return indexChequeId;
    }

    public Optional<IntegerFilter> optionalIndexChequeId() {
        return Optional.ofNullable(indexChequeId);
    }

    public IntegerFilter indexChequeId() {
        if (indexChequeId == null) {
            setIndexChequeId(new IntegerFilter());
        }
        return indexChequeId;
    }

    public void setIndexChequeId(IntegerFilter indexChequeId) {
        this.indexChequeId = indexChequeId;
    }

    public IntegerFilter getIndexProductId() {
        return indexProductId;
    }

    public Optional<IntegerFilter> optionalIndexProductId() {
        return Optional.ofNullable(indexProductId);
    }

    public IntegerFilter indexProductId() {
        if (indexProductId == null) {
            setIndexProductId(new IntegerFilter());
        }
        return indexProductId;
    }

    public void setIndexProductId(IntegerFilter indexProductId) {
        this.indexProductId = indexProductId;
    }

    public LongFilter getCultivatedFarmerId() {
        return cultivatedFarmerId;
    }

    public Optional<LongFilter> optionalCultivatedFarmerId() {
        return Optional.ofNullable(cultivatedFarmerId);
    }

    public LongFilter cultivatedFarmerId() {
        if (cultivatedFarmerId == null) {
            setCultivatedFarmerId(new LongFilter());
        }
        return cultivatedFarmerId;
    }

    public void setCultivatedFarmerId(LongFilter cultivatedFarmerId) {
        this.cultivatedFarmerId = cultivatedFarmerId;
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
        final IndexPayoutEventListCriteria that = (IndexPayoutEventListCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(indexPayoutEventId, that.indexPayoutEventId) &&
            Objects.equals(ascId, that.ascId) &&
            Objects.equals(confirmedBy, that.confirmedBy) &&
            Objects.equals(cultivatedExtent, that.cultivatedExtent) &&
            Objects.equals(payout, that.payout) &&
            Objects.equals(confirmedDate, that.confirmedDate) &&
            Objects.equals(rejectedBy, that.rejectedBy) &&
            Objects.equals(rejectedDate, that.rejectedDate) &&
            Objects.equals(reason, that.reason) &&
            Objects.equals(finalPayout, that.finalPayout) &&
            Objects.equals(indexPayoutEventStatus, that.indexPayoutEventStatus) &&
            Objects.equals(isApproved, that.isApproved) &&
            Objects.equals(monitoringRange, that.monitoringRange) &&
            Objects.equals(isInsurance, that.isInsurance) &&
            Objects.equals(insuranceCultivatedLand, that.insuranceCultivatedLand) &&
            Objects.equals(indexChequeId, that.indexChequeId) &&
            Objects.equals(indexProductId, that.indexProductId) &&
            Objects.equals(cultivatedFarmerId, that.cultivatedFarmerId) &&
            Objects.equals(cultivatedLandId, that.cultivatedLandId) &&
            Objects.equals(seasonId, that.seasonId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            indexPayoutEventId,
            ascId,
            confirmedBy,
            cultivatedExtent,
            payout,
            confirmedDate,
            rejectedBy,
            rejectedDate,
            reason,
            finalPayout,
            indexPayoutEventStatus,
            isApproved,
            monitoringRange,
            isInsurance,
            insuranceCultivatedLand,
            indexChequeId,
            indexProductId,
            cultivatedFarmerId,
            cultivatedLandId,
            seasonId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndexPayoutEventListCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIndexPayoutEventId().map(f -> "indexPayoutEventId=" + f + ", ").orElse("") +
            optionalAscId().map(f -> "ascId=" + f + ", ").orElse("") +
            optionalConfirmedBy().map(f -> "confirmedBy=" + f + ", ").orElse("") +
            optionalCultivatedExtent().map(f -> "cultivatedExtent=" + f + ", ").orElse("") +
            optionalPayout().map(f -> "payout=" + f + ", ").orElse("") +
            optionalConfirmedDate().map(f -> "confirmedDate=" + f + ", ").orElse("") +
            optionalRejectedBy().map(f -> "rejectedBy=" + f + ", ").orElse("") +
            optionalRejectedDate().map(f -> "rejectedDate=" + f + ", ").orElse("") +
            optionalReason().map(f -> "reason=" + f + ", ").orElse("") +
            optionalFinalPayout().map(f -> "finalPayout=" + f + ", ").orElse("") +
            optionalIndexPayoutEventStatus().map(f -> "indexPayoutEventStatus=" + f + ", ").orElse("") +
            optionalIsApproved().map(f -> "isApproved=" + f + ", ").orElse("") +
            optionalMonitoringRange().map(f -> "monitoringRange=" + f + ", ").orElse("") +
            optionalIsInsurance().map(f -> "isInsurance=" + f + ", ").orElse("") +
            optionalInsuranceCultivatedLand().map(f -> "insuranceCultivatedLand=" + f + ", ").orElse("") +
            optionalIndexChequeId().map(f -> "indexChequeId=" + f + ", ").orElse("") +
            optionalIndexProductId().map(f -> "indexProductId=" + f + ", ").orElse("") +
            optionalCultivatedFarmerId().map(f -> "cultivatedFarmerId=" + f + ", ").orElse("") +
            optionalCultivatedLandId().map(f -> "cultivatedLandId=" + f + ", ").orElse("") +
            optionalSeasonId().map(f -> "seasonId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
