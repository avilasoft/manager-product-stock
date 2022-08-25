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
@Table(name = "provider_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProviderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "cost", nullable = false)
    private Float cost;

    @Column(name = "description")
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "businessAssociate" }, allowSetters = true)
    private Provider provider;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "projectItemListItems", "providerItems" }, allowSetters = true)
    private Unit unit;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "providerItems" }, allowSetters = true)
    private Item item;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProviderItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public ProviderItem code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Float getCost() {
        return this.cost;
    }

    public ProviderItem cost(Float cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return this.description;
    }

    public ProviderItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Provider getProvider() {
        return this.provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public ProviderItem provider(Provider provider) {
        this.setProvider(provider);
        return this;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public ProviderItem unit(Unit unit) {
        this.setUnit(unit);
        return this;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public ProviderItem item(Item item) {
        this.setItem(item);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProviderItem)) {
            return false;
        }
        return id != null && id.equals(((ProviderItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProviderItem{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", cost=" + getCost() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
