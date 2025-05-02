package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.FarmerFieldLandOwner} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.FarmerFieldLandOwnerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /farmer-field-land-owners?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FarmerFieldLandOwnerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter createdAt;

    private StringFilter addedBy;

    private LongFilter farmerFieldOwnerId;

    private LongFilter farmerId;

    private Boolean distinct;

    public FarmerFieldLandOwnerCriteria() {}

    public FarmerFieldLandOwnerCriteria(FarmerFieldLandOwnerCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(LocalDateFilter::copy).orElse(null);
        this.addedBy = other.optionalAddedBy().map(StringFilter::copy).orElse(null);
        this.farmerFieldOwnerId = other.optionalFarmerFieldOwnerId().map(LongFilter::copy).orElse(null);
        this.farmerId = other.optionalFarmerId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FarmerFieldLandOwnerCriteria copy() {
        return new FarmerFieldLandOwnerCriteria(this);
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

    public LongFilter getFarmerFieldOwnerId() {
        return farmerFieldOwnerId;
    }

    public Optional<LongFilter> optionalFarmerFieldOwnerId() {
        return Optional.ofNullable(farmerFieldOwnerId);
    }

    public LongFilter farmerFieldOwnerId() {
        if (farmerFieldOwnerId == null) {
            setFarmerFieldOwnerId(new LongFilter());
        }
        return farmerFieldOwnerId;
    }

    public void setFarmerFieldOwnerId(LongFilter farmerFieldOwnerId) {
        this.farmerFieldOwnerId = farmerFieldOwnerId;
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
        final FarmerFieldLandOwnerCriteria that = (FarmerFieldLandOwnerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(addedBy, that.addedBy) &&
            Objects.equals(farmerFieldOwnerId, that.farmerFieldOwnerId) &&
            Objects.equals(farmerId, that.farmerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, addedBy, farmerFieldOwnerId, farmerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FarmerFieldLandOwnerCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalAddedBy().map(f -> "addedBy=" + f + ", ").orElse("") +
            optionalFarmerFieldOwnerId().map(f -> "farmerFieldOwnerId=" + f + ", ").orElse("") +
            optionalFarmerId().map(f -> "farmerId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
