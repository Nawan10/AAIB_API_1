package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A CultivatedLandFarmersFarmer.
 */
@Entity
@Table(name = "farmer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CultivatedLandFarmersFarmer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "farmer_id")
    private String farmerId;

    @Column(name = "farmwe_name")
    private String farmerName;

    @Column(name = "nic_no")
    private String nicNo;

    @Column(name = "address_first_line")
    private String addressFirstLine;

    @Column(name = "contact_no_email")
    private String contactNoEmail;

    @Column(name = "province_id")
    private Integer provinceId;

    @Column(name = "district_id")
    private Integer districtId;

    @Column(name = "ds_id")
    private Integer dsId;

    @Column(name = "gn_id")
    private Integer gnId;

    @Column(name = "city")
    private String city;

    @Column(name = "added_date")
    private Instant addedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CultivatedLandFarmersFarmer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFarmerId() {
        return this.farmerId;
    }

    public CultivatedLandFarmersFarmer farmerId(String farmerId) {
        this.setFarmerId(farmerId);
        return this;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public String getFarmerName() {
        return this.farmerName;
    }

    public CultivatedLandFarmersFarmer farmerName(String farmerName) {
        this.setFarmerName(farmerName);
        return this;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getNicNo() {
        return this.nicNo;
    }

    public CultivatedLandFarmersFarmer nicNo(String nicNo) {
        this.setNicNo(nicNo);
        return this;
    }

    public void setNicNo(String nicNo) {
        this.nicNo = nicNo;
    }

    public String getAddressFirstLine() {
        return this.addressFirstLine;
    }

    public CultivatedLandFarmersFarmer addressFirstLine(String addressFirstLine) {
        this.setAddressFirstLine(addressFirstLine);
        return this;
    }

    public void setAddressFirstLine(String addressFirstLine) {
        this.addressFirstLine = addressFirstLine;
    }

    public String getContactNoEmail() {
        return this.contactNoEmail;
    }

    public CultivatedLandFarmersFarmer contactNoEmail(String contactNoEmail) {
        this.setContactNoEmail(contactNoEmail);
        return this;
    }

    public void setContactNoEmail(String contactNoEmail) {
        this.contactNoEmail = contactNoEmail;
    }

    public Integer getProvinceId() {
        return this.provinceId;
    }

    public CultivatedLandFarmersFarmer provinceId(Integer provinceId) {
        this.setProvinceId(provinceId);
        return this;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getDistrictId() {
        return this.districtId;
    }

    public CultivatedLandFarmersFarmer districtId(Integer districtId) {
        this.setDistrictId(districtId);
        return this;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public Integer getDsId() {
        return this.dsId;
    }

    public CultivatedLandFarmersFarmer dsId(Integer dsId) {
        this.setDsId(dsId);
        return this;
    }

    public void setDsId(Integer dsId) {
        this.dsId = dsId;
    }

    public Integer getGnId() {
        return this.gnId;
    }

    public CultivatedLandFarmersFarmer gnId(Integer gnId) {
        this.setGnId(gnId);
        return this;
    }

    public void setGnId(Integer gnId) {
        this.gnId = gnId;
    }

    public String getCity() {
        return this.city;
    }

    public CultivatedLandFarmersFarmer city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Instant getAddedDate() {
        return this.addedDate;
    }

    public CultivatedLandFarmersFarmer addedDate(Instant addedDate) {
        this.setAddedDate(addedDate);
        return this;
    }

    public void setAddedDate(Instant addedDate) {
        this.addedDate = addedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CultivatedLandFarmersFarmer)) {
            return false;
        }
        return getId() != null && getId().equals(((CultivatedLandFarmersFarmer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CultivatedLandFarmersFarmer{" +
            "id=" + getId() +
            ", farmerId='" + getFarmerId() + "'" +
            ", farmerName='" + getFarmerName() + "'" +
            ", nicNo='" + getNicNo() + "'" +
            ", addressFirstLine='" + getAddressFirstLine() + "'" +
            ", contactNoEmail='" + getContactNoEmail() + "'" +
            ", provinceId=" + getProvinceId() +
            ", districtId=" + getDistrictId() +
            ", dsId=" + getDsId() +
            ", gnId=" + getGnId() +
            ", city='" + getCity() + "'" +
            ", addedDate='" + getAddedDate() + "'" +
            "}";
    }
}
