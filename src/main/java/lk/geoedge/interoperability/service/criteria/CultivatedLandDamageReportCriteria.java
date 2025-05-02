package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.CultivatedLandDamageReport} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.CultivatedLandDamageReportResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cultivated-land-damage-reports?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CultivatedLandDamageReportCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter damageReasonId;

    private StringFilter damageServerityId;

    private StringFilter damageDateMonitor;

    private StringFilter description;

    private StringFilter farmerComment;

    private StringFilter estimatedYield;

    private InstantFilter createdAt;

    private StringFilter addedBy;

    private LongFilter cropId;

    private LongFilter damageCategoryId;

    private LongFilter damageTypeId;

    private Boolean distinct;

    public CultivatedLandDamageReportCriteria() {}

    public CultivatedLandDamageReportCriteria(CultivatedLandDamageReportCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.damageReasonId = other.optionalDamageReasonId().map(StringFilter::copy).orElse(null);
        this.damageServerityId = other.optionalDamageServerityId().map(StringFilter::copy).orElse(null);
        this.damageDateMonitor = other.optionalDamageDateMonitor().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.farmerComment = other.optionalFarmerComment().map(StringFilter::copy).orElse(null);
        this.estimatedYield = other.optionalEstimatedYield().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.addedBy = other.optionalAddedBy().map(StringFilter::copy).orElse(null);
        this.cropId = other.optionalCropId().map(LongFilter::copy).orElse(null);
        this.damageCategoryId = other.optionalDamageCategoryId().map(LongFilter::copy).orElse(null);
        this.damageTypeId = other.optionalDamageTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CultivatedLandDamageReportCriteria copy() {
        return new CultivatedLandDamageReportCriteria(this);
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

    public StringFilter getDamageReasonId() {
        return damageReasonId;
    }

    public Optional<StringFilter> optionalDamageReasonId() {
        return Optional.ofNullable(damageReasonId);
    }

    public StringFilter damageReasonId() {
        if (damageReasonId == null) {
            setDamageReasonId(new StringFilter());
        }
        return damageReasonId;
    }

    public void setDamageReasonId(StringFilter damageReasonId) {
        this.damageReasonId = damageReasonId;
    }

    public StringFilter getDamageServerityId() {
        return damageServerityId;
    }

    public Optional<StringFilter> optionalDamageServerityId() {
        return Optional.ofNullable(damageServerityId);
    }

    public StringFilter damageServerityId() {
        if (damageServerityId == null) {
            setDamageServerityId(new StringFilter());
        }
        return damageServerityId;
    }

    public void setDamageServerityId(StringFilter damageServerityId) {
        this.damageServerityId = damageServerityId;
    }

    public StringFilter getDamageDateMonitor() {
        return damageDateMonitor;
    }

    public Optional<StringFilter> optionalDamageDateMonitor() {
        return Optional.ofNullable(damageDateMonitor);
    }

    public StringFilter damageDateMonitor() {
        if (damageDateMonitor == null) {
            setDamageDateMonitor(new StringFilter());
        }
        return damageDateMonitor;
    }

    public void setDamageDateMonitor(StringFilter damageDateMonitor) {
        this.damageDateMonitor = damageDateMonitor;
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

    public StringFilter getFarmerComment() {
        return farmerComment;
    }

    public Optional<StringFilter> optionalFarmerComment() {
        return Optional.ofNullable(farmerComment);
    }

    public StringFilter farmerComment() {
        if (farmerComment == null) {
            setFarmerComment(new StringFilter());
        }
        return farmerComment;
    }

    public void setFarmerComment(StringFilter farmerComment) {
        this.farmerComment = farmerComment;
    }

    public StringFilter getEstimatedYield() {
        return estimatedYield;
    }

    public Optional<StringFilter> optionalEstimatedYield() {
        return Optional.ofNullable(estimatedYield);
    }

    public StringFilter estimatedYield() {
        if (estimatedYield == null) {
            setEstimatedYield(new StringFilter());
        }
        return estimatedYield;
    }

    public void setEstimatedYield(StringFilter estimatedYield) {
        this.estimatedYield = estimatedYield;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<InstantFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new InstantFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
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

    public LongFilter getDamageCategoryId() {
        return damageCategoryId;
    }

    public Optional<LongFilter> optionalDamageCategoryId() {
        return Optional.ofNullable(damageCategoryId);
    }

    public LongFilter damageCategoryId() {
        if (damageCategoryId == null) {
            setDamageCategoryId(new LongFilter());
        }
        return damageCategoryId;
    }

    public void setDamageCategoryId(LongFilter damageCategoryId) {
        this.damageCategoryId = damageCategoryId;
    }

    public LongFilter getDamageTypeId() {
        return damageTypeId;
    }

    public Optional<LongFilter> optionalDamageTypeId() {
        return Optional.ofNullable(damageTypeId);
    }

    public LongFilter damageTypeId() {
        if (damageTypeId == null) {
            setDamageTypeId(new LongFilter());
        }
        return damageTypeId;
    }

    public void setDamageTypeId(LongFilter damageTypeId) {
        this.damageTypeId = damageTypeId;
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
        final CultivatedLandDamageReportCriteria that = (CultivatedLandDamageReportCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(damageReasonId, that.damageReasonId) &&
            Objects.equals(damageServerityId, that.damageServerityId) &&
            Objects.equals(damageDateMonitor, that.damageDateMonitor) &&
            Objects.equals(description, that.description) &&
            Objects.equals(farmerComment, that.farmerComment) &&
            Objects.equals(estimatedYield, that.estimatedYield) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(addedBy, that.addedBy) &&
            Objects.equals(cropId, that.cropId) &&
            Objects.equals(damageCategoryId, that.damageCategoryId) &&
            Objects.equals(damageTypeId, that.damageTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            damageReasonId,
            damageServerityId,
            damageDateMonitor,
            description,
            farmerComment,
            estimatedYield,
            createdAt,
            addedBy,
            cropId,
            damageCategoryId,
            damageTypeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CultivatedLandDamageReportCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDamageReasonId().map(f -> "damageReasonId=" + f + ", ").orElse("") +
            optionalDamageServerityId().map(f -> "damageServerityId=" + f + ", ").orElse("") +
            optionalDamageDateMonitor().map(f -> "damageDateMonitor=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalFarmerComment().map(f -> "farmerComment=" + f + ", ").orElse("") +
            optionalEstimatedYield().map(f -> "estimatedYield=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalAddedBy().map(f -> "addedBy=" + f + ", ").orElse("") +
            optionalCropId().map(f -> "cropId=" + f + ", ").orElse("") +
            optionalDamageCategoryId().map(f -> "damageCategoryId=" + f + ", ").orElse("") +
            optionalDamageTypeId().map(f -> "damageTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
