package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A CropVarietyCropDuration.
 */
@Entity
@Table(name = "crop_duration")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CropVarietyCropDuration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "name")
    private String name;

    @Column(name = "stages")
    private String stages;

    @Column(name = "added_by")
    private Integer addedBy;

    @Column(name = "added_date")
    private LocalDate addedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CropVarietyCropDuration id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public CropVarietyCropDuration duration(Integer duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getName() {
        return this.name;
    }

    public CropVarietyCropDuration name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStages() {
        return this.stages;
    }

    public CropVarietyCropDuration stages(String stages) {
        this.setStages(stages);
        return this;
    }

    public void setStages(String stages) {
        this.stages = stages;
    }

    public Integer getAddedBy() {
        return this.addedBy;
    }

    public CropVarietyCropDuration addedBy(Integer addedBy) {
        this.setAddedBy(addedBy);
        return this;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public LocalDate getAddedDate() {
        return this.addedDate;
    }

    public CropVarietyCropDuration addedDate(LocalDate addedDate) {
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
        if (!(o instanceof CropVarietyCropDuration)) {
            return false;
        }
        return getId() != null && getId().equals(((CropVarietyCropDuration) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CropVarietyCropDuration{" +
            "id=" + getId() +
            ", duration=" + getDuration() +
            ", name='" + getName() + "'" +
            ", stages='" + getStages() + "'" +
            ", addedBy=" + getAddedBy() +
            ", addedDate='" + getAddedDate() + "'" +
            "}";
    }
}
