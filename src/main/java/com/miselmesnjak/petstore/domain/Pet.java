package com.miselmesnjak.petstore.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.miselmesnjak.petstore.domain.enumeration.PetStatus;

/**
 * A Pet.
 */
@Entity
@Table(name = "pet")
public class Pet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PetStatus status;

    @ManyToMany
    @JoinTable(name = "pet_tag",
        joinColumns = @JoinColumn(name = "pets_id", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "tags_id", referencedColumnName = "ID"))
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    private Category category;

    @ElementCollection
    @CollectionTable(name = "photo_url", joinColumns = @JoinColumn(name = "pet_id"))
    @Column(name = "url")
    private Set<String> photoUrls;

    public Set<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(Set<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PetStatus getStatus() {
        return status;
    }

    public void setStatus(PetStatus status) {
        this.status = status;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pet pet = (Pet) o;
        if (pet.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, pet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pet{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
