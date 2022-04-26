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
 * A Unit.
 */
@Entity
@Table(name = "unit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Unit implements Serializable {

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
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Column(name = "is_base")
    private Boolean isBase;

    @OneToMany(mappedBy = "unit")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "unit", "providerProduct", "productCategory", "batchProducts" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "unit")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "providerProducts", "price", "unit" }, allowSetters = true)
    private Set<ProviderProductPrice> providerProductPrices = new HashSet<>();

    @OneToMany(mappedBy = "unit")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parents", "unit", "child" }, allowSetters = true)
    private Set<UnitEquivalence> unitEquivalences = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private UnitType unitType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Unit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Unit name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Unit description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public Unit symbol(String symbol) {
        this.setSymbol(symbol);
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Boolean getIsBase() {
        return this.isBase;
    }

    public Unit isBase(Boolean isBase) {
        this.setIsBase(isBase);
        return this;
    }

    public void setIsBase(Boolean isBase) {
        this.isBase = isBase;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setUnit(null));
        }
        if (products != null) {
            products.forEach(i -> i.setUnit(this));
        }
        this.products = products;
    }

    public Unit products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Unit addProduct(Product product) {
        this.products.add(product);
        product.setUnit(this);
        return this;
    }

    public Unit removeProduct(Product product) {
        this.products.remove(product);
        product.setUnit(null);
        return this;
    }

    public Set<ProviderProductPrice> getProviderProductPrices() {
        return this.providerProductPrices;
    }

    public void setProviderProductPrices(Set<ProviderProductPrice> providerProductPrices) {
        if (this.providerProductPrices != null) {
            this.providerProductPrices.forEach(i -> i.setUnit(null));
        }
        if (providerProductPrices != null) {
            providerProductPrices.forEach(i -> i.setUnit(this));
        }
        this.providerProductPrices = providerProductPrices;
    }

    public Unit providerProductPrices(Set<ProviderProductPrice> providerProductPrices) {
        this.setProviderProductPrices(providerProductPrices);
        return this;
    }

    public Unit addProviderProductPrice(ProviderProductPrice providerProductPrice) {
        this.providerProductPrices.add(providerProductPrice);
        providerProductPrice.setUnit(this);
        return this;
    }

    public Unit removeProviderProductPrice(ProviderProductPrice providerProductPrice) {
        this.providerProductPrices.remove(providerProductPrice);
        providerProductPrice.setUnit(null);
        return this;
    }

    public Set<UnitEquivalence> getUnitEquivalences() {
        return this.unitEquivalences;
    }

    public void setUnitEquivalences(Set<UnitEquivalence> unitEquivalences) {
        if (this.unitEquivalences != null) {
            this.unitEquivalences.forEach(i -> i.setUnit(null));
        }
        if (unitEquivalences != null) {
            unitEquivalences.forEach(i -> i.setUnit(this));
        }
        this.unitEquivalences = unitEquivalences;
    }

    public Unit unitEquivalences(Set<UnitEquivalence> unitEquivalences) {
        this.setUnitEquivalences(unitEquivalences);
        return this;
    }

    public Unit addUnitEquivalence(UnitEquivalence unitEquivalence) {
        this.unitEquivalences.add(unitEquivalence);
        unitEquivalence.setUnit(this);
        return this;
    }

    public Unit removeUnitEquivalence(UnitEquivalence unitEquivalence) {
        this.unitEquivalences.remove(unitEquivalence);
        unitEquivalence.setUnit(null);
        return this;
    }

    public UnitType getUnitType() {
        return this.unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public Unit unitType(UnitType unitType) {
        this.setUnitType(unitType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Unit)) {
            return false;
        }
        return id != null && id.equals(((Unit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Unit{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", isBase='" + getIsBase() + "'" +
            "}";
    }
}
