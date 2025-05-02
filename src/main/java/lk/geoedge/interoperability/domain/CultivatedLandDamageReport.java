package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A CultivatedLandDamageReport.
 */
@Entity
@Table(name = "cultivated_land_damage_report")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CultivatedLandDamageReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "damage_reason_id")
    private String damageReasonId;

    @Column(name = "damage_serverity_id")
    private String damageServerityId;

    @Column(name = "damage_date_mointor")
    private String damageDateMonitor;

    @Column(name = "description")
    private String description;

    @Column(name = "farmer_comment")
    private String farmerComment;

    @Column(name = "estimated_yield")
    private String estimatedYield;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "added_by")
    private String addedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    private CropType crop;

    @ManyToOne(fetch = FetchType.EAGER)
    private CultivatedLandDamageReportDamageCategory damageCategory;

    @ManyToOne(fetch = FetchType.EAGER)
    private CultivatedLandDamageReportDamageType damageType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CultivatedLandDamageReport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDamageReasonId() {
        return this.damageReasonId;
    }

    public CultivatedLandDamageReport damageReasonId(String damageReasonId) {
        this.setDamageReasonId(damageReasonId);
        return this;
    }

    public void setDamageReasonId(String damageReasonId) {
        this.damageReasonId = damageReasonId;
    }

    public String getDamageServerityId() {
        return this.damageServerityId;
    }

    public CultivatedLandDamageReport damageServerityId(String damageServerityId) {
        this.setDamageServerityId(damageServerityId);
        return this;
    }

    public void setDamageServerityId(String damageServerityId) {
        this.damageServerityId = damageServerityId;
    }

    public String getDamageDateMonitor() {
        return this.damageDateMonitor;
    }

    public CultivatedLandDamageReport damageDateMonitor(String damageDateMonitor) {
        this.setDamageDateMonitor(damageDateMonitor);
        return this;
    }

    public void setDamageDateMonitor(String damageDateMonitor) {
        this.damageDateMonitor = damageDateMonitor;
    }

    public String getDescription() {
        return this.description;
    }

    public CultivatedLandDamageReport description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFarmerComment() {
        return this.farmerComment;
    }

    public CultivatedLandDamageReport farmerComment(String farmerComment) {
        this.setFarmerComment(farmerComment);
        return this;
    }

    public void setFarmerComment(String farmerComment) {
        this.farmerComment = farmerComment;
    }

    public String getEstimatedYield() {
        return this.estimatedYield;
    }

    public CultivatedLandDamageReport estimatedYield(String estimatedYield) {
        this.setEstimatedYield(estimatedYield);
        return this;
    }

    public void setEstimatedYield(String estimatedYield) {
        this.estimatedYield = estimatedYield;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public CultivatedLandDamageReport createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getAddedBy() {
        return this.addedBy;
    }

    public CultivatedLandDamageReport addedBy(String addedBy) {
        this.setAddedBy(addedBy);
        return this;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public CropType getCrop() {
        return this.crop;
    }

    public void setCrop(CropType cropType) {
        this.crop = cropType;
    }

    public CultivatedLandDamageReport crop(CropType cropType) {
        this.setCrop(cropType);
        return this;
    }

    public CultivatedLandDamageReportDamageCategory getDamageCategory() {
        return this.damageCategory;
    }

    public void setDamageCategory(CultivatedLandDamageReportDamageCategory cultivatedLandDamageReportDamageCategory) {
        this.damageCategory = cultivatedLandDamageReportDamageCategory;
    }

    public CultivatedLandDamageReport damageCategory(CultivatedLandDamageReportDamageCategory cultivatedLandDamageReportDamageCategory) {
        this.setDamageCategory(cultivatedLandDamageReportDamageCategory);
        return this;
    }

    public CultivatedLandDamageReportDamageType getDamageType() {
        return this.damageType;
    }

    public void setDamageType(CultivatedLandDamageReportDamageType cultivatedLandDamageReportDamageType) {
        this.damageType = cultivatedLandDamageReportDamageType;
    }

    public CultivatedLandDamageReport damageType(CultivatedLandDamageReportDamageType cultivatedLandDamageReportDamageType) {
        this.setDamageType(cultivatedLandDamageReportDamageType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CultivatedLandDamageReport)) {
            return false;
        }
        return getId() != null && getId().equals(((CultivatedLandDamageReport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CultivatedLandDamageReport{" +
            "id=" + getId() +
            ", damageReasonId='" + getDamageReasonId() + "'" +
            ", damageServerityId='" + getDamageServerityId() + "'" +
            ", damageDateMonitor='" + getDamageDateMonitor() + "'" +
            ", description='" + getDescription() + "'" +
            ", farmerComment='" + getFarmerComment() + "'" +
            ", estimatedYield='" + getEstimatedYield() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", addedBy='" + getAddedBy() + "'" +
            "}";
    }
}
