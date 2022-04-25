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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "bachStatus", "businessAssociate", "batchProducts" }, allowSetters = true)
    private Batch batch;

    @OneToMany(mappedBy = "batchProduct")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "batchProduct", "providerProductPrice", "provider" }, allowSetters = true)
    private Set<ProviderProduct> providerProducts = new HashSet<>();

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

    public Batch getBatch() {
        return this.batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public BatchProduct batch(Batch batch) {
        this.setBatch(batch);
        return this;
    }

    public Set<ProviderProduct> getProviderProducts() {
        return this.providerProducts;
    }

    public void setProviderProducts(Set<ProviderProduct> providerProducts) {
        if (this.providerProducts != null) {
            this.providerProducts.forEach(i -> i.setBatchProduct(null));
        }
        if (providerProducts != null) {
            providerProducts.forEach(i -> i.setBatchProduct(this));
        }
        this.providerProducts = providerProducts;
    }

    public BatchProduct providerProducts(Set<ProviderProduct> providerProducts) {
        this.setProviderProducts(providerProducts);
        return this;
    }

    public BatchProduct addProviderProduct(ProviderProduct providerProduct) {
        this.providerProducts.add(providerProduct);
        providerProduct.setBatchProduct(this);
        return this;
    }

    public BatchProduct removeProviderProduct(ProviderProduct providerProduct) {
        this.providerProducts.remove(providerProduct);
        providerProduct.setBatchProduct(null);
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
