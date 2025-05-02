package lk.geoedge.interoperability.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A CultivatedCrop.
 */
@Entity
@Table(name = "cultivated_crop")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CultivatedCrop implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "cultivated_extend")
    private Double cultivatedExtend;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "yield")
    private Double yield;

    @Column(name = "unit_id")
    private String unitId;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "added_by")
    private String addedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "season" }, allowSetters = true)
    private CultivatedCropCultivatedLand cultivatedLand;

    @ManyToOne(fetch = FetchType.EAGER)
    private CultivatedCropCropType crop;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CultivatedCrop id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCultivatedExtend() {
        return this.cultivatedExtend;
    }

    public CultivatedCrop cultivatedExtend(Double cultivatedExtend) {
        this.setCultivatedExtend(cultivatedExtend);
        return this;
    }

    public void setCultivatedExtend(Double cultivatedExtend) {
        this.cultivatedExtend = cultivatedExtend;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public CultivatedCrop startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public CultivatedCrop endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getYield() {
        return this.yield;
    }

    public CultivatedCrop yield(Double yield) {
        this.setYield(yield);
        return this;
    }

    public void setYield(Double yield) {
        this.yield = yield;
    }

    public String getUnitId() {
        return this.unitId;
    }

    public CultivatedCrop unitId(String unitId) {
        this.setUnitId(unitId);
        return this;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public CultivatedCrop createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getAddedBy() {
        return this.addedBy;
    }

    public CultivatedCrop addedBy(String addedBy) {
        this.setAddedBy(addedBy);
        return this;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public CultivatedCropCultivatedLand getCultivatedLand() {
        return this.cultivatedLand;
    }

    public void setCultivatedLand(CultivatedCropCultivatedLand cultivatedCropCultivatedLand) {
        this.cultivatedLand = cultivatedCropCultivatedLand;
    }

    public CultivatedCrop cultivatedLand(CultivatedCropCultivatedLand cultivatedCropCultivatedLand) {
        this.setCultivatedLand(cultivatedCropCultivatedLand);
        return this;
    }

    public CultivatedCropCropType getCrop() {
        return this.crop;
    }

    public void setCrop(CultivatedCropCropType cultivatedCropCropType) {
        this.crop = cultivatedCropCropType;
    }

    public CultivatedCrop crop(CultivatedCropCropType cultivatedCropCropType) {
        this.setCrop(cultivatedCropCropType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CultivatedCrop)) {
            return false;
        }
        return getId() != null && getId().equals(((CultivatedCrop) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CultivatedCrop{" +
            "id=" + getId() +
            ", cultivatedExtend=" + getCultivatedExtend() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", yield=" + getYield() +
            ", unitId='" + getUnitId() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", addedBy='" + getAddedBy() + "'" +
            "}";
    }
}
