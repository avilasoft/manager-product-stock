package com.avilasoft.managerproductstock.domain;

import com.avilasoft.managerproductstock.domain.enumeration.BusinessAssociateType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BusinessAssociate.
 */
@Entity
@Table(name = "business_associate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BusinessAssociate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nickname", nullable = false)
    private String nickname;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "comercial_name")
    private String comercialName;

    @Column(name = "phone")
    private String phone;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private BusinessAssociateType type;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @JsonIgnoreProperties(value = { "businessAssociate" }, allowSetters = true)
    @OneToOne(mappedBy = "businessAssociate")
    private Provider provider;

    @JsonIgnoreProperties(value = { "businessAssociate", "projectGroup" }, allowSetters = true)
    @OneToOne(mappedBy = "businessAssociate")
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BusinessAssociate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return this.nickname;
    }

    public BusinessAssociate nickname(String nickname) {
        this.setNickname(nickname);
        return this;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return this.name;
    }

    public BusinessAssociate name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return this.lastname;
    }

    public BusinessAssociate lastname(String lastname) {
        this.setLastname(lastname);
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getComercialName() {
        return this.comercialName;
    }

    public BusinessAssociate comercialName(String comercialName) {
        this.setComercialName(comercialName);
        return this;
    }

    public void setComercialName(String comercialName) {
        this.comercialName = comercialName;
    }

    public String getPhone() {
        return this.phone;
    }

    public BusinessAssociate phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BusinessAssociateType getType() {
        return this.type;
    }

    public BusinessAssociate type(BusinessAssociateType type) {
        this.setType(type);
        return this;
    }

    public void setType(BusinessAssociateType type) {
        this.type = type;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BusinessAssociate user(User user) {
        this.setUser(user);
        return this;
    }

    public Provider getProvider() {
        return this.provider;
    }

    public void setProvider(Provider provider) {
        if (this.provider != null) {
            this.provider.setBusinessAssociate(null);
        }
        if (provider != null) {
            provider.setBusinessAssociate(this);
        }
        this.provider = provider;
    }

    public BusinessAssociate provider(Provider provider) {
        this.setProvider(provider);
        return this;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        if (this.project != null) {
            this.project.setBusinessAssociate(null);
        }
        if (project != null) {
            project.setBusinessAssociate(this);
        }
        this.project = project;
    }

    public BusinessAssociate project(Project project) {
        this.setProject(project);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessAssociate)) {
            return false;
        }
        return id != null && id.equals(((BusinessAssociate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessAssociate{" +
            "id=" + getId() +
            ", nickname='" + getNickname() + "'" +
            ", name='" + getName() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", comercialName='" + getComercialName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
