package com.avilasoft.managerproductstock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BatchStatus.
 */
@Entity
@Table(name = "batch_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BatchStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "bachStatus")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "bachStatus", "businessAssociate", "batchProducts" }, allowSetters = true)
    private Set<Batch> baches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BatchStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public BatchStatus name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public BatchStatus description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Batch> getBaches() {
        return this.baches;
    }

    public void setBaches(Set<Batch> batches) {
        if (this.baches != null) {
            this.baches.forEach(i -> i.setBachStatus(null));
        }
        if (batches != null) {
            batches.forEach(i -> i.setBachStatus(this));
        }
        this.baches = batches;
    }

    public BatchStatus baches(Set<Batch> batches) {
        this.setBaches(batches);
        return this;
    }

    public BatchStatus addBach(Batch batch) {
        this.baches.add(batch);
        batch.setBachStatus(this);
        return this;
    }

    public BatchStatus removeBach(Batch batch) {
        this.baches.remove(batch);
        batch.setBachStatus(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BatchStatus)) {
            return false;
        }
        return id != null && id.equals(((BatchStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BatchStatus{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
