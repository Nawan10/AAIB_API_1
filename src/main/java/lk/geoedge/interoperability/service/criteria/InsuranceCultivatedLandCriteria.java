package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.InsuranceCultivatedLand} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.InsuranceCultivatedLandResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /insurance-cultivated-lands?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsuranceCultivatedLandCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter cropDurationId;

    private StringFilter insurancePoliceId;

    private IntegerFilter sumInsuredPerAcre;

    private IntegerFilter insuranceExtent;

    private IntegerFilter sumAmount;

    private StringFilter insuranceStatus;

    private LocalDateFilter createdAt;

    private StringFilter addedBy;

    private LongFilter farmerId;

    private LongFilter cultivatedLandId;

    private LongFilter cropId;

    private Boolean distinct;

    public InsuranceCultivatedLandCriteria() {}

    public InsuranceCultivatedLandCriteria(InsuranceCultivatedLandCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.cropDurationId = other.optionalCropDurationId().map(StringFilter::copy).orElse(null);
        this.insurancePoliceId = other.optionalInsurancePoliceId().map(StringFilter::copy).orElse(null);
        this.sumInsuredPerAcre = other.optionalSumInsuredPerAcre().map(IntegerFilter::copy).orElse(null);
        this.insuranceExtent = other.optionalInsuranceExtent().map(IntegerFilter::copy).orElse(null);
        this.sumAmount = other.optionalSumAmount().map(IntegerFilter::copy).orElse(null);
        this.insuranceStatus = other.optionalInsuranceStatus().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(LocalDateFilter::copy).orElse(null);
        this.addedBy = other.optionalAddedBy().map(StringFilter::copy).orElse(null);
        this.farmerId = other.optionalFarmerId().map(LongFilter::copy).orElse(null);
        this.cultivatedLandId = other.optionalCultivatedLandId().map(LongFilter::copy).orElse(null);
        this.cropId = other.optionalCropId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InsuranceCultivatedLandCriteria copy() {
        return new InsuranceCultivatedLandCriteria(this);
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

    public StringFilter getCropDurationId() {
        return cropDurationId;
    }

    public Optional<StringFilter> optionalCropDurationId() {
        return Optional.ofNullable(cropDurationId);
    }

    public StringFilter cropDurationId() {
        if (cropDurationId == null) {
            setCropDurationId(new StringFilter());
        }
        return cropDurationId;
    }

    public void setCropDurationId(StringFilter cropDurationId) {
        this.cropDurationId = cropDurationId;
    }

    public StringFilter getInsurancePoliceId() {
        return insurancePoliceId;
    }

    public Optional<StringFilter> optionalInsurancePoliceId() {
        return Optional.ofNullable(insurancePoliceId);
    }

    public StringFilter insurancePoliceId() {
        if (insurancePoliceId == null) {
            setInsurancePoliceId(new StringFilter());
        }
        return insurancePoliceId;
    }

    public void setInsurancePoliceId(StringFilter insurancePoliceId) {
        this.insurancePoliceId = insurancePoliceId;
    }

    public IntegerFilter getSumInsuredPerAcre() {
        return sumInsuredPerAcre;
    }

    public Optional<IntegerFilter> optionalSumInsuredPerAcre() {
        return Optional.ofNullable(sumInsuredPerAcre);
    }

    public IntegerFilter sumInsuredPerAcre() {
        if (sumInsuredPerAcre == null) {
            setSumInsuredPerAcre(new IntegerFilter());
        }
        return sumInsuredPerAcre;
    }

    public void setSumInsuredPerAcre(IntegerFilter sumInsuredPerAcre) {
        this.sumInsuredPerAcre = sumInsuredPerAcre;
    }

    public IntegerFilter getInsuranceExtent() {
        return insuranceExtent;
    }

    public Optional<IntegerFilter> optionalInsuranceExtent() {
        return Optional.ofNullable(insuranceExtent);
    }

    public IntegerFilter insuranceExtent() {
        if (insuranceExtent == null) {
            setInsuranceExtent(new IntegerFilter());
        }
        return insuranceExtent;
    }

    public void setInsuranceExtent(IntegerFilter insuranceExtent) {
        this.insuranceExtent = insuranceExtent;
    }

    public IntegerFilter getSumAmount() {
        return sumAmount;
    }

    public Optional<IntegerFilter> optionalSumAmount() {
        return Optional.ofNullable(sumAmount);
    }

    public IntegerFilter sumAmount() {
        if (sumAmount == null) {
            setSumAmount(new IntegerFilter());
        }
        return sumAmount;
    }

    public void setSumAmount(IntegerFilter sumAmount) {
        this.sumAmount = sumAmount;
    }

    public StringFilter getInsuranceStatus() {
        return insuranceStatus;
    }

    public Optional<StringFilter> optionalInsuranceStatus() {
        return Optional.ofNullable(insuranceStatus);
    }

    public StringFilter insuranceStatus() {
        if (insuranceStatus == null) {
            setInsuranceStatus(new StringFilter());
        }
        return insuranceStatus;
    }

    public void setInsuranceStatus(StringFilter insuranceStatus) {
        this.insuranceStatus = insuranceStatus;
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

    public LongFilter getFarmerId() {
        return farmerId;
    }

    public Optional<LongFilter> optionalFarmerId() {
        return Optional.ofNullable(farmerId);
    }

    public LongFilter farmerId() {
        if (farmerId == null) {
            setFarmerId(new LongFilter());
        }
        return farmerId;
    }

    public void setFarmerId(LongFilter farmerId) {
        this.farmerId = farmerId;
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
        final InsuranceCultivatedLandCriteria that = (InsuranceCultivatedLandCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cropDurationId, that.cropDurationId) &&
            Objects.equals(insurancePoliceId, that.insurancePoliceId) &&
            Objects.equals(sumInsuredPerAcre, that.sumInsuredPerAcre) &&
            Objects.equals(insuranceExtent, that.insuranceExtent) &&
            Objects.equals(sumAmount, that.sumAmount) &&
            Objects.equals(insuranceStatus, that.insuranceStatus) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(addedBy, that.addedBy) &&
            Objects.equals(farmerId, that.farmerId) &&
            Objects.equals(cultivatedLandId, that.cultivatedLandId) &&
            Objects.equals(cropId, that.cropId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            cropDurationId,
            insurancePoliceId,
            sumInsuredPerAcre,
            insuranceExtent,
            sumAmount,
            insuranceStatus,
            createdAt,
            addedBy,
            farmerId,
            cultivatedLandId,
            cropId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuranceCultivatedLandCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCropDurationId().map(f -> "cropDurationId=" + f + ", ").orElse("") +
            optionalInsurancePoliceId().map(f -> "insurancePoliceId=" + f + ", ").orElse("") +
            optionalSumInsuredPerAcre().map(f -> "sumInsuredPerAcre=" + f + ", ").orElse("") +
            optionalInsuranceExtent().map(f -> "insuranceExtent=" + f + ", ").orElse("") +
            optionalSumAmount().map(f -> "sumAmount=" + f + ", ").orElse("") +
            optionalInsuranceStatus().map(f -> "insuranceStatus=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalAddedBy().map(f -> "addedBy=" + f + ", ").orElse("") +
            optionalFarmerId().map(f -> "farmerId=" + f + ", ").orElse("") +
            optionalCultivatedLandId().map(f -> "cultivatedLandId=" + f + ", ").orElse("") +
            optionalCropId().map(f -> "cropId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
