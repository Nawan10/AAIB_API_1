package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A IndexPayoutEventListSeason.
 */
@Entity
@Table(name = "season")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IndexPayoutEventListSeason implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "period")
    private String period;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IndexPayoutEventListSeason id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public IndexPayoutEventListSeason name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPeriod() {
        return this.period;
    }

    public IndexPayoutEventListSeason period(String period) {
        this.setPeriod(period);
        return this;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndexPayoutEventListSeason)) {
            return false;
        }
        return getId() != null && getId().equals(((IndexPayoutEventListSeason) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndexPayoutEventListSeason{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", period='" + getPeriod() + "'" +
            "}";
    }
}
