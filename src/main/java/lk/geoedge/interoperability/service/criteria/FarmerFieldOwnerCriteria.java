package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.FarmerFieldOwner} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.FarmerFieldOwnerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /farmer-field-owners?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FarmerFieldOwnerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter landPlotName;

    private StringFilter landRegistryNo;

    private DoubleFilter totalLandExtent;

    private DoubleFilter calculatedArea;

    private StringFilter provinceId;

    private StringFilter districtId;

    private StringFilter dsId;

    private StringFilter gnId;

    private DoubleFilter centerLat;

    private DoubleFilter centerLng;

    private LongFilter cropId;

    private Boolean distinct;

    public FarmerFieldOwnerCriteria() {}

    public FarmerFieldOwnerCriteria(FarmerFieldOwnerCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.landPlotName = other.optionalLandPlotName().map(StringFilter::copy).orElse(null);
        this.landRegistryNo = other.optionalLandRegistryNo().map(StringFilter::copy).orElse(null);
        this.totalLandExtent = other.optionalTotalLandExtent().map(DoubleFilter::copy).orElse(null);
        this.calculatedArea = other.optionalCalculatedArea().map(DoubleFilter::copy).orElse(null);
        this.provinceId = other.optionalProvinceId().map(StringFilter::copy).orElse(null);
        this.districtId = other.optionalDistrictId().map(StringFilter::copy).orElse(null);
        this.dsId = other.optionalDsId().map(StringFilter::copy).orElse(null);
        this.gnId = other.optionalGnId().map(StringFilter::copy).orElse(null);
        this.centerLat = other.optionalCenterLat().map(DoubleFilter::copy).orElse(null);
        this.centerLng = other.optionalCenterLng().map(DoubleFilter::copy).orElse(null);
        this.cropId = other.optionalCropId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FarmerFieldOwnerCriteria copy() {
        return new FarmerFieldOwnerCriteria(this);
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

    public StringFilter getLandPlotName() {
        return landPlotName;
    }

    public Optional<StringFilter> optionalLandPlotName() {
        return Optional.ofNullable(landPlotName);
    }

    public StringFilter landPlotName() {
        if (landPlotName == null) {
            setLandPlotName(new StringFilter());
        }
        return landPlotName;
    }

    public void setLandPlotName(StringFilter landPlotName) {
        this.landPlotName = landPlotName;
    }

    public StringFilter getLandRegistryNo() {
        return landRegistryNo;
    }

    public Optional<StringFilter> optionalLandRegistryNo() {
        return Optional.ofNullable(landRegistryNo);
    }

    public StringFilter landRegistryNo() {
        if (landRegistryNo == null) {
            setLandRegistryNo(new StringFilter());
        }
        return landRegistryNo;
    }

    public void setLandRegistryNo(StringFilter landRegistryNo) {
        this.landRegistryNo = landRegistryNo;
    }

    public DoubleFilter getTotalLandExtent() {
        return totalLandExtent;
    }

    public Optional<DoubleFilter> optionalTotalLandExtent() {
        return Optional.ofNullable(totalLandExtent);
    }

    public DoubleFilter totalLandExtent() {
        if (totalLandExtent == null) {
            setTotalLandExtent(new DoubleFilter());
        }
        return totalLandExtent;
    }

    public void setTotalLandExtent(DoubleFilter totalLandExtent) {
        this.totalLandExtent = totalLandExtent;
    }

    public DoubleFilter getCalculatedArea() {
        return calculatedArea;
    }

    public Optional<DoubleFilter> optionalCalculatedArea() {
        return Optional.ofNullable(calculatedArea);
    }

    public DoubleFilter calculatedArea() {
        if (calculatedArea == null) {
            setCalculatedArea(new DoubleFilter());
        }
        return calculatedArea;
    }

    public void setCalculatedArea(DoubleFilter calculatedArea) {
        this.calculatedArea = calculatedArea;
    }

    public StringFilter getProvinceId() {
        return provinceId;
    }

    public Optional<StringFilter> optionalProvinceId() {
        return Optional.ofNullable(provinceId);
    }

    public StringFilter provinceId() {
        if (provinceId == null) {
            setProvinceId(new StringFilter());
        }
        return provinceId;
    }

    public void setProvinceId(StringFilter provinceId) {
        this.provinceId = provinceId;
    }

    public StringFilter getDistrictId() {
        return districtId;
    }

    public Optional<StringFilter> optionalDistrictId() {
        return Optional.ofNullable(districtId);
    }

    public StringFilter districtId() {
        if (districtId == null) {
            setDistrictId(new StringFilter());
        }
        return districtId;
    }

    public void setDistrictId(StringFilter districtId) {
        this.districtId = districtId;
    }

    public StringFilter getDsId() {
        return dsId;
    }

    public Optional<StringFilter> optionalDsId() {
        return Optional.ofNullable(dsId);
    }

    public StringFilter dsId() {
        if (dsId == null) {
            setDsId(new StringFilter());
        }
        return dsId;
    }

    public void setDsId(StringFilter dsId) {
        this.dsId = dsId;
    }

    public StringFilter getGnId() {
        return gnId;
    }

    public Optional<StringFilter> optionalGnId() {
        return Optional.ofNullable(gnId);
    }

    public StringFilter gnId() {
        if (gnId == null) {
            setGnId(new StringFilter());
        }
        return gnId;
    }

    public void setGnId(StringFilter gnId) {
        this.gnId = gnId;
    }

    public DoubleFilter getCenterLat() {
        return centerLat;
    }

    public Optional<DoubleFilter> optionalCenterLat() {
        return Optional.ofNullable(centerLat);
    }

    public DoubleFilter centerLat() {
        if (centerLat == null) {
            setCenterLat(new DoubleFilter());
        }
        return centerLat;
    }

    public void setCenterLat(DoubleFilter centerLat) {
        this.centerLat = centerLat;
    }

    public DoubleFilter getCenterLng() {
        return centerLng;
    }

    public Optional<DoubleFilter> optionalCenterLng() {
        return Optional.ofNullable(centerLng);
    }

    public DoubleFilter centerLng() {
        if (centerLng == null) {
            setCenterLng(new DoubleFilter());
        }
        return centerLng;
    }

    public void setCenterLng(DoubleFilter centerLng) {
        this.centerLng = centerLng;
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
        final FarmerFieldOwnerCriteria that = (FarmerFieldOwnerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(landPlotName, that.landPlotName) &&
            Objects.equals(landRegistryNo, that.landRegistryNo) &&
            Objects.equals(totalLandExtent, that.totalLandExtent) &&
            Objects.equals(calculatedArea, that.calculatedArea) &&
            Objects.equals(provinceId, that.provinceId) &&
            Objects.equals(districtId, that.districtId) &&
            Objects.equals(dsId, that.dsId) &&
            Objects.equals(gnId, that.gnId) &&
            Objects.equals(centerLat, that.centerLat) &&
            Objects.equals(centerLng, that.centerLng) &&
            Objects.equals(cropId, that.cropId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            landPlotName,
            landRegistryNo,
            totalLandExtent,
            calculatedArea,
            provinceId,
            districtId,
            dsId,
            gnId,
            centerLat,
            centerLng,
            cropId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FarmerFieldOwnerCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalLandPlotName().map(f -> "landPlotName=" + f + ", ").orElse("") +
            optionalLandRegistryNo().map(f -> "landRegistryNo=" + f + ", ").orElse("") +
            optionalTotalLandExtent().map(f -> "totalLandExtent=" + f + ", ").orElse("") +
            optionalCalculatedArea().map(f -> "calculatedArea=" + f + ", ").orElse("") +
            optionalProvinceId().map(f -> "provinceId=" + f + ", ").orElse("") +
            optionalDistrictId().map(f -> "districtId=" + f + ", ").orElse("") +
            optionalDsId().map(f -> "dsId=" + f + ", ").orElse("") +
            optionalGnId().map(f -> "gnId=" + f + ", ").orElse("") +
            optionalCenterLat().map(f -> "centerLat=" + f + ", ").orElse("") +
            optionalCenterLng().map(f -> "centerLng=" + f + ", ").orElse("") +
            optionalCropId().map(f -> "cropId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
