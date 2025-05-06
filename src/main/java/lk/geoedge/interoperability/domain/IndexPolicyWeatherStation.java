package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A IndexPolicyWeatherStation.
 */
@Entity
@Table(name = "weather_station")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IndexPolicyWeatherStation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "gn_id")
    private Integer gnId;

    @Column(name = "district_id")
    private Integer districtId;

    @Column(name = "province_id")
    private Integer provinceId;

    @Column(name = "ds_id")
    private Integer dsId;

    @Column(name = "added_by")
    private Integer addedBy;

    @Column(name = "created_at")
    private LocalDate createdAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IndexPolicyWeatherStation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public IndexPolicyWeatherStation name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public IndexPolicyWeatherStation code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public IndexPolicyWeatherStation latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public IndexPolicyWeatherStation longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getGnId() {
        return this.gnId;
    }

    public IndexPolicyWeatherStation gnId(Integer gnId) {
        this.setGnId(gnId);
        return this;
    }

    public void setGnId(Integer gnId) {
        this.gnId = gnId;
    }

    public Integer getDistrictId() {
        return this.districtId;
    }

    public IndexPolicyWeatherStation districtId(Integer districtId) {
        this.setDistrictId(districtId);
        return this;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public Integer getProvinceId() {
        return this.provinceId;
    }

    public IndexPolicyWeatherStation provinceId(Integer provinceId) {
        this.setProvinceId(provinceId);
        return this;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getDsId() {
        return this.dsId;
    }

    public IndexPolicyWeatherStation dsId(Integer dsId) {
        this.setDsId(dsId);
        return this;
    }

    public void setDsId(Integer dsId) {
        this.dsId = dsId;
    }

    public Integer getAddedBy() {
        return this.addedBy;
    }

    public IndexPolicyWeatherStation addedBy(Integer addedBy) {
        this.setAddedBy(addedBy);
        return this;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public IndexPolicyWeatherStation createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndexPolicyWeatherStation)) {
            return false;
        }
        return getId() != null && getId().equals(((IndexPolicyWeatherStation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndexPolicyWeatherStation{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", gnId=" + getGnId() +
            ", districtId=" + getDistrictId() +
            ", provinceId=" + getProvinceId() +
            ", dsId=" + getDsId() +
            ", addedBy=" + getAddedBy() +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
