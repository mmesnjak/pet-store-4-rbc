package com.miselmesnjak.petstore.web.rest;

import com.miselmesnjak.petstore.PetStoreApp;
import com.miselmesnjak.petstore.domain.PhotoUrl;
import com.miselmesnjak.petstore.repository.PhotoUrlRepository;
import com.miselmesnjak.petstore.service.PhotoUrlService;

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


/**
 * Test class for the PhotoUrlResource REST controller.
 *
 * @see PhotoUrlResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PetStoreApp.class)
@WebAppConfiguration
@IntegrationTest
public class PhotoUrlResourceIntTest {

    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";

    @Inject
    private PhotoUrlRepository photoUrlRepository;

    @Inject
    private PhotoUrlService photoUrlService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPhotoUrlMockMvc;

    private PhotoUrl photoUrl;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PhotoUrlResource photoUrlResource = new PhotoUrlResource();
        ReflectionTestUtils.setField(photoUrlResource, "photoUrlService", photoUrlService);
        this.restPhotoUrlMockMvc = MockMvcBuilders.standaloneSetup(photoUrlResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        photoUrl = new PhotoUrl();
        photoUrl.setUrl(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void createPhotoUrl() throws Exception {
        int databaseSizeBeforeCreate = photoUrlRepository.findAll().size();

        // Create the PhotoUrl

        restPhotoUrlMockMvc.perform(post("/api/photo-urls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoUrl)))
                .andExpect(status().isCreated());

        // Validate the PhotoUrl in the database
        List<PhotoUrl> photoUrls = photoUrlRepository.findAll();
        assertThat(photoUrls).hasSize(databaseSizeBeforeCreate + 1);
        PhotoUrl testPhotoUrl = photoUrls.get(photoUrls.size() - 1);
        assertThat(testPhotoUrl.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void getAllPhotoUrls() throws Exception {
        // Initialize the database
        photoUrlRepository.saveAndFlush(photoUrl);

        // Get all the photoUrls
        restPhotoUrlMockMvc.perform(get("/api/photo-urls?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(photoUrl.getId().intValue())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    public void getPhotoUrl() throws Exception {
        // Initialize the database
        photoUrlRepository.saveAndFlush(photoUrl);

        // Get the photoUrl
        restPhotoUrlMockMvc.perform(get("/api/photo-urls/{id}", photoUrl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(photoUrl.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPhotoUrl() throws Exception {
        // Get the photoUrl
        restPhotoUrlMockMvc.perform(get("/api/photo-urls/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhotoUrl() throws Exception {
        // Initialize the database
        photoUrlService.save(photoUrl);

        int databaseSizeBeforeUpdate = photoUrlRepository.findAll().size();

        // Update the photoUrl
        PhotoUrl updatedPhotoUrl = new PhotoUrl();
        updatedPhotoUrl.setId(photoUrl.getId());
        updatedPhotoUrl.setUrl(UPDATED_URL);

        restPhotoUrlMockMvc.perform(put("/api/photo-urls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPhotoUrl)))
                .andExpect(status().isOk());

        // Validate the PhotoUrl in the database
        List<PhotoUrl> photoUrls = photoUrlRepository.findAll();
        assertThat(photoUrls).hasSize(databaseSizeBeforeUpdate);
        PhotoUrl testPhotoUrl = photoUrls.get(photoUrls.size() - 1);
        assertThat(testPhotoUrl.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void deletePhotoUrl() throws Exception {
        // Initialize the database
        photoUrlService.save(photoUrl);

        int databaseSizeBeforeDelete = photoUrlRepository.findAll().size();

        // Get the photoUrl
        restPhotoUrlMockMvc.perform(delete("/api/photo-urls/{id}", photoUrl.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PhotoUrl> photoUrls = photoUrlRepository.findAll();
        assertThat(photoUrls).hasSize(databaseSizeBeforeDelete - 1);
    }
}
