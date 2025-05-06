package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A CultivatedLandFarmerFieldOwner.
 */
@Entity
@Table(name = "farmer_field_owner")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CultivatedLandFarmerFieldOwner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "land_plot_name")
    private String landPlotName;

    @Column(name = "land_registry_no")
    private String landRegistryNo;

    @Column(name = "total_land_extent")
    private Double totalLandExtent;

    @Column(name = "calculated_area")
    private Double calculatedArea;

    @Column(name = "province_id")
    private String provinceId;

    @Column(name = "district_id")
    private String districtId;

    @Column(name = "ds_id")
    private String dsId;

    @Column(name = "gn_id")
    private String gnId;

    @Column(name = "center_lat")
    private Double centerLat;

    @Column(name = "center_lng")
    private Double centerLng;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CultivatedLandFarmerFieldOwner id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLandPlotName() {
        return this.landPlotName;
    }

    public CultivatedLandFarmerFieldOwner landPlotName(String landPlotName) {
        this.setLandPlotName(landPlotName);
        return this;
    }

    public void setLandPlotName(String landPlotName) {
        this.landPlotName = landPlotName;
    }

    public String getLandRegistryNo() {
        return this.landRegistryNo;
    }

    public CultivatedLandFarmerFieldOwner landRegistryNo(String landRegistryNo) {
        this.setLandRegistryNo(landRegistryNo);
        return this;
    }

    public void setLandRegistryNo(String landRegistryNo) {
        this.landRegistryNo = landRegistryNo;
    }

    public Double getTotalLandExtent() {
        return this.totalLandExtent;
    }

    public CultivatedLandFarmerFieldOwner totalLandExtent(Double totalLandExtent) {
        this.setTotalLandExtent(totalLandExtent);
        return this;
    }

    public void setTotalLandExtent(Double totalLandExtent) {
        this.totalLandExtent = totalLandExtent;
    }

    public Double getCalculatedArea() {
        return this.calculatedArea;
    }

    public CultivatedLandFarmerFieldOwner calculatedArea(Double calculatedArea) {
        this.setCalculatedArea(calculatedArea);
        return this;
    }

    public void setCalculatedArea(Double calculatedArea) {
        this.calculatedArea = calculatedArea;
    }

    public String getProvinceId() {
        return this.provinceId;
    }

    public CultivatedLandFarmerFieldOwner provinceId(String provinceId) {
        this.setProvinceId(provinceId);
        return this;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getDistrictId() {
        return this.districtId;
    }

    public CultivatedLandFarmerFieldOwner districtId(String districtId) {
        this.setDistrictId(districtId);
        return this;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDsId() {
        return this.dsId;
    }

    public CultivatedLandFarmerFieldOwner dsId(String dsId) {
        this.setDsId(dsId);
        return this;
    }

    public void setDsId(String dsId) {
        this.dsId = dsId;
    }

    public String getGnId() {
        return this.gnId;
    }

    public CultivatedLandFarmerFieldOwner gnId(String gnId) {
        this.setGnId(gnId);
        return this;
    }

    public void setGnId(String gnId) {
        this.gnId = gnId;
    }

    public Double getCenterLat() {
        return this.centerLat;
    }

    public CultivatedLandFarmerFieldOwner centerLat(Double centerLat) {
        this.setCenterLat(centerLat);
        return this;
    }

    public void setCenterLat(Double centerLat) {
        this.centerLat = centerLat;
    }

    public Double getCenterLng() {
        return this.centerLng;
    }

    public CultivatedLandFarmerFieldOwner centerLng(Double centerLng) {
        this.setCenterLng(centerLng);
        return this;
    }

    public void setCenterLng(Double centerLng) {
        this.centerLng = centerLng;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CultivatedLandFarmerFieldOwner)) {
            return false;
        }
        return getId() != null && getId().equals(((CultivatedLandFarmerFieldOwner) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CultivatedLandFarmerFieldOwner{" +
            "id=" + getId() +
            ", landPlotName='" + getLandPlotName() + "'" +
            ", landRegistryNo='" + getLandRegistryNo() + "'" +
            ", totalLandExtent=" + getTotalLandExtent() +
            ", calculatedArea=" + getCalculatedArea() +
            ", provinceId='" + getProvinceId() + "'" +
            ", districtId='" + getDistrictId() + "'" +
            ", dsId='" + getDsId() + "'" +
            ", gnId='" + getGnId() + "'" +
            ", centerLat=" + getCenterLat() +
            ", centerLng=" + getCenterLng() +
            "}";
    }
}
