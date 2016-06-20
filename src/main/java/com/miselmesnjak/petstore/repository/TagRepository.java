package com.miselmesnjak.petstore.repository;

import com.miselmesnjak.petstore.domain.Tag;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Tag entity.
 */
@SuppressWarnings("unused")
public interface TagRepository extends JpaRepository<Tag, Long> {

}
