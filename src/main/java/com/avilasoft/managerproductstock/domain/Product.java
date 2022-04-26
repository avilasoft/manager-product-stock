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

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "quantity")
    private Float quantity;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "products", "providerProductPrices", "unitEquivalences", "unitType" }, allowSetters = true)
    private Unit unit;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "products", "provider", "providerProductPrice" }, allowSetters = true)
    private ProviderProduct providerProduct;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "products", "category" }, allowSetters = true)
    private ProductCategory productCategory;

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "batches", "product" }, allowSetters = true)
    private Set<BatchProduct> batchProducts = new HashSet<>();

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

    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Product unit(Unit unit) {
        this.setUnit(unit);
        return this;
    }

    public ProviderProduct getProviderProduct() {
        return this.providerProduct;
    }

    public void setProviderProduct(ProviderProduct providerProduct) {
        this.providerProduct = providerProduct;
    }

    public Product providerProduct(ProviderProduct providerProduct) {
        this.setProviderProduct(providerProduct);
        return this;
    }

    public ProductCategory getProductCategory() {
        return this.productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public Product productCategory(ProductCategory productCategory) {
        this.setProductCategory(productCategory);
        return this;
    }

    public Set<BatchProduct> getBatchProducts() {
        return this.batchProducts;
    }

    public void setBatchProducts(Set<BatchProduct> batchProducts) {
        if (this.batchProducts != null) {
            this.batchProducts.forEach(i -> i.setProduct(null));
        }
        if (batchProducts != null) {
            batchProducts.forEach(i -> i.setProduct(this));
        }
        this.batchProducts = batchProducts;
    }

    public Product batchProducts(Set<BatchProduct> batchProducts) {
        this.setBatchProducts(batchProducts);
        return this;
    }

    public Product addBatchProduct(BatchProduct batchProduct) {
        this.batchProducts.add(batchProduct);
        batchProduct.setProduct(this);
        return this;
    }

    public Product removeBatchProduct(BatchProduct batchProduct) {
        this.batchProducts.remove(batchProduct);
        batchProduct.setProduct(null);
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
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", quantity=" + getQuantity() +
            "}";
    }
}
