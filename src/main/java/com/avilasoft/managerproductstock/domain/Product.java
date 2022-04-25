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
 * Product: producto necesario para su consumición final
 */
@Schema(description = "Product: producto necesario para su consumición final")
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "item", nullable = false)
    private String item;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity")
    private Float quantity;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "unitType", "providerProductPrice", "parents", "product", "children" }, allowSetters = true)
    private Set<Unit> units = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "batchProduct", "providerProductPrice", "provider" }, allowSetters = true)
    private Set<ProviderProduct> providerProducts = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "category" }, allowSetters = true)
    private Set<ProductCategory> productCategories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItem() {
        return this.item;
    }

    public Product item(String item) {
        this.setItem(item);
        return this;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getQuantity() {
        return this.quantity;
    }

    public Product quantity(Float quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Product image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Product imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Set<Unit> getUnits() {
        return this.units;
    }

    public void setUnits(Set<Unit> units) {
        if (this.units != null) {
            this.units.forEach(i -> i.setProduct(null));
        }
        if (units != null) {
            units.forEach(i -> i.setProduct(this));
        }
        this.units = units;
    }

    public Product units(Set<Unit> units) {
        this.setUnits(units);
        return this;
    }

    public Product addUnit(Unit unit) {
        this.units.add(unit);
        unit.setProduct(this);
        return this;
    }

    public Product removeUnit(Unit unit) {
        this.units.remove(unit);
        unit.setProduct(null);
        return this;
    }

    public Set<ProviderProduct> getProviderProducts() {
        return this.providerProducts;
    }

    public void setProviderProducts(Set<ProviderProduct> providerProducts) {
        if (this.providerProducts != null) {
            this.providerProducts.forEach(i -> i.setProduct(null));
        }
        if (providerProducts != null) {
            providerProducts.forEach(i -> i.setProduct(this));
        }
        this.providerProducts = providerProducts;
    }

    public Product providerProducts(Set<ProviderProduct> providerProducts) {
        this.setProviderProducts(providerProducts);
        return this;
    }

    public Product addProviderProduct(ProviderProduct providerProduct) {
        this.providerProducts.add(providerProduct);
        providerProduct.setProduct(this);
        return this;
    }

    public Product removeProviderProduct(ProviderProduct providerProduct) {
        this.providerProducts.remove(providerProduct);
        providerProduct.setProduct(null);
        return this;
    }

    public Set<ProductCategory> getProductCategories() {
        return this.productCategories;
    }

    public void setProductCategories(Set<ProductCategory> productCategories) {
        if (this.productCategories != null) {
            this.productCategories.forEach(i -> i.setProduct(null));
        }
        if (productCategories != null) {
            productCategories.forEach(i -> i.setProduct(this));
        }
        this.productCategories = productCategories;
    }

    public Product productCategories(Set<ProductCategory> productCategories) {
        this.setProductCategories(productCategories);
        return this;
    }

    public Product addProductCategory(ProductCategory productCategory) {
        this.productCategories.add(productCategory);
        productCategory.setProduct(this);
        return this;
    }

    public Product removeProductCategory(ProductCategory productCategory) {
        this.productCategories.remove(productCategory);
        productCategory.setProduct(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", item='" + getItem() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", quantity=" + getQuantity() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
