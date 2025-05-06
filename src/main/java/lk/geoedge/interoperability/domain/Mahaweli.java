package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Mahaweli.
 */
@Entity
@Table(name = "mahaweli")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Mahaweli implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "mahaweli")
    private String mahaweli;

    @Column(name = "code")
    private String code;

    @Column(name = "added_by")
    private Integer addedBy;

    @Column(name = "added_date")
    private LocalDate addedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Mahaweli id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMahaweli() {
        return this.mahaweli;
    }

    public Mahaweli mahaweli(String mahaweli) {
        this.setMahaweli(mahaweli);
        return this;
    }

    public void setMahaweli(String mahaweli) {
        this.mahaweli = mahaweli;
    }

    public String getCode() {
        return this.code;
    }

    public Mahaweli code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getAddedBy() {
        return this.addedBy;
    }

    public Mahaweli addedBy(Integer addedBy) {
        this.setAddedBy(addedBy);
        return this;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public LocalDate getAddedDate() {
        return this.addedDate;
    }

    public Mahaweli addedDate(LocalDate addedDate) {
        this.setAddedDate(addedDate);
        return this;
    }

    public void setAddedDate(LocalDate addedDate) {
        this.addedDate = addedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mahaweli)) {
            return false;
        }
        return getId() != null && getId().equals(((Mahaweli) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mahaweli{" +
            "id=" + getId() +
            ", mahaweli='" + getMahaweli() + "'" +
            ", code='" + getCode() + "'" +
            ", addedBy=" + getAddedBy() +
            ", addedDate='" + getAddedDate() + "'" +
            "}";
    }
}
