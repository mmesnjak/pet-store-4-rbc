package com.miselmesnjak.petstore.service.impl;

import com.miselmesnjak.petstore.service.PhotoUrlService;
import com.miselmesnjak.petstore.domain.PhotoUrl;
import com.miselmesnjak.petstore.repository.PhotoUrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing PhotoUrl.
 */
@Service
@Transactional
public class PhotoUrlServiceImpl implements PhotoUrlService{

    private final Logger log = LoggerFactory.getLogger(PhotoUrlServiceImpl.class);
    
    @Inject
    private PhotoUrlRepository photoUrlRepository;
    
    /**
     * Save a photoUrl.
     * 
     * @param photoUrl the entity to save
     * @return the persisted entity
     */
    public PhotoUrl save(PhotoUrl photoUrl) {
        log.debug("Request to save PhotoUrl : {}", photoUrl);
        PhotoUrl result = photoUrlRepository.save(photoUrl);
        return result;
    }

    /**
     *  Get all the photoUrls.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<PhotoUrl> findAll(Pageable pageable) {
        log.debug("Request to get all PhotoUrls");
        Page<PhotoUrl> result = photoUrlRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one photoUrl by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PhotoUrl findOne(Long id) {
        log.debug("Request to get PhotoUrl : {}", id);
        PhotoUrl photoUrl = photoUrlRepository.findOne(id);
        return photoUrl;
    }

    /**
     *  Delete the  photoUrl by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PhotoUrl : {}", id);
        photoUrlRepository.delete(id);
    }
}
