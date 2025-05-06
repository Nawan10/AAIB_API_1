package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A InsuranceCultivatedLand.
 */
@Entity
@Table(name = "insurance_cultivated_land")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsuranceCultivatedLand implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "crop_duration_id")
    private String cropDurationId;

    @Column(name = "insurance_police_id")
    private String insurancePoliceId;

    @Column(name = "sum_insured_per_acre")
    private Integer sumInsuredPerAcre;

    @Column(name = "insurance_extent")
    private Integer insuranceExtent;

    @Column(name = "sum_amount")
    private Integer sumAmount;

    @Column(name = "insurance_status")
    private String insuranceStatus;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "added_by")
    private String addedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    private InsuranceCultivatedLandFarmer farmer;

    @ManyToOne(fetch = FetchType.LAZY)
    private InsuranceCultivatedLandCultivatedLand cultivatedLand;

    @ManyToOne(fetch = FetchType.EAGER)
    private InsuranceCultivatedLandCropType crop;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InsuranceCultivatedLand id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCropDurationId() {
        return this.cropDurationId;
    }

    public InsuranceCultivatedLand cropDurationId(String cropDurationId) {
        this.setCropDurationId(cropDurationId);
        return this;
    }

    public void setCropDurationId(String cropDurationId) {
        this.cropDurationId = cropDurationId;
    }

    public String getInsurancePoliceId() {
        return this.insurancePoliceId;
    }

    public InsuranceCultivatedLand insurancePoliceId(String insurancePoliceId) {
        this.setInsurancePoliceId(insurancePoliceId);
        return this;
    }

    public void setInsurancePoliceId(String insurancePoliceId) {
        this.insurancePoliceId = insurancePoliceId;
    }

    public Integer getSumInsuredPerAcre() {
        return this.sumInsuredPerAcre;
    }

    public InsuranceCultivatedLand sumInsuredPerAcre(Integer sumInsuredPerAcre) {
        this.setSumInsuredPerAcre(sumInsuredPerAcre);
        return this;
    }

    public void setSumInsuredPerAcre(Integer sumInsuredPerAcre) {
        this.sumInsuredPerAcre = sumInsuredPerAcre;
    }

    public Integer getInsuranceExtent() {
        return this.insuranceExtent;
    }

    public InsuranceCultivatedLand insuranceExtent(Integer insuranceExtent) {
        this.setInsuranceExtent(insuranceExtent);
        return this;
    }

    public void setInsuranceExtent(Integer insuranceExtent) {
        this.insuranceExtent = insuranceExtent;
    }

    public Integer getSumAmount() {
        return this.sumAmount;
    }

    public InsuranceCultivatedLand sumAmount(Integer sumAmount) {
        this.setSumAmount(sumAmount);
        return this;
    }

    public void setSumAmount(Integer sumAmount) {
        this.sumAmount = sumAmount;
    }

    public String getInsuranceStatus() {
        return this.insuranceStatus;
    }

    public InsuranceCultivatedLand insuranceStatus(String insuranceStatus) {
        this.setInsuranceStatus(insuranceStatus);
        return this;
    }

    public void setInsuranceStatus(String insuranceStatus) {
        this.insuranceStatus = insuranceStatus;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public InsuranceCultivatedLand createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getAddedBy() {
        return this.addedBy;
    }

    public InsuranceCultivatedLand addedBy(String addedBy) {
        this.setAddedBy(addedBy);
        return this;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public InsuranceCultivatedLandFarmer getFarmer() {
        return this.farmer;
    }

    public void setFarmer(InsuranceCultivatedLandFarmer insuranceCultivatedLandFarmer) {
        this.farmer = insuranceCultivatedLandFarmer;
    }

    public InsuranceCultivatedLand farmer(InsuranceCultivatedLandFarmer insuranceCultivatedLandFarmer) {
        this.setFarmer(insuranceCultivatedLandFarmer);
        return this;
    }

    public InsuranceCultivatedLandCultivatedLand getCultivatedLand() {
        return this.cultivatedLand;
    }

    public void setCultivatedLand(InsuranceCultivatedLandCultivatedLand insuranceCultivatedLandCultivatedLand) {
        this.cultivatedLand = insuranceCultivatedLandCultivatedLand;
    }

    public InsuranceCultivatedLand cultivatedLand(InsuranceCultivatedLandCultivatedLand insuranceCultivatedLandCultivatedLand) {
        this.setCultivatedLand(insuranceCultivatedLandCultivatedLand);
        return this;
    }

    public InsuranceCultivatedLandCropType getCrop() {
        return this.crop;
    }

    public void setCrop(InsuranceCultivatedLandCropType insuranceCultivatedLandCropType) {
        this.crop = insuranceCultivatedLandCropType;
    }

    public InsuranceCultivatedLand crop(InsuranceCultivatedLandCropType insuranceCultivatedLandCropType) {
        this.setCrop(insuranceCultivatedLandCropType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsuranceCultivatedLand)) {
            return false;
        }
        return getId() != null && getId().equals(((InsuranceCultivatedLand) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuranceCultivatedLand{" +
            "id=" + getId() +
            ", cropDurationId='" + getCropDurationId() + "'" +
            ", insurancePoliceId='" + getInsurancePoliceId() + "'" +
            ", sumInsuredPerAcre=" + getSumInsuredPerAcre() +
            ", insuranceExtent=" + getInsuranceExtent() +
            ", sumAmount=" + getSumAmount() +
            ", insuranceStatus='" + getInsuranceStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", addedBy='" + getAddedBy() + "'" +
            "}";
    }
}
