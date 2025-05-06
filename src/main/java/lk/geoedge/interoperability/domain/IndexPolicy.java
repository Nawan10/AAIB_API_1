package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A IndexPolicy.
 */
@Entity
@Table(name = "index_policy")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IndexPolicy implements Serializable {

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

    @Column(name = "stage_no")
    private Integer stageNo;

    @Column(name = "index_status")
    private Integer indexStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    private IndexPolicyInsurancePolicy policy;

    @ManyToOne(fetch = FetchType.EAGER)
    private IndexPolicySeason season;

    @ManyToOne(fetch = FetchType.EAGER)
    private IndexPolicyCropVariety cropVariety;

    @ManyToOne(fetch = FetchType.EAGER)
    private IndexPolicyCropType crop;

    @ManyToOne(fetch = FetchType.EAGER)
    private IndexPolicyWeatherStation weatherStation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IndexPolicy id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public IndexPolicy startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public IndexPolicy endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getStageNo() {
        return this.stageNo;
    }

    public IndexPolicy stageNo(Integer stageNo) {
        this.setStageNo(stageNo);
        return this;
    }

    public void setStageNo(Integer stageNo) {
        this.stageNo = stageNo;
    }

    public Integer getIndexStatus() {
        return this.indexStatus;
    }

    public IndexPolicy indexStatus(Integer indexStatus) {
        this.setIndexStatus(indexStatus);
        return this;
    }

    public void setIndexStatus(Integer indexStatus) {
        this.indexStatus = indexStatus;
    }

    public IndexPolicyInsurancePolicy getPolicy() {
        return this.policy;
    }

    public void setPolicy(IndexPolicyInsurancePolicy indexPolicyInsurancePolicy) {
        this.policy = indexPolicyInsurancePolicy;
    }

    public IndexPolicy policy(IndexPolicyInsurancePolicy indexPolicyInsurancePolicy) {
        this.setPolicy(indexPolicyInsurancePolicy);
        return this;
    }

    public IndexPolicySeason getSeason() {
        return this.season;
    }

    public void setSeason(IndexPolicySeason indexPolicySeason) {
        this.season = indexPolicySeason;
    }

    public IndexPolicy season(IndexPolicySeason indexPolicySeason) {
        this.setSeason(indexPolicySeason);
        return this;
    }

    public IndexPolicyCropVariety getCropVariety() {
        return this.cropVariety;
    }

    public void setCropVariety(IndexPolicyCropVariety indexPolicyCropVariety) {
        this.cropVariety = indexPolicyCropVariety;
    }

    public IndexPolicy cropVariety(IndexPolicyCropVariety indexPolicyCropVariety) {
        this.setCropVariety(indexPolicyCropVariety);
        return this;
    }

    public IndexPolicyCropType getCrop() {
        return this.crop;
    }

    public void setCrop(IndexPolicyCropType indexPolicyCropType) {
        this.crop = indexPolicyCropType;
    }

    public IndexPolicy crop(IndexPolicyCropType indexPolicyCropType) {
        this.setCrop(indexPolicyCropType);
        return this;
    }

    public IndexPolicyWeatherStation getWeatherStation() {
        return this.weatherStation;
    }

    public void setWeatherStation(IndexPolicyWeatherStation indexPolicyWeatherStation) {
        this.weatherStation = indexPolicyWeatherStation;
    }

    public IndexPolicy weatherStation(IndexPolicyWeatherStation indexPolicyWeatherStation) {
        this.setWeatherStation(indexPolicyWeatherStation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndexPolicy)) {
            return false;
        }
        return getId() != null && getId().equals(((IndexPolicy) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndexPolicy{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", stageNo=" + getStageNo() +
            ", indexStatus=" + getIndexStatus() +
            "}";
    }
}
