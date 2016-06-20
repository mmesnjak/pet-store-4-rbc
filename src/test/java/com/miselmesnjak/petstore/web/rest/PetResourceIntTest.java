package com.miselmesnjak.petstore.web.rest;

import com.miselmesnjak.petstore.PetStoreApp;
import com.miselmesnjak.petstore.domain.Pet;
import com.miselmesnjak.petstore.repository.PetRepository;
import com.miselmesnjak.petstore.service.PetService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.miselmesnjak.petstore.domain.enumeration.PetStatus;

/**
 * Test class for the PetResource REST controller.
 *
 * @see PetResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PetStoreApp.class)
@WebAppConfiguration
@IntegrationTest
public class PetResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final PetStatus DEFAULT_STATUS = PetStatus.available;
    private static final PetStatus UPDATED_STATUS = PetStatus.pending;

    @Inject
    private PetRepository petRepository;

    @Inject
    private PetService petService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPetMockMvc;

    private Pet pet;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PetResource petResource = new PetResource();
        ReflectionTestUtils.setField(petResource, "petService", petService);
        this.restPetMockMvc = MockMvcBuilders.standaloneSetup(petResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pet = new Pet();
        pet.setName(DEFAULT_NAME);
        pet.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createPet() throws Exception {
        int databaseSizeBeforeCreate = petRepository.findAll().size();

        // Create the Pet

        restPetMockMvc.perform(post("/pet")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pet)))
                .andExpect(status().isCreated());

        // Validate the Pet in the database
        List<Pet> pets = petRepository.findAll();
        assertThat(pets).hasSize(databaseSizeBeforeCreate + 1);
        Pet testPet = pets.get(pets.size() - 1);
        assertThat(testPet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPet.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = petRepository.findAll().size();
        // set the field null
        pet.setName(null);

        // Create the Pet, which fails.

        restPetMockMvc.perform(post("/pet")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pet)))
                .andExpect(status().isBadRequest());

        List<Pet> pets = petRepository.findAll();
        assertThat(pets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPets() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the pets
        restPetMockMvc.perform(get("/pet?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pet.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getPet() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get the pet
        restPetMockMvc.perform(get("/pet/{id}", pet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(pet.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPet() throws Exception {
        // Get the pet
        restPetMockMvc.perform(get("/pet/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePet() throws Exception {
        // Initialize the database
        petService.save(pet);

        int databaseSizeBeforeUpdate = petRepository.findAll().size();

        // Update the pet
        Pet updatedPet = new Pet();
        updatedPet.setId(pet.getId());
        updatedPet.setName(UPDATED_NAME);
        updatedPet.setStatus(UPDATED_STATUS);

        restPetMockMvc.perform(put("/pet")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPet)))
                .andExpect(status().isOk());

        // Validate the Pet in the database
        List<Pet> pets = petRepository.findAll();
        assertThat(pets).hasSize(databaseSizeBeforeUpdate);
        Pet testPet = pets.get(pets.size() - 1);
        assertThat(testPet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPet.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deletePet() throws Exception {
        // Initialize the database
        petService.save(pet);

        int databaseSizeBeforeDelete = petRepository.findAll().size();

        // Get the pet
        restPetMockMvc.perform(delete("/pet/{id}", pet.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Pet> pets = petRepository.findAll();
        assertThat(pets).hasSize(databaseSizeBeforeDelete - 1);
    }
}
