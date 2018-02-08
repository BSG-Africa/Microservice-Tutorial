package za.co.bsg.web.rest;

import za.co.bsg.ExampleApp;

import za.co.bsg.config.SecurityBeanOverrideConfiguration;

import za.co.bsg.domain.MovieTicket;
import za.co.bsg.repository.MovieTicketRepository;
import za.co.bsg.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static za.co.bsg.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MovieTicketResource REST controller.
 *
 * @see MovieTicketResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ExampleApp.class, SecurityBeanOverrideConfiguration.class})
public class MovieTicketResourceIntTest {

    private static final String DEFAULT_MOVIE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MOVIE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    @Autowired
    private MovieTicketRepository movieTicketRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMovieTicketMockMvc;

    private MovieTicket movieTicket;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MovieTicketResource movieTicketResource = new MovieTicketResource(movieTicketRepository);
        this.restMovieTicketMockMvc = MockMvcBuilders.standaloneSetup(movieTicketResource)
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
    public static MovieTicket createEntity(EntityManager em) {
        MovieTicket movieTicket = new MovieTicket()
            .movieName(DEFAULT_MOVIE_NAME)
            .userLogin(DEFAULT_USER_LOGIN);
        return movieTicket;
    }

    @Before
    public void initTest() {
        movieTicket = createEntity(em);
    }

    @Test
    @Transactional
    public void createMovieTicket() throws Exception {
        int databaseSizeBeforeCreate = movieTicketRepository.findAll().size();

        // Create the MovieTicket
        restMovieTicketMockMvc.perform(post("/api/movie-tickets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieTicket)))
            .andExpect(status().isCreated());

        // Validate the MovieTicket in the database
        List<MovieTicket> movieTicketList = movieTicketRepository.findAll();
        assertThat(movieTicketList).hasSize(databaseSizeBeforeCreate + 1);
        MovieTicket testMovieTicket = movieTicketList.get(movieTicketList.size() - 1);
        assertThat(testMovieTicket.getMovieName()).isEqualTo(DEFAULT_MOVIE_NAME);
        assertThat(testMovieTicket.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
    }

    @Test
    @Transactional
    public void createMovieTicketWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = movieTicketRepository.findAll().size();

        // Create the MovieTicket with an existing ID
        movieTicket.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMovieTicketMockMvc.perform(post("/api/movie-tickets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieTicket)))
            .andExpect(status().isBadRequest());

        // Validate the MovieTicket in the database
        List<MovieTicket> movieTicketList = movieTicketRepository.findAll();
        assertThat(movieTicketList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMovieTickets() throws Exception {
        // Initialize the database
        movieTicketRepository.saveAndFlush(movieTicket);

        // Get all the movieTicketList
        restMovieTicketMockMvc.perform(get("/api/movie-tickets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movieTicket.getId().intValue())))
            .andExpect(jsonPath("$.[*].movieName").value(hasItem(DEFAULT_MOVIE_NAME.toString())))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN.toString())));
    }

    @Test
    @Transactional
    public void getMovieTicket() throws Exception {
        // Initialize the database
        movieTicketRepository.saveAndFlush(movieTicket);

        // Get the movieTicket
        restMovieTicketMockMvc.perform(get("/api/movie-tickets/{id}", movieTicket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(movieTicket.getId().intValue()))
            .andExpect(jsonPath("$.movieName").value(DEFAULT_MOVIE_NAME.toString()))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMovieTicket() throws Exception {
        // Get the movieTicket
        restMovieTicketMockMvc.perform(get("/api/movie-tickets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMovieTicket() throws Exception {
        // Initialize the database
        movieTicketRepository.saveAndFlush(movieTicket);
        int databaseSizeBeforeUpdate = movieTicketRepository.findAll().size();

        // Update the movieTicket
        MovieTicket updatedMovieTicket = movieTicketRepository.findOne(movieTicket.getId());
        // Disconnect from session so that the updates on updatedMovieTicket are not directly saved in db
        em.detach(updatedMovieTicket);
        updatedMovieTicket
            .movieName(UPDATED_MOVIE_NAME)
            .userLogin(UPDATED_USER_LOGIN);

        restMovieTicketMockMvc.perform(put("/api/movie-tickets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMovieTicket)))
            .andExpect(status().isOk());

        // Validate the MovieTicket in the database
        List<MovieTicket> movieTicketList = movieTicketRepository.findAll();
        assertThat(movieTicketList).hasSize(databaseSizeBeforeUpdate);
        MovieTicket testMovieTicket = movieTicketList.get(movieTicketList.size() - 1);
        assertThat(testMovieTicket.getMovieName()).isEqualTo(UPDATED_MOVIE_NAME);
        assertThat(testMovieTicket.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
    }

    @Test
    @Transactional
    public void updateNonExistingMovieTicket() throws Exception {
        int databaseSizeBeforeUpdate = movieTicketRepository.findAll().size();

        // Create the MovieTicket

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMovieTicketMockMvc.perform(put("/api/movie-tickets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieTicket)))
            .andExpect(status().isCreated());

        // Validate the MovieTicket in the database
        List<MovieTicket> movieTicketList = movieTicketRepository.findAll();
        assertThat(movieTicketList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMovieTicket() throws Exception {
        // Initialize the database
        movieTicketRepository.saveAndFlush(movieTicket);
        int databaseSizeBeforeDelete = movieTicketRepository.findAll().size();

        // Get the movieTicket
        restMovieTicketMockMvc.perform(delete("/api/movie-tickets/{id}", movieTicket.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MovieTicket> movieTicketList = movieTicketRepository.findAll();
        assertThat(movieTicketList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovieTicket.class);
        MovieTicket movieTicket1 = new MovieTicket();
        movieTicket1.setId(1L);
        MovieTicket movieTicket2 = new MovieTicket();
        movieTicket2.setId(movieTicket1.getId());
        assertThat(movieTicket1).isEqualTo(movieTicket2);
        movieTicket2.setId(2L);
        assertThat(movieTicket1).isNotEqualTo(movieTicket2);
        movieTicket1.setId(null);
        assertThat(movieTicket1).isNotEqualTo(movieTicket2);
    }
}
