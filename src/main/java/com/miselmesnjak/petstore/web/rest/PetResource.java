package com.miselmesnjak.petstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.miselmesnjak.petstore.domain.Category;
import com.miselmesnjak.petstore.domain.Pet;
import com.miselmesnjak.petstore.service.PetService;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Pet.
 */
@RestController
@RequestMapping("/pet")
public class PetResource {

    private final Logger log = LoggerFactory.getLogger(PetResource.class);

    @Inject
    private PetService petService;

    /**
     * POST  /pet : Create a new pet.
     *
     * @param body the pet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pet, or with status 400 (Bad Request) if the pet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pet> createPet(@Valid @RequestBody Pet body) throws URISyntaxException {
        log.debug("REST request to save Pet : {}", body);
        if (body.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("pet", "idexists", "A new body cannot already have an ID")).body(null);
        }
        Pet result = petService.save(body);
        return ResponseEntity.created(new URI("/pet/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("pet", result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pet : get all the pets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pets in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Pet>> getAllPets(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Pets");
        Page<Pet> page = petService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/body");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pet/:petId : get the "petId" body.
     *
     * @param petId the petId of the pet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pet, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/{petId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pet> getPet(@PathVariable Long petId) {
        log.debug("REST request to get Pet : {}", petId);
        Pet pet = petService.findOne(petId);
        return Optional.ofNullable(pet)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pet/:petId : delete the "petId" body.
     *
     * @param petId the petId of the pet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/{petId}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePet(@PathVariable Long petId) {
        log.debug("REST request to delete Pet : {}", petId);
        petService.delete(petId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pet", petId.toString())).build();
    }

    /**
     * PUT  /categories : Updates an existing category.
     *
     * @param pet the category to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated category,
     * or with status 400 (Bad Request) if the category is not valid,
     * or with status 500 (Internal Server Error) if the category couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pet> updatePet(@Valid @RequestBody Pet pet) throws URISyntaxException {
        log.debug("REST request to update Pet : {}", pet);
        if (pet.getId() == null) {
            return createPet(pet);
        }
        Pet result = petService.save(pet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("pet", pet.getId().toString()))
            .body(result);
    }
}
