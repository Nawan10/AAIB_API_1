package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A CultivatedLandDamageReason.
 */
@Entity
@Table(name = "cultivated_land_damage_reason")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CultivatedLandDamageReason implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private CultivatedLandDamageReasonDamageCategory damageCategory;

    @ManyToOne(fetch = FetchType.EAGER)
    private CultivatedLandDamageReasonDamageType damageType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CultivatedLandDamageReason id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public CultivatedLandDamageReason name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CultivatedLandDamageReasonDamageCategory getDamageCategory() {
        return this.damageCategory;
    }

    public void setDamageCategory(CultivatedLandDamageReasonDamageCategory cultivatedLandDamageReasonDamageCategory) {
        this.damageCategory = cultivatedLandDamageReasonDamageCategory;
    }

    public CultivatedLandDamageReason damageCategory(CultivatedLandDamageReasonDamageCategory cultivatedLandDamageReasonDamageCategory) {
        this.setDamageCategory(cultivatedLandDamageReasonDamageCategory);
        return this;
    }

    public CultivatedLandDamageReasonDamageType getDamageType() {
        return this.damageType;
    }

    public void setDamageType(CultivatedLandDamageReasonDamageType cultivatedLandDamageReasonDamageType) {
        this.damageType = cultivatedLandDamageReasonDamageType;
    }

    public CultivatedLandDamageReason damageType(CultivatedLandDamageReasonDamageType cultivatedLandDamageReasonDamageType) {
        this.setDamageType(cultivatedLandDamageReasonDamageType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CultivatedLandDamageReason)) {
            return false;
        }
        return getId() != null && getId().equals(((CultivatedLandDamageReason) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CultivatedLandDamageReason{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
