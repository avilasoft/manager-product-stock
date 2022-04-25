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
 * A Batch.
 */
@Entity
@Table(name = "batch")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Batch implements Serializable {

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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "baches" }, allowSetters = true)
    private BatchStatus bachStatus;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "batches" }, allowSetters = true)
    private BusinessAssociate businessAssociate;

    @OneToMany(mappedBy = "batch")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "batch", "providerProducts" }, allowSetters = true)
    private Set<BatchProduct> batchProducts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Batch id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Batch name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Batch description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BatchStatus getBachStatus() {
        return this.bachStatus;
    }

    public void setBachStatus(BatchStatus batchStatus) {
        this.bachStatus = batchStatus;
    }

    public Batch bachStatus(BatchStatus batchStatus) {
        this.setBachStatus(batchStatus);
        return this;
    }

    public BusinessAssociate getBusinessAssociate() {
        return this.businessAssociate;
    }

    public void setBusinessAssociate(BusinessAssociate businessAssociate) {
        this.businessAssociate = businessAssociate;
    }

    public Batch businessAssociate(BusinessAssociate businessAssociate) {
        this.setBusinessAssociate(businessAssociate);
        return this;
    }

    public Set<BatchProduct> getBatchProducts() {
        return this.batchProducts;
    }

    public void setBatchProducts(Set<BatchProduct> batchProducts) {
        if (this.batchProducts != null) {
            this.batchProducts.forEach(i -> i.setBatch(null));
        }
        if (batchProducts != null) {
            batchProducts.forEach(i -> i.setBatch(this));
        }
        this.batchProducts = batchProducts;
    }

    public Batch batchProducts(Set<BatchProduct> batchProducts) {
        this.setBatchProducts(batchProducts);
        return this;
    }

    public Batch addBatchProduct(BatchProduct batchProduct) {
        this.batchProducts.add(batchProduct);
        batchProduct.setBatch(this);
        return this;
    }

    public Batch removeBatchProduct(BatchProduct batchProduct) {
        this.batchProducts.remove(batchProduct);
        batchProduct.setBatch(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Batch)) {
            return false;
        }
        return id != null && id.equals(((Batch) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Batch{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
