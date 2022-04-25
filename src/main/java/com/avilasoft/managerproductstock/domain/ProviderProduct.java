package com.avilasoft.managerproductstock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * ProviderProduct: Producto suministrado por el proveedor
 */
@Schema(description = "ProviderProduct: Producto suministrado por el proveedor")
@Entity
@Table(name = "provider_product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProviderProduct implements Serializable {

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

    @ManyToOne
    @JsonIgnoreProperties(value = { "units", "providerProducts", "productCategories" }, allowSetters = true)
    private Product product;

    @ManyToOne
    @JsonIgnoreProperties(value = { "batch", "providerProducts" }, allowSetters = true)
    private BatchProduct batchProduct;

    @JsonIgnoreProperties(value = { "providerProduct", "units" }, allowSetters = true)
    @OneToOne(mappedBy = "providerProduct")
    private ProviderProductPrice providerProductPrice;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "providerProducts" }, allowSetters = true)
    private Provider provider;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProviderProduct id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ProviderProduct name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ProviderProduct description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProviderProduct product(Product product) {
        this.setProduct(product);
        return this;
    }

    public BatchProduct getBatchProduct() {
        return this.batchProduct;
    }

    public void setBatchProduct(BatchProduct batchProduct) {
        this.batchProduct = batchProduct;
    }

    public ProviderProduct batchProduct(BatchProduct batchProduct) {
        this.setBatchProduct(batchProduct);
        return this;
    }

    public ProviderProductPrice getProviderProductPrice() {
        return this.providerProductPrice;
    }

    public void setProviderProductPrice(ProviderProductPrice providerProductPrice) {
        if (this.providerProductPrice != null) {
            this.providerProductPrice.setProviderProduct(null);
        }
        if (providerProductPrice != null) {
            providerProductPrice.setProviderProduct(this);
        }
        this.providerProductPrice = providerProductPrice;
    }

    public ProviderProduct providerProductPrice(ProviderProductPrice providerProductPrice) {
        this.setProviderProductPrice(providerProductPrice);
        return this;
    }

    public Provider getProvider() {
        return this.provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public ProviderProduct provider(Provider provider) {
        this.setProvider(provider);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProviderProduct)) {
            return false;
        }
        return id != null && id.equals(((ProviderProduct) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProviderProduct{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
