package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A IndexCoverages.
 */
@Entity
@Table(name = "index_coverages")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IndexCoverages implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "index_product_id")
    private Integer indexProductId;

    @Column(name = "premium_rate")
    private Integer premiumRate;

    @Column(name = "is_free")
    private Integer isFree;

    @Column(name = "is_paid")
    private Integer isPaid;

    @ManyToOne(fetch = FetchType.EAGER)
    private IndexCoveragesCultivatedLandDamageReason damageReason;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IndexCoverages id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIndexProductId() {
        return this.indexProductId;
    }

    public IndexCoverages indexProductId(Integer indexProductId) {
        this.setIndexProductId(indexProductId);
        return this;
    }

    public void setIndexProductId(Integer indexProductId) {
        this.indexProductId = indexProductId;
    }

    public Integer getPremiumRate() {
        return this.premiumRate;
    }

    public IndexCoverages premiumRate(Integer premiumRate) {
        this.setPremiumRate(premiumRate);
        return this;
    }

    public void setPremiumRate(Integer premiumRate) {
        this.premiumRate = premiumRate;
    }

    public Integer getIsFree() {
        return this.isFree;
    }

    public IndexCoverages isFree(Integer isFree) {
        this.setIsFree(isFree);
        return this;
    }

    public void setIsFree(Integer isFree) {
        this.isFree = isFree;
    }

    public Integer getIsPaid() {
        return this.isPaid;
    }

    public IndexCoverages isPaid(Integer isPaid) {
        this.setIsPaid(isPaid);
        return this;
    }

    public void setIsPaid(Integer isPaid) {
        this.isPaid = isPaid;
    }

    public IndexCoveragesCultivatedLandDamageReason getDamageReason() {
        return this.damageReason;
    }

    public void setDamageReason(IndexCoveragesCultivatedLandDamageReason indexCoveragesCultivatedLandDamageReason) {
        this.damageReason = indexCoveragesCultivatedLandDamageReason;
    }

    public IndexCoverages damageReason(IndexCoveragesCultivatedLandDamageReason indexCoveragesCultivatedLandDamageReason) {
        this.setDamageReason(indexCoveragesCultivatedLandDamageReason);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndexCoverages)) {
            return false;
        }
        return getId() != null && getId().equals(((IndexCoverages) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndexCoverages{" +
            "id=" + getId() +
            ", indexProductId=" + getIndexProductId() +
            ", premiumRate=" + getPremiumRate() +
            ", isFree=" + getIsFree() +
            ", isPaid=" + getIsPaid() +
            "}";
    }
}
