package com.zabir.searchweb.web.rest;

import com.zabir.searchweb.SearchWebApp;

import com.zabir.searchweb.domain.Herb;
import com.zabir.searchweb.repository.HerbRepository;
import com.zabir.searchweb.repository.search.HerbSearchRepository;
import com.zabir.searchweb.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.zabir.searchweb.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HerbResource REST controller.
 *
 * @see HerbResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchWebApp.class)
public class HerbResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private HerbRepository herbRepository;


    /**
     * This repository is mocked in the com.zabir.searchweb.repository.search test package.
     *
     * @see com.zabir.searchweb.repository.search.HerbSearchRepositoryMockConfiguration
     */
    @Autowired
    private HerbSearchRepository mockHerbSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHerbMockMvc;

    private Herb herb;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HerbResource herbResource = new HerbResource(herbRepository, mockHerbSearchRepository);
        this.restHerbMockMvc = MockMvcBuilders.standaloneSetup(herbResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Herb createEntity(EntityManager em) {
        Herb herb = new Herb()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return herb;
    }

    @Before
    public void initTest() {
        herb = createEntity(em);
    }

    @Test
    @Transactional
    public void createHerb() throws Exception {
        int databaseSizeBeforeCreate = herbRepository.findAll().size();

        // Create the Herb
        restHerbMockMvc.perform(post("/api/herbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(herb)))
            .andExpect(status().isCreated());

        // Validate the Herb in the database
        List<Herb> herbList = herbRepository.findAll();
        assertThat(herbList).hasSize(databaseSizeBeforeCreate + 1);
        Herb testHerb = herbList.get(herbList.size() - 1);
        assertThat(testHerb.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHerb.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Herb in Elasticsearch
        verify(mockHerbSearchRepository, times(1)).save(testHerb);
    }

    @Test
    @Transactional
    public void createHerbWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = herbRepository.findAll().size();

        // Create the Herb with an existing ID
        herb.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHerbMockMvc.perform(post("/api/herbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(herb)))
            .andExpect(status().isBadRequest());

        // Validate the Herb in the database
        List<Herb> herbList = herbRepository.findAll();
        assertThat(herbList).hasSize(databaseSizeBeforeCreate);

        // Validate the Herb in Elasticsearch
        verify(mockHerbSearchRepository, times(0)).save(herb);
    }

    @Test
    @Transactional
    public void getAllHerbs() throws Exception {
        // Initialize the database
        herbRepository.saveAndFlush(herb);

        // Get all the herbList
        restHerbMockMvc.perform(get("/api/herbs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(herb.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    

    @Test
    @Transactional
    public void getHerb() throws Exception {
        // Initialize the database
        herbRepository.saveAndFlush(herb);

        // Get the herb
        restHerbMockMvc.perform(get("/api/herbs/{id}", herb.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(herb.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingHerb() throws Exception {
        // Get the herb
        restHerbMockMvc.perform(get("/api/herbs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHerb() throws Exception {
        // Initialize the database
        herbRepository.saveAndFlush(herb);

        int databaseSizeBeforeUpdate = herbRepository.findAll().size();

        // Update the herb
        Herb updatedHerb = herbRepository.findById(herb.getId()).get();
        // Disconnect from session so that the updates on updatedHerb are not directly saved in db
        em.detach(updatedHerb);
        updatedHerb
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restHerbMockMvc.perform(put("/api/herbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHerb)))
            .andExpect(status().isOk());

        // Validate the Herb in the database
        List<Herb> herbList = herbRepository.findAll();
        assertThat(herbList).hasSize(databaseSizeBeforeUpdate);
        Herb testHerb = herbList.get(herbList.size() - 1);
        assertThat(testHerb.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHerb.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Herb in Elasticsearch
        verify(mockHerbSearchRepository, times(1)).save(testHerb);
    }

    @Test
    @Transactional
    public void updateNonExistingHerb() throws Exception {
        int databaseSizeBeforeUpdate = herbRepository.findAll().size();

        // Create the Herb

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHerbMockMvc.perform(put("/api/herbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(herb)))
            .andExpect(status().isBadRequest());

        // Validate the Herb in the database
        List<Herb> herbList = herbRepository.findAll();
        assertThat(herbList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Herb in Elasticsearch
        verify(mockHerbSearchRepository, times(0)).save(herb);
    }

    @Test
    @Transactional
    public void deleteHerb() throws Exception {
        // Initialize the database
        herbRepository.saveAndFlush(herb);

        int databaseSizeBeforeDelete = herbRepository.findAll().size();

        // Get the herb
        restHerbMockMvc.perform(delete("/api/herbs/{id}", herb.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Herb> herbList = herbRepository.findAll();
        assertThat(herbList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Herb in Elasticsearch
        verify(mockHerbSearchRepository, times(1)).deleteById(herb.getId());
    }

    @Test
    @Transactional
    public void searchHerb() throws Exception {
        // Initialize the database
        herbRepository.saveAndFlush(herb);
        when(mockHerbSearchRepository.search(queryStringQuery("id:" + herb.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(herb), PageRequest.of(0, 1), 1));
        // Search the herb
        restHerbMockMvc.perform(get("/api/_search/herbs?query=id:" + herb.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(herb.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Herb.class);
        Herb herb1 = new Herb();
        herb1.setId(1L);
        Herb herb2 = new Herb();
        herb2.setId(herb1.getId());
        assertThat(herb1).isEqualTo(herb2);
        herb2.setId(2L);
        assertThat(herb1).isNotEqualTo(herb2);
        herb1.setId(null);
        assertThat(herb1).isNotEqualTo(herb2);
    }
}
