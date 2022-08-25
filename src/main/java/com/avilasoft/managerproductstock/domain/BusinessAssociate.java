package com.avilasoft.managerproductstock.domain;

import com.avilasoft.managerproductstock.domain.enumeration.BusinessAssociateType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
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
    @Column(name = "comercial_name", nullable = false)
    private String comercialName;

    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private BusinessAssociateType type;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @OneToMany(mappedBy = "businessAssociate")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "businessAssociate" }, allowSetters = true)
    private Set<Provider> providers = new HashSet<>();

    @OneToMany(mappedBy = "businessAssociate")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "businessAssociate", "projectGroup" }, allowSetters = true)
    private Set<Project> projects = new HashSet<>();

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

    public String getDescription() {
        return this.description;
    }

    public BusinessAssociate description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Set<Provider> getProviders() {
        return this.providers;
    }

    public void setProviders(Set<Provider> providers) {
        if (this.providers != null) {
            this.providers.forEach(i -> i.setBusinessAssociate(null));
        }
        if (providers != null) {
            providers.forEach(i -> i.setBusinessAssociate(this));
        }
        this.providers = providers;
    }

    public BusinessAssociate providers(Set<Provider> providers) {
        this.setProviders(providers);
        return this;
    }

    public BusinessAssociate addProvider(Provider provider) {
        this.providers.add(provider);
        provider.setBusinessAssociate(this);
        return this;
    }

    public BusinessAssociate removeProvider(Provider provider) {
        this.providers.remove(provider);
        provider.setBusinessAssociate(null);
        return this;
    }

    public Set<Project> getProjects() {
        return this.projects;
    }

    public void setProjects(Set<Project> projects) {
        if (this.projects != null) {
            this.projects.forEach(i -> i.setBusinessAssociate(null));
        }
        if (projects != null) {
            projects.forEach(i -> i.setBusinessAssociate(this));
        }
        this.projects = projects;
    }

    public BusinessAssociate projects(Set<Project> projects) {
        this.setProjects(projects);
        return this;
    }

    public BusinessAssociate addProject(Project project) {
        this.projects.add(project);
        project.setBusinessAssociate(this);
        return this;
    }

    public BusinessAssociate removeProject(Project project) {
        this.projects.remove(project);
        project.setBusinessAssociate(null);
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
            ", comercialName='" + getComercialName() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
