package com.miselmesnjak.petstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.miselmesnjak.petstore.domain.PhotoUrl;
import com.miselmesnjak.petstore.service.PhotoUrlService;
import com.miselmesnjak.petstore.web.rest.util.HeaderUtil;
import com.miselmesnjak.petstore.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PhotoUrl.
 */
@RestController
@RequestMapping("/api")
public class PhotoUrlResource {

    private final Logger log = LoggerFactory.getLogger(PhotoUrlResource.class);
        
    @Inject
    private PhotoUrlService photoUrlService;
    
    /**
     * POST  /photo-urls : Create a new photoUrl.
     *
     * @param photoUrl the photoUrl to create
     * @return the ResponseEntity with status 201 (Created) and with body the new photoUrl, or with status 400 (Bad Request) if the photoUrl has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/photo-urls",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoUrl> createPhotoUrl(@RequestBody PhotoUrl photoUrl) throws URISyntaxException {
        log.debug("REST request to save PhotoUrl : {}", photoUrl);
        if (photoUrl.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("photoUrl", "idexists", "A new photoUrl cannot already have an ID")).body(null);
        }
        PhotoUrl result = photoUrlService.save(photoUrl);
        return ResponseEntity.created(new URI("/api/photo-urls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("photoUrl", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /photo-urls : Updates an existing photoUrl.
     *
     * @param photoUrl the photoUrl to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated photoUrl,
     * or with status 400 (Bad Request) if the photoUrl is not valid,
     * or with status 500 (Internal Server Error) if the photoUrl couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/photo-urls",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoUrl> updatePhotoUrl(@RequestBody PhotoUrl photoUrl) throws URISyntaxException {
        log.debug("REST request to update PhotoUrl : {}", photoUrl);
        if (photoUrl.getId() == null) {
            return createPhotoUrl(photoUrl);
        }
        PhotoUrl result = photoUrlService.save(photoUrl);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("photoUrl", photoUrl.getId().toString()))
            .body(result);
    }

    /**
     * GET  /photo-urls : get all the photoUrls.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of photoUrls in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/photo-urls",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PhotoUrl>> getAllPhotoUrls(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PhotoUrls");
        Page<PhotoUrl> page = photoUrlService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/photo-urls");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /photo-urls/:id : get the "id" photoUrl.
     *
     * @param id the id of the photoUrl to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the photoUrl, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/photo-urls/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoUrl> getPhotoUrl(@PathVariable Long id) {
        log.debug("REST request to get PhotoUrl : {}", id);
        PhotoUrl photoUrl = photoUrlService.findOne(id);
        return Optional.ofNullable(photoUrl)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /photo-urls/:id : delete the "id" photoUrl.
     *
     * @param id the id of the photoUrl to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/photo-urls/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePhotoUrl(@PathVariable Long id) {
        log.debug("REST request to delete PhotoUrl : {}", id);
        photoUrlService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("photoUrl", id.toString())).build();
    }

}
