package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A CultivatedLandDamageReportDamageCategory.
 */
@Entity
@Table(name = "damage_category")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CultivatedLandDamageReportDamageCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "category_name")
    private String categoryName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CultivatedLandDamageReportDamageCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public CultivatedLandDamageReportDamageCategory categoryName(String categoryName) {
        this.setCategoryName(categoryName);
        return this;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CultivatedLandDamageReportDamageCategory)) {
            return false;
        }
        return getId() != null && getId().equals(((CultivatedLandDamageReportDamageCategory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CultivatedLandDamageReportDamageCategory{" +
            "id=" + getId() +
            ", categoryName='" + getCategoryName() + "'" +
            "}";
    }
}
