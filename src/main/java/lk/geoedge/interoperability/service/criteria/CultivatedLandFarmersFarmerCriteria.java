package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.CultivatedLandFarmersFarmer} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.CultivatedLandFarmersFarmerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cultivated-land-farmers-farmers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CultivatedLandFarmersFarmerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter farmerId;

    private StringFilter farmerName;

    private StringFilter nicNo;

    private StringFilter addressFirstLine;

    private StringFilter contactNoEmail;

    private IntegerFilter provinceId;

    private IntegerFilter districtId;

    private IntegerFilter dsId;

    private IntegerFilter gnId;

    private StringFilter city;

    private InstantFilter addedDate;

    private Boolean distinct;

    public CultivatedLandFarmersFarmerCriteria() {}

    public CultivatedLandFarmersFarmerCriteria(CultivatedLandFarmersFarmerCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.farmerId = other.optionalFarmerId().map(StringFilter::copy).orElse(null);
        this.farmerName = other.optionalFarmerName().map(StringFilter::copy).orElse(null);
        this.nicNo = other.optionalNicNo().map(StringFilter::copy).orElse(null);
        this.addressFirstLine = other.optionalAddressFirstLine().map(StringFilter::copy).orElse(null);
        this.contactNoEmail = other.optionalContactNoEmail().map(StringFilter::copy).orElse(null);
        this.provinceId = other.optionalProvinceId().map(IntegerFilter::copy).orElse(null);
        this.districtId = other.optionalDistrictId().map(IntegerFilter::copy).orElse(null);
        this.dsId = other.optionalDsId().map(IntegerFilter::copy).orElse(null);
        this.gnId = other.optionalGnId().map(IntegerFilter::copy).orElse(null);
        this.city = other.optionalCity().map(StringFilter::copy).orElse(null);
        this.addedDate = other.optionalAddedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CultivatedLandFarmersFarmerCriteria copy() {
        return new CultivatedLandFarmersFarmerCriteria(this);
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

    public StringFilter getFarmerId() {
        return farmerId;
    }

    public Optional<StringFilter> optionalFarmerId() {
        return Optional.ofNullable(farmerId);
    }

    public StringFilter farmerId() {
        if (farmerId == null) {
            setFarmerId(new StringFilter());
        }
        return farmerId;
    }

    public void setFarmerId(StringFilter farmerId) {
        this.farmerId = farmerId;
    }

    public StringFilter getFarmerName() {
        return farmerName;
    }

    public Optional<StringFilter> optionalFarmerName() {
        return Optional.ofNullable(farmerName);
    }

    public StringFilter farmerName() {
        if (farmerName == null) {
            setFarmerName(new StringFilter());
        }
        return farmerName;
    }

    public void setFarmerName(StringFilter farmerName) {
        this.farmerName = farmerName;
    }

    public StringFilter getNicNo() {
        return nicNo;
    }

    public Optional<StringFilter> optionalNicNo() {
        return Optional.ofNullable(nicNo);
    }

    public StringFilter nicNo() {
        if (nicNo == null) {
            setNicNo(new StringFilter());
        }
        return nicNo;
    }

    public void setNicNo(StringFilter nicNo) {
        this.nicNo = nicNo;
    }

    public StringFilter getAddressFirstLine() {
        return addressFirstLine;
    }

    public Optional<StringFilter> optionalAddressFirstLine() {
        return Optional.ofNullable(addressFirstLine);
    }

    public StringFilter addressFirstLine() {
        if (addressFirstLine == null) {
            setAddressFirstLine(new StringFilter());
        }
        return addressFirstLine;
    }

    public void setAddressFirstLine(StringFilter addressFirstLine) {
        this.addressFirstLine = addressFirstLine;
    }

    public StringFilter getContactNoEmail() {
        return contactNoEmail;
    }

    public Optional<StringFilter> optionalContactNoEmail() {
        return Optional.ofNullable(contactNoEmail);
    }

    public StringFilter contactNoEmail() {
        if (contactNoEmail == null) {
            setContactNoEmail(new StringFilter());
        }
        return contactNoEmail;
    }

    public void setContactNoEmail(StringFilter contactNoEmail) {
        this.contactNoEmail = contactNoEmail;
    }

    public IntegerFilter getProvinceId() {
        return provinceId;
    }

    public Optional<IntegerFilter> optionalProvinceId() {
        return Optional.ofNullable(provinceId);
    }

    public IntegerFilter provinceId() {
        if (provinceId == null) {
            setProvinceId(new IntegerFilter());
        }
        return provinceId;
    }

    public void setProvinceId(IntegerFilter provinceId) {
        this.provinceId = provinceId;
    }

    public IntegerFilter getDistrictId() {
        return districtId;
    }

    public Optional<IntegerFilter> optionalDistrictId() {
        return Optional.ofNullable(districtId);
    }

    public IntegerFilter districtId() {
        if (districtId == null) {
            setDistrictId(new IntegerFilter());
        }
        return districtId;
    }

    public void setDistrictId(IntegerFilter districtId) {
        this.districtId = districtId;
    }

    public IntegerFilter getDsId() {
        return dsId;
    }

    public Optional<IntegerFilter> optionalDsId() {
        return Optional.ofNullable(dsId);
    }

    public IntegerFilter dsId() {
        if (dsId == null) {
            setDsId(new IntegerFilter());
        }
        return dsId;
    }

    public void setDsId(IntegerFilter dsId) {
        this.dsId = dsId;
    }

    public IntegerFilter getGnId() {
        return gnId;
    }

    public Optional<IntegerFilter> optionalGnId() {
        return Optional.ofNullable(gnId);
    }

    public IntegerFilter gnId() {
        if (gnId == null) {
            setGnId(new IntegerFilter());
        }
        return gnId;
    }

    public void setGnId(IntegerFilter gnId) {
        this.gnId = gnId;
    }

    public StringFilter getCity() {
        return city;
    }

    public Optional<StringFilter> optionalCity() {
        return Optional.ofNullable(city);
    }

    public StringFilter city() {
        if (city == null) {
            setCity(new StringFilter());
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public InstantFilter getAddedDate() {
        return addedDate;
    }

    public Optional<InstantFilter> optionalAddedDate() {
        return Optional.ofNullable(addedDate);
    }

    public InstantFilter addedDate() {
        if (addedDate == null) {
            setAddedDate(new InstantFilter());
        }
        return addedDate;
    }

    public void setAddedDate(InstantFilter addedDate) {
        this.addedDate = addedDate;
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
        final CultivatedLandFarmersFarmerCriteria that = (CultivatedLandFarmersFarmerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(farmerId, that.farmerId) &&
            Objects.equals(farmerName, that.farmerName) &&
            Objects.equals(nicNo, that.nicNo) &&
            Objects.equals(addressFirstLine, that.addressFirstLine) &&
            Objects.equals(contactNoEmail, that.contactNoEmail) &&
            Objects.equals(provinceId, that.provinceId) &&
            Objects.equals(districtId, that.districtId) &&
            Objects.equals(dsId, that.dsId) &&
            Objects.equals(gnId, that.gnId) &&
            Objects.equals(city, that.city) &&
            Objects.equals(addedDate, that.addedDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            farmerId,
            farmerName,
            nicNo,
            addressFirstLine,
            contactNoEmail,
            provinceId,
            districtId,
            dsId,
            gnId,
            city,
            addedDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CultivatedLandFarmersFarmerCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFarmerId().map(f -> "farmerId=" + f + ", ").orElse("") +
            optionalFarmerName().map(f -> "farmerName=" + f + ", ").orElse("") +
            optionalNicNo().map(f -> "nicNo=" + f + ", ").orElse("") +
            optionalAddressFirstLine().map(f -> "addressFirstLine=" + f + ", ").orElse("") +
            optionalContactNoEmail().map(f -> "contactNoEmail=" + f + ", ").orElse("") +
            optionalProvinceId().map(f -> "provinceId=" + f + ", ").orElse("") +
            optionalDistrictId().map(f -> "districtId=" + f + ", ").orElse("") +
            optionalDsId().map(f -> "dsId=" + f + ", ").orElse("") +
            optionalGnId().map(f -> "gnId=" + f + ", ").orElse("") +
            optionalCity().map(f -> "city=" + f + ", ").orElse("") +
            optionalAddedDate().map(f -> "addedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
