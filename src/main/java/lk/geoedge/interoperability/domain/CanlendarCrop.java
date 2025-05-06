package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A CanlendarCrop.
 */
@Entity
@Table(name = "canlendar_crop")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CanlendarCrop implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "percentage")
    private Double percentage;

    @Column(name = "canlendar_crop_status")
    private Integer canlendarCropStatus;

    @Column(name = "reason")
    private String reason;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "added_by")
    private String addedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    private CanlendarCropSeason season;

    @ManyToOne(fetch = FetchType.EAGER)
    private CanlendarCropCropType crop;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CanlendarCrop id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public CanlendarCrop startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public CanlendarCrop endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getPercentage() {
        return this.percentage;
    }

    public CanlendarCrop percentage(Double percentage) {
        this.setPercentage(percentage);
        return this;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Integer getCanlendarCropStatus() {
        return this.canlendarCropStatus;
    }

    public CanlendarCrop canlendarCropStatus(Integer canlendarCropStatus) {
        this.setCanlendarCropStatus(canlendarCropStatus);
        return this;
    }

    public void setCanlendarCropStatus(Integer canlendarCropStatus) {
        this.canlendarCropStatus = canlendarCropStatus;
    }

    public String getReason() {
        return this.reason;
    }

    public CanlendarCrop reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public CanlendarCrop createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getAddedBy() {
        return this.addedBy;
    }

    public CanlendarCrop addedBy(String addedBy) {
        this.setAddedBy(addedBy);
        return this;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public CanlendarCropSeason getSeason() {
        return this.season;
    }

    public void setSeason(CanlendarCropSeason canlendarCropSeason) {
        this.season = canlendarCropSeason;
    }

    public CanlendarCrop season(CanlendarCropSeason canlendarCropSeason) {
        this.setSeason(canlendarCropSeason);
        return this;
    }

    public CanlendarCropCropType getCrop() {
        return this.crop;
    }

    public void setCrop(CanlendarCropCropType canlendarCropCropType) {
        this.crop = canlendarCropCropType;
    }

    public CanlendarCrop crop(CanlendarCropCropType canlendarCropCropType) {
        this.setCrop(canlendarCropCropType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CanlendarCrop)) {
            return false;
        }
        return getId() != null && getId().equals(((CanlendarCrop) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CanlendarCrop{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", percentage=" + getPercentage() +
            ", canlendarCropStatus=" + getCanlendarCropStatus() +
            ", reason='" + getReason() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", addedBy='" + getAddedBy() + "'" +
            "}";
    }
}
