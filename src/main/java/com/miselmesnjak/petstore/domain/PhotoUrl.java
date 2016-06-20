package com.miselmesnjak.petstore.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PhotoUrl.
 */
@Entity
@Table(name = "photo_url")
public class PhotoUrl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "url")
    private String url;

    @ManyToOne
    private Pet pet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PhotoUrl photoUrl = (PhotoUrl) o;
        if(photoUrl.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, photoUrl.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PhotoUrl{" +
            "id=" + id +
            ", url='" + url + "'" +
            '}';
    }
}
