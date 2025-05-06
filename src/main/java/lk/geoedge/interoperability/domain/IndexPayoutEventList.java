package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A IndexPayoutEventList.
 */
@Entity
@Table(name = "index_payout_event_list")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IndexPayoutEventList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "index_payout_event_id")
    private Integer indexPayoutEventId;

    @Column(name = "asc_id")
    private Integer ascId;

    @Column(name = "confirmed_by")
    private Integer confirmedBy;

    @Column(name = "cultivated_extent")
    private Double cultivatedExtent;

    @Column(name = "payout")
    private Double payout;

    @Column(name = "confirmed_date")
    private String confirmedDate;

    @Column(name = "rejected_by")
    private Integer rejectedBy;

    @Column(name = "rejected_date")
    private String rejectedDate;

    @Column(name = "reason")
    private String reason;

    @Column(name = "final_payout")
    private Double finalPayout;

    @Column(name = "index_payout_event_status")
    private Integer indexPayoutEventStatus;

    @Column(name = "is_approved")
    private Integer isApproved;

    @Column(name = "monitoring_range")
    private Double monitoringRange;

    @Column(name = "is_insurance")
    private Integer isInsurance;

    @Column(name = "insurance_cultivated_land")
    private Integer insuranceCultivatedLand;

    @Column(name = "index_cheque_id")
    private Integer indexChequeId;

    @Column(name = "index_product_id")
    private Integer indexProductId;

    @ManyToOne(fetch = FetchType.EAGER)
    private IndexPayoutEventListFarmer cultivatedFarmer;

    @ManyToOne(fetch = FetchType.EAGER)
    private IndexPayoutEventListCultivatedLand cultivatedLand;

    @ManyToOne(fetch = FetchType.EAGER)
    private IndexPayoutEventListSeason season;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IndexPayoutEventList id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIndexPayoutEventId() {
        return this.indexPayoutEventId;
    }

    public IndexPayoutEventList indexPayoutEventId(Integer indexPayoutEventId) {
        this.setIndexPayoutEventId(indexPayoutEventId);
        return this;
    }

    public void setIndexPayoutEventId(Integer indexPayoutEventId) {
        this.indexPayoutEventId = indexPayoutEventId;
    }

    public Integer getAscId() {
        return this.ascId;
    }

    public IndexPayoutEventList ascId(Integer ascId) {
        this.setAscId(ascId);
        return this;
    }

    public void setAscId(Integer ascId) {
        this.ascId = ascId;
    }

    public Integer getConfirmedBy() {
        return this.confirmedBy;
    }

    public IndexPayoutEventList confirmedBy(Integer confirmedBy) {
        this.setConfirmedBy(confirmedBy);
        return this;
    }

    public void setConfirmedBy(Integer confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    public Double getCultivatedExtent() {
        return this.cultivatedExtent;
    }

    public IndexPayoutEventList cultivatedExtent(Double cultivatedExtent) {
        this.setCultivatedExtent(cultivatedExtent);
        return this;
    }

    public void setCultivatedExtent(Double cultivatedExtent) {
        this.cultivatedExtent = cultivatedExtent;
    }

    public Double getPayout() {
        return this.payout;
    }

    public IndexPayoutEventList payout(Double payout) {
        this.setPayout(payout);
        return this;
    }

    public void setPayout(Double payout) {
        this.payout = payout;
    }

    public String getConfirmedDate() {
        return this.confirmedDate;
    }

    public IndexPayoutEventList confirmedDate(String confirmedDate) {
        this.setConfirmedDate(confirmedDate);
        return this;
    }

    public void setConfirmedDate(String confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    public Integer getRejectedBy() {
        return this.rejectedBy;
    }

    public IndexPayoutEventList rejectedBy(Integer rejectedBy) {
        this.setRejectedBy(rejectedBy);
        return this;
    }

    public void setRejectedBy(Integer rejectedBy) {
        this.rejectedBy = rejectedBy;
    }

    public String getRejectedDate() {
        return this.rejectedDate;
    }

    public IndexPayoutEventList rejectedDate(String rejectedDate) {
        this.setRejectedDate(rejectedDate);
        return this;
    }

    public void setRejectedDate(String rejectedDate) {
        this.rejectedDate = rejectedDate;
    }

    public String getReason() {
        return this.reason;
    }

    public IndexPayoutEventList reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Double getFinalPayout() {
        return this.finalPayout;
    }

    public IndexPayoutEventList finalPayout(Double finalPayout) {
        this.setFinalPayout(finalPayout);
        return this;
    }

    public void setFinalPayout(Double finalPayout) {
        this.finalPayout = finalPayout;
    }

    public Integer getIndexPayoutEventStatus() {
        return this.indexPayoutEventStatus;
    }

    public IndexPayoutEventList indexPayoutEventStatus(Integer indexPayoutEventStatus) {
        this.setIndexPayoutEventStatus(indexPayoutEventStatus);
        return this;
    }

    public void setIndexPayoutEventStatus(Integer indexPayoutEventStatus) {
        this.indexPayoutEventStatus = indexPayoutEventStatus;
    }

    public Integer getIsApproved() {
        return this.isApproved;
    }

    public IndexPayoutEventList isApproved(Integer isApproved) {
        this.setIsApproved(isApproved);
        return this;
    }

    public void setIsApproved(Integer isApproved) {
        this.isApproved = isApproved;
    }

    public Double getMonitoringRange() {
        return this.monitoringRange;
    }

    public IndexPayoutEventList monitoringRange(Double monitoringRange) {
        this.setMonitoringRange(monitoringRange);
        return this;
    }

    public void setMonitoringRange(Double monitoringRange) {
        this.monitoringRange = monitoringRange;
    }

    public Integer getIsInsurance() {
        return this.isInsurance;
    }

    public IndexPayoutEventList isInsurance(Integer isInsurance) {
        this.setIsInsurance(isInsurance);
        return this;
    }

    public void setIsInsurance(Integer isInsurance) {
        this.isInsurance = isInsurance;
    }

    public Integer getInsuranceCultivatedLand() {
        return this.insuranceCultivatedLand;
    }

    public IndexPayoutEventList insuranceCultivatedLand(Integer insuranceCultivatedLand) {
        this.setInsuranceCultivatedLand(insuranceCultivatedLand);
        return this;
    }

    public void setInsuranceCultivatedLand(Integer insuranceCultivatedLand) {
        this.insuranceCultivatedLand = insuranceCultivatedLand;
    }

    public Integer getIndexChequeId() {
        return this.indexChequeId;
    }

    public IndexPayoutEventList indexChequeId(Integer indexChequeId) {
        this.setIndexChequeId(indexChequeId);
        return this;
    }

    public void setIndexChequeId(Integer indexChequeId) {
        this.indexChequeId = indexChequeId;
    }

    public Integer getIndexProductId() {
        return this.indexProductId;
    }

    public IndexPayoutEventList indexProductId(Integer indexProductId) {
        this.setIndexProductId(indexProductId);
        return this;
    }

    public void setIndexProductId(Integer indexProductId) {
        this.indexProductId = indexProductId;
    }

    public IndexPayoutEventListFarmer getCultivatedFarmer() {
        return this.cultivatedFarmer;
    }

    public void setCultivatedFarmer(IndexPayoutEventListFarmer indexPayoutEventListFarmer) {
        this.cultivatedFarmer = indexPayoutEventListFarmer;
    }

    public IndexPayoutEventList cultivatedFarmer(IndexPayoutEventListFarmer indexPayoutEventListFarmer) {
        this.setCultivatedFarmer(indexPayoutEventListFarmer);
        return this;
    }

    public IndexPayoutEventListCultivatedLand getCultivatedLand() {
        return this.cultivatedLand;
    }

    public void setCultivatedLand(IndexPayoutEventListCultivatedLand indexPayoutEventListCultivatedLand) {
        this.cultivatedLand = indexPayoutEventListCultivatedLand;
    }

    public IndexPayoutEventList cultivatedLand(IndexPayoutEventListCultivatedLand indexPayoutEventListCultivatedLand) {
        this.setCultivatedLand(indexPayoutEventListCultivatedLand);
        return this;
    }

    public IndexPayoutEventListSeason getSeason() {
        return this.season;
    }

    public void setSeason(IndexPayoutEventListSeason indexPayoutEventListSeason) {
        this.season = indexPayoutEventListSeason;
    }

    public IndexPayoutEventList season(IndexPayoutEventListSeason indexPayoutEventListSeason) {
        this.setSeason(indexPayoutEventListSeason);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndexPayoutEventList)) {
            return false;
        }
        return getId() != null && getId().equals(((IndexPayoutEventList) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndexPayoutEventList{" +
            "id=" + getId() +
            ", indexPayoutEventId=" + getIndexPayoutEventId() +
            ", ascId=" + getAscId() +
            ", confirmedBy=" + getConfirmedBy() +
            ", cultivatedExtent=" + getCultivatedExtent() +
            ", payout=" + getPayout() +
            ", confirmedDate='" + getConfirmedDate() + "'" +
            ", rejectedBy=" + getRejectedBy() +
            ", rejectedDate='" + getRejectedDate() + "'" +
            ", reason='" + getReason() + "'" +
            ", finalPayout=" + getFinalPayout() +
            ", indexPayoutEventStatus=" + getIndexPayoutEventStatus() +
            ", isApproved=" + getIsApproved() +
            ", monitoringRange=" + getMonitoringRange() +
            ", isInsurance=" + getIsInsurance() +
            ", insuranceCultivatedLand=" + getInsuranceCultivatedLand() +
            ", indexChequeId=" + getIndexChequeId() +
            ", indexProductId=" + getIndexProductId() +
            "}";
    }
}
