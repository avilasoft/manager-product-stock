package com.avilasoft.managerproductstock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
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

    @OneToMany(mappedBy = "providerProduct")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "unit", "providerProduct", "productCategory", "batchProducts" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Provider provider;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "providerProducts", "price", "unit" }, allowSetters = true)
    private ProviderProductPrice providerProductPrice;

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

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setProviderProduct(null));
        }
        if (products != null) {
            products.forEach(i -> i.setProviderProduct(this));
        }
        this.products = products;
    }

    public ProviderProduct products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public ProviderProduct addProduct(Product product) {
        this.products.add(product);
        product.setProviderProduct(this);
        return this;
    }

    public ProviderProduct removeProduct(Product product) {
        this.products.remove(product);
        product.setProviderProduct(null);
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

    public ProviderProductPrice getProviderProductPrice() {
        return this.providerProductPrice;
    }

    public void setProviderProductPrice(ProviderProductPrice providerProductPrice) {
        this.providerProductPrice = providerProductPrice;
    }

    public ProviderProduct providerProductPrice(ProviderProductPrice providerProductPrice) {
        this.setProviderProductPrice(providerProductPrice);
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
