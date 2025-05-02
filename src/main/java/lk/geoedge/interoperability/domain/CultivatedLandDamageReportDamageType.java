package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A CultivatedLandDamageReportDamageType.
 */
@Entity
@Table(name = "damage_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CultivatedLandDamageReportDamageType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "type_name")
    private String typeName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CultivatedLandDamageReportDamageType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public CultivatedLandDamageReportDamageType typeName(String typeName) {
        this.setTypeName(typeName);
        return this;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CultivatedLandDamageReportDamageType)) {
            return false;
        }
        return getId() != null && getId().equals(((CultivatedLandDamageReportDamageType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CultivatedLandDamageReportDamageType{" +
            "id=" + getId() +
            ", typeName='" + getTypeName() + "'" +
            "}";
    }
}
