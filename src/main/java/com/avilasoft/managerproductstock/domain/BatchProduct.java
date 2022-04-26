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
 * A BatchProduct.
 */
@Entity
@Table(name = "batch_product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BatchProduct implements Serializable {

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

    @OneToMany(mappedBy = "batchProduct")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "bachStatus", "businessAssociate", "batchProduct" }, allowSetters = true)
    private Set<Batch> batches = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "unit", "providerProduct", "productCategory", "batchProducts" }, allowSetters = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BatchProduct id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public BatchProduct name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public BatchProduct description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Batch> getBatches() {
        return this.batches;
    }

    public void setBatches(Set<Batch> batches) {
        if (this.batches != null) {
            this.batches.forEach(i -> i.setBatchProduct(null));
        }
        if (batches != null) {
            batches.forEach(i -> i.setBatchProduct(this));
        }
        this.batches = batches;
    }

    public BatchProduct batches(Set<Batch> batches) {
        this.setBatches(batches);
        return this;
    }

    public BatchProduct addBatch(Batch batch) {
        this.batches.add(batch);
        batch.setBatchProduct(this);
        return this;
    }

    public BatchProduct removeBatch(Batch batch) {
        this.batches.remove(batch);
        batch.setBatchProduct(null);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BatchProduct product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BatchProduct)) {
            return false;
        }
        return id != null && id.equals(((BatchProduct) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BatchProduct{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
