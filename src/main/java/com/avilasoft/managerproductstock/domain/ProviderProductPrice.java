package com.avilasoft.managerproductstock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * ProviderProductPrice: Precio base del producto suministrado por el proedor
 */
@Schema(description = "ProviderProductPrice: Precio base del producto suministrado por el proedor")
@Entity
@Table(name = "provider_product_price")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProviderProductPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "value", precision = 21, scale = 2, nullable = false)
    private BigDecimal value;

    @OneToMany(mappedBy = "providerProductPrice")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "products", "provider", "providerProductPrice" }, allowSetters = true)
    private Set<ProviderProduct> providerProducts = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "priceHistories" }, allowSetters = true)
    private Price price;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "products", "providerProductPrices", "unitEquivalences", "unitType" }, allowSetters = true)
    private Unit unit;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProviderProductPrice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ProviderProductPrice name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public ProviderProductPrice value(BigDecimal value) {
        this.setValue(value);
        return this;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Set<ProviderProduct> getProviderProducts() {
        return this.providerProducts;
    }

    public void setProviderProducts(Set<ProviderProduct> providerProducts) {
        if (this.providerProducts != null) {
            this.providerProducts.forEach(i -> i.setProviderProductPrice(null));
        }
        if (providerProducts != null) {
            providerProducts.forEach(i -> i.setProviderProductPrice(this));
        }
        this.providerProducts = providerProducts;
    }

    public ProviderProductPrice providerProducts(Set<ProviderProduct> providerProducts) {
        this.setProviderProducts(providerProducts);
        return this;
    }

    public ProviderProductPrice addProviderProduct(ProviderProduct providerProduct) {
        this.providerProducts.add(providerProduct);
        providerProduct.setProviderProductPrice(this);
        return this;
    }

    public ProviderProductPrice removeProviderProduct(ProviderProduct providerProduct) {
        this.providerProducts.remove(providerProduct);
        providerProduct.setProviderProductPrice(null);
        return this;
    }

    public Price getPrice() {
        return this.price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public ProviderProductPrice price(Price price) {
        this.setPrice(price);
        return this;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public ProviderProductPrice unit(Unit unit) {
        this.setUnit(unit);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProviderProductPrice)) {
            return false;
        }
        return id != null && id.equals(((ProviderProductPrice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProviderProductPrice{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
