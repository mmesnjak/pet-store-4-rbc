package com.miselmesnjak.petstore.service;

import com.miselmesnjak.petstore.domain.PhotoUrl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing PhotoUrl.
 */
public interface PhotoUrlService {

    /**
     * Save a photoUrl.
     * 
     * @param photoUrl the entity to save
     * @return the persisted entity
     */
    PhotoUrl save(PhotoUrl photoUrl);

    /**
     *  Get all the photoUrls.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PhotoUrl> findAll(Pageable pageable);

    /**
     *  Get the "id" photoUrl.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    PhotoUrl findOne(Long id);

    /**
     *  Delete the "id" photoUrl.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
