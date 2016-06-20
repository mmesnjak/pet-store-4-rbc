package com.miselmesnjak.petstore.repository;

import com.miselmesnjak.petstore.domain.Pet;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Pet entity.
 */
@SuppressWarnings("unused")
public interface PetRepository extends JpaRepository<Pet,Long> {

    @Query("select distinct pet from Pet pet left join fetch pet.tags")
    List<Pet> findAllWithEagerRelationships();

    @Query("select pet from Pet pet left join fetch pet.tags left join fetch pet.photoUrls where pet.id =:id")
    Pet findOneWithEagerRelationships(@Param("id") Long id);

}
