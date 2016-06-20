package com.miselmesnjak.petstore.repository;

import com.miselmesnjak.petstore.domain.PhotoUrl;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PhotoUrl entity.
 */
@SuppressWarnings("unused")
public interface PhotoUrlRepository extends JpaRepository<PhotoUrl,Long> {

}
