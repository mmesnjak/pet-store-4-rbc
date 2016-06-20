package com.miselmesnjak.petstore.service;

import com.miselmesnjak.petstore.domain.Pet;
import com.miselmesnjak.petstore.repository.CategoryRepository;
import com.miselmesnjak.petstore.repository.PetRepository;
import com.miselmesnjak.petstore.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Service Implementation for managing Pet.
 */
@Service
@Transactional
public class PetService {

    private final Logger log = LoggerFactory.getLogger(PetService.class);

    @Inject
    private PetRepository petRepository;

    @Inject
    private CategoryRepository categoryRepository;

    @Inject
    private TagRepository tagRepository;

    /**
     * Save a pet.
     *
     * @param pet the entity to save
     * @return the persisted entity
     */
    public Pet save(Pet pet) {
        log.debug("Request to save Pet : {}", pet);

        if (pet.getCategory() != null
            && (pet.getCategory().getId() == null || categoryRepository.getOne(pet.getCategory().getId()) == null)) {
            categoryRepository.save(pet.getCategory());
        }

        if (pet.getTags() != null) {
            tagRepository.save(pet.getTags());
        }

        Pet result = petRepository.save(pet);
        return result;
    }

    /**
     * Get all the pets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Pet> findAll(Pageable pageable) {
        log.debug("Request to get all Pets");
        Page<Pet> result = petRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one pet by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Pet findOne(Long id) {
        log.debug("Request to get Pet : {}", id);
        Pet pet = petRepository.findOneWithEagerRelationships(id);
        return pet;
    }

    /**
     * Delete the  pet by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Pet : {}", id);
        petRepository.delete(id);
    }
}
