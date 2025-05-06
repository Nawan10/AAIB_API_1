package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.CultivatedLandFarmers} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.CultivatedLandFarmersResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cultivated-land-farmers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CultivatedLandFarmersCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter relationId;

    private LocalDateFilter createdAt;

    private StringFilter addedBy;

    private LongFilter farmerId;

    private LongFilter cultivatedLandId;

    private Boolean distinct;

    public CultivatedLandFarmersCriteria() {}

    public CultivatedLandFarmersCriteria(CultivatedLandFarmersCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.relationId = other.optionalRelationId().map(IntegerFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(LocalDateFilter::copy).orElse(null);
        this.addedBy = other.optionalAddedBy().map(StringFilter::copy).orElse(null);
        this.farmerId = other.optionalFarmerId().map(LongFilter::copy).orElse(null);
        this.cultivatedLandId = other.optionalCultivatedLandId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CultivatedLandFarmersCriteria copy() {
        return new CultivatedLandFarmersCriteria(this);
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

    public IntegerFilter getRelationId() {
        return relationId;
    }

    public Optional<IntegerFilter> optionalRelationId() {
        return Optional.ofNullable(relationId);
    }

    public IntegerFilter relationId() {
        if (relationId == null) {
            setRelationId(new IntegerFilter());
        }
        return relationId;
    }

    public void setRelationId(IntegerFilter relationId) {
        this.relationId = relationId;
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
        final CultivatedLandFarmersCriteria that = (CultivatedLandFarmersCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(relationId, that.relationId) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(addedBy, that.addedBy) &&
            Objects.equals(farmerId, that.farmerId) &&
            Objects.equals(cultivatedLandId, that.cultivatedLandId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, relationId, createdAt, addedBy, farmerId, cultivatedLandId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CultivatedLandFarmersCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalRelationId().map(f -> "relationId=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalAddedBy().map(f -> "addedBy=" + f + ", ").orElse("") +
            optionalFarmerId().map(f -> "farmerId=" + f + ", ").orElse("") +
            optionalCultivatedLandId().map(f -> "cultivatedLandId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
