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

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @JsonIgnoreProperties(value = { "product", "batchProduct", "providerProductPrice", "provider" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private ProviderProduct providerProduct;

    @OneToMany(mappedBy = "providerProductPrice")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "unitType", "providerProductPrice", "parents", "product", "children" }, allowSetters = true)
    private Set<Unit> units = new HashSet<>();

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

    public BigDecimal getPrice() {
        return this.price;
    }

    public ProviderProductPrice price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProviderProduct getProviderProduct() {
        return this.providerProduct;
    }

    public void setProviderProduct(ProviderProduct providerProduct) {
        this.providerProduct = providerProduct;
    }

    public ProviderProductPrice providerProduct(ProviderProduct providerProduct) {
        this.setProviderProduct(providerProduct);
        return this;
    }

    public Set<Unit> getUnits() {
        return this.units;
    }

    public void setUnits(Set<Unit> units) {
        if (this.units != null) {
            this.units.forEach(i -> i.setProviderProductPrice(null));
        }
        if (units != null) {
            units.forEach(i -> i.setProviderProductPrice(this));
        }
        this.units = units;
    }

    public ProviderProductPrice units(Set<Unit> units) {
        this.setUnits(units);
        return this;
    }

    public ProviderProductPrice addUnit(Unit unit) {
        this.units.add(unit);
        unit.setProviderProductPrice(this);
        return this;
    }

    public ProviderProductPrice removeUnit(Unit unit) {
        this.units.remove(unit);
        unit.setProviderProductPrice(null);
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
            ", price=" + getPrice() +
            "}";
    }
}
